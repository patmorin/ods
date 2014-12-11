/*******************************************************************************
 * File         : arraydeque.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_ARRAYDEQUE_H_
#define ODS_ARRAYDEQUE_H_

#define ods_arraydeque_add_front(d, elem) \
            ods_arraydeque_add((d), 0, (elem))

#define ods_arraydeque_add_back(d, elem) \
            ods_arraydeque_add((d), (d)->length, (elem))

#define ods_arraydeque_remove_front(d, elem_out) \
            ods_arraydeque_remove((d), 0, (elem_out))

#define ods_arraydeque_remove_back(d, elem_out) \
            ods_arraydeque_remove((d), (d)->length - 1, (elem_out))

#include <stdlib.h>

typedef struct {

    size_t alloc_length;
    size_t length;
    size_t pos;
    size_t elem_size;
    void* array;

} arraydeque_t;

/* FUNCTION
 *      ods_arraydeque_init
 *
 * DESCRIPTION
 *      Initializes an arraydeque_t struct with an array size of 1. This
 *      function should be called before any other arrraydeque functions.
 *
 * PARAMETERS
 *      d           A valid pointer to an arraydeque_t struct.
 *      elem_size   Size of the elements that will be stored in the stack.
 */
extern void ods_arraydeque_init(arraydeque_t* d,
                                size_t elem_size);

/* FUNCTION
 *      ods_arraydeque_get
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
extern void ods_arraydeque_get(arraydeque_t* d,
                               size_t pos,
                               void* elem_out);

/* FUNCTION
 *      ods_arraydeque_set
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
extern void ods_arraydeque_set(arraydeque_t* d,
                               size_t pos,
                               void* elem,
                               void* elem_out);

/* FUNCTION
 *      ods_arraydeque_add
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
extern void ods_arraydeque_add(arraydeque_t* d,
                               size_t pos,
                               void* elem);

/* FUNCTION
 *      ods_arraydeque_remove
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
extern void ods_arraydeque_remove(arraydeque_t* d,
                                  size_t pos,
                                  void* elem_out);

/* FUNCTION
 *      ods_arraydeque_clear
 *
 * DESCRIPTION
 *      Clears the deque. Removes all elements and minimizes allocated space.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized arraydeque_t struct.
 */
extern void ods_arraydeque_clear(arraydeque_t* d);

/* FUNCTION
 *      ods_arraydeque_dispose
 *
 * DESCRIPTION
 *      Releases allocated memory. Call this when the arraydeque_t is done.
 *      Will leak memory if not called. After calling this function, the struct
 *      must be reinitialized in order to be used again.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized arraydeque_t struct.
 */
extern void ods_arraydeque_dispose(arraydeque_t* d);

#endif
