/*******************************************************************************
 * File         : sllist.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_SLLIST_H_
#define ODS_SLLIST_H_

#include <stdlib.h>

#define sllist_dequeue(l, elem) \
            sllist_pop((l), (elem))

typedef struct slnode_t {

    void* data;
    struct slnode_t* next;

} slnode_t;

typedef struct {

    size_t elem_size;
    size_t length;
    slnode_t* head;
    slnode_t* tail;

} sllist_t;

/* FUNCTION
 *      sllist_dispose
 *
 * DESCRIPTION
 *      Releases all memory allocated by other sllist_* functions. If you have
 *      any pointer references to nodes, they will be invalid after a call to
 *      this function.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized sllist_t struct.
 */
extern void sllist_dispose(sllist_t* l);

/* FUNCTION
 *      sllist_enqueue
 *
 * DESCRIPTION
 *      Adds an element to the tail of the list.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized sllist_t struct.
 *      elem        Pointer to the element that will be inserted. Cannot be
 *                  null.
 */
extern void sllist_enqueue(sllist_t* l,
                           void* elem);

/* FUNCTION
 *      sllist_init
 *
 * DESCRIPTION
 *      Initializes an sllist_t struct.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized sllist_t struct.
 *      elem_size   Size of the elements that will be stored in the list.
 */
extern void sllist_init(sllist_t* l,
                        size_t elem_size);

/* FUNCTION
 *      sllist_pop (sllist_dequeue)
 *
 * DESCRIPTION
 *      Pops an element from the head of the list.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized sllist_t struct.
 *      elem_out    If not null, the popped element will be copied into this
 *                  memory.
 */
extern void sllist_pop(sllist_t* l,
                       void* elem_out);

/* FUNCTION
 *      sllist_push
 *
 * DESCRIPTION
 *      Pushes an element to the head of the list.
 *
 * PARAMETERS
 *      l           A valid pointer to an initialized sllist_t struct.
 *      elem        Pointer to the element that will be inserted. Cannot be
 *                  null.
 */
extern void sllist_push(sllist_t* l,
                        void* elem);

#endif
