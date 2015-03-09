/*******************************************************************************
 * File         : arraystack.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_ARRAYSTACK_H_
#define ODS_ARRAYSTACK_H_

#include <stdlib.h>

#include <iterator.h>

#define arraystack_push(s, elem) \
            arraystack_add((s), (s)->length, (elem))

#define arraystack_pop(s, elem_out) \
            arraystack_remove((s), (s)->length - 1, (elem_out))

typedef struct {

    size_t alloc_length;
    size_t length;
    size_t elem_size;
    void* array;
    
} arraystack_t;

/* FUNCTION
 *      arraystack_add
 *
 * DESCRIPTION
 *      Adds an element to the specified position in the stack.
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 *      pos         The position where the element will be inserted.
 *      elem        Pointer to the element that will inserted to the stack. This
 *                  argument cannot be a null pointer. The memory pointed to by
 *                  this argument will be copied into the array.
 */
extern void arraystack_add(arraystack_t* s,
                           size_t pos,
                           void* elem);

/* FUNCTION
 *      arraystack_clear
 *
 * DESCRIPTION
 *      Clears the stack. Removes all elements and minimizes allocated space.
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 */
extern void arraystack_clear(arraystack_t* s);

/* FUNCTION
 *      arraystack_copy
 *
 * DESCRIPTION
 *      Copies a number of elements from the specified position in the source
 *      arraystack to the specified position in the destination arraystack.
 *
 * PARAMETERS
 *      dest        A valid pointer to an initialized arraystack_t struct. This
 *                  is the destination stack.
 *      dest_pos    The position in the destination where the elements will be
 *                  inserted into.
 *      src         A valid pointer to an initialized arraystack_t struct. This
 *                  is the source stack.
 *      src_pos     The position in the source array where the copying will
 *                  begin.
 *      num_elems   The number of elements to be copied from source to
 *                  destination.
 */
extern void arraystack_copy(arraystack_t* dest,
                            size_t dest_pos,
                            arraystack_t* src,
                            size_t src_pos,
                            size_t num_elems);

/* FUNCTION
 *      arraystack_dispose
 *
 * DESCRIPTION
 *      Releases allocated memory. Make sure to call this function to avoid
 *      memory leaks. The stack, after a call to this function, is considered to
 *      be uninitialized and must be re-initialized in order to be used again.
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 */
extern void arraystack_dispose(arraystack_t* s);

/* FUNCTION
 *      arraystack_get
 *
 * DESCRIPTION
 *      Gets the element at the specified position.
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 *      pos         The position of the element that will be retrieved.
 *      elem        Pointer to a memory with at least elem_size bytes allocated.
 *                  The data at pos will be copied into this memory.
 */
extern void arraystack_get(arraystack_t* s,
                           size_t pos,
                           void* elem_out);

/* FUNCTION
 *      arraystack_init
 *
 * DESCRIPTION
 *      Initializes an arraystack_t struct with an array size of 1. This
 *      function must be called before any other arraystack-related functions.
 *
 * PARAMETERS
 *      s           A valid pointer to an arraystack_t struct.
 *      elem_size   Size of the elements that will be stored in the stack.
 */
extern void arraystack_init(arraystack_t* s,
                            size_t elem_size);

/* FUNCTION
 *      arraystack_iterator
 *
 * ITERABLE
 *      FORWARD     start <= end
 *      REVERSE     start >  end
 *
 * DESCRIPTION
 *      Initializes an iterator_t for the specified range [start, end].
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 *      it          A valid pointer to an iterator_t struct.
 *      start       Start position (inclusive, must be less than length).
 *      end         End position (inclusive, must be less than length).
 */
extern void arraystack_iterator(arraystack_t* s,
                                iterator_t* it,
                                size_t start,
                                size_t end);

/* FUNCTION
 *      arraystack_remove
 *
 * DESCRIPTION
 *      Removes the element at the specified position. The removed element can
 *      be returned through an output parameter.
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 *      pos         The position of the element to be removed.
 *      elem_out    If not a null pointer, the removed element will be coped
 *                  into the memory pointed to by this pointer.
 */
extern void arraystack_remove(arraystack_t* s,
                              size_t pos,
                              void* elem_out);

/* FUNCTION
 *      arraystack_reserve
 *
 * DESCRIPTION
 *      Reserves space for the specified amount of elements. The arraystack will
 *      be able to store this amount of elements without having to reallocate
 *      memory. This does not impose a limit on the length of the arraystack.
 *
 *      Note: keep in mind that arraystack_remove will invoke a resize if
 *      length * 3 < allocated space.
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 *      n           The number of elements to reserve space for. This value must
 *                  be greater than (or equal to) the length of the stack.
 */
extern void arraystack_reserve(arraystack_t* s,
                               size_t n);

/* FUNCTION
 *      arraystack_reverse
 *
 * DESCRIPTION
 *      Reverses the specified range in the array.
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 *      pos         The position where reversing begins.
 *      num_elems   Number of elements to reverse starting from pos.
 */
extern void arraystack_reverse(arraystack_t* s,
                               size_t pos,
                               size_t num_elems);

/* FUNCTION
 *      arraystack_set
 *
 * DESCRIPTION
 *      Sets the element at the specified position. The old element can be
 *      returned through an output parameter.
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 *      pos         The position of the element that will be set.
 *      elem        Pointer to the new element that will be inserted into the
 *                  stack. This argument cannot be null.
 *      old_elem    If not a null pointer, the old element at pos will be copied
 *                  into the memory pointed to by this pointer.
 */
extern void arraystack_set(arraystack_t* s,
                           size_t pos,
                           void* elem,
                           void* old_elem);

/* FUNCTION
 *      arraystack_truncate
 *
 * DESCRIPTION
 *      Removes specified number of elements starting from the specified
 *      position.
 *
 * PARAMETERS
 *      s           A valid pointer to an initialized arraystack_t struct.
 *      pos         The position where removal will start.
 *      num_elems   Number of elements to remove starting from pos.
 */
extern void arraystack_truncate(arraystack_t* s,
                                size_t pos,
                                size_t num_elems);

#endif
