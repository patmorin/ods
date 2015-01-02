/*******************************************************************************
 * File         : skiplistsset.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_SKIPLISTSSET_H_
#define ODS_SKIPLISTSSET_H_

#include <stdlib.h>

#include <iterator.h>

typedef struct skiplistssetnode_t {

    void* data;
    struct skiplistssetnode_t** next;
    size_t next_length;

} skiplistssetnode_t;

typedef struct {

    skiplistssetnode_t* sentinel;
    skiplistssetnode_t** stack;

    size_t length;
    size_t elem_size;
    size_t height;    

    int (*cmp)(void*, void*);
    int (*rand)();

} skiplistsset_t;

/* FUNCTION
 *      skiplistsset_add
 *
 * DESCRIPTION
 *      Adds an element to the set.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplistsset_t struct.
 *      elem        The element that will be added to the set.
 *
 * RETURN VALUE
 *      Returns 1 if the element was added to the set, 0 otherwise. If the
 *      element already exists in the set, it will return 0.
 */
extern int skiplistsset_add(skiplistsset_t* s,
                            void* elem);

/* FUNCTION
 *      skiplistsset_dispose
 *
 * DESCRIPTION
 *      Releases all allocated memory.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplistsset_t struct.
 */
extern void skiplistsset_dispose(skiplistsset_t* s);

/* FUNCTION
 *      skiplistsset_contains
 *
 * DESCRIPTION
 *      Checks whether an element is contained in the set.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplistsset_t struct.
 *      elem        The element that will be searched for in the set.
 *
 * RETURN VALUE
 *      Returns 1 if the element exists in the set, 0 otherwise.
 */
extern int skiplistsset_contains(skiplistsset_t* s, void* elem);

/* FUNCTION
 *      skiplistsset_init
 *
 * DESCRIPTION
 *      Initializes a skiplist sorted set.
 *
 * PARAMETERS
 *      s           Pointer to a skiplistsset_t struct.
 *      elem_size   Size of the elements that will be stored in the set.
 *      comparator  Pointer to a comparator function. Cannot be null. The
 *                  function must take two void* arguments and return an int.
 *                  The return value must be 0 if the two arguments are equal,
 *                  some negative value if the first argument comes before the
 *                  second argument, or some positive value if second argument
 *                  comes before first argument.
 *      random      Pointer to a random number generator. If left null, default
 *                  "rand" will be used (from stdlib.h). The function must take
 *                  no arguments and return an int.
 */
extern void skiplistsset_init(skiplistsset_t* s,
                              size_t elem_size,
                              int (*comparator)(void*, void*),
                              int (*random)(void));

/* FUNCTION
 *      skiplistsset_iterator
 *
 * ITERABLE
 *      FORWARD
 *
 * DESCRIPTION
 *      Initializes an iterator for the sorted set.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplistsset_t struct.
 *      it          Pointer to an interator_t struct.
 */
extern void skiplistsset_iterator(skiplistsset_t* s,
                                  iterator_t* it);

/* FUNCTION
 *      skiplistsset_remove
 *
 * DESCRIPTION
 *      Removes an element from the set.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplistsset_t struct.
 *      elem        The element that will be removed from the set.
 *
 * RETURN VALUE
 *      Returns 1 if the element was removed, 0 otherwise. If the element does
 *      not exist in the set, it will return 0.
 */
extern int skiplistsset_remove(skiplistsset_t* s,
                               void* elem);

#endif
