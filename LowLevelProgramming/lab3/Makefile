CFLAGS= -Wall -Wextra -Wpedantic -g $(OPTFLAGS)

SOURCES=$(wildcard *.c)
OBJECTS=$(patsubst %.c, %.o, $(SOURCES))
all: $(OBJECTS)
	$(info this objects was built: $(OBJECTS))

$(OBJECTS): $(SOURCES)
	gcc $(CFLAGS) -o $@ $(patsubst %.o, %.c, $@)