#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define __USE_MISC 1    //for normal use of constants
                        // see math.h: 1064
#include <math.h>
#include "dbg.h"
#include "bmp_handling.h"

typedef struct string{
    char *string;
    int length;
    int string_size;
}string;

typedef enum double_conversion_result{
        CONVERSION_OK = 0,
        NOT_A_NUMBER,
        OVERFLOW,
        OUT_OF_MEMORY
}double_conversion_result;

string* new_string(){
    string *tmp = malloc(20*sizeof(char));
    tmp->length = 20;
    tmp->string = calloc(20,sizeof(char));

    return tmp;
}

void free_string(string *str){
    free(str->string);
    free(str);
}
void enlarge_string(string *str){
    char *copy = str->string;
    str->string = calloc(str->length*1.25,sizeof(char));
    str->length = str->length*1.25;
    strcpy(str->string,copy);
    free(copy);
}

int parse_str(string *str){
    int pointer = 0;
    int character;
    while ((character = getc(stdin)) !=EOF){
        if (character == '\n'){
            break;
        }
        str->string[pointer] = character;
        if (pointer ==str->length-1){
            enlarge_string(str);
        }
        pointer++;
        
    }
    str->string_size = pointer-1;
    return pointer;
}


double_conversion_result str_to_double(double *destination){
    bool sign = false;
    bool low_part = false;
    size_t part = 0;
    int character;
    int pointer = 0;
    string *low = new_string();
    while ((character = getc(stdin)) != EOF){
        if (character =='\n'){
            if (pointer || part){
                break;
            }
            return NOT_A_NUMBER;
        }
        if (((character > '9' || character < '0')&& 
                        character !='.' && character !='+' && 
                        character !='-')){
                    
                    return NOT_A_NUMBER;
            }
        if (character == '.'){
            low_part = true;
            continue;
        }
        if (character =='-'){
            sign = true;
            continue;
        }
        if (character=='+'){
            continue;
        }
        
        if (!low_part){
            part = part*10+character-'0';
        }else{
            low->string[pointer++] = character-'0';
            if (pointer ==low->length-1){
                enlarge_string(low);
            }
        }        
    }
    double return_val = (double)part;
   if (pointer){
        double val = 0;
        for (int i = pointer-1; i >0; i--){
            val = val*0.1+low->string[i];
        }
        val*=0.1;
        return_val+=val;
        if (sign){
            return_val*=-1;
        }
   }
    if (!part){
        return NOT_A_NUMBER;
    }
    
    *destination = return_val;
    free_string(low);
    return CONVERSION_OK;
    
}



int main(void){
    string *filename = new_string();
    puts("ъеъ file name here:");
    parse_str(filename);

    printf("Your file name: %s\nReading.....\n",filename->string);

    FILE *file = fopen(filename->string,"rb");

    if (file == NULL){
        puts("This file does not exist");
        exit(1);
    }

    bmp_image *result;
    read_bmp_status status = read_bmp(file,&result);

    if (status == READ_OK){
        puts("Read succesfull\nEnter degrees:");
        double tmp_double = 0;
        double_conversion_result d_conversion = str_to_double(&tmp_double);
        if (d_conversion ==CONVERSION_OK){
	    printf("turning image to %f degrees.....\n",tmp_double);
            bmp_rotate(result, tmp_double*M_PI / 180);//transform to radians
            puts("what name to save file?");
            string *file_savename = new_string();
            int read =parse_str(file_savename);
            bmp_save_status save_status = bmp_save(result,file_savename->string);
            if (save_status == SAVE_OK && read){
                puts("we saved file........");
            }else{
                puts("something's wrong while saving...............");
            }
        }else
        {
            puts("Something's wrong with getting degrees..............");
        }
        
    }else{
        puts("Can't open this file. Maybe it's a dir?");
    }
    
    return 0;

}
