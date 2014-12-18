/*******************************************************************************
 * File         : dualarraydeque.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_DUALARRAYDEQUE_H_
#define ODS_DUALARRAYDEQUE_H_

#include <stdlib.h>

#include <iterator.h>
#include <arraystack.h>

#define dualarraydeque_add_front(d, elem) \
            dualarraydeque_add((d), 0, (elem))

#define dualarraydeque_add_back(d, elem) \
            dualarraydeque_add((d), (d)->length, (elem))

#define dualarraydeque_remove_front(d, elem_out) \
            dualarraydeque_remove((d), 0, (elem_out))

#define dualarraydeque_remove_back(d, elem_out) \
            dualarraydeque_remove((d), (d)->length - 1, (elem_out))

typedef struct {

    size_t length;
    size_t elem_size;
    arraystack_t* front;
    arraystack_t* back;
    
} dualarraydeque_t;

/* FUNCTION
 *      dualarraydeque_add
 *
 * DESCRIPTION
 *      Adds an element into the deque.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized dualarraydeque_t struct.
 *      pos         The position of insertion.
 *      elem        The element that will be copied into the deque. Cannot be
 *                  null.
 */
extern void dualarraydeque_add(dualarraydeque_t* d,
                               size_t pos,
                               void* elem);

/* FUNCTION
 *      dualarraydeque_dispose
 *
 * DESCRIPTION
 *      Releases memory and cleans up.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized dualarraydeque_t struct.
 */
extern void dualarraydeque_dispose(dualarraydeque_t* d);

/* FUNCTION
 *      dualarraydeque_get
 *
 * DESCRIPTION
 *      Gets the element at the specified position in the deque.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized dualarraydeque_t struct.
 *      pos         Position of the element to be retrieved.
 *      elem_out    The retrieved element will be copied into the memory pointed
 *                  to by this argument. Cannot be null. Must have at least
 *                  elem_size bytes allocated.
 */
extern void dualarraydeque_get(dualarraydeque_t* d,
                               size_t pos,
                               void* elem_out);

/* FUNCTION
 *      dualarraydeque_init
 *
 * DESCRIPTION
 *      Initializes a dualarraydeque_t struct. Allocates memory for internal
 *      structs, etc.
 *
 * PARAMETERS
 *      d           A valid pointer to a dualarraydeque_t struct.
 *      elem_size   The size of the elements that will be stored in the deque.
 */
extern void dualarraydeque_init(dualarraydeque_t* d,
                                size_t elem_size);

/* FUNCTION
 *      dualarraydeque_iterator
 *
 * ITERABLE
 *      FORWARD     start <= end
 *      REVERSE     start >  end
 *
 * DESCRIPTION
 *      Initializes an iterator_t for the specified range [start, end].
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized dualarraydeque_t struct.
 *      it          A valid pointer to an iterator_t struct.
 *      start       Start position (inclusive, must be less than length).
 *      end         End position (inclusive, must be less than length).
 */
extern void dualarraydeque_iterator(dualarraydeque_t* d, iterator_t* it,
                                    size_t start, size_t end);

/* FUNCTION
 *      dualarraydeque_remove
 *
 * DESCRIPTION
 *      Removes an element from the deque.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized dualarraydeque_t struct.
 *      pos         The position of the element to be removed.
 *      elem_out    If not null, the element at pos will be copied into the
 *                  memory pointed to by this argument.
 */
extern void dualarraydeque_remove(dualarraydeque_t* d,
                                  size_t pos,
                                  void* elem_out);

/* FUNCTION
 *      dualarraydeque_set
 *
 * DESCRIPTION
 *      Sets the element at specified position to a new element.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized dualarraydeque_t struct.
 *      pos         Position of the element that will be overwritten.
 *      elem        Pointer to the new element. Cannot be null.
 *      old_elem    If not null, the old element at pos will be copied into the
 *                  memory pointed to by this argument.
 */
extern void dualarraydeque_set(dualarraydeque_t* d,
                               size_t pos,
                               void* elem,
                               void* old_elem);

#endif
