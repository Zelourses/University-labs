#include "Commands.h"

#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "work/NTFS.h"
#include "work/InnerFunctions.h"

bool command_exit   (NTFSInfoStruct*, int,   char**);
bool command_help   (NTFSInfoStruct*, int,   char**);
bool command_ls     (NTFSInfoStruct*, int,   char**);
bool command_cp     (NTFSInfoStruct*, int,   char**);
bool command_pwd    (NTFSInfoStruct*, int,   char**);
bool command_cd     (NTFSInfoStruct*, int,   char**);

run_command RUN_COMMANDS_LIST[] ={
        {
            .name = "exit",
            .description = "Close partition and exit program, no arguments",
            .function = command_exit
        }, {
            .name = "help",
            .description = "shows short help about commands, 1 argument - shows longer description",
            .function = command_help
        }, {
            .name = "ls",
            .description = "list files and directories. With path, shows files and directories in it",
            .function = command_ls
        }, {
            .name = "cp",
            .description = "Copy files, 2 arguments - from to where you want to copy it",
            .function = command_cp
        }, {
            .name = "pwd",
            .description = "Shows current working folder",
            .function = command_pwd
        }, {
            .name = "cd",
            .description = "change folder, 1 argument - to what directory change",
            .function = command_cd
        }
};

int NUMBER_OF_COMMANDS = sizeof (RUN_COMMANDS_LIST)/sizeof (RUN_COMMANDS_LIST[0]);

bool command_exit(  NTFSInfoStruct* ntfs, int size, char** args){
    freeInode(ntfs->root_inode);
    free(ntfs);
    return false;
}
bool command_help(  NTFSInfoStruct* ntfs, int size, char** args){
    if (size > 0){
        for (int i = 0; i< NUMBER_OF_COMMANDS; i++){
            if(memcmp(RUN_COMMANDS_LIST[i].name,args[0], strlen(args[0]))==0){
                printf("%s:\n%s\n",RUN_COMMANDS_LIST[i].name,
                                    RUN_COMMANDS_LIST[i].description);
                return true;
            }
        }
        puts("No matching commands found, try 'help'");
        return true;
    }

    for (int i = 0; i< NUMBER_OF_COMMANDS; i++) {
        printf("%s - %s\n",RUN_COMMANDS_LIST[i].name,RUN_COMMANDS_LIST[i].description);
    }

    return true;
}
bool command_cp(    NTFSInfoStruct* ntfs, int size, char** args){
    if (size < 1){
        puts("Unspecified what to copy and where. Abort");
        return true;
    }
    if (size <2){
        puts("Unspecified where to copy. Abort");
        return true;
    }

    ntfs_inode* inputInode = ntfs->current_inode;
    NTFSFindInfo * result;
    int err = findNodeByName(ntfs,args[0], &inputInode, &result);
    if (err == -1){
        puts("No such file or directory. Abort");
        return true;
    }
    err = copyByInode(ntfs,result->result, args[1]);
    if (err == -1){
        puts("Some error occurred");
        return true;
    }
    puts("Copied");
    return true;
}

#define DIR_NAME "<DIR>"
#define FILE_NAME "<FILE>"
bool command_ls(    NTFSInfoStruct* ntfs, int size, char** args){
    NTFSFindInfo* result_info = malloc(sizeof(NTFSFindInfo));
    result_info->result = ntfs->current_inode;

    int err = ntfsReadDir(ntfs, &(result_info->result));
    if (err == -1 )
        return true;

    ntfs_inode* tmp_inode =result_info->result->next;

    while (tmp_inode != NULL){
        if (tmp_inode->type & MFT_RECORD_IS_DIRECTORY){
            printf("%-8s%s\n",DIR_NAME,tmp_inode->filename);
        } else {
            printf("%-8s%s\n",FILE_NAME,tmp_inode->filename);
        }
        tmp_inode =tmp_inode->next;
    }

    freeInode(result_info->result->next);
    result_info->result->next = NULL;

    result_info->result = NULL;
    result_info->start = NULL;

    free(result_info);

    return true;
}
bool command_pwd(   NTFSInfoStruct* ntfs, int size, char** args){
    ntfs_inode* temp = ntfs->root_inode->next;
    printf("/");
    while (temp != NULL){
        printf("%s",temp->filename);
        temp = temp->next;
        if (temp != NULL)
            printf("/");
    }
    puts("");
    return true;
}
bool command_cd(    NTFSInfoStruct* ntfs, int size, char** args){
    if (size < 1 || strcmp(args[0], ".") == 0)
        return true;
    if (strcmp(args[0], "..")==0){
        if (ntfs->current_inode->mft_number == FILE_root){
            return true;
        }
        ntfs_inode* tmp =ntfs->current_inode->parent;
        freeInode(ntfs->current_inode);
        ntfs->current_inode =tmp;
        ntfs->current_inode->next = NULL;
        return true;
    }

    NTFSFindInfo* result;

    int error = findNodeByName(ntfs, args[0], &(ntfs->current_inode), &result);

    if(error == -1){
        goto nothing;
    }

    if (result->result->type & MFT_RECORD_IS_DIRECTORY){
        ntfs->current_inode->next = result->start->next;
        result->start->next->parent = ntfs->current_inode;
        ntfs->current_inode =result->result;
        result->start->next = NULL;
        freeInode((result->start));
        free(result);
        return true;
    }else{ //If it's a file
        freeInode(result->start);
        free(result);
        puts("Not a directory");
        return true;
    }

    nothing:
        puts("No such file or directory");
        return true;
}