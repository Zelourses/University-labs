cmake_minimum_required(VERSION 3.16)
project(lab1 C)

set(CMAKE_C_STANDARD 11)

set(SOURCES
        src/main.c
        src/work.c
        src/work.h
        src/List/list.c
        src/List/list.h

)

include_directories(${PROJECT_SOURCE_DIR}/src)

add_executable(lab1 ${SOURCES})
