.PHONY: all clean

all: run
main.o: main.asm words.inc
		nasm -felf64 main.asm

lib.o:	lib.asm
		nasm -felf64 lib.asm

dict.o: dict.asm
		nasm -felf64 dict.asm

run:	main.o lib.o dict.o 
		ld -o main main.o lib.o dict.o

clean:
	rm -f *.o *.out main