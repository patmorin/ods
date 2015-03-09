/*******************************************************************************
 * File         : rootisharraystack.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_ROOTISHARRAYSTACK_H_
#define ODS_ROOTISHARRAYSTACK_H_

#include <stdlib.h>

#include <iterator.h>

#define rootisharraystack_push(s, elem) \
            rootisharraystack_add((s), (s)->length, (elem))

#define rootisharraystack_pop(s, elem_out) \
            rootisharraystack_remove((s), (s)->length - 1, (elem_out))

typedef struct {

    size_t length;
    size_t elem_size;
    size_t b_length;
    void** blocks;

} rootisharraystack_t;

/* FUNCTION
 *      rootisharraystack_add
 *
 * DESCRIPTION
 *      Adds an element into the array.
 *
 * PARAMETERS
 *      r           A valid pointer to an initialized rootisharraystack_t
 *                  struct.
 *      pos         The position of insertion.
 *      elem        The element that will be copied into the array. Cannot be
 *                  null.
 */
extern void rootisharraystack_add(rootisharraystack_t* r,
                                  size_t pos,
                                  void* elem);

/* FUNCTION
 *      rootisharraystack_clear
 *
 * DESCRIPTION
 *      Clears the array.
 *
 * PARAMETERS
 *      r           A valid pointer to an initialized rootisharraystack_t
 *                  struct.
 */
extern void rootisharraystack_clear(rootisharraystack_t* r);

/* FUNCTION
 *      rootisharraystack_dispose
 *
 * DESCRIPTION
 *      Releases memory and cleans up.
 *
 * PARAMETERS
 *      r           A valid pointer to an initialized rootisharraystack_t
 *                  struct.
 */
extern void rootisharraystack_dispose(rootisharraystack_t* r);

/* FUNCTION
 *      rootisharraystack_get
 *
 * DESCRIPTION
 *      Gets the element at the specified position in the array.
 *
 * PARAMETERS
 *      r           A valid pointer to an initialized rootisharraystack_t
 *                  struct.
 *      pos         Position of the element to be retrieved.
 *      elem_out    The retrieved element will be copied into the memory pointed
 *                  to by this argument. Cannot be null. Must have at least
 *                  elem_size bytes allocated.
 */
extern void rootisharraystack_get(rootisharraystack_t* r,
                                  size_t pos,
                                  void* elem_out);

/* FUNCTION
 *      rootisharraystack_init
 *
 * DESCRIPTION
 *      Initializes a rootisharraystack_t struct. Allocates memory for internal
 *      structs, etc.
 *
 * PARAMETERS
 *      r           A valid pointer to a rootisharraystack_t struct.
 *      elem_size   The size of the elements that will be stored in the array.
 */
extern void rootisharraystack_init(rootisharraystack_t* r,
                                   size_t elem_size);

/* FUNCTION
 *      rootisharraystack_iterator
 *
 * ITERABLE
 *      FORWARD     start <= end
 *      REVERSE     start >  end
 *
 * DESCRIPTION
 *      Initializes an iterator_t for the specified range [start, end].
 *
 * PARAMETERS
 *      r           A valid pointer to an initialized rootisharraystack_t
 *                  struct.
 *      it          A valid pointer to an iterator_t struct.
 *      start       Start position (inclusive, must be less than length).
 *      end         End position (inclusive, must be less than length).
 */
extern void rootisharraystack_iterator(rootisharraystack_t* r,
                                       iterator_t* it,
                                       size_t start,
                                       size_t end);

/* FUNCTION
 *      rootisharraystack_remove
 *
 * DESCRIPTION
 *      Removes an element from the array.
 *
 * PARAMETERS
 *      r           A valid pointer to an initialized rootisharraystack_t
 *                  struct.
 *      pos         The position of the element to be removed.
 *      elem_out    If not null, the element at pos will be copied into the
 *                  memory pointed to by this argument.
 */
extern void rootisharraystack_remove(rootisharraystack_t* r,
                                     size_t pos,
                                     void* elem_out);

/* FUNCTION
 *      rootisharraystack_set
 *
 * DESCRIPTION
 *      Sets the element at specified position to a new element.
 *
 * PARAMETERS
 *      r           A valid pointer to an initialized rootisharraystack_t
 *                  struct.
 *      pos         Position of the element that will be overwritten.
 *      elem        Pointer to the new element. Cannot be null.
 *      old_elem    If not null, the old element at pos will be copied into the
 *                  memory pointed to by this argument.
 */
extern void rootisharraystack_set(rootisharraystack_t* r,
                                  size_t pos,
                                  void* elem,
                                  void* old_elem);

#endif
