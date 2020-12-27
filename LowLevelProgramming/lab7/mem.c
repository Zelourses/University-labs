#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include "mem.h"

static mem *created = NULL;

static void *heap_init(size_t initial_size);
static void* mem_new(void *addr,size_t query, mem *last);

void *_malloc(size_t query){
    if (unlikely(query>>(sizeof(size_t)*8-1))){
        return NULL;
    }
    if (unlikely(MINIMAL_BLOCK_SIZE < 1)){
        printf("Cannot allocate %d bytes, too small\n",MINIMAL_BLOCK_SIZE);
        return NULL;
    }
    if (query < BLOCK_MIN_SIZE){
        query = BLOCK_MIN_SIZE;
    }
    if (unlikely(created == NULL)){
        printf("first call of malloc\n");
        return heap_init(query);
    }
    
    
    mem *last;
    void *result;
    for (last = created;last !=NULL;last = last->next){
        if ((last->is_free && 
        query<=last->capacity-sizeof(mem)-BLOCK_MIN_SIZE)||
        last->next==NULL ){
            break;
        }   
    }
    
    if (last->is_free && query<=last->capacity-sizeof(mem)-BLOCK_MIN_SIZE){
        mem new_mem;
        new_mem.is_free = true;
        new_mem.next = NULL;
        last->next = (mem*)((uint8_t*)(last)+sizeof(mem)+query);
        new_mem.capacity = last->capacity-sizeof(mem)-query;
        last->capacity =query;
        last->is_free = false;
        memcpy(last->next,&new_mem,sizeof(mem));
        return (void*)((uint8_t*)last+sizeof(mem));
        
    }else if (last->is_free && last->next ==NULL){
        size_t number_of_pages = (query-last->capacity)/MINIMAL_BLOCK_SIZE;
        if ((query-last->capacity)%MINIMAL_BLOCK_SIZE){
            number_of_pages++;
        }
        errno=0;
        //end of memory. literally
        void *addr = (uint8_t*)last+last->capacity+sizeof(mem);
        /*Compatibility issue: MAP_FIXED_NOREPLACE was introduced in
         * Linux kernel 4.17
         * could be possible problems with overlaping
        */
        result = mmap(addr,number_of_pages*MINIMAL_BLOCK_SIZE,
                        MAP_PROTECTION,
                        MAP_FLAGS | MAP_FIXED_NOREPLACE,-1,0);
        if (result == MAP_FAILED || errno){
            return mem_new(addr,query,last);
        }
        //Oh, we really did great work and now can enlarge the last block
        if ((number_of_pages*MINIMAL_BLOCK_SIZE+last->capacity)-query >= sizeof(mem)+BLOCK_MIN_SIZE){
            mem tail;
            tail.next = NULL;
            tail.is_free = true;
            tail.capacity = (number_of_pages*MINIMAL_BLOCK_SIZE+last->capacity)-
                            (query+sizeof(mem));
            last->next = (mem*)((uint8_t *)last+sizeof(mem)+query);
            memcpy(last->next,&tail,sizeof(mem));
        }
        last->capacity = query;
        last->is_free = false;

        return (void *)((uint8_t*)last+sizeof(mem));

    }else if (!(last->is_free)){
        return mem_new((uint8_t *)last+sizeof(mem)+last->capacity,query,last);
    }else{
        perror("Oh, it's a very strange situation");
        return NULL;
    }
    puts("somehow we skipped all and now here. How?");
    return NULL;
}
static void* mem_new(void *addr,size_t query, mem *last){
    void * result;
    size_t number_of_pages = (query+sizeof(mem))/MINIMAL_BLOCK_SIZE;
    if ((query+sizeof(mem))%MINIMAL_BLOCK_SIZE){
        number_of_pages++;
    }
    errno = 0;
    result = mmap(addr,number_of_pages*MINIMAL_BLOCK_SIZE,MAP_PROTECTION,
                    MAP_FLAGS,-1,0);
    if (result == MAP_FAILED || errno){
        perror(strerror(errno));
        return NULL;
    }
    mem head,tail;
    head.is_free = false;
    head.capacity = query;
    head.next = NULL;
    if (number_of_pages*MINIMAL_BLOCK_SIZE >=
            query+sizeof(mem)+BLOCK_MIN_SIZE){

        head.next = (mem*)((uint8_t*)result+query+sizeof(mem));

        tail.next = NULL;
        tail.is_free = true;
        tail.capacity = number_of_pages*MINIMAL_BLOCK_SIZE-
                        (query+sizeof(mem)*2);
        memcpy(head.next,&tail,sizeof(mem));
    }
    memcpy(result,&head,sizeof(mem));
    last->next = (mem *)result;
    //void * for clearness
    return (void*)((uint8_t*)result+sizeof(mem));
}
/*
 * reutrns pointer to allocated memory
 */ 
