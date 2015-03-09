/*******************************************************************************
 * File         : arrayqueue.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_ARRAYQUEUE_H_
#define ODS_ARRAYQUEUE_H_

#include <stdlib.h>

#include <iterator.h>

typedef struct {

    size_t alloc_length;
    size_t length;
    size_t pos;
    size_t elem_size; 
    void* array;

} arrayqueue_t;

/* FUNCTION
 *      arrayqueue_clear
 *
 * DESCRIPTION
 *      Clears the queue. Removes all elements and minimizes allocated space.
 *
 * PARAMETERS
 *      q           A valid pointer to an initialized arrayqueue_t struct.
 */
extern void arrayqueue_clear(arrayqueue_t* q);

/* FUNCTION
 *      arrayqueue_remove
 *
 * DESCRIPTION
 *      Removes an element from the queue.
 *
 * PARAMETERS
 *      q           A valid pointer to an initialized arrayqueue_t struct.
 *      elem_out    If not null, pointer to a memory with at least elem_size
 *                  bytes allocated. The removed element will be copied into
 *                  this memory.
 */
extern void arrayqueue_dequeue(arrayqueue_t* q,
                               void* elem_out);

/* FUNCTION
 *      arrayqueue_dispose
 *
 * DESCRIPTION
 *      Frees allocated memory. This function must be called when done with the
 *      queue, to avoid memory leaks.
 *
 * PARAMETERS
 *      q           A valid pointer to an initialized arrayqueue_t struct.
 */
extern void arrayqueue_dispose(arrayqueue_t* q);

/* FUNCTION
 *      arrayqueue_add
 *
 * DESCRIPTION
 *      Adds an element to the queue.
 *
 * PARAMETERS
 *      q           A valid pointer to an initialized arrayqueue_t struct.
 *      elem        Pointer to the element that will be inserted to the queue.
 *                  This argument cannot be null.
 */
extern void arrayqueue_enqueue(arrayqueue_t* q,
                               void* elem);

/* FUNCTION
 *      arrayqueue_init
 *
 * DESCRIPTION
 *      Initializes an arrayqueue_t struct with an array size of 1. This
 *      function must be called before any other arrayqueue-related functions.
 *
 * PARAMETERS
 *      q           A valid pointer to an arrayqueue_t struct.
 *      elem_size   Size of the elements that will be stored in the stack.
 */
extern void arrayqueue_init(arrayqueue_t* q,
                            size_t elem_size);

/* FUNCTION
 *      arrayqueue_iterator
 *
 * ITERABLE
 *      FORWARD     start <= end
 *      REVERSE     start >  end
 *
 * DESCRIPTION
 *      Initializes an iterator_t for the specified range [start, end].
 *
 * PARAMETERS
 *      q           A valid pointer to an initialized arrayqueue_t struct.
 *      it          A valid pointer to an iterator_t struct.
 *      start       Start position (inclusive, must be less than length).
 *      end         End position (inclusive, must be less than length).
 */
extern void arrayqueue_iterator(arrayqueue_t* q,
                                iterator_t* it,
                                size_t start,
                                size_t end);

/* FUNCTION
 *      arrayqueue_peek
 *
 * DESCRIPTION
 *      Retrieves the element next in line, without removing it.
 *
 * PARAMETERS
 *      q           A valid pointer to an initialized arrayqueue_t struct.
 *      elem_out    The frontmost element will be copied into the memory pointed
 *                  to by this argument. Cannot be null.
 */
extern void arrayqueue_peek(arrayqueue_t* q,
                            void* elem_out);

#endif
