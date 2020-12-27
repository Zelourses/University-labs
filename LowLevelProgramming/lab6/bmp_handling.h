#pragma once
#include <stdint.h>
#include <stdio.h>
#include "image.h"

//BM in 
static const uint16_t BM_TYPE = 0x4D42;

typedef image bmp_image;
typedef struct __attribute__((packed)) bmp_header{
    //https://ru.bmstu.wiki/BMP_(Bitmap_Picture)
    //or in Low-Level programming by Igor Zhirkov chapter 13, page 257

    //BitMapFileHeader start
    uint16_t bmp_type;
    uint32_t bmp_file_size;
    uint16_t bmp_reserved1;
    uint16_t bmp_reserved2;
    uint32_t bmp_offstetBits;
    //BitMapFileHeader end

    //BitMapInfoHeader
    uint32_t bmp_headerSize;
    uint32_t bmp_width;
    uint32_t bmp_height;
    uint16_t bmp_planes;
    uint16_t bmp_bitCount;
    uint32_t bmp_compression;
    uint32_t bmp_image_size;
    uint32_t bmp_xpelsPerMetr;
    uint32_t bmp_ypelsPerMetr;
    uint32_t bmp_colorsUsed;
    uint32_t bmp_colorsImportant;
    //BitMapInfoHeader end

}bmp_header;

typedef struct __attribute__((packed)) bmp_pixel{
    uint8_t b,g,r;
}bmp_pixel;


typedef enum read_bmp_status{
    READ_OK = 0,
    READ_UNSUPORTED_FILE,
    READ_IMAGE_FAIL,
    READ_BITS_PER_PIXEL_UNSUPORTED,
    READ_COMPRESSION_UNSUPORTED
}read_bmp_status;

typedef enum bmp_save_status{
    SAVE_OK = 0,
    NO_ACCESS,
    FILE_OPEN_PROBLEMS,
    WRITE_TO_FILE_PROBLEM,
    MEMORRY_ALLOCATION_ERROR
}bmp_save_status;

read_bmp_status read_bmp(FILE *file,image** destination);

void bmp_rotate(image*img, double radioans);

bmp_save_status bmp_save(image *src,char *filename);