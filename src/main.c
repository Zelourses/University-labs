#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#include "List/list.h"
#include "work.h"


#define MINIMAL_AMOUNT_OF_ARGUMENTS 2
#define WORK_STRING "--work"
#define LIST_STRING "--list"
#define HELP_STRING "--help"

#define WORK_CHAR "-w"
#define LIST_CHAR "-l"
#define HELP_CHAR "-h"


typedef enum modes{
    NONE,LIST, WORK
} modes;


void printHelp(){
    puts("this program supports next arguments:\n\
    -h or --help\n\
         Print this help\n\
    -w or --work <path_to_filesystem>\n\
         Start of work with NTFS filesystem.\n\
    -l or --list\n\
         Print available devices");
}
void exit_text(char *argv[]){
    printf("%s: missing arguments\nTry '%s --help' for more information\n",argv[0],argv[0]);
    exit(0);
}
int main(int argc, char *argv[]){
    modes mode = NONE;
    if (argc == 1){
        exit_text(argv);
        return 0;
    }
    if (!strcmp(argv[1],HELP_STRING) || !strcmp(argv[1],HELP_CHAR)){
        printHelp();
        exit(0);
    }else if (!strcmp(argv[1],WORK_STRING) || !strcmp(argv[1],WORK_CHAR)){
        mode = WORK;
        if (argc != 3){
            printf("%s: missing agrument\n",argv[1]);
            exit(0);
        }
    }else if (!strcmp(argv[1],LIST_STRING) || !strcmp(argv[1],LIST_CHAR)){
        mode = LIST;
    }
    

    if (mode == LIST){
        listAllpartitions();
    }else if(mode == WORK){
        startWork(argv[2]);
    }else{
        exit_text(argv);
    }
    return 0;
}