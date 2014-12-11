/*******************************************************************************
 * File         : arraydeque.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_ARRAYDEQUE_H_
#define ODS_ARRAYDEQUE_H_

#include <stdlib.h>

typedef struct {

    size_t alloc_length;
    size_t length;
    size_t pos;
    size_t elem_size;
    void* array;

} arraydeque_t;

void ods_arraydeque_init(arraydeque_t* d,
                         size_t elem_size);

void ods_arraydeque_get(arraydeque_t* d,
                        size_t pos,
                        void* elem_out);

void ods_arraydeque_set(arraydeque_t* d,
                        size_t pos,
                        void* elem);

void ods_arraydeque_add(arraydeque_t* d,
                        size_t pos,
                        void* elem);

void ods_arraydeque_remove(arraydeque_t*d,
                           size_t pos,
                           void* elem_out);

void ods_arraydeque_dispose(arraydeque_t* d);

#endif
