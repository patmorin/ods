## Open Data Structures in C

This is an implementation of [Open Data Structures](http://opendatastructures.org) in C.
It is written in ANSI C (C89) to maximize compatibility with other compilers.

##### How to use the library

The [include](include/) directory contains the headers you will need to include in your
program. Detailed information on the functions and their arguments can be found in the
header files.

This library follows a general structure: `ods_[data structure]_[function]`. For example,
the function to initialize an ArrayStack is `ods_arraystack_init`.

##### Example

```c
#include <stdio.h>
#include <arraystack.h>

int main() {

    int i;

    arraystack_t stack;
    
    /* initialize the struct with the size of the elements you want to store
     * inside. this will allocate memory for the backing array. */
    ods_arraystack_init(&stack, sizeof(int));
    
    for (i = 0; i < 10; i++)
        ods_arraystack_push(&stack, &i);

    while (stack.length > 0) {
        
        ods_arraystack_pop(&stack, &i);
        printf("popped: %d\n", i);
    }

    /* make sure to call dispose when you're done, to avoid memory leaks */
    ods_arraystack_dispose(&stack);

    return 0;
}
```