static void *heap_init(size_t initial_size){
    mem head,tail;
    void *result;
    
    errno = 0;
   if (initial_size<=MINIMAL_BLOCK_SIZE-(BLOCK_MIN_SIZE*2+sizeof(mem)*2)){
       /*
     * fd is ignored, but on some systems......
     * and also because of that on the end we have 0
     * see: man mmap on MAP_ANONYMOUS description
    */
    result = mmap(HEAP_START,MINIMAL_BLOCK_SIZE,MAP_PROTECTION,MAP_FLAGS,-1,0);
    if (result == MAP_FAILED || errno){
        perror(strerror(errno));
        return NULL;
    }
    
    head.is_free = false;
    head.capacity = initial_size;
    head.next = (mem*)((uint8_t*)result+(initial_size+sizeof(mem)));
    memcpy(result,&head,sizeof(mem));
    tail.next = NULL;
    tail.capacity = MINIMAL_BLOCK_SIZE-initial_size-sizeof(mem)*2;
    tail.is_free = true;
    memcpy(head.next,&tail,sizeof(mem));
   }else{
       size_t number_of_pages = (initial_size+sizeof(mem))/MINIMAL_BLOCK_SIZE;
       if ((initial_size+sizeof(mem))%MINIMAL_BLOCK_SIZE){
           number_of_pages++;
       }
       
        //upper comment on mmap
        result = mmap(HEAP_START,number_of_pages*MINIMAL_BLOCK_SIZE,MAP_PROTECTION,MAP_FLAGS,-1,0);
        if (result == MAP_FAILED || errno){
            perror(strerror(errno));
            return NULL;
        }
        head.is_free = false;
        head.capacity = initial_size;
        head.next = NULL;
        if (number_of_pages*MINIMAL_BLOCK_SIZE-(initial_size+sizeof(mem)) >= sizeof(mem)+BLOCK_MIN_SIZE){
            head.next = (mem*)((uint8_t*)result+(initial_size+sizeof(mem)));
            tail.next = NULL;
            tail.capacity = number_of_pages*MINIMAL_BLOCK_SIZE -initial_size-sizeof(mem)*2;
            tail.is_free = true;
            memcpy(head.next,&tail,sizeof(mem));
        }
        memcpy(result,&head,sizeof(mem));
   }
    created = (mem*)result;
    //(void*) could be deleted
    //stays for clearness
    return (void*)((uint8_t*)result+sizeof(mem));
}



void debug_heap(){
    puts("HEAP STATE:");
    mem *state = created;
    size_t i;
    printf("%-8s|%-16s|%-28s|%-5s\n","Number","Start address","Capacity","State");
    for(i = 0;state !=NULL;i++,state=state->next){
        puts("--------+----------------+----------------------------+-----");
        printf("%-8zu|%-16p|%22zu bytes|%5s\n",i,(void*)state,state->capacity,(state->is_free)?"FREE":"BUSY");
    }
    if (!i){
        printf("%25s\n","EMPTY");
    }
    puts("--------+----------------+----------------------------+-----");
}

void _free(void *memory){
    //header start
    memory = (uint8_t*)memory-sizeof(mem);
    mem *header = (mem*)memory;
    mem *nearest = header->next;
    if (header->next !=NULL && header->next == (mem*)((uint8_t*)header+sizeof(mem)+header->capacity)){
        if (nearest->is_free){
            header->next = nearest->next;
            header->capacity = header->capacity + sizeof(mem) +nearest->capacity;
        }
    }
    header->is_free = true;
    
}