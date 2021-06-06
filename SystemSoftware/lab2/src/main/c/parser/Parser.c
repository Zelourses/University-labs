#include "Parser.h"

#include "stdio.h"
#include "stdbool.h"
#include "stdlib.h"

#define SPACE_CHAR ' '
#define NEW_LINE_CHAR '\n'

static int countWords(const char* line){
    size_t ptr = 0;
    bool in_space = true;
    char current;
    int counter = 0;

    while ((current = line[ptr])!= 0){
        if ((current != SPACE_CHAR && current != NEW_LINE_CHAR) && in_space){
            in_space = false;
            counter++;
        } else if (current == SPACE_CHAR || current == NEW_LINE_CHAR){
            in_space = true;
        }
        ptr++;
    }
    return counter;
}
int parseCommand(char* line, char*** result){
    if (line == NULL){
        return 0;
    }
    int amountOfWords = countWords(line);

    *result = malloc(sizeof(char*)*amountOfWords);

    size_t ptr = 0;
    size_t word_number = 0;
    bool in_space = true;
    char current;
    while ((current = line[ptr]) != 0){
        if ((current != SPACE_CHAR && current != NEW_LINE_CHAR) && in_space) {
            in_space = false;
            result[0][word_number] = &line[ptr];
            word_number++;
        } else if ((current == SPACE_CHAR || current == NEW_LINE_CHAR) && !in_space){
            line[ptr] = 0;
            in_space = true;
        }
        ptr++;
    }
    return amountOfWords;
}