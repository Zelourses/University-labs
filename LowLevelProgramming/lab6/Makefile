CFLAGS=-Wall -Wextra -Wpedantic -march=native -std=c17 -g $(OPTFLAGS)

SOURCES=$(wildcard *.c)
OBJECTS=$(patsubst %.c, %.o, $(SOURCES))
TARGET=lab
all: $(TARGET)
$(TARGET): $(OBJECTS)
	$(CC) $(CFLAGS) -o $@ $(OBJECTS) -lm

clean:
	rm -rf $(TARGET)
	rm -rf $(OBJECTS)

