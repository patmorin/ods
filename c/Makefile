COMPILER = gcc

OBJ = arraystack.o

shared-lib: $(OBJ)
	$(COMPILER) -shared -o libodsc.so $^

static-lib: $(OBJ)
	ar rc libodsc.a $^

%.o: src/%.c
	$(COMPILER) -ansi -pedantic -Wall -Werror -fpic -I include -c -o $@ $<

clean:
	rm -f *.o *.so *.a
