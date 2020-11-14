#include <stdlib.h>
#include <linux/futex.h>
#include <sys/syscall.h>
#include <errno.h>
#include <time.h>
#include <stdatomic.h>
#include <unistd.h>
#include "futex.h"
#include <stdio.h>
#include "main.h"



int futex(int *uaddr, int futex_op, int val,const struct timespec *timeout, int *uaddr2, int val3){
    return syscall(SYS_futex, uaddr, futex_op, val,timeout, uaddr2, val3);
}

void wait_on_futex_value(int* futex_addr, int val) {
  while (1) {
    int futex_rc = futex(futex_addr, FUTEX_PRIVATE_FLAG | FUTEX_WAIT, val, NULL, NULL, 0);
    if (futex_rc == -1) {
      if (errno != EAGAIN) {
        perror("futex");
        exit(1);
      }else if (*futex_addr != val) {
        return;
      }
    } else if (futex_rc == 0) {
      if (*futex_addr == val) {
        // здесь мы просыпаемся
        return;
      }
    } else {
      abort();
    }
  }
}

void wake_futex_blocking(int* futex_addr) {
  while (1) {
    int futex_rc = futex(futex_addr, FUTEX_PRIVATE_FLAG | FUTEX_WAKE, 1, NULL, NULL, 0);
    if (futex_rc == -1) {
      perror("futex wake");
      exit(1);
    } else if (futex_rc >= 0) {
      return;
    }
  }
}

void write_file_lock(int index){

    RWlock *lock = &rwlocks[index];
    int zero;
    do{
        zero = 0;
        wait_on_futex_value(&lock->write_lock, 1);

     } while (!atomic_compare_exchange_strong(&lock->write_lock, &zero,1));
}

void write_file_unlock(int index){
  RWlock *lock = &rwlocks[index];
  lock->write_lock = 0;
  wake_futex_blocking(&lock->write_lock);
}

void read_file_lock(int index){
    RWlock *lock = &rwlocks[index];

    //trying to get Sync lock
    int zero;
    do{
        zero=0;
        wait_on_futex_value(&lock->sync_lock,1);
    } while (!atomic_compare_exchange_strong(&lock->sync_lock,&zero,1));
    //after getting the sync lock

    lock->readers++;//one more reader. We are this reader!
    if(lock->readers == 1){
        do{
            zero=0;
            wait_on_futex_value(&lock->write_lock,1);
        }while(!atomic_compare_exchange_strong(&lock->write_lock,&zero,1));
    }
    //finally, we get our read lock!
    lock->sync_lock=0;
    //wake our other waiters
    wake_futex_blocking(&lock->sync_lock);
    
}

void read_file_unlock(int index){
  RWlock *lock = &rwlocks[index];

  int zero;
  //
  do{
    zero=0;
    wait_on_futex_value(&lock->sync_lock,1);
  } while (!atomic_compare_exchange_strong(&lock->sync_lock,&zero,1));
  lock->readers--;
  if (lock->readers==0){ //when there is no one left to read

      //all writers now can work!
      lock->write_lock=0;
      //wake up them
      wake_futex_blocking(&lock->write_lock);
  }

  lock->sync_lock=0;
  wake_futex_blocking(&lock->sync_lock);
  

}