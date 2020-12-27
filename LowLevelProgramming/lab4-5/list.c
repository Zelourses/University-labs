#include "list.h"
#include <unistd.h>

List *List_create(){
    return calloc(1,sizeof(List)); 
}

void List_add_front(List *list,int value){
    listNode *node = calloc(1,sizeof(listNode));
    check_mem(node);

    node->value = value;
    
    if (list->first == NULL){
        list->last=node;
        list->first=node;
    }else{
        node->next = list->first;
        list->first=node;
    }
    list->count++;

    return;
    error:
        return;
}

void List_add_back(List *list,int value){
    listNode *node = calloc(1,sizeof(listNode));
    check_mem(node);

    node->value = value;

    if (list->last == NULL){
        list->last=node;
        list->first=node;
    }else{
        list->last->next=node;
        list->last=node;
    }

    list->count++;
    return;
    error:
        puts("Some problem");
        return;
}

void List_destroy(List *list){
    listNode *current = list->first;
    listNode *next;
    while (current !=NULL)
    {   
        next = current->next;
        free(current);
        current = next;
    }
    free(list);
}

size_t List_count_value(List *list){
    if(!(List_count(list))){
        return 0;
    }
    size_t i = 0;
    //gets value from current node
    LIST_FOREACH(list,next,current){
        i+=current->value;
    }

    return i;
}
listNode *List_find(List *list,int index){
    LIST_FOREACH(list,next,current){
        if (!(index--))
        {
            return current;
        }      
    }
    return NULL;

}