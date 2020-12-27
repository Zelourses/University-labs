#ifndef _list_h_
#define _list_h_ 

#include <stdlib.h>
#include "dbg.h"

struct listNode;
typedef struct listNode{
    struct listNode *next;
    int value;
}listNode;
typedef struct List{
    int count;
    listNode *first;
    listNode *last;
}List;
/*
 * Creates pointer to List sctruct that 
*/ 
List *List_create();
void List_add_front(List *list,int value);
void List_add_back(List *list,int value);

/*
 * Destroying all nodes and list itself
*/ 
void List_destroy(List *list);


/*
 * Counts values of elements
 * return int
 * 
*/
size_t List_count_value(List *list);

/*
 * Returns the element on the index place
 * index counts from zero
 * if element doesn't found, returns NULL
*/ 
listNode *List_find(List *list, int index);

#define List_count(LIST) ((LIST)->count)

/*
 * LIST - list that we are gonna use
 * NEXT - value that we must take(i mean, it's not double-linked list)
 * so why not to get rid of it?
 * VALUE - valie that we will use in our foreach
 * 
 * Example: (Code from list_destroy() function)
 * LIST_FOREACH(list,next,current){
 *       if(current->next){
 *          free(current);
 *      }
 * }
 * free(list->last);
 * free(list);
 * Here we take the current value and check if next !=NULL
 * (we do not want to free() last node, it will break the for loop)
 * so, after that we will delete last element and the lis itself
 * WARNING! don't try to free memory with that macros, use List_destroy()
*/ 
#define LIST_FOREACH(LIST,NEXT,VALUE) listNode *_node = NULL;\
    listNode *VALUE = NULL;\
    for(VALUE=_node = LIST->first;_node!=NULL;VALUE=_node=_node->next)
    

#endif