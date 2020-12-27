#pragma once
#include <stdint.h>
typedef struct pixel{
    uint8_t r,g,b,a;
}pixel;

typedef struct image{
    uint32_t width;
    uint32_t height;
    pixel* data;
}image;

typedef struct coords{
    uint32_t x,y;
}coords;

void set_pixel_on_image(image *img,coords coordinates,pixel);

image* image_create(uint32_t height,uint32_t width);

void matrix3_multiply(double *m1,double *m2,double *destination);

void image_rotate(image* img,double radians);

image *image_copy(image *img);

pixel get_pixel(image *img, coords c);

coords matrix_move(double *matrix_3,coords c);

pixel get_pixel(image *img, coords c);

void image_destroy(image *img);

image* image_sepia(image *img);

image* image_sepia_sse(image *img);