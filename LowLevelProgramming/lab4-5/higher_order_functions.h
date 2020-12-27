#ifndef _higher_order_functions_h_
#define _higher_order_functions_h_
#include "list.h"
#include <stdbool.h>

/*
 * Accepts pointer to list and function that accepts value of list
 * function work with value and DON'T changes it
*/
void List_foreach(List *list,void (*function)(int));


/*
 * Accepts pointer to list with work for and function
 * function must accept int(value from node) and return the result
 * of some inner operation with this list
 * DON'T changes the array
*/ 
List *List_map(List *list, int (*function)(int));

void List_map_mut(List *list, int (*function)(int));

/*
 * Accepts list, function that accepts value and a, and starting value of a
 * uses function on every value and changes a to pass it again
 * DON'T changes the array
*/
double List_foldl(List *list, double (*function)(int,double), double a);

List *List_iterate(int value,int length, int (*function)(int));

bool List_save(List *list, const char*filename);

bool List_load(List *list, const char*filename);

bool List_serialize(List *list,const char *filename);

bool List_desirialize(List *list,const char *filename);

#endif