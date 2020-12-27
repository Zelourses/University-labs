#include <stdio.h>
#include "higher_order_functions.h"

List *List_map(List *list, int (*function)(int)){
    List *copy = List_create();
    LIST_FOREACH(list,next,current){
        List_add_back(copy,function(current->value));
    }
    return copy;
}

void List_foreach(List *list,void (*function)(int)){
    LIST_FOREACH(list,next,current){
        function(current->value);
    }
}

void List_map_mut(List *list, int (*function)(int)){
    LIST_FOREACH(list,next,current){
        current->value = function(current->value);
    }
}

double List_foldl(List *list, double (*function)(int,double), double a){
    double tmp = a;
    LIST_FOREACH(list,next,current){
        tmp = function(current->value,tmp);
    }

    return tmp;
}

List *List_iterate(int value,int length, int (*function)(int)){
    List *list = List_create();
    for (int i = 0; i < length; i++){
        int function_result = value;
        for (int j = 0; j< i; j++){
            function_result = function(function_result);
        }
        List_add_back(list,function_result);
    }

    return list;
    
}

static bool _list_save(List *list, const char*filename,char *mode){
    FILE *file = fopen(filename,mode);
    LIST_FOREACH(list,next,current){
        if(!(fwrite(&current->value,sizeof(int),1,file))){
            fclose(file);
            return false;
        }
    }
    fclose(file);
    return true;
}



static bool _List_load(List *list, const char*filename, char *mode){
    FILE *file = fopen(filename,mode);
    int value = 0;
    while (fread(&value,sizeof(int),1,file) == 1){
        List_add_back(list,value);
    }
    if (ferror(file)){
        fclose(file);
        return false;
    }
    fclose(file);
    return true;
}

bool List_load(List *list, const char*filename){
   return _List_load(list,filename,"r");
}

bool List_save(List *list, const char*filename){
    return _list_save(list,filename,"w");
}

bool List_desirialize(List *list,const char *filename){
    return _List_load(list,filename,"rb");
}

bool List_serialize(List *list,const char *filename){
    return _list_save(list,filename,"wb");
}

