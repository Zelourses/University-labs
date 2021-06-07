cmake_minimum_required(VERSION 3.16)
project(lab3 C)

set(CMAKE_C_STANDARD 11)

set(SOURCES
        src/Server/Server.c
        src/Parser/Parser.c
        src/Server/ClientHandling.c

        src/main.c
)

set(HEADERS
        src/Server/Server.h
        src/Parser/Parser.h
        src/Server/ClientHandling.h

        src/main.h
)
find_package(Threads REQUIRED)
include_directories(${PROJECT_SOURCE_DIR}/src)

add_compile_options(-Wall -Wextra -pedantic)

add_executable(lab3 ${SOURCES} ${HEADERS})

target_link_libraries(${PROJECT_NAME} Threads::Threads)