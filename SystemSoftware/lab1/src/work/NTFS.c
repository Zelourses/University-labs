#include "NTFS.h"

#include "util.h"
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>

//record - out
//returns offset
u64 mftFindRecord(NTFSInfoStruct* ntfs, u32 file, mft_record** record){
    u64 offset;
    u32 record_shift = ilog2(ntfs->mft_record_size);
    u32 cluster_shift = ntfs->cluster_byte_shift;
    /*
     * Counting the cluster of this file by knowing starting cluster +
     *  number of file translated to record and downgraded to the size of record
     *  (file << record_shift will give record number that contains that file)
     *  (<previous_result> >> cluster_shift will give us cluster number of this file)
     *
     *  let's imagine that we starting with standard ntfs, where MFT starts at cluster 4,
     *  record is 1024 bytes long, and cluster is equals 512 * 8 = 4096 bytes
     *  and we are trying to find . (dot) or root record, which is number 5
     *  from what we got,
     *      file            = 5 (root folder)
     *      record_shift    = 10 (2^10 = 1024 bytes)
     *      cluster_shift   = 12 (2^12 = 4096 bytes)
     *      mft_logical_cluster_number = 4
     *  So, logical_cluster_number = 4 + (5 << 10 >> 12) = 4 + (5120 >> 12) =
     *                             = 4 + (1) = 5
     *  And now we know that we need cluster 5 to get our record
     *
     *  So now we must find offset from start where to find this record.
     *  we get this by transforming file number to record (mus be less than cluster size)
     *  and + this with cluster address
     *  So, follow our example, 5(logical cluster number) << 12 = 20,480
     *  plus, (5(file number) << 10) % 4096 = 5120 % 4096 = 1024,
     *  1024 + 20480 = 21504 bytes (5400) - offset from start of our disk.
     *
     *  And you don't believe.
     *  In our example disk, on 0x5400 we can find record with number 5!
     *  the root folder itself!
     *  Congratulations, we found our record, we can return offset and read record automatically
     *  will be placed in record argument!
     *
    */
    u64 logical_cluster_number = ntfs->mft_logical_cluster_number+ (file << record_shift >> cluster_shift);
    offset =((file << record_shift) % ntfs->cluster_size) + (logical_cluster_number << ntfs->cluster_byte_shift);
    while ((*record)->magic != NTFS_MAGIC_FILE || (*record)->mft_record_number != file){
        pread(ntfs->fd,(*record),ntfs->mft_record_size,(long )offset);
        offset+= ntfs->mft_record_size;
    }
    /*
     * If we suddenly found something wrong
     */
    if ((*record)->magic != NTFS_MAGIC_FILE){
        return -1;
    }

    return offset - ntfs->mft_record_size; // Real offset, because while() loop works like it works
}

/* Convert an UTF-16LE LFN to OEM LFN
 * from https://github.com/joyent/syslinux/blob/master/core/fs/ntfs/ntfs.c
*/
static uint8_t ntfs_cvt_filename(char *filename,
                                 const ntfs_idx_entry *idxEntry)
{
    const uint16_t *entry_fn;
    uint8_t entry_fn_len;
    unsigned i;

    entry_fn = idxEntry->key.file_name.file_name;
    entry_fn_len = idxEntry->key.file_name.file_name_len;

    for (i = 0; i < entry_fn_len; i++)
        filename[i] = (char)entry_fn[i];

    filename[i] = '\0';

    return entry_fn_len+1; // length + null-symbol
}
/*
 * from https://github.com/joyent/syslinux/blob/master/core/fs/ntfs/ntfs.c
 */
static inline bool is_filename_printable(const char *s)
{
    return s && (*s != '.' && *s != '$');
}

int readClusterToBuf(NTFSInfoStruct* ntfs, u8** buf, u64* currentBufSize, u64* bufSize, s64 lcn, u64 length){
    length <<=ntfs->cluster_byte_shift;
    u64 offset = lcn << ntfs->cluster_byte_shift;

    if (length > (*bufSize - *currentBufSize)){
        *buf = realloc(*buf, *bufSize + ntfs->block_size);
        if (*buf == NULL){
            return -1;
        } else {
            *bufSize += ntfs->block_size;
        }
    }
    pread(ntfs->fd, *buf + *currentBufSize, length, (long) offset);
    *currentBufSize +=length;
    return 0;
}

