CFLAGS= -Wall -Wextra -pthread -g -pedantic -pie $(OPTFLAGS)

SOURCES=$(wildcard *.c)
OBJECTS=$(patsubst %.c, %.o, $(SOURCES))
TARGET=lab
all: $(TARGET)
$(TARGET): $(OBJECTS)
	$(CC) $(CFLAGS) -o $@ $(OBJECTS)

clean:
	rm -rf $(TARGET)
	rm -rf $(OBJECTS)
	rm -f `find . -name "*[0-9]"`

