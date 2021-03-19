#include "work.h"
#include <stdio.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>

#include "NTFS/NTFS_structs.h"

void startWork(char *disk_partition){
    //Выполнять операции над файловой системой, представленной на заданном диске, разделе или в файле.
    puts("start of work");
    printf("loading into partition: %s\n",disk_partition);
    int fd = open(disk_partition, O_RDWR, 0666);
    PBSector* sector = malloc(sizeof(PBSector));
    pread(fd, sector,sizeof(PBSector),0);
    pread(fd, sector,sizeof(PBSector),0);
    char* oem_id = &sector->oem_id;
    //TODO: Fix it
    printf("%.8sEND\n",oem_id);
    uint64_t size = sector->bpb.bytes_per_sector * sector->bpb.sectors_per_cluster;
    uint64_t logicalClusterNumber = sector->ebpb.logical_cluster_number;
    ntfs_mft_record* record = malloc(sizeof(ntfs_mft_record));
        pread(fd,record,sizeof(ntfs_mft_record),size*6);
        char* name = &(record->magic);
        printf("%ld: %.4s, flag = %d\n", size*6,name,record->flags);
}