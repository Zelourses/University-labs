#ifndef _main_h_
#define _main_h_

//all ariphmetic is done, and file size written in bytes
#define MEMORY 110100480 //105 Mb

#define MMAP_START_ADDRESS (void*) 0x9C487460
#define MMAP_FLAGS PROT_READ|PROT_WRITE
#define THREADS_COUNT 88
#define MMAP_TYPE MAP_PRIVATE|MAP_ANONYMOUS
#define FILE_SIZE 52428800 //50 Mb
#define IO_SIZE 64 // 64 bytes of IO block
#define READ_THREADS_COUNT 111
#define FILE_OPEN_MODE S_IRWXU | S_IRGRP | S_IROTH

//amount of files that we need to write in
#define FILES_COUNT MEMORY/FILE_SIZE + (MEMORY%FILE_SIZE?1:0)

typedef struct RWlock{
    int file_created;
    int sync_lock;
    int write_lock;
    int readers; 
}RWlock;

extern struct RWlock rwlocks[];

typedef struct data{
    int thread_number;
    void *addres;
}thread_data;


void* thread_handler(void *arg);
void itoa(int n,char string[]);
void reverse(char string[]);
void write_to_file(char file_name[], int size, void* region_start_ptr);
void analazyer();
int count_length();
void* file_analyzer(void* arg);
void analyze(char *file_name, int file_index,int analyzer_indicator);
int write_to_files();
double rand2();
void* work_with_memory();

#endif
