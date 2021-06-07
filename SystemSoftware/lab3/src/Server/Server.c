#include "Server.h"
#include <sys/socket.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
//#include <fcntl.h>
#include <stdlib.h>
#include "string.h"
#include "stdatomic.h"

#include "Parser/Parser.h"

static _Atomic bool serverShouldStop;

void serverMainThreadWorker(void *){
    bool mainWorkerShouldStop = false;
    while (!mainWorkerShouldStop){

        if (serverShouldStop){
            mainWorkerShouldStop = true;
        }
    }
    puts("We stopped");
}

void serverWork(){
    pthread_t serverThread;
    pthread_create(&serverThread,NULL,serverMainThreadWorker,NULL);
    serverShouldStop = false;
    char* line = NULL;
    size_t bufferLength = 0;
    while (!serverShouldStop){
        size_t charsNum = getline(&line,&bufferLength, stdin);
        printf("> ");
        if (charsNum == EOF){
            free(line);
            puts("Server got EOF. Abort.");
            return;
        }
        char** args = NULL;
        int amountOfWords = parseCommand(line, &args);
        if (amountOfWords == 0){
            free(line);
            if (args != NULL){
                free(args);
            }
        }
        if (strcmp("exit",args[0]) == 0){
            serverShouldStop = true;
        }
    }
    puts("Server stopped by command. Turning off");
    pthread_join(serverThread,NULL);
}