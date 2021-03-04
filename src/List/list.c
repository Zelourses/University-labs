#include "List/list.h"
#include <stdio.h>
#include <stdlib.h>
#define COMMAND "df -h"
void listAllpartitions(){
    puts("Seriously? You can't write "COMMAND" yourself?..\nOk, here it is:\n"COMMAND);
    system(COMMAND);
}