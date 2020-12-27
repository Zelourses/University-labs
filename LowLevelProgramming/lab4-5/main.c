#include <stdio.h>
#include <stdlib.h>
#include <float.h>
#include "list.h"
#include "higher_order_functions.h"
static int error = 0;
static double rand2(){
    return ((double)rand()/(double)RAND_MAX);
}


static void function_apply(int value){
    printf("%d\n",value);
}
static int function_map(int value){
    return value*value;
}

static int function_power2(int value){    
    return value*2;
}


static int function_map2(int value){
    return value*value*value;
}

static double function_foldl(int value,double a){
    return a+=value;
}

static double function_foldl_minimum(int value,double a){
    return (a<(double)value)?a:(double)value;
}
static double function_foldl_maximum(int value,double a){
    return (a>(double)value)?a:(double)value;
}



static void find(List *list){
    int index = (int)(rand2()*List_count(list));
    listNode *node = List_find(list,index);

    printf("We found the list under index: %d, and it's value:%d \n",index,node->value);
}

static void print(List *list){
    printf("number of elements: %d\n",List_count(list));
    printf("Numbers: [");
    LIST_FOREACH(list,next,current){
        printf("%d",current->value);
        (current->next!=NULL)?printf(", "):puts("]");
    }
    puts("");
}
static void apply_higher_order_functions(List *list){

    puts("List_foreach:");
    List_foreach(list,function_apply);
    puts("");

    puts("List_map(x^2):");
    List *list_copy = List_map(list,function_map);
    print(list_copy);

    puts("List_map(x^3):");
    List *list_copy2 = List_map(list,function_map2);
    print(list_copy2);

    double value = 0;
    puts("List_foldl(sum):");
    value = List_foldl(list,function_foldl,value);
    printf("sum value = %f\n",value);

    puts("List_foldl(min):");
    value = DBL_MAX;
    value = List_foldl(list,function_foldl_minimum,value);
    printf("Minimum value = %f\n",value);

    puts("List_foldl(max):");
    value = DBL_MIN;
    value = List_foldl(list,function_foldl_maximum,value);
    printf("Maximum value = %f\n",value);

    puts("List_map_mut(on original, module):");
    //function from stdlib.h
    List_map_mut(list,abs);
    print(list);

    puts("List_iterate:");
    List *list_power2 =List_iterate(1,10,function_power2);
    print(list_power2);

    char *filename = "SHIET";
    printf("Writing list to file %s\n",filename);
    puts("inner value of list:");
    print(list);
    if(List_save(list,filename) == false){
        puts("Something went wrong while saving to file");
        exit(-1);
    }
    printf("Reading list from file %s\n",filename);
    List *list_load = List_create();
    if(List_load(list_load,filename) == false){
        puts("Loading failed.........");
        exit(-1);
    }
    puts("Here is the values:");
    print(list_load);

    puts("Working on serialization.....\n We will serialize this:");
    print(list);
    char *filename_bin = "SHIET.bin";
    if(List_serialize(list,filename_bin) == false){
        puts("Something went wrong while serializing to file");
        exit(-1);
    }
    List *list_bin = List_create();
    if(List_desirialize(list_bin,filename_bin) == false){
        puts("Something went wrong while deserializing file");
        exit(-1);
    }
    puts("What we desirialize: ");
    print(list_bin);

    List_destroy(list_bin);
    List_destroy(list_load);
    List_destroy(list_power2);
    List_destroy(list_copy2);
    List_destroy(list_copy);    
       
}
static int str_to_int(char *string,int amount){
    error = 0;
    int value=0;
    int sign = 1;
    int i = 0;
    if (amount==0){
        error = 1;
        return 0;
    }
    if (string[0] =='-'){
            sign = -1;
            i++;
            amount--;
        }
    for (; amount>0; amount--,i++){
        value=10*value+ (string[i]-'0');
        
    }
    value*=sign;
    return value;
    
}
static void get_data(List *list){
    int integer;
    char character=' ';
    char message[11] = {0};
    int counter = 0;
    int skip = 0;

    while ((character = getc(stdin)) !=EOF){
        if (character ==' '){
            skip = 0;
        }
        
        if (skip == 0 && counter <11 && character !=' ' && ((character >='0' && character <='9') || (character=='-' || character=='+'))){
            message[counter] = (char)character;
            counter++;
        }else if (skip == 0 && counter <11 && character ==' '){
            integer = str_to_int(message,counter);
            counter = 0;
            if (error!=1){
                List_add_front(list,integer);
            }
        }
        if (counter==11 && character >='0' && character <='9'){
            counter = 0;
            skip = 1;
        }
        if (character =='\n'){
            integer = str_to_int(message,counter);
            if (error!=1){
                List_add_front(list,integer);
            }
            break;
        }
        
    }
}

int main(void){
    List *list = List_create();
    get_data(list);
    print(list);
    find(list);
    apply_higher_order_functions(list);
    List_destroy(list);
    //print(list);
    return 0;
}

