/*******************************************************************************
 * File         : dllist.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_DLLIST_H_
#define ODS_DLLIST_H_

#include <stdlib.h>

#include <iterator.h>

typedef struct dlnode_t {

    void* data;
    struct dlnode_t* next;
    struct dlnode_t* prev;

} dlnode_t;

typedef struct {

    size_t elem_size;
    size_t length;
    dlnode_t* dummy;

} dllist_t;

/* FUNCTION
 *      dllist_add
 *
 * DESCRIPTION
 *      Adds an element to the specified position in the list.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized dllist_t struct.
 *      pos         The position where the element will be inserted.
 *      elem        The element that will be copied into the list.
 */
extern void dllist_add(dllist_t* l,
                       size_t pos,
                       void* elem);

/* FUNCTION
 *      dllist_dispose
 *
 * DESCRIPTION
 *      Releases all memory allocated by other dllist_* functions. If you have
 *      any pointer references to nodes, they will be invalid after a call to
 *      this function.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized dllist_t struct.
 */
extern void dllist_dispose(dllist_t* l);

/* FUNCTION
 *      dllist_get
 *
 * DESCRIPTION
 *      Retrieves an element from the list.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized dllist_t struct.
 *      pos         The position of the element that will be retrieved.
 *      elem_out    The element at pos will be copied into the memory pointed to
 *                  by this argument.
 */
extern void dllist_get(dllist_t* l,
                       size_t pos,
                       void* elem_out);

/* FUNCTION
 *      dllist_init
 *
 * DESCRIPTION
 *      Initializes an dllist_t struct.
 *
 * PARAMETERS
 *      l           A valid pointer to an dllist_t struct.
 *      elem_size   Size of the elements that will be stored in the list.
 */
extern void dllist_init(dllist_t* l,
                        size_t elem_size);

/* FUNCTION
 *      dllist_iterator
 *
 * ITERABLE
 *      FORWARD     start <= end
 *      REVERSE     start >  end
 *
 * DESCRIPTION
 *      Initializes an iterator_t for the specified range [start, end].
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized dllist_t struct.
 *      it          A valid pointer to an iterator_t struct.
 *      start       Start position (inclusive, must be less than length).
 *      end         End position (inclusive, must be less than length).
 */
extern void dllist_iterator(dllist_t* l,
                            iterator_t* it,
                            size_t start,
                            size_t end);

/* FUNCTION
 *      dllist_remove
 *
 * DESCRIPTION
 *      Retrieves an element from the list.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized dllist_t struct.
 *      pos         The position of the element that will be removed.
 *      elem_out    If not null, the removed element will be copied into this
 *                  memory.
 */
extern void dllist_remove(dllist_t* l,
                          size_t pos,
                          void* elem_out);

/* FUNCTION
 *      dllist_set
 *
 * DESCRIPTION
 *      Set an element at the specified position.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized dllist_t struct.
 *      pos         The position of the element that will be set.
 *      elem        The new element that will replace the element at pos.
 *      old_elem    If not null, the old element at this position will be copied
 *                  into this memory.
 */
extern void dllist_set(dllist_t* l,
                       size_t pos,
                       void* elem,
                       void* old_elem);

#endif
