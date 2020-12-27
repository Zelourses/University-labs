#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define __USE_MISC 1    //for normal use of constants
                        //see math.h:1064
#include <math.h>
#include "dbg.h"
#include "bmp_handling.h"
#include <sys/time.h>
#include <sys/resource.h>
#include<time.h>

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

int test_function(char *open_file, size_t times);
int user_interface();

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

int read_int(){
    int answer = 0;
    int result = 0;
    while (!result || result == EOF || answer == 0){
        result = scanf("%d",&answer);
        if (result ==0){
            puts("write 1 or 2");
        }
    }
    while (fgetc(stdin) !='\n')
        ;
    return answer;
}

int main(int argc, char *argv[]){
    if (argc == 1){
        return user_interface();
    }
    for (int i=0; i < argc; i++){
        if (!strcmp(argv[1],"-test")){
            test_function(argv[2],(size_t)atoll(argv[3]));
            break;
        }
        
    }
        
    return 0;

}
int user_interface(){
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
        puts("Read succesfull\nWhat to do with this image? [1 - Rotate, 2 - Sepia filter]");
        int answer = read_int();
        double tmp_double = 0;
        double_conversion_result d_conversion;   
        switch(answer){
            case 1:
                puts("On which degrees turn image?");
                d_conversion = str_to_double(&tmp_double);
                if (d_conversion ==CONVERSION_OK){
                    printf("turning image to %f degrees.....\n",tmp_double);
                    bmp_rotate(result, tmp_double*M_PI / 180);//transform to radians
                    
                }else{
                    puts("Something's wrong with getting degrees..............");
                }
            break;

            case 2:
                result = bmp_sepia(result);
                puts("Sepia done.");
            break;

            default:
                puts("write 1 or 2");
                return 2;
        }
        
        puts("what name to save file?");
        string *file_savename = new_string();
        int read =parse_str(file_savename);
        bmp_save_status save_status = bmp_save(result,file_savename->string);

        if (save_status == SAVE_OK && read){
            puts("we saved file........");
        }else{
            puts("something's wrong while saving...............");
            return 3;
        }
        
        
    }else{
        puts("Can't open this file. Maybe it's a dir?");
        return 1;
    }
    return 0;
}

int test_function(char *open_file, size_t times){
    printf("TESTING SSE and normal sepia instructions %zu times without saving and opening time(But with destroying time!)\n",times);
    FILE *file = fopen(open_file,"rb");
    struct rusage r;
    struct timeval start;
    struct timeval end;
    long time_result;
    time_t t = time(NULL);
    struct tm timer = *localtime(&t);
    printf("----------\nSTART OF WORK: %d-%02d-%02d %02d:%02d:%02d\n\n",
        timer.tm_year + 1900, timer.tm_mon + 1, timer.tm_mday, timer.tm_hour, timer.tm_min, timer.tm_sec);
    if (file == NULL){
        puts("This file does not exist:");
        printf("%s\n",open_file);
        exit(1);
    }

    bmp_image *result;
    read_bmp_status status = read_bmp(file,&result);
    uint32_t width = result->width*3;
    uint32_t width_padding = width%4 == 0? width:width-width%4 +4;
    printf("IMAGE INFO:\nNAME: %s\nWIDTH:%5d HEIGHT:%5d\nSIZE:%zu bytes\n",
        open_file,result->width,result->height,sizeof(bmp_header)+width_padding*result->height);
    bmp_image *img;

    if (status == READ_OK){
        getrusage(RUSAGE_SELF,&r);
        start = r.ru_utime;
        for (size_t i = 0; i < times; i++){
            img = image_sepia_sse(result);
            image_destroy(img);
        }
        getrusage(RUSAGE_SELF,&r);
        end = r.ru_utime;

        time_result = ((end.tv_sec - start.tv_sec)*1000000L)+end.tv_usec-start.tv_usec;
        printf("RESULT: SSE - %ld microseconds for %zu times or %f microseconds average\n",
            time_result,times,(double)time_result/(double)times);

        getrusage(RUSAGE_SELF,&r);
        start = r.ru_utime;
        for (size_t i = 0; i < times; i++){
            img = bmp_sepia(result);
            image_destroy(img);
        }
        getrusage(RUSAGE_SELF,&r);
        end = r.ru_utime;

        time_result = ((end.tv_sec - start.tv_sec)*1000000L)+end.tv_usec-start.tv_usec;
        printf("RESULT: normal - %ld microseconds for %zu times or %f microseconds average\n",
            time_result,times,(double)time_result/(double)times);

        t = time(NULL);
        timer = *localtime(&t);
        printf("----------\nEND OF WORK: %d-%02d-%02d %02d:%02d:%02d\n",
            timer.tm_year + 1900, timer.tm_mon + 1, timer.tm_mday, timer.tm_hour, timer.tm_min, timer.tm_sec);
        
    }else{
        puts("Can't open this file. Maybe it's a dir?");
        return 1;
    }

    return 0;
}