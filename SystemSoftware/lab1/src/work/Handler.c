#include "Handler.h"
#include <stdio.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdbool.h>
#include "string.h"

#include "util.h"
#include "parser/Parser.h"
#include "parser/Commands.h"

#define MAGIC "NTFS    " //4 spaces!!!
#define SIZE_OF_MAGIC 8

static NTFSInfoStruct* preloadCheck(char* );
static NTFSInfoStruct* stuffNTFSInfoStruct(PBSector *sector, int fd);
static void run(NTFSInfoStruct *ntfs);
static bool checkAllPBFields(PBSector*);
static bool executeCommands(NTFSInfoStruct*);

//Выполнять операции над файловой системой, представленной на заданном диске, разделе или в файле.
void startWork(char* disk_partition){
    puts("start of work");
    NTFSInfoStruct* info = preloadCheck(disk_partition);
    if (info == NULL){
        return;
    }
    run(info);
}

static void run(NTFSInfoStruct* ntfs){
    puts("Partition loaded");
    while (executeCommands(ntfs));
}
static bool executeCommands(NTFSInfoStruct *ntfs){
    printf("> ");
    char* line = NULL;
    size_t bufferLengths =0;
    size_t charsNum = getline(&line, &bufferLengths,stdin);
    if (charsNum == (size_t)(-1)){
        free(line);
        return false;
    }
    char** args = NULL;
    int amountOfWords = parseCommand(line, &args);
    if (amountOfWords == 0){
        free(line);
        if (args != NULL){
            free(args);
        }
        return true;
    }
    for (int i = 0; i< NUMBER_OF_COMMANDS; i++){
        run_command candidate = RUN_COMMANDS_LIST[i];
        if (strcmp(candidate.name, args[0]) == 0){
            bool result = candidate.function(ntfs,amountOfWords-1, args+1);
            free(line);
            free(args);
            return result;
        }
    }

    puts("command not found, type 'help' for help information");
    free(line);
    return true;
}

static NTFSInfoStruct* preloadCheck(char* partition){
    printf("Trying to load: %s\n",partition);
    int fd = open(partition, O_RDWR, 0666);
    if (unlikely(fd == -1)){
        puts("Program can't properly open this file.\nAbort.");
        return NULL;
    }
    PBSector* sector = malloc(sizeof(PBSector));
    pread(fd, sector,sizeof(PBSector),0);
    if (unlikely(strncmp((const char *) &sector->oem_id, MAGIC, SIZE_OF_MAGIC) != 0)){
        puts("This is not a NTFS partition\nAbort.");
        return NULL;
    }
    puts("Found NTFS partition, continue loading");
    if(checkAllPBFields(sector) == false) {
        puts("One or more NTFS Boot sector's fields is wrong.\nAbort");
        return NULL;
    }
    NTFSInfoStruct* ntfs = stuffNTFSInfoStruct(sector, fd);
    return  ntfs;
}

static bool checkAllPBFields(PBSector* sector){
    if (sector->bpb.zero0 || sector->bpb.zero1 || sector->bpb.zero2 ||
        sector->bpb.zeroes0[0] || sector->bpb.zeroes0[1] || sector->bpb.zeroes0[2]){
        return false;
    }
    return true;
}


//NOTE: here is NTFS info struct is created, all changes to ntfs info structure must also change this function
// to create valid struct!
static NTFSInfoStruct* stuffNTFSInfoStruct(PBSector* sector, int fd){
    NTFSInfoStruct *info = malloc(sizeof (NTFSInfoStruct));
    info->sector_shift = ilog2(sector->bpb.bytes_per_sector);
    info->fd = fd;

    /* NOTE: ebpb.clusters_per_mft_record can be a negative number.
     * If negative, it represents a shift count(2 in a row of this number), else it represents
     * a multiplier for the cluster size.
     * ilog2() everywhere here just to work always like a bit shift operation,
     * and don't creating any flag like - we working in shift mode or multiplying mode
     * Engineers are clever
    */
    u8 mft_record_shift = sector->ebpb.clusters_per_mft_record < 0 ?
                      -(sector->ebpb.clusters_per_mft_record) :
                          info->sector_shift +
                                  ilog2(sector->bpb.sectors_per_cluster) +
                          ilog2(sector->ebpb.clusters_per_mft_record);
    info->mft_record_size = 1 << mft_record_shift;

    info->cluster_byte_shift = ilog2(sector->bpb.sectors_per_cluster) + info->sector_shift;
    info->cluster_size = sector->bpb.sectors_per_cluster << info->sector_shift;
    info->block_shift = ilog2(sector->ebpb.clusters_per_index_buffer) + info->cluster_byte_shift;
    info->block_size = 1 << info->block_shift;
    info->mft_logical_cluster_number = sector->ebpb.logical_cluster_number;

    ntfs_inode* root = malloc(sizeof (ntfs_inode));
    root->mft_number= FILE_root;
    root->type = MFT_RECORD_IN_USE | MFT_RECORD_IS_DIRECTORY;
    root->parent = root;
    root->next = NULL;

    info->current_inode = root;
    info->root_inode = root;

    return info;
}

