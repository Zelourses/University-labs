CFLAGS=-Wall -Wextra -Wpedantic  -g -O3 -march=native -Isrc $(OPTFLAGS)
ASMFLAGS=-felf64 -g -O3 -Wall
SOURCES=$(wildcard src/**/*.c src/*.c)
OBJECTS=$(patsubst %.c, %.o, $(SOURCES))
ASM_SOURCES=$(wildcard src/*.asm src/**/*.asm)
ASM_OBJECTS=$(patsubst %.asm, %.o, $(ASM_SOURCES))
TARGET=lab
all: $(TARGET)
$(TARGET): build $(OBJECTS) $(ASM_OBJECTS)
	$(CC) $(CFLAGS) -o $@ $(OBJECTS) $(ASM_OBJECTS) -lm

build:
	@mkdir -p bin

$(ASM_OBJECTS): $(ASM_SOURCES)
	nasm $(ASMFLAGS) $(ASM_SOURCES)
clean:
	rm -rf build $(TARGET) $(OBJECTS) $(ASM_OBJECTS)

