CFLAGS = -Wall -Wextra -Wpedantic -march=native -Isrc -g $(OPTFLAGS)
SOURCES=$(wildcard src/**/*.c src/*.c)
OBJECTS=$(patsubst %.c, %.o, $(SOURCES))
TARGET = lab
BUILD_DIR = out
all: $(TARGET)
$(TARGET): $(OBJECTS)
	$(CC) -o $@ $(patsubst %.o,$(BUILD_DIR)/%.o,$(OBJECTS))

%.o: %.c
	@mkdir -p "$$(dirname $(BUILD_DIR)/$@)"
	$(CC) $(CFLAGS) -c $< -o $(BUILD_DIR)/$@

clean:
	rm -rf build $(TARGET)