#include "Server.h"
#include <sys/socket.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdatomic.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>

#include "Parser/Parser.h"
#include "main.h"
#include "ClientHandling.h"


typedef struct m_ {
    struct m_* next;
    struct m_* children;

    size_t id;
    char* author;
    char* message;
}message;

typedef struct {
    struct {
        int capacity;
        int amount;
        int* sockets;
    }clients;
    size_t prev_id;

    message* messages;
    _Atomic bool serverShouldStop;
}serverStruct;

void* serverMainThreadWorker(void *serverContextTemplate){
    serverStruct* context = ((serverStruct*)serverContextTemplate);

    bool mainWorkerShouldStop = false;

    // Starting up socket
    int serverSocket;
    if ((serverSocket = (socket(AF_INET, SOCK_STREAM | SOCK_NONBLOCK, 0))) == -1){
        perror("Server couldn't start");
        context->serverShouldStop = true;
        return NULL;
    }

    struct sockaddr_in sockAddr;
    sockAddr.sin_family = AF_INET;
    sockAddr.sin_addr.s_addr =INADDR_ANY;
    sockAddr.sin_port = htons(PORT);

    //bind server socket
    if ((bind(serverSocket, (struct sockaddr*)&sockAddr, sizeof(sockAddr))) < 0){
        perror("Error binding socket");
        context->serverShouldStop = true;
        return NULL;
    }

    listen(serverSocket, 255);

    while (!context->serverShouldStop){
        int ret = accept(serverSocket, NULL, NULL);

        if (ret >0){
            handleClient();
        }
        //Don't burn the cpu
        sched_yield();
    }
    puts("We are shutting down...");

    for (int i=0; i<context->clients.amount; i++){
        close(context->clients.sockets[i]);
    }
    close(serverSocket);
    return NULL;
}

void serverStart(){
    serverStruct* context = malloc(sizeof(serverStruct));
    context->serverShouldStop = false;
    context->clients.amount = 0;
    context->clients.capacity = 2;
    context->clients.sockets = malloc(sizeof(int)*2);
    context->prev_id = 0;
    context->messages = NULL;


    pthread_t serverThread;
    pthread_create(&serverThread,NULL,&serverMainThreadWorker,(void*)context);
    //Something with SIGPIPE
    {
        //include signal.h
    }

    char* line = NULL;
    size_t bufferLength = 0;

    while (!context->serverShouldStop){
        printf("> ");
        size_t charsNum = getline(&line,&bufferLength, stdin);
        if (charsNum == (size_t)-1){ //? I am not sure about that
            free(line);
            puts("Server got EOF. Abort.");
            return;
        }
        char** args = NULL;
        int amountOfWords = parseCommand(line, &args);
        if (amountOfWords == 0){
            //free(line);
            if (args != NULL){
                free(args);
            }
            continue;
        }
        if (strcmp("exit",args[0]) == 0){
            context->serverShouldStop = true;
        }
    }
    puts("Server stopped. Turning off");
    pthread_join(serverThread,NULL);
}
