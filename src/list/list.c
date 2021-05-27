#include <stdio.h>
#include <stdlib.h>

#include "List/list.h"

#define COMMAND "df -h"
void listAllPartitions(){
    puts("Seriously? You can't write "COMMAND" yourself?..\nOk, here it is:\n"COMMAND);
    system(COMMAND);
}
