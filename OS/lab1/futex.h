#ifndef _futex_h_
#define _futex_h_
/*
 * Realisation of futex from man futex
 * Funny thing: in man it's a little bit broken, because uaddr2 is not used
 * so, i changed code to be more normal (where uaddr2 were was uaddr)
 * Looks ugly
*/
int futex(int *uaddr, int futex_op, int val,const struct timespec *timeout, int *uaddr2, int val3);

/*
 * code that waits, on futex. 
 * see https://habr.com/ru/company/infopulse/blog/418705/
*/
void wait_on_futex_value(int* futex_addr, int val);

/*
 * code that wakes threads, on futex. 
 * see https://habr.com/ru/company/infopulse/blog/418705/
*/
void wake_futex_blocking(int* futex_addr);


/*
 * below you could see the lock realization
 * looks very ugly
 * how somebody wrote in kernel/futex.c:"The futexes are cursed"
 * idea of this RW lock was taken from here:
 * https://en.wikipedia.org/wiki/Readers%E2%80%93writer_lock 
*/ 

/*
 * Primitive of synchronization on write
 * here we are using atomic compare
 * i believe that is right decision
 * 
*/
void write_file_lock(int index);
void write_file_unlock(int index);
void read_file_lock(int index);
void read_file_unlock(int index);

#endif