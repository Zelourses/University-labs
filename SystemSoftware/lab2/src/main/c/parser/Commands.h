#pragma once
#include <stdbool.h>
#include <work/Handler.h>

typedef struct {
    char* name;
    char* description;
    bool (*function)(NTFSInfoStruct*, int, char**);
}run_command;

extern run_command RUN_COMMANDS_LIST[];
extern int NUMBER_OF_COMMANDS;
