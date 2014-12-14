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

extern void sllist_dispose(sllist_t* l);

extern void sllist_enqueue(sllist_t* l,
                           void* elem);

extern void sllist_init(sllist_t* l,
                        size_t elem_size);

extern void sllist_pop(sllist_t* l,
                       void* elem_out);

extern void sllist_push(sllist_t* l,
                        void* elem);

#endif
