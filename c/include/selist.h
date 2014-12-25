/*******************************************************************************
 * File         : selist.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_SELIST_H_
#define ODS_SELIST_H_

#include <stdlib.h>

#include <iterator.h>
#include <arraydeque.h>

typedef struct senode_t {

    arraydeque_t* bdeque;
    struct senode_t* next;
    struct senode_t* prev;

} senode_t;

typedef struct selist_t {

    size_t elem_size;
    size_t length;
    size_t block_size;
    senode_t* dummy;

} selist_t;

/* FUNCTION
 *      selist_add
 *
 * DESCRIPTION
 *      Adds an element to the list.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized selist_t struct.
 *      pos         The position where the new element will be inserted.
 *      elem        Pointer to the element that will be copied into the list.
 */
extern void selist_add(selist_t* l,
                       size_t pos,
                       void* elem);

/* FUNCTION
 *      selist_dispose
 *
 * DESCRIPTION
 *      Releases allocated memory, cleans up. Call when done with the list.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized selist_t struct.
 */
extern void selist_dispose(selist_t* l);

/* FUNCTION
 *      selist_get
 *
 * DESCRIPTION
 *      Retrieves an element from the list.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized selist_t struct.
 *      pos         The position of the element that will be retrieved.
 *      elem_out    Pointer to the memory where the retrieved element will be
 *                  copied. Must have at least elem_size bytes allocated. Cannot
 *                  be null.
 */
extern void selist_get(selist_t* l,
                       size_t pos,
                       void* elem_out);

/* FUNCTION
 *      selist_init
 *
 * DESCRIPTION
 *      Initializes an selist_t struct with the specified block size.
 *
 * PARAMETERS
 *      l           A valid pointer to an selist_t struct.
 *      elem_size   Size of the elements that will be stored in the list.
 *      block_size  Blocks inside the nodes will have (block_size + 1) space
 *                  allocated (read section 3.3.1 in the book).
 */
extern void selist_init(selist_t* l,
                        size_t elem_size,
                        size_t block_size);

/* FUNCTION
 *      selist_iterator
 *
 * ITERABLE
 *      FORWARD     start <= end
 *      REVERSE     start >  end
 *
 * DESCRIPTION
 *      Initializes an iterator for the specified range [start, end].
 *
 * PARAMETERS
 *      l           Pointer to an initialized selist_t struct.
 *      it          A valid pointer to an iterator_t struct.
 *      start       Start position (inclusive, must be less than length).
 *      end         End position (inclusive, must be less than length).
 */
extern void selist_iterator(selist_t* l,
                            iterator_t* it,
                            size_t start,
                            size_t end);

/* FUNCTION
 *      selist_set
 *
 * DESCRIPTION
 *      Sets the element at the specified position.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized selist_t struct.
 *      pos         The position of the element that will be overwritten.
 *      elem        Pointer to the new element.
 *      old_elem    If not null, the old element at the specified position will
 *                  be copied to this memory.
 */
extern void selist_set(selist_t* l,
                       size_t pos,
                       void* elem,
                       void* old_elem);

/* FUNCTION
 *      selist_remove
 *
 * DESCRIPTION
 *      Removes an element from the specified position.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized selist_t struct.
 *      pos         The position of the element that will be removed.
 *      elem_out    If not null, the removed element will be copied into the
 *                  memory pointed to by this argument.
 */
extern void selist_remove(selist_t* l,
                          size_t pos,
                          void* elem_out);

#endif
