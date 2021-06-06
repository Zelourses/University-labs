#include <JNI/ru_JNI_Filesystem.h>
#include <stdlib.h>

#include "main.h"
#include "List/list.h"
#include "work/Handler.h"

JNIEXPORT void JNICALL Java_ru_JNI_Filesystem_print
  (JNIEnv *test, jobject obj) {
    printf("Hello world!");
}

JNIEXPORT void JNICALL Java_ru_JNI_Filesystem_printHelp
  (JNIEnv *env, jobject obj){
   printHelp();
}

JNIEXPORT void JNICALL Java_ru_JNI_Filesystem_startWork
  (JNIEnv *env, jobject obj, jstring path) {
    char* partition = (*env)->GetStringUTFChars(env, path, NULL);
    startWork(partition);
}

JNIEXPORT void JNICALL Java_ru_JNI_Filesystem_listAllPartitions
  (JNIEnv *env, jobject obj){
    listAllPartitions();
}

JNIEXPORT void JNICALL Java_ru_JNI_Filesystem_printExitText
  (JNIEnv *env, jobject obj, jstring argument, jstring programName){
  char* arg = (*env)->GetStringUTFChars(env, argument, NULL);
  char* program = (*env)->GetStringUTFChars(env, programName, NULL);
  exitText(arg, program);
}