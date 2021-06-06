#pragma once

#include "types.h"
#include <stdio.h>

#define likely(x)      __builtin_expect(!!(x), 1)
#define unlikely(x)    __builtin_expect(!!(x), 0)


/*
 * input: unsigned 32 bit number
 * output: amount of bytes to shift to get this number
 * !!!Undefined!!! with x == 0
 *
 * Genius function I got from https://github.com/joyent/syslinux/blob/master/com32/include/ilog2.h
 *
 * If you get 512 in here (default cluster size) you will get next:
 * 512 in 10 it's 0000.0000.0000.0000.0000.0010.0000.0000 in binary.
 * __builtin_clz(x) will return 22 - the number of zeroes in a row.
 * and then we will do 22 XOR 31:
 * 22 in binary - 0000.0000.0000.0000.0000.0000.0001.0110
 * 31 in binary - 0000.0000.0000.0000.0000.0000.0001.1111
 * after xor    - 0000.0000.0000.0000.0000.0000.0000.1001
 *
 * Which, magically, gives us 9!
 * and now,     - 0000.0000.0000.0000.0000.0000.0000.0001 << 9 ==
 *             == 0000.0000.0000.0000.0000.0010.0000.0000
 * and this gives us 512!
 *
 *
 * ilog because log is already taken by lib function
*/
static inline s32 ilog2(u32 x){
    return __builtin_clz(x) ^ 31;
}

static void dumpHex(const void* data, size_t size) {
    char ascii[17];
    size_t i, j;
    ascii[16] = '\0';
    for (i = 0; i < size; ++i) {
        printf("%02X ", ((unsigned char*)data)[i]);
        if (((unsigned char*)data)[i] >= ' ' && ((unsigned char*)data)[i] <= '~') {
            ascii[i % 16] = ((unsigned char*)data)[i];
        } else {
            ascii[i % 16] = '.';
        }
        if ((i+1) % 8 == 0 || i+1 == size) {
            printf(" ");
            if ((i+1) % 16 == 0) {
                printf("|  %s \n", ascii);
            } else if (i+1 == size) {
                ascii[(i+1) % 16] = '\0';
                if ((i+1) % 16 <= 8) {
                    printf(" ");
                }
                for (j = (i+1) % 16; j < 16; ++j) {
                    printf("   ");
                }
                printf("|  %s \n", ascii);
            }
        }
    }
}
