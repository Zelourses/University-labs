#include "mem.h"
#include <unistd.h>
static void * unit_test(size_t size);
int main(void){
    #define MINIMAL_BLOCK_SIZE -3
    //int *test1 = unit_test(4095);
    //int *test2 = unit_test(10);
    //int *test3 = unit_test(-1);//MUST FAIL
    char *test4 = unit_test(8589934592-17); //8GiB
    //printf("Got for test4 address: %p",(void *)test4);
    int *test5 = unit_test(5006);
    
    //_free(test1);
    debug_heap();

    return 0;
}

static void *unit_test(size_t size){
    static int number_of_test=1;
    char *test = _malloc(size);
    if (test !=NULL){
        for (size_t i = 0; i < size; i++){
            test[i] = 'a';
        }
        printf("[%d TEST] PASSED: ALLOCATED %zu bytes.\n",number_of_test,size);
    }else{
        printf("[%d TEST] FAILED: TRIED ALLOCATE %zu bytes\n",number_of_test,size);
    }
    number_of_test++;
    debug_heap();
    return (void*)test;
}