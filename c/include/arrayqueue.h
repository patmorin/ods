/*******************************************************************************
 * File         : arrayqueue.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 *
 * Implementation of an array-based queue. The arrayqueue_t struct holds the
 * ownership of the backing array. This struct must be initialized using
 * ods_arrayqueue_init before use and disposed of using ods_arrayqueue_dispose
 * when done so that all system resources allocated by the functions can be
 * released.
 ******************************************************************************/

#ifndef ODS_ARRAYQUEUE_H_
#define ODS_ARRAYQUEUE_H_

typedef struct {

    size_t alloc_length;
    size_t length;
    size_t pos;             /* pointer position on circular array */
    size_t elem_size; 
    void* array;

} arrayqueue_t;

/* FUNCTION
 *      ods_arrayqueue_init
 *
 * DESCRIPTION
 *      Initializes an arrayqueue_t struct with an array size of 1. This
 *      function must be called before any other arrayqueue-related functions.
 *
 * PARAMETERS
 *      q           A valid pointer to an arrayqueue_t struct.
 *      elem_size   Size of the elements that will be stored in the stack.
 */
void ods_arrayqueue_init(arrayqueue_t* q, size_t elem_size);

/* FUNCTION
 *      ods_arrayqueue_add
 *
 * DESCRIPTION
 *      Adds an element to the queue.
 *
 * PARAMETERS
 *      q           A valid pointer to an initialized arrayqueue_t struct.
 *      elem        Pointer to the element that will be inserted to the queue.
 *                  This argument cannot be null.
 */
void ods_arrayqueue_add(arrayqueue_t* q,
                        void* elem);

/* FUNCTION
 *      ods_arrayqueue_remove
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
void ods_arrayqueue_remove(arrayqueue_t* q,
                           void* elem_out);

/* FUNCTION
 *      ods_arrayqueue_dispose
 *
 * DESCRIPTION
 *      Frees allocated memory. This function must be called when done with the
 *      queue, to avoid memory leaks.
 *
 * PARAMETERS
 *      q           A valid pointer to an initialized arrayqueue_t struct.
 */
void ods_arrayqueue_dispose(arrayqueue_t* q);

#endif
