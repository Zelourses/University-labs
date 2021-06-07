#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {
    if(argc < 2) {
        puts("lab: insufficient arguments. Try lab --help");
    }
    if (strcmp(argv[1], "--help") == 0){
        puts("This lab is working in 2 modes");
    }
}
