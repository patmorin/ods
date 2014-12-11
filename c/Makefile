CC = gcc

OBJ = arraystack.o arrayqueue.o arraydeque.o

shared-lib: $(OBJ)
	$(CC) -shared -o libodsc.so $^

static-lib: $(OBJ)
	ar rc libodsc.a $^

%.o: src/%.c
	$(CC) -ansi -pedantic -Wall -Werror -fpic -I include -c -o $@ $<

clean:
	rm -f *.o *.so *.a a.out
