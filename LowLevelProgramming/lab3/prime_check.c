#include <stdio.h>

int prime_check(size_t number){
    for (size_t i =2; i<number-1; i++){
        if (number%i==0)
            return 0;
    }
    return 1;
    
}
int main(void){
    size_t check=0;
    scanf("%lu",&check);
    printf("number is %s\n", (prime_check(check)>0)?"prime": "not prime");
}
