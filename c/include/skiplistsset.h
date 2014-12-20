/*******************************************************************************
 * File         : skiplistsset.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_SKIPLISTSSET_H_
#define ODS_SKIPLISTSSET_H_

#include <stdlib.h>

#include <arraydeque.h>

typedef struct skiplistssetnode_t {

    void* data;
    struct skiplistssetnode_t** next;
    size_t next_length;

} skiplistssetnode_t;

typedef struct {

    skiplistssetnode_t* sentinel;
    skiplistssetnode_t** stack;

    size_t length;
    size_t elem_size;
    size_t height;    

    int (*cmp)(void*, void*);
    int (*rand)();

} skiplistsset_t;

extern int skiplistsset_add(skiplistsset_t* s,
                            void* elem);

extern void skiplistsset_init(skiplistsset_t* s,
                              size_t elem_size,
                              int (*comparator)(void*, void*),
                              int (*random)(void));

#endif
