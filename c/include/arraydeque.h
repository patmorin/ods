/*******************************************************************************
 * File         : arraydeque.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_ARRAYDEQUE_H_
#define ODS_ARRAYDEQUE_H_

#include <stdlib.h>

#include <iterator.h>

#define arraydeque_add_front(d, elem) \
            arraydeque_add((d), 0, (elem))

#define arraydeque_add_back(d, elem) \
            arraydeque_add((d), (d)->length, (elem))

#define arraydeque_remove_front(d, elem_out) \
            arraydeque_remove((d), 0, (elem_out))

#define arraydeque_remove_back(d, elem_out) \
            arraydeque_remove((d), (d)->length - 1, (elem_out))

typedef struct {

    size_t alloc_length;
    size_t length;
    size_t pos;
    size_t elem_size;
    int bound;
    void* array;

} arraydeque_t;

/* FUNCTION
 *      arraydeque_add
 *
 * DESCRIPTION
 *      Inserts an element at the specified position.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized arraydeque_t struct.
 *      pos         The position where the new element will be inserted
 *      elem        Pointer to the new element that will be copied into the
 *                  deque.
 */
extern void arraydeque_add(arraydeque_t* d,
                           size_t pos,
                           void* elem);

/* FUNCTION
 *      arraydeque_clear
 *
 * DESCRIPTION
 *      Clears the deque. Removes all elements and minimizes allocated space.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized arraydeque_t struct.
 */
extern void arraydeque_clear(arraydeque_t* d);

/* FUNCTION
 *      arraydeque_dispose
 *
 * DESCRIPTION
 *      Releases allocated memory. Call this when the arraydeque_t is done.
 *      Will leak memory if not called. After calling this function, the struct
 *      must be reinitialized in order to be used again.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized arraydeque_t struct.
 */
extern void arraydeque_dispose(arraydeque_t* d);

/* FUNCTION
 *      arraydeque_get
 *
 * DESCRIPTION
 *      Retrieves the element at the specified position.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized arraydeque_t struct.
 *      pos         The position of the element that will be retrieved.
 *      elem_out    Pointer to a memory where the retrieved element will be
 *                  copied. Must have at least elem_size bytes allocated. Cannot
 *                  be null.
 */
extern void arraydeque_get(arraydeque_t* d,
                           size_t pos,
                           void* elem_out);

/* FUNCTION
 *      arraydeque_init
 *
 * DESCRIPTION
 *      Initializes an arraydeque_t struct with an array size of 1. This
 *      function should be called before any other arrraydeque functions.
 *
 * PARAMETERS
 *      d           A valid pointer to an arraydeque_t struct.
 *      elem_size   Size of the elements that will be stored in the stack.
 */
extern void arraydeque_init(arraydeque_t* d,
                            size_t elem_size);

/* FUNCTION
 *      arraydeque_init_bound
 *
 * DESCRIPTION
 *      Initializes an arraydeque_t struct bounded by the specified space.
 *      When initialized with this function, the deque will never grow or
 *      shrink and thus the space is limited to alloc_length elements.
 *
 * PARAMETERS
 *      d           A valid pointer to an arraydeque_t struct.
 *      elem_size   Size of the elements that will be stored in the stack.
 *      space       The number of elements that this deque will be bound to.
 */
extern void arraydeque_init_bound(arraydeque_t* d,
                                  size_t elem_size,
                                  size_t space);

/* FUNCTION
 *      arraydeque_iterator
 *
 * ITERABLE
 *      FORWARD     start <= end
 *      REVERSE     start >  end
 *
 * DESCRIPTION
 *      Initializes an iterator_t for the specified range [start, end].
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized arraydeque_t struct.
 *      it          A valid pointer to an iterator_t struct.
 *      start       Start position (inclusive, must be less than length).
 *      end         End position (inclusive, must be less than length).
 */
extern void arraydeque_iterator(arraydeque_t* d,
                                iterator_t* it,
                                size_t start,
                                size_t end);

/* FUNCTION
 *      arraydeque_remove
 *
 * DESCRIPTION
 *      Removes the element at the specified position.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized arraydeque_t struct.
 *      pos         The position of the element that will be removed.
 *      elem_out    If not null, the removed element will be copied into the
 *                  memory pointed to by this argument.
 */
extern void arraydeque_remove(arraydeque_t* d,
                              size_t pos,
                              void* elem_out);

/* FUNCTION
 *      arraydeque_set
 *
 * DESCRIPTION
 *      Sets the element at the specified position.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized arraydeque_t struct.
 *      pos         The position of the element that will be set.
 *      elem        Pointer to the new element that will replace the old one.
 *                  This cannot be a null pointer.
 *      elem_out    If not null, the old element will be copied into the memory
 *                  pointed to by this argument. If null, old data will be
 *                  overwritten.
 */
extern void arraydeque_set(arraydeque_t* d,
                           size_t pos,
                           void* elem,
                           void* elem_out);

#endif
