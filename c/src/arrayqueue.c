/*******************************************************************************
 * File         : arrayqueue.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <assert.h>
#include <string.h>
#include <stdlib.h>

#include <iterator.h>
#include <arrayqueue.h>

struct arrayqueue_it {

    arrayqueue_t* queue;

    size_t end;
    size_t curr;
    int started;
    int fwd;

};

static int it_next(iterator_t* it) {

    struct arrayqueue_it* a = it->istruct;

    if (!a->started) {
        
        a->started = 1;
        return 1;
    }

    if (a->curr == a->end)
        return 0;

    if (a->fwd)
        a->curr++;
    else
        a->curr--;

    return 1;
}

static void* it_elem(iterator_t* it) {

    struct arrayqueue_it* a = it->istruct;
    
    size_t i = (a->queue->pos + a->curr) % a->queue->alloc_length;
    
    return (char *)a->queue->array + (i * a->queue->elem_size);
}

static void it_dispose(iterator_t* it) {

    free(it->istruct);
}

static void resize(arrayqueue_t* q) {

    void* new;
    size_t z;
    size_t realloc_size = 1;

    if (q->length > 0)
        realloc_size = q->length * 2;

    new = malloc(realloc_size * q->elem_size); /* avoid realloc */

    assert(new != NULL);

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

void arrayqueue_clear(arrayqueue_t* q) {

    assert((void *)q != NULL);

    q->length       = 0;
    q->pos          = 0;
    q->alloc_length = 1;
    q->array        = realloc(q->array, q->elem_size);

    assert(q->array != NULL);
}

void arrayqueue_dequeue(arrayqueue_t* q, void* elem_out) {

    assert((void *)q != NULL);
    assert(q->length > 0);

    if (elem_out != NULL)
        memcpy(elem_out,
               (char *)q->array + (q->pos * q->elem_size),
               q->elem_size);

    q->pos = (q->pos + 1) % q->alloc_length;
    --q->length;

    if (q->alloc_length >= 3 * q->length)
        resize(q);
}

void arrayqueue_dispose(arrayqueue_t* q) {

    assert((void *)q != NULL);

    free(q->array);
}

void arrayqueue_enqueue(arrayqueue_t* q, void* elem) {

    assert((void *)q != NULL);
    assert(elem != NULL);

    if (q->length + 1 > q->alloc_length)
        resize(q);

    memcpy((char *)q->array +
                (((q->pos + q->length) % q->alloc_length) * q->elem_size),
           elem,
           q->elem_size);

    ++q->length;
}

void arrayqueue_init(arrayqueue_t* q, size_t elem_size) {

    assert((void *)q != NULL);
    assert(elem_size > 0);

    q->array = malloc(elem_size);

    assert(q->array != NULL);

    q->alloc_length = 1;
    q->length       = 0;
    q->pos          = 0;
    q->elem_size    = elem_size;
}

void arrayqueue_iterator(arrayqueue_t* q, iterator_t *it,
                         size_t start, size_t end) {

    struct arrayqueue_it* istruct;

    assert((void *)q != NULL);
    assert((void *)it != NULL);
    assert(start < q->length);
    assert(end < q->length);

    it->next    = it_next;
    it->elem    = it_elem;
    it->dispose = it_dispose;

    istruct = malloc(sizeof(struct arrayqueue_it));
    assert(istruct != NULL);

    istruct->queue   = q;
    istruct->end     = end;
    istruct->curr    = start;
    istruct->started = 0;
    istruct->fwd     = 0;

    if (start <= end)
        istruct->fwd = 1;

    it->istruct = istruct;
}

void arrayqueue_peek(arrayqueue_t* q, void* elem_out) {

    assert((void *)q != NULL);
    assert(elem_out != NULL);
    assert(q->length > 0);
    
    memcpy(
        elem_out,
        (char *)q->array + (q->pos * q->elem_size),
        q->elem_size
    );
}
