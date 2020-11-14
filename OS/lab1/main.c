#include <stdio.h>
#include <pthread.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <pthread.h>
#include <unistd.h>
#include "dbg.h"
#include <stdint.h>
#include <stdlib.h>
#include "futex.h"
#include "main.h"


//file descriptors are bad. fopen With FILE struct better
int urandom_fd;
RWlock rwlocks[FILES_COUNT]={0};

/*
 * we are counting length of filename for our needs to get the right amount of space
*/
int count_length(){
    int copy_files_count = FILES_COUNT;
    int counter=0;
    while (copy_files_count !=0)
    {
        copy_files_count/=10;
        counter++;
    }
    return counter;
}

void analazyer(){
    int threads_count=READ_THREADS_COUNT;
    puts("Start of analyzing.....");

    pthread_t *threads=malloc(threads_count*sizeof(pthread_t));
    int *thread_indicator=malloc(threads_count*sizeof(int));    //array of integers

    for(int i=0;i<threads_count;i++){
        thread_indicator[i]=i;
        pthread_create(&threads[i],NULL,file_analyzer,&thread_indicator[i]);
    }

    /* for(int i=0;i<threads_count;i++){
        pthread_join(threads[i],NULL); // waits until all threads terminate
    } */

    free(threads);
    free(thread_indicator);

}

void* file_analyzer(void* arg){
    int indicator = *(int*)arg;
    while (1){
        for (int i = 0; i < FILES_COUNT; i++)
        {
            char file_name[count_length(i)+1];
            itoa(i,file_name);
            analyze(file_name,i,indicator);
        }
        
    }
    return NULL;
    
}
void analyze(char *file_name, int file_index,int analyzer_indicator){
    read_file_lock(file_index);

    int file_descriptor = open(file_name,O_RDONLY,FILE_OPEN_MODE);

    if(file_descriptor<0 && errno == ENOENT){
        printf("[ANALYZER %4d] file %s does not exist on that moment.....\n",analyzer_indicator,file_name);
        read_file_unlock(file_index);
        wait_on_futex_value(&rwlocks[file_index].file_created,0);
        return; /* VERY IMPORTANT!
                 * because if we don't exit after we discover that
                 * file doesn't exist, we will try to find value inside 
                 * random piece of memory( lseek, read_bytes on
                 * the next lines)
                 * because file_descriptor is missing!
                */

    }else if(file_descriptor <0){
        printf("Error while reading file %s, errno: %d\n",file_name,errno);
        exit(1);
    }

    off_t size = lseek(file_descriptor,0,SEEK_END);//find end of file to get size
    lseek(file_descriptor,0,SEEK_SET);//return the pointer to the start of file

    uint32_t *content = malloc(size);//now we have our memory for file
    ssize_t read_bytes= read(file_descriptor,content,size); // read all file to memory(oof)

    close(file_descriptor);
    read_file_unlock(file_index);

    
    /*
     * This is from math work
     * if we want to find avg we can user this formula
     * 
     * prev = avg of([0] and [1]) value from array(on the start)
     * after that prev changes to previous avg value
     * 
     * current = [i] value from array
     * (for n>2)
     *
     *       prev*(n-1)+current  
     * avg= ___________________
     *              n
     * 
     * so, by that we will get avg even if file VERY long
     * (and also we can check the overflow(but why?))
    */
    long double avg = (content[0]+content[1])/2;

    for (size_t i = 2; i < read_bytes/sizeof(uint32_t); i++){
        avg = (avg*(i-1)+content[i])/i;
    }

    printf("[ANALYZER %4d] file %s average equals: %Lf\n end of work....\n",analyzer_indicator,file_name,avg);

    free(content); //Don't forget to free this!

}



int write_to_files(void *TargetRegionPtr){
    puts("Starting to write data......");
    for (int i = 0; i < FILES_COUNT; i++){   
        char file_name[count_length(i)+1];//array of some size;
        itoa(i,file_name);

        int size = FILE_SIZE;
        if(i==FILES_COUNT - 1){//if this a last file, we set the right size(5Mb standart)
            size = MEMORY - FILE_SIZE*(FILES_COUNT-1);
        }
        
        //first things first, we must aquire lock
        write_file_lock(i);
        write_to_file(file_name,size, (uint8_t*)TargetRegionPtr+FILE_SIZE*i);

        rwlocks[i].file_created = 1;
        wake_futex_blocking(&rwlocks[i].file_created); 

        write_file_unlock(i);


    }
    return 0;   
}
//generating random number
double rand2(){
    return (double)rand()/(double)RAND_MAX;
}


