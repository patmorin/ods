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
* [SkiplistSSet](include/skiplistsset.h)
* [SkiplistList](include/skiplist.h)
* [ChainedHashTable](include/chainedhashtable.h)
* [BinarySearchTree](include/binarysearchtree.h)

#### How to use the library

If you have [gcc](https://gcc.gnu.org/onlinedocs/gcc/) installed, simply run
either `make` to compile the shared object file or `make install` to compile and
install the library (by default, it is moved to `/usr/lib/libodsc.so`).
After that, you can append `-lodsc` to your compiler arguments to link your code
with the library.

If you don't have gcc, you can modify the Makefile for your compiler (with
[clang](http://clang.llvm.org), you only need to change the compiler variable,
everything else should work the same). Or, compile the sources in `src/` with
the headers in `include/`.

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

#### Uniform iteration

Different data structures can require different methods to efficiently iterate
over them. For example, it is possible to iterate through an ArrayStack using
`_get()` but using the same method on a DLList would be costly. Uniform
iterators provide a simple way of iterating over these data structures. Best to
consider them invalidated if you do any kind of insertion/removal on the data
structure you are iterating on. Iterators come with overhead since they have to
maintain state and keep function pointers.

If an iterator is implemented for a data structure, it can be initialized
through `_iterator()`. Iterators have three functions:

* `next(iterator_t *)`: Advances to the next element and returns 1, if
available. Otherwise, returns 0. If iterating in reverse, this will advance to
the previous element (**not all data structures can be iterated in reverse
order**, check the documentation for `_iterator()`).
* `elem(iterator_t *)`: Returns a pointer to the element *in* the data structure
(i.e. not a copy). Be careful not to exceed the bounds of this pointer (which is
`elem_size` bytes).
* `dispose(iterator_t *)`: Releases allocated memory.

##### Example

```c
#include <stdio.h>
#include <iterator.h>
#include <arraystack.h>

int main() {

    int i;

    arraystack_t stack;
    arraystack_init(&stack, sizeof(int));

    for (i = 0; i < 10; i++)
        arraystack_push(&stack, &i);

    /* make an iterator for stack[1,4] */
    iterator_t it;
    arraystack_iterator(&stack, &it, 1, 4);

    while (it.next(&it))
        printf("%d\n", *(int *)it.elem(&it));

    /* iterators allocate memory for state, which must be released. */
    it.dispose(&it);

    arraystack_dispose(&stack);

    return 0;
}
```
