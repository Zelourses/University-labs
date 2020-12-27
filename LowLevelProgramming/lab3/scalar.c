#include <stddef.h>
#include <stdio.h>

#define ARRAY_SIZE 8

const int array1[] = {12,7,23,57,1,673,234,612};
const int array2[] = {732,7,32,67,34,568,45,567};


size_t scalar_product(const int first_array[], const int second_array[], int size){
    size_t result=0;
    for (; size>0;size--, result += first_array[size]*second_array[size])
		; 
    return result;
}
void print_array(const int array[], size_t length){
    for (size_t i = 0; i < length; i++)
    {
        printf("%d%c ",array[i], (i!=length-1)? ',': '\0');
    }
}


int main(void){
    printf("First array: ");
    print_array(array1, ARRAY_SIZE);
    printf("\nSecond array: ");
    print_array(array2, ARRAY_SIZE);
    
    printf("\nscalar product: %lu\n", scalar_product(array1,array2, sizeof(array1)/sizeof(int)));
}