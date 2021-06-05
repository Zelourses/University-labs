cmake_minimum_required(VERSION 3.16)
project(lab C)

set(CMAKE_C_STANDARD 11)

set(SOURCES
        src/main.c
        src/work/Handler.c
        src/list/list.c
        src/parser/Parser.c
        src/parser/Commands.c
        src/work/NTFS.c
        src/work/InnerFunctions.c
)
set(HEADERS
        src/work/Handler.h
        src/list/list.h
        src/parser/Parser.h
        src/parser/Commands.h
        src/work/NTFS.h
        src/types.h
        src/util.h
        src/work/InnerFunctions.h
)

include_directories(${PROJECT_SOURCE_DIR}/src)

add_executable(lab ${SOURCES} ${HEADERS})
