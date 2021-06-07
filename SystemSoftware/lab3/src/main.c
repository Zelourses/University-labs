#include <stdio.h>
#include <string.h>
#include <Server/Server.h>

int main(int argc, char *argv[]) {
    if(argc < 2) {
        puts("lab: insufficient arguments. Try lab --help");
    }
    if (strcmp(argv[1], "--help") == 0){
        puts("This lab is working in 2 modes:\n"
             "--server: server mode\n"
             "--client: client mode");
    }
    if (strcmp(argv[1],"--server") == 0){
        serverStart();
    } else {
        puts("TODO: implement");
    }
}
