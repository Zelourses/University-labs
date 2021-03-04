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
}