#pragma once

#define _USE_MISC 
#define __USE_MISC  //for hiding IDE error in mmap with MAP_ANONYMOUS flag

#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <malloc.h>
#include <sys/mman.h>
#include <stdbool.h>

#define likely(x)      __builtin_expect(!!(x), 1)
#define unlikely(x)    __builtin_expect(!!(x), 0)

#define HEAP_START ((void*)0x04040000)

#define BLOCK_MIN_SIZE (2*sizeof(size_t))

//0x1000 we are aligning all!
#define MINIMAL_BLOCK_SIZE 4096


#define MAP_PROTECTION PROT_READ | PROT_WRITE
#define MAP_FLAGS MAP_ANONYMOUS | MAP_PRIVATE

#pragma pack(push,1)
typedef struct mem{
    struct mem *next;
    size_t capacity;
    bool is_free;
}mem;
#pragma pack(pop)

void *_malloc(size_t query);
void _free(void *memory);

#define DEBUG_FIRST_BYTES 4

void memalloc_debug_struct_info(FILE *file, mem const* const address);

void memalloc_debug_heap(FILE *file, mem const* ptr);
void debug_heap();
