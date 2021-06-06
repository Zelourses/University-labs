#pragma once

/*
 *       in: line to parse(must have \0)
 *           IMPORTANT: Program changing original line!
 *
 *      out:    result - pointer to array of arguments
 *  returns:    amount of words in array
 *
 *  Function will automatically allocate necessary amount of memory.
 *  If result not NULL it will cause to pointer loss!
 */
int parseCommand(char*line, char*** result);