/*
 * Function to write to the file with specific name
 * fills it by blocks of IO_SIZE size
 * 
 * this will be a long writing.....
*/ 
void write_to_file(char file_name[], int size, void* region_start_ptr){
    int file_descriptor = open(file_name, O_WRONLY | O_CREAT, FILE_OPEN_MODE); //now we have our file descriptor
    check(file_descriptor >=0,"We have a problem with opening a file");

    int blocks_count = size / IO_SIZE;
    if(blocks_count<size){
        blocks_count++;
    }
    /*
     * Now we know the amount of blocks we need to fill our file
     * Let's start
    */

   int block_size;
   for (int i = 0; i < blocks_count; i++){
       block_size = IO_SIZE;
       if (i==blocks_count-1){
           block_size = size - IO_SIZE*(blocks_count-1);
       }
        /*
        * This will return the random region start to write
        * works by rand2, that will return number from 0 to 1 inclusive
        * then, we get the FILE_SIZE and divide it by BLOCK_SIZE
        * so by that we get the block that we need to write
        * and so we again multiply it by IO_SIZE to get the needed block
        */
        void *write_region_start = (uint8_t*)region_start_ptr+((int)(rand2()*size/block_size)*block_size);

       ssize_t size =  write(file_descriptor,write_region_start,block_size);
       
       check(size >-1,"Oh shit, we have a problem with writing");
       printf("Writing %d of %d blocks to file_name=%s\r",i+1,blocks_count,file_name);
    
   }
   puts("");//fancy look :>
   error:
    close(file_descriptor);
    return;
}
/*
 * This is the realisation of non-standart function itoa, that transforms integer to string
 * very simple, can't work with negative numbers, and gets only int
 * and don't have error checking
 * 
 * get the integer and place to write it
 * 
 * from K&R
*/
void itoa(int n,char string[]){
    int i=0;
    do{
        string[i++]=n % 10+'0'; //hehe, little hack with '0'
    } while ((n /=10)>0);
    string[i]='\0'; //null-pointer...
    reverse(string);
}
/*
 * Function from K&R for itoa work 
 * revesing the string
*/
void reverse(char string[]){
    int i,j;
    char c;

    for ( i = 0, j=strlen(string)-1; i < j; i++,j--){
        c=string[i];
        string[i]=string[j];
        string[j]=c;
    }
    
}

void* work_with_memory(){
    printf("start of work, generationg address on: %p\n",MMAP_START_ADDRESS);
    void* TargetRegionPtr = mmap(MMAP_START_ADDRESS, MEMORY, MMAP_FLAGS, MMAP_TYPE, -1, 0);       //see: man mmap
    check(TargetRegionPtr!=MAP_FAILED,"Map creation failed");
    printf("real starting address: %p\n",TargetRegionPtr);

    urandom_fd = open("/dev/urandom",O_RDONLY); //получить дексриптор urandom
    check(urandom_fd>0,"Problem with geting file descriptor");

    pthread_t threads[THREADS_COUNT];
    thread_data thread_addr[THREADS_COUNT];

    for (size_t i = 0; i < THREADS_COUNT; i++){
        thread_addr[i].thread_number = i;
        thread_addr[i].addres = TargetRegionPtr;
        pthread_create(&threads[i], NULL,thread_handler,&thread_addr[i]);
    }
    //here we are trying to stop their work (safety first)
    for (int i = 0; i < THREADS_COUNT; i++){
        pthread_join(threads[i],NULL); //see: man pthread_join, we do not need to know return value, so, NULL
    }

    //You must close all your connections!
    close(urandom_fd);
    
    /*
    *finally, we have our region that contains all our data
    * now we must write this somewhere
    * return from this
    */
        
    return TargetRegionPtr;

    error:
        exit(1);
        return NULL;
}
/*
 *   thread_handler used by every thread to fill the file's chunk memory
 *   it's using chunk_size to get the size of chunk that it must use
 *   BUT 105MB can't be divided on 88 without residue, so we use little hack for this   
*/
void* thread_handler(void *arg){
    int chunk_size = MEMORY / THREADS_COUNT;
    thread_data *data = (thread_data*)arg;
    void *TargetRegionPtr = data->addres;
    if (data->thread_number == THREADS_COUNT-1){     /*
                                                * little hack - we will recreate the right value of chunk_size
                                                */
        chunk_size=MEMORY % THREADS_COUNT +(MEMORY / THREADS_COUNT);
    }
    /**
    * Now we know the size of chunk to write here, let's start real work(filling memeory)
    * we read urandom and fill memory with this data
    * funny thing - this notorious second argument is calculate where we must move
    * by moving this by bytes(we believe that MEMORY size written in bytes)
    *  
    **/
    read(urandom_fd,(void*)((uint8_t *)TargetRegionPtr+*((int*)arg)*(MEMORY / THREADS_COUNT)),chunk_size);
    return NULL;    //why we need to return something?
}
void free_data(void *TargetRegionPtr){
    munmap(TargetRegionPtr,FILE_SIZE);
}
int main(/* int argc, char *argv[] */void){
    analazyer();
    void *ptr;
    while (1){
        if ((ptr = work_with_memory()) !=NULL){
            write_to_files(ptr);
            free_data(ptr);
        }

    }
    
}

typedef struct aaa{
    int a;
}aaa;

aaa vvv = {.a = 10};

void bbb(aaa* aa){
    printf("here: %d",aa->a);
}