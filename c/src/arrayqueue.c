/*******************************************************************************
 * File         : arrayqueue.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <assert.h>
#include <string.h>
#include <stdlib.h>

#include <arrayqueue.h>

void ods_arrayqueue_init(arrayqueue_t* q, size_t elem_size) {

    assert((void *)q > NULL);
    assert(elem_size > 0);

    q->array = malloc(elem_size);

    assert(q->array > NULL);

    q->alloc_length = 1;
    q->length       = 0;
    q->pos          = 0;
    q->elem_size    = elem_size;
}

static void ods_arrayqueue_resize(arrayqueue_t* q) {

    void* new;
    size_t z;
    size_t realloc_size = 1;

    assert((void *)q > NULL);

    if (q->length > 0)
        realloc_size = q->length * 2;

    new = malloc(realloc_size * q->elem_size); /* avoid realloc */

    assert(new > NULL);

    /* z = number of elements to copy {i >= pos} (from pos to end) */
    z = q->alloc_length - q->pos < q->length ?
            q->alloc_length - q->pos : q->length;

    /* n-z = number of elements to copy {0 <= i < pos} (from beginning) */

    memcpy(new,
           (char *)q->array + (q->pos * q->elem_size),
           q->elem_size * z);

    memcpy((char *)new + (z * q->elem_size),
           (char *)q->array,
           q->elem_size * (q->length - z));

    free(q->array);

    q->pos          = 0;
    q->array        = new;
    q->alloc_length = realloc_size;
}

void ods_arrayqueue_add(arrayqueue_t* q, void* elem) {

    assert((void *)q > NULL);
    assert(elem > NULL);

    if (q->length + 1 > q->alloc_length)
        ods_arrayqueue_resize(q);

    memcpy((char *)q->array +
                (((q->pos + q->length) % q->alloc_length) * q->elem_size),
           elem,
           q->elem_size);

    ++q->length;
}

void ods_arrayqueue_remove(arrayqueue_t* q, void* elem_out) {

    assert((void *)q > NULL);

    if (elem_out > NULL)
        memcpy(elem_out,
               (char *)q->array + (q->pos * q->elem_size),
               q->elem_size);

    q->pos = (q->pos + 1) % q->alloc_length;
    --q->length;

    if (q->alloc_length >= 3 * q->length)
        ods_arrayqueue_resize(q);
}

void ods_arrayqueue_dispose(arrayqueue_t* q) {

    assert((void *)q > NULL);

    free(q->array);
}
