/*******************************************************************************
 * File         : dualarraydeque.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_DUALARRAYDEQUE_H_
#define ODS_DUALARRAYDEQUE_H_

#include <stdlib.h>
#include <arraystack.h>

typedef struct {

    size_t length;
    size_t elem_size;
    arraystack_t* front;
    arraystack_t* back;
    
} dualarraydeque_t;

/* FUNCTION
 *      ods_dualarraydeque_add
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
extern void ods_dualarraydeque_add(dualarraydeque_t* d,
                                   size_t pos,
                                   void* elem);

/* FUNCTION
 *      ods_dualarraydeque_dispose
 *
 * DESCRIPTION
 *      Releases memory and cleans up.
 *
 * PARAMETERS
 *      d           A valid pointer to an initialized dualarraydeque_t struct.
 */
extern void ods_dualarraydeque_dispose(dualarraydeque_t* d);

/* FUNCTION
 *      ods_dualarraydeque_get
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
extern void ods_dualarraydeque_get(dualarraydeque_t* d,
                                   size_t pos,
                                   void* elem_out);

/* FUNCTION
 *      ods_dualarraydeque_init
 *
 * DESCRIPTION
 *      Initializes a dualarraydeque_t struct. Allocates memory for internal
 *      structs, etc.
 *
 * PARAMETERS
 *      d           A valid pointer to a dualarraydeque_t struct.
 *      elem_size   The size of the elements that will be stored in the deque.
 */
extern void ods_dualarraydeque_init(dualarraydeque_t* d,
                                    size_t elem_size);

/* FUNCTION
 *      ods_dualarraydeque_remove
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
extern void ods_dualarraydeque_remove(dualarraydeque_t* d,
                                      size_t pos,
                                      void* elem_out);

/* FUNCTION
 *      ods_dualarraydeque_set
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
extern void ods_dualarraydeque_set(dualarraydeque_t* d,
                                   size_t pos,
                                   void* elem,
                                   void* old_elem);

#endif
