/*******************************************************************************
 * File         : skiplist.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_SKIPLIST_H_
#define ODS_SKIPLIST_H_

#include <stdlib.h>

#include <iterator.h>

typedef struct skiplistnode_t {

    void* data;
    struct skiplistnode_t** next;
    size_t* length;
    size_t next_length;

} skiplistnode_t;

typedef struct skiplist_t {

    skiplistnode_t* sentinel;

    size_t elem_size;
    size_t length;
    size_t height;    

    int (*cmp)(void*, void*);
    int (*rand)();
    
} skiplist_t;

/* FUNCTION
 *      skiplist_add
 *
 * DESCRIPTION
 *      Adds an element to the list.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplist_t struct.
 *      pos         Position where the element will be inserted.
 *      elem        Pointer to the element that will be copied into the list.
 *                  Cannot be null.
 */
void skiplist_add(skiplist_t* s,
                  size_t pos,
                  void* elem);

/* FUNCTION
 *      skiplist_dispose
 *
 * DESCRIPTION
 *      Releases all allocated memory.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplist_t struct.
 */
void skiplist_dispose(skiplist_t* s);

/* FUNCTION
 *      skiplist_get
 *
 * DESCRIPTION
 *      Retrieves the element at the specified position.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplist_t struct.
 *      pos         Position of the element that will be retrieved.
 *      elem_out    Pointer to a memory with at least elem_size bytes allocated.
 *                  The element at pos will be copied into this memory.
 */
void skiplist_get(skiplist_t* s,
                  size_t pos,
                  void* elem_out);

/* FUNCTION
 *      skiplist_init
 *
 * DESCRIPTION
 *      Initializes a skiplist_t struct.
 *
 * PARAMETERS
 *      s           Pointer to a skiplist_t struct.
 *      elem_size   Size of the elements that will be stored in the list.
 *      random      Pointer to a function that generates random integers. This
 *                  function must take no arguments and return an int. If left
 *                  null, the default rand() from stdlib.h will be used.
 */
void skiplist_init(skiplist_t* s,
                   size_t elem_size,
                   int (*random)(void));

/* FUNCTION
 *      skiplist_iterator
 *
 * ITERABLE
 *      FORWARD
 *
 * DESCRIPTION
 *      Initializes an iterator_t struct for the specified skiplist.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplist_t struct.
 *      it          Pointer to an iterator_t struct.
 */
void skiplist_iterator(skiplist_t* s,
                       iterator_t* it);

/* FUNCTION
 *      skiplist_remove
 *
 * DESCRIPTION
 *      Removes an element from the list.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplist_t struct.
 *      pos         Position of the element that will be removed.
 *      elem_out    If not null, the element that is being removed will be
 *                  copied into the memory pointed to by this parameter.
 */
void skiplist_remove(skiplist_t* s,
                     size_t pos,
                     void* elem_out);

/* FUNCTION
 *      skiplist_set
 *
 * DESCRIPTION
 *      Sets the element at the specified position.
 *
 * PARAMETERS
 *      s           Pointer to an initialized skiplist_t struct.
 *      pos         Position of the element that will be set.
 *      elem        The element that will be copied into the list.
 *      old_elem    If not null, the element at pos will be copied into the
 *                  memory pointed to by this parameter before being replaced
 *                  with elem.
 */
void skiplist_set(skiplist_t* s,
                  size_t pos,
                  void* elem,
                  void* old_elem);

#endif
