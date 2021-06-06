CFLAGS = -I ./ -I ${GENERATED_HEADERS_DIR} -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux

SRCS = $(wildcard *.c **/*.c)
OBJS = $(patsubst %.c,%.o,$(SRCS))

objects: $(OBJS)

%.o: %.c
	@mkdir -p "$$(dirname ${OBJECTS_DIR}/$@)"
	$(CC) $(CFLAGS) -fPIC -c $< -lblkid -o ${OBJECTS_DIR}/$@

shared_object: $(OBJS)
	rm -f ${SHARED_OBJECT} || true
	mkdir -p ${SHARED_OBJECT} && rmdir ${SHARED_OBJECT}
	$(CC) -shared -fPIC -o ${SHARED_OBJECT} $(patsubst %.o,${OBJECTS_DIR}/%.o,$(OBJS)) -lblkid