CFLAGS=-Wall -Wextra -Wpedantic -march=native -Wshadow -std=c11 -g $(OPTFLAGS)

SOURCES=$(wildcard *.c)
OBJECTS=$(patsubst %.c, %.o, $(SOURCES))
TARGET=lab
all: $(TARGET)
$(TARGET): CFLAGS += -fPIC
$(TARGET): $(OBJECTS)
	$(CC) $(CFLAGS) -o $@ $(OBJECTS)

clean:
	rm -rf $(TARGET)
	rm -rf $(OBJECTS)

