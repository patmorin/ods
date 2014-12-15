## Open Data Structures in C

This is an implementation of [Open Data Structures](http://opendatastructures.org)
in C. It is written in ANSI C (C89) to maximize compatibility with other
compilers.

#### Implemented data structures

More information on the data structures (implementation details, running times,
etc.) can be found in [the book](http://opendatastructures.org).

* [ArrayStack](include/arraystack.h) (equivalent to FastArrayStack in the book)
* [ArrayQueue](include/arrayqueue.h)
* [ArrayDeque](include/arraydeque.h)
* [DualArrayDeque](include/dualarraydeque.h)
* [RootishArrayStack](include/rootisharraystack.h)
* [SLList](include/sllist.h)
* [DLList](include/dllist.h)
* [SEList](include/selist.h)

#### How to use the library

If you have [gcc](https://gcc.gnu.org/onlinedocs/gcc/) installed, simply run
either `make` to compile the shared object file or `make install` to compile and
install the library (by default, it is moved to `/usr/lib`). After that, you can
append `-lodsc` to your compiler arguments to link your code with the library.

If you don't have gcc, you can modify the Makefile for your compiler. Or,
compile the sources in `src/` with the headers in `include/`.

**Note:** Some data structures use `<math.h>` and thus require libm to be
linked. Add `-lm` to your compiler when using these data structures.

The [include](include/) directory contains the headers you will need to include
in your program. Detailed information on the functions and their arguments can
be found in the header files.

This library follows a general structure: `[data structure]_[function]`. For
example, the function to initialize an ArrayStack is `arraystack_init`.

##### Example

```c
#include <stdio.h>
#include <arraystack.h>

int main() {

    int i;

    arraystack_t stack;
    
    /* initialize the struct with the size of the elements you want to store
     * inside. this will allocate memory for the backing array. */
    arraystack_init(&stack, sizeof(int));
    
    for (i = 0; i < 10; i++)
        arraystack_push(&stack, &i);

    while (stack.length > 0) {
        
        arraystack_pop(&stack, &i);
        printf("popped: %d\n", i);
    }

    /* make sure to call dispose when you're done, to avoid memory leaks */
    arraystack_dispose(&stack);

    return 0;
}
```
