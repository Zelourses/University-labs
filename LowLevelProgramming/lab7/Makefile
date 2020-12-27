CFLAGS=-Wall -Wextra -Wpedantic -march=native -std=c17$(OPTFLAGS)

SOURCES=$(wildcard *.c)
OBJECTS=$(patsubst %.c, %.o, $(SOURCES))
TARGET=lab
all: $(TARGET)
$(TARGET): $(OBJECTS)
	$(CC) $(CFLAGS) -o $@ $(OBJECTS) -lm

dev: CFLAGS+=-g
dev: all
clean:
	rm -rf $(TARGET)
	rm -rf $(OBJECTS)

