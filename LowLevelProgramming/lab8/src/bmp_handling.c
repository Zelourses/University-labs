#include <stdlib.h>
#include "bmp_handling.h"
#include "dbg.h"

read_bmp_status read_bmp(FILE *file,image** destination){
    bmp_header header;
    fread(&header,sizeof(bmp_header),1,file);
    if (ferror(file)){
        return READ_IMAGE_FAIL;
    }
    if (header.bmp_type != BM_TYPE){
        return READ_UNSUPORTED_FILE;
    }
    if (header.bmp_bitCount !=24){
        return READ_BITS_PER_PIXEL_UNSUPORTED;
    }
    if (header.bmp_compression !=0){
        return READ_COMPRESSION_UNSUPORTED;
    }
    
    uint32_t image_start = header.bmp_offstetBits;

    uint32_t width = header.bmp_width*3;

    uint32_t width_padding = (4-width%4)%4;

    fseek(file,image_start,SEEK_SET);

    image *result = image_create(header.bmp_height,header.bmp_width);
    bmp_pixel *pixel_line = malloc(sizeof(bmp_pixel)*width);
    for(uint32_t i = 0;i<header.bmp_height;i++){
        fread(pixel_line,width,1,file);
        for (uint32_t j = 0; j<header.bmp_width; j++){
            set_pixel_on_image(result,
            (coords){
                j,
                result->height - i - 1 //image in BMP saves top pixels on bottom and vise versa
            },(pixel){
                pixel_line[j].r,
                pixel_line[j].g,
                pixel_line[j].b,
                255             //alpha channel
            });
        }
        fseek(file,width_padding,SEEK_CUR);
        
    }

    free(pixel_line);
    *destination = result;
    return READ_OK;
    
}

void bmp_rotate(bmp_image*img, double radioans){
   image_rotate(img,radioans);
}

bmp_image* bmp_sepia(image *img){
    return image_sepia(img);
}

bmp_save_status bmp_save(image *src,char *filename){
    FILE *file = fopen(filename,"w+b");

    if (ferror(file)){
        if (errno == EACCES){
            return NO_ACCESS;
        }
        return FILE_OPEN_PROBLEMS;      
    }
    uint32_t width = src->width*3;

    uint32_t width_padding = width%4 == 0? width:width-width%4 +4;    

    bmp_header header;
    header.bmp_type = BM_TYPE;
    header.bmp_file_size = sizeof(header)+width_padding*src->height;
    header.bmp_reserved1 = 0;
    header.bmp_reserved2 = 0;
    header.bmp_offstetBits = sizeof(header);
    header.bmp_headerSize = 40;
    header.bmp_width = src->width;
    header.bmp_height = src->height;
    header.bmp_planes = 1;
    header.bmp_bitCount = 24;
    header.bmp_compression = 0;
    header.bmp_image_size = 0;
    header.bmp_xpelsPerMetr = 1;
    header.bmp_ypelsPerMetr = 1;
    header.bmp_colorsUsed = 16777216; //256^3
    header.bmp_colorsImportant = 0;

    fwrite(&header,sizeof(header),1,file);

    if (ferror(file)){
        return WRITE_TO_FILE_PROBLEM;
    }

    bmp_pixel *pixels = malloc(width_padding);
    check_mem(pixels);

    for (uint32_t i = 0; i < src->height; i++){
        for (uint32_t index = 0; index < src->width; index++){
            pixel tmp = get_pixel(src,(coords){index,src->height-i-1});
            pixels[index] = (bmp_pixel){tmp.b,tmp.g,tmp.r};
        }
        fwrite(pixels,width_padding,1,file);
        if (ferror(file)){
            return WRITE_TO_FILE_PROBLEM;
        }
        
    }
    free(pixels);
    fclose(file);

    return SAVE_OK;
    error:
        return MEMORRY_ALLOCATION_ERROR;
}