#include "InnerFunctions.h"
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>


int copyByInode(NTFSInfoStruct* ntfs, ntfs_inode* inode, char* outputPath){

    char* nodePath = malloc(strlen(outputPath) + strlen(inode->filename) + 2);
    strcpy(nodePath,outputPath);
    strcat(nodePath, "/");
    strcat(nodePath, inode->filename);
    if (!(inode->type & MFT_RECORD_IS_DIRECTORY)){
        int fd = open(nodePath, O_CREAT | O_WRONLY | O_TRUNC, 00666);
        if (fd == -1){
            free(nodePath);
            return -1;
        }
        mappingChunkData* chunkData;
        int err = ntfsReadFileData(ntfs, &chunkData,inode);
        if (err == -1){
            free(nodePath);
            close(fd);
            return -1;
        }
        if (chunkData->resident){
            pwrite(fd, chunkData->buf, chunkData->length, 0);
            close(fd);
            ntfsFreeDataChunk(chunkData);
            free(nodePath);
            return 1;
        } else {
            long offset = 0;
            unsigned long size;
            while (readBlockFile(ntfs, &chunkData) == 0){
                if (chunkData->blocks_count << ntfs->block_shift > chunkData->length){
                    size =chunkData->length - ((chunkData->blocks_count-1) << ntfs->block_shift);
                    pwrite(fd, chunkData->buf, size, offset);
                    break;
                }else {
                    size = ntfs->block_size;
                }
                offset += pwrite(fd, chunkData->buf, size, offset);
            }
            close(fd);
            int result = chunkData->signal;
            ntfsFreeDataChunk(chunkData);
            free(nodePath);
            return result;
        }
    } else {
        if (mkdir(nodePath, 00777) != 0){
            free(nodePath);
            return -1;
        }
        ntfs_inode* readNode = malloc(sizeof(ntfs_inode));
        memcpy(readNode, inode, sizeof(ntfs_inode));
        readNode->filename = NULL;
        int err = ntfsReadDir(ntfs, &readNode);
        if(err == -1){
            free(nodePath);
            freeInode(readNode);
            return -1;
        }
        ntfs_inode* tmp = readNode->next;
        while (tmp !=NULL){
            if(copyByInode(ntfs,tmp, nodePath) == -1){
                freeInode(readNode);
                free(nodePath);
                return -1;
            }
            tmp = tmp->next;
        }
        free(nodePath);
        freeInode(readNode);
    }
    return 0;
}