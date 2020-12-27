#include "image.h"
#include <stdbool.h>
#define __USE_MISC 1    //for normal use of constants
                        // see math.h: 1064
#include <math.h>
#include <string.h>
#include <stdlib.h>

void set_pixel_on_image(image *img,coords coordinates,pixel p){
    img->data[img->width*coordinates.y+coordinates.x] = p;
}

image* image_create(uint32_t height,uint32_t width){
    image *result = malloc(sizeof(image));
    result->height = height;
    result->width = width;
    result->data = malloc(sizeof(pixel)*width*height);

    return result;
}

void matrix3_multiply(double *m1,double *m2,double *destination){
    destination[0] = m1[0]*m2[0]+m1[1]*m2[3]+m1[2]*m2[6];
    destination[1] = m1[0]*m2[1]+m1[1]*m2[4]+m1[2]*m2[7];
    destination[2] = m1[0]*m2[2]+m1[1]*m2[5]+m1[2]*m2[8];

    destination[3] = m1[3]*m2[0]+m1[4]*m2[3]+m1[5]*m2[6];
    destination[4] = m1[3]*m2[1]+m1[4]*m2[4]+m1[5]*m2[7];
    destination[5] = m1[3]*m2[2]+m1[4]*m2[5]+m1[5]*m2[8];

    destination[6] = m1[6]*m2[0]+m1[7]*m2[3]+m1[8]*m2[6];
    destination[7] = m1[6]*m2[1]+m1[7]*m2[4]+m1[8]*m2[7];
    destination[8] = m1[6]*m2[2]+m1[7]*m2[5]+m1[8]*m2[8];
}

/*
 * for normal work of future pixel placement we do this:
 * we ask every pixel their place before rotaiotn happened,
 * and paint pixels with needed colors
 * 
 * For example:
 * In this situation we ask every pixel where to place it
 * image n*m pixels
 * ||
 * \/                             
 * _____                        /\  
 * |_|_|                      x/x \
 * |_|_| => rotating... =>     \  /x  
 * ¯¯¯¯¯                        \/
 * now we have image with some problems:
 * because pixel have finite coords in space, we must round our
 * coordinates that we get after rotation by rotation(hehe) matrix
 * so, we could possibly have some pixels that just black or have invalid color
 * to eliminate this defect we "ask" every pixel their previous state,
 * like we rotated image and now seek home place of every pixel
 * this technique will pass every pixel and get right color for them
 * 
 * important note: rotation is happening in the center of image by dividing
 * width and height by 2
 * 
*/
void image_rotate(image* img,double radians){
    /*
     * Some funny thing. We need to swap width with height, cause, you know,
     * picture of 1x3 px can't be placed in 3x1 px image
     * So, we do this:
     * We have a circle that represents our rotation:(imagine that it's a circle)
     * 
     * (imagine that zero here)
     *    0             we must swap width and height after 45° and stop swapping 
     *  _____           after 135° and vise versa after 180°(or something like that)
     * /  |  \          and now we could do this:
     * |  o  |          we have x degrees, so:
     * \_____/          45° < x < 135° ; add 45°, because we work with previous data 
     *                  (if we worked with normal rotation, not backwards, we would sub 45°)
     *                  90° < (x+45°) < 180°
     *                   swapping after 180° is equivelent to rotation before, so
     *                  (90° < (x+45°) < 180°) % 180 (it possible in math, i checked)
     *                  90° < (x+45°)%180 < 0° ; we know that 0° is the same as 180°, so we can just delete them
     *                  (hope math can do this)
     *                  90° < (x+45°)%180° ; change positions
     *                  (x+45°)%180° > 90°
     *                  looks like line below, don't you think?
     * 
    */ 
    bool need_swap_width_height = fmod(fabs(radians)+M_PI_4,M_PI)>M_PI_2;

    // see https://en.wikipedia.org/wiki/Rotation_matrix
    // and also https://en.wikipedia.org/wiki/Affine_transformation

    //minuses before radians because it's reverse rotation
    double reverse_rotation_matrix[] = {
        cos(-radians), -sin(-radians),  0,
        sin(-radians),  cos(-radians),  0,
            0,          0,              1 
    };

    /* if (radians != 90*M_PI/180){
        uint32_t real = (uint32_t)sqrt((img->height)*(img->height)+(img->width)*(img->width));
        uint32_t height_change = real - img->height;
        uint32_t width_change = real - img->width;

        //img->width = real;
        //img->height = real;
    } */

    double center_x = (double)img->width/2.0;
    double center_y = (double)img->height/2.0;

    double center_matrix[] = {
        1, 0, -center_x,
        0, 1, -center_y,
        0, 0, 1
    };

    double uncenter_matrix[] = {
        1, 0, center_x,
        0, 1, center_y,
        0, 0, 1
    };

    double correction[] = {
        1, 0, 0,
        0, 1, 0,
        0, 0, 1
    };

    image *src_img = image_copy(img);

    if (need_swap_width_height){
        //uint32_t real = (uint32_t)sqrt((img->height)*(img->height)-(img->width)*(img->width));
        uint32_t tmp = img->width;
        img->width = img->height;
        img->height = tmp;

        correction[2] = center_x - center_y;
        correction[5] = center_y - center_x;
    }
    

    //hard thing to understand
    double center_rotation[9];
    double full_rotation[9];
    double transform_matrix[9];
    matrix3_multiply(uncenter_matrix,reverse_rotation_matrix,center_rotation);
    matrix3_multiply(center_rotation, center_matrix,full_rotation);
    matrix3_multiply(full_rotation,correction,transform_matrix);

    //idea that described in comment on this function(about black pixels)
    for (uint32_t x = 0; x < img->width; x++){
        for (uint32_t y = 0; y < img->height; y++){
            coords dest = {x,y};
            coords src = matrix_move(transform_matrix,dest);
            pixel src_pixel = get_pixel(src_img,src);
            set_pixel_on_image(img,dest,src_pixel);
        }
        
    }
    image_destroy(src_img);

}

image *image_copy(image *img){
    image *new_img = image_create(img->height,img->width);
    memcpy(new_img->data,img->data,sizeof(pixel)*img->height*img->width);
    return new_img;
}
coords matrix_move(double *matrix_3,coords c){
    // 6 7 and 8 in matrix - for z, we don't use that
    return (coords){
        floor(matrix_3[0]*(c.x+0.5) +matrix_3[1]*(c.y+0.5) + matrix_3[2]),
        floor(matrix_3[3]*(c.x+0.5)+matrix_3[4]*(c.y+0.5)+ matrix_3[5])
    };
}

pixel get_pixel(image *img, coords c){
    if (c.x < 0 || c.x >=img->width || c.y < 0 || c.y> img->height){
        //default pixel
        return (pixel){0,0,0,255};
    }
    return img->data[img->width*c.y+c.x];
}

void image_destroy(image *img){
    free(img->data);
    free(img);
}