int parseDataRun(NTFSInfoStruct* ntfs, u64 offset, mapping_chunk** chunk){
    u8 lengthByte;
    u8 v, l;    /* v is the number of changed low-order VCN bytes;
                 * l is the number of changed low-order LCN bytes
                */
    u8* runList = malloc(ntfs->block_size);
    pread(ntfs->fd, runList,ntfs->block_size, (long)offset);

    u8* runListPtr = runList;

    u8* byte;
    int byteShift = 8;
    int mask;
    u8 val;
    s64 res;
    s64 lcn = 0;


    u8* buf = malloc(ntfs->block_size);
    u64 bufferSize =ntfs->block_size;
    u64 currentBufferSize = 0;
    int err;
    do {
        lengthByte = *runListPtr;
        v = lengthByte & 0x0F;
        l = lengthByte >> 4;
        u64 length = 0;
        u8 count = v;

        byte = (u8 *)runListPtr + v;
        count = v;

        res = 0LL;
        while (count--){
            val =*byte--;
            mask = val >>(byteShift -1);
            res =(res << byteShift) | ((val + mask) ^ mask);
        }

        length = res;

        byte = (u8*)runListPtr + v + l;
        count = l;

        mask = 0xFFFFFFFF;
        res = 0LL;

        if (*byte & 0x80) {
            res |=(s64) mask;
        }
        /* get vcn */
        while (count--){
            res =(res << byteShift) | *byte--;
        }

        lcn += res;
        runListPtr += v + l +1;
        err = readClusterToBuf(ntfs,&buf,&currentBufferSize, &bufferSize, lcn, length);
        if (err == -1){
            free(runListPtr);
            free(buf);
            return -1;
        }
    } while (*runListPtr);

    free(runList);
    (*chunk)->buf = buf;
    (*chunk)->length =currentBufferSize;
    return 0;

}

int ntfsReadDir(NTFSInfoStruct* ntfs, ntfs_inode** inode){
    mft_record * dirRecord = malloc(ntfs->mft_record_size);
    u64 offset = mftFindRecord(ntfs, (*inode)->mft_number, &dirRecord);
    if (offset < 0){
        return -1;
    }

    ntfs_attribute_record* attributeRecord = NULL;
    int findError= ntfsFindAttributes(ntfs, NTFS_ATTR_INDEX_ROOT, dirRecord, &attributeRecord);
    if (findError == -1 || !attributeRecord){
        free(dirRecord);
        return -1;
    }
    char filename[NTFS_MAX_FILENAME_LENGTH+1];

    mft_record* dirEntry = malloc(ntfs->mft_record_size);

    ntfs_index_root* indexRoot = (ntfs_index_root*)((u8*)attributeRecord + attributeRecord->data.resident.offset_to_attribute);
    u8* indexEntryOffset =((u8*)&indexRoot->index +indexRoot->index.offset_fo_first_index_entry );

    u64 mftEntryOffset = 0;
    ntfs_idx_entry *indexEntry = NULL;
    u8 filenameLength;
    ntfs_inode* current_inode = *inode;
    int counter = 0;
    do {
        indexEntry = (ntfs_idx_entry *) indexEntryOffset;
        indexEntryOffset = ((u8 *)indexEntry + indexEntry->len);
        if (indexEntry->key_len > 0){
            filenameLength = ntfs_cvt_filename(filename, indexEntry);
            if (is_filename_printable(filename)){
                current_inode->next = malloc(sizeof (ntfs_inode));
                current_inode->next->next = NULL;
                current_inode = current_inode->next;
                current_inode->parent =*inode;
                current_inode->filename = malloc(filenameLength);
                memcpy(current_inode->filename,&filename[0], filenameLength);

                mftEntryOffset = mftFindRecord(ntfs,indexEntry->data.dir.indexed_file & NTFS_MFT_REF_MASK, &dirEntry);
                if (mftEntryOffset == -1){
                    free(dirEntry);
                    free(dirRecord);
                    return -1;
                }
                current_inode->type =dirEntry->flags;
                current_inode->mft_number = dirEntry->mft_record_number;
                counter++;
            }
        }
    } while (!(indexEntry->flags & INDEX_ENTRY_END));

    if (!(indexEntry->flags & INDEX_ENTRY_NODE)){
        if (dirRecord->magic == NTFS_MAGIC_FILE){
            free(dirRecord);
        }
        return counter;
    }

    int error = ntfsFindAttributes(ntfs, NTFS_ATTR_INDEX_ALLOCATION, dirRecord, &attributeRecord);

    if (!attributeRecord || error == -1) {
        free(dirRecord);
        free(dirEntry);
        return counter;
    }

    //Checked resident value
    if (!attributeRecord->non_resident){
        free(dirRecord);
        free(dirEntry);
        return -1;
    }

    //This code for non-resident directories

    ntfs_idx_allocation* indexAllocation;
    u64 stream = offset + ((u8*)attributeRecord - (u8*)dirRecord) + attributeRecord->data.non_resident.offset_to_data_runs;
    free(dirRecord);
    mapping_chunk* mappingChunk = malloc(sizeof(mapping_chunk));
    mappingChunk->current_block = 0;

    parseDataRun(ntfs,stream,&mappingChunk);

    do {
        if (mappingChunk->length >= 0){
            indexAllocation =(ntfs_idx_allocation *)(mappingChunk->buf + (mappingChunk->current_block << ntfs->block_shift));
            if (indexAllocation->magic != NTFS_MAGIC_INDX){
                free(dirEntry);
                free(mappingChunk->buf);
                free(mappingChunk);
                return -1;
            }
        } else {
            free(dirEntry);
            free(mappingChunk->buf);
            free(mappingChunk);
            return 0;
        }
        indexEntryOffset =((u8 *)&indexAllocation->index + indexAllocation->index.offset_fo_first_index_entry);

        do {
            indexEntry = (ntfs_idx_entry *)indexEntryOffset;
            indexEntryOffset =((u8*)indexEntry + indexEntry->len);
            if (indexEntry->key_len > 0){
                filenameLength = ntfs_cvt_filename(filename, indexEntry);
                if (is_filename_printable(filename)){
                    current_inode->next = malloc(sizeof (ntfs_inode));
                    current_inode->next->next = NULL;
                    current_inode = current_inode->next;
                    current_inode->parent = *inode;
                    current_inode->filename = malloc(filenameLength);
                    memcpy(current_inode->filename, &filename[0], filenameLength);
                    mftEntryOffset = mftFindRecord(ntfs,indexEntry->data.dir.indexed_file & NTFS_MFT_REF_MASK, &dirEntry);

                    if (mftEntryOffset == -1){
                        free(dirEntry);
                        free(mappingChunk->buf);
                        free(mappingChunk);
                        return -1;
                    }
                    current_inode->type = dirEntry->flags;
                    current_inode->mft_number =indexEntry->data.dir.indexed_file & NTFS_MFT_REF_MASK;
                    counter++;
                }
            }
        } while (indexEntryOffset < (mappingChunk->buf + mappingChunk->length) && !(indexEntry->flags & INDEX_ENTRY_END));

        mappingChunk->current_block++;

    } while (mappingChunk->current_block < (mappingChunk->length >> ntfs->block_shift));

    free(dirEntry);
    free(mappingChunk->buf);
    free(mappingChunk);
    return counter;
}

void freeInode(ntfs_inode* inode){
    ntfs_inode* tmp_inode;

    while (inode != NULL){
        tmp_inode = inode;
        inode = inode->next;
//        tmp_inode->parent = NULL;
//        tmp_inode->next = NULL;
        free(tmp_inode->filename);
        free(tmp_inode);

    }
}

int ntfsFindAttributes(NTFSInfoStruct* ntfs,
                       u32 attribute_type,
                       mft_record* record,
                       ntfs_attribute_record** attribute_record
                         ){
    if (!record || attribute_type == NTFS_ATTR_END){
        attribute_record = NULL;
        return -1;
    }

    *attribute_record = (ntfs_attribute_record*)((u8*)record + record->first_attrib_offset);

    void* end =record + ntfs->mft_record_size - sizeof (attribute_record);

    while ((*attribute_record)->attr_type != NTFS_ATTR_END &&
           (*attribute_record)->attr_type != attribute_type &&
            (void*)(*attribute_record) < end){
        *attribute_record = (ntfs_attribute_record*)((u8 *)(*attribute_record) + (*attribute_record)->length);
    }
    if ((*attribute_record)->attr_type == NTFS_ATTR_END ||
            (void*)(*attribute_record) >=end){
        attribute_record = NULL;
    }
    return 0;
}

int countAmountOfNodes(const char* path){
    char pathBuffer[400];
    strcpy(pathBuffer,path);
    int result = 0;
    char* subDirs = strtok(pathBuffer, "/");
    while (subDirs != NULL){
        result++;
        subDirs = strtok(NULL, "/");
    }
    return result;
}

int findNodeByName(NTFSInfoStruct* ntfs, char* path, ntfs_inode** startingInode, NTFSFindInfo** result){
    ntfs_inode* resultInode = malloc(sizeof(ntfs_inode));
    ntfs_inode* head;
    memcpy(resultInode, *startingInode, sizeof(ntfs_inode));
    resultInode->filename = NULL;
    ntfs_inode* resultInodeStart =resultInode;
    bool found = false;
    char pathBuffer[400];
    strcpy(pathBuffer, path);
    int counter = countAmountOfNodes(path);
    char* subDir = strtok(pathBuffer, "/");
    int err = 0;

    while (subDir != NULL){
        found = false;
        if (!(resultInode->type & MFT_RECORD_IS_DIRECTORY)) {
            freeInode(resultInodeStart);
            return -1;
        }
        err = ntfsReadDir(ntfs, &resultInode);
        if (err == -1){
            freeInode(resultInodeStart);
            return -1;
        }
        head =resultInode->next;
        while (head !=NULL){
            if (strcmp(head->filename, subDir) == 0){
                ntfs_inode *tmp = malloc(sizeof (ntfs_inode));
                memcpy(tmp, head, sizeof(ntfs_inode));
                tmp->filename = malloc(strlen(head->filename)+1);
                strcpy(tmp->filename,  head->filename);
                freeInode(resultInode->next);
                resultInode->next = tmp;
                found = true;
                break;
            }
            head = head->next;
        }
        if (!found){
            freeInode(resultInodeStart);
            return -1;
        }
        resultInode = resultInode->next;
        resultInode->next = NULL;
        if (counter == 1){
            *result = malloc(sizeof(NTFSFindInfo));
            (*result)->start =resultInodeStart;
            (*result)->result = resultInode;
            return 0;
        } else{
            counter--;
        }
        subDir = strtok(NULL,"/");
    }
    return -1;
}

int ntfsInitChunkData(NTFSInfoStruct* ntfs, u64 offset, mappingChunkData** chunk){
    u8 lengthByte;
    u8 v,l;

    u8* runList = malloc(ntfs->block_size);
    pread(ntfs->fd, runList, ntfs->block_size, (long)offset);
    u8* runListPtr =runList;

    u8* byte;
    int byteShift = 8;
    int mask;
    u8 val;
    s64 res;
    s64 lcn = 0;
    int err;

    (*chunk)->lcns = malloc(sizeof(s64)*10);
    (*chunk)->lengths = malloc(sizeof(u64)*10);
    int bufSize = 10;
    int currentSize = 0;

    do {
        lengthByte =*runListPtr;
        v =lengthByte & 0x0F;
        l =lengthByte >> 4;
        u64 length = 0;
        u8 count = v;

        byte =(u8*)runListPtr + v;
        count = v;

        res = 0LL;
        while (count--){
            val =*byte--;
            mask =val >>(byteShift - 1);
            res = (res << byteShift) | ((val + mask) ^ mask);
        }
        length = res;

        byte = (u8*)runListPtr + v + l;
        count = l;
        mask = 0xFFFFFFFF;
        res = 0LL;
        if (*byte & 0x80){
            res |= (s64)mask;
        }
        while (count--){
            res = (res << byteShift) | *byte--;
        }
        if (currentSize == bufSize){
            (*chunk)->lcns = realloc((*chunk)->lcns, bufSize+10);
            (*chunk)->lengths = realloc((*chunk)->lengths, bufSize+10);
            bufSize+=10;
        }
        lcn += res;
        (*chunk)->lcns[currentSize] = lcn;
        (*chunk)->lengths[currentSize] = length;
        currentSize++;
        runListPtr += v + l + 1;
    } while (*runListPtr);

    (*chunk)->cur_lcn = 0;
    (*chunk)->lcn_count = currentSize;
    (*chunk)->cur_block = 0;
    free(runList);

    return 0;
}

int ntfsReadFileData(NTFSInfoStruct* ntfs, mappingChunkData** chunk, ntfs_inode* inode){
    if (inode->type & MFT_RECORD_IS_DIRECTORY) return -1;

    mft_record* fileRecord = malloc(ntfs->mft_record_size);
    u64 offset = mftFindRecord(ntfs, inode->mft_number, &fileRecord);

    if (offset == -1){
        free(fileRecord);
        return -1;
    }

    ntfs_attribute_record* dataAttr = NULL;
    int err = ntfsFindAttributes(ntfs, NTFS_ATTR_DATA, fileRecord, &dataAttr);

    if (err == -1){
        free(fileRecord);
        return -1;
    }

    *chunk = malloc(sizeof(mappingChunkData));

    if (!dataAttr->non_resident){
        (*chunk)->resident = 1;
        (*chunk)->buf = malloc(dataAttr->data.resident.length_of_attribute);
        memcpy((*chunk)->buf, (u8*)dataAttr + dataAttr->data.resident.offset_to_attribute,
               dataAttr->data.resident.length_of_attribute);
        (*chunk)->length=dataAttr->data.resident.length_of_attribute;
        (*chunk)->lcns = NULL;
        (*chunk)->lengths = NULL;
    } else {
        (*chunk)->resident = 0;
        u64 stream =offset + ((u8*)dataAttr - (u8*)fileRecord) + dataAttr->data.non_resident.offset_to_data_runs;
        ntfsInitChunkData(ntfs,stream,&(*chunk));
        (*chunk)->length = dataAttr->data.non_resident.real_size;
        (*chunk)->buf = malloc(ntfs->block_size);
        (*chunk)->blocks_count = 0;
    }
    free(fileRecord);
    return 0;
}

void ntfsFreeDataChunk(mappingChunkData* chunkData){
    if ((chunkData)->buf != NULL){
        free((chunkData)->buf);
    }
    if ((chunkData)->lcns != NULL){
        free((chunkData)->lcns);
    }
    if ((chunkData)->lengths != NULL){
        free((chunkData)->lengths);
    }
    free(chunkData);
}

int readBlockFile(NTFSInfoStruct* ntfs, mappingChunkData** chunk){
    u64 bufferCurrentSize = 0;
    u64 bufferSize = ntfs->block_size;

    if((*chunk)->cur_block == (*chunk)->lengths[(*chunk)->cur_lcn]){
        (*chunk)->cur_lcn++;
    }
    if((*chunk)->cur_lcn == (*chunk)->lcn_count){
        (*chunk)->signal = 1;
        return 1;
    }
    s64 lcn =(s64)((*chunk)->lcns[(*chunk)->cur_lcn]+(*chunk)->cur_block);
    int err = readClusterToBuf(ntfs,&(*chunk)->buf, &bufferCurrentSize, &bufferSize, lcn, 1);
    if(err == -1){
        (*chunk)->signal = -1;
        return -1;
    }
    (*chunk)->blocks_count++;
    (*chunk)->cur_block++;
    (*chunk)->signal = 0;
    return 0;
}