/*******************************************************************************
 * File         : arraydeque.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include <iterator.h>
#include <arraydeque.h>

struct arraydeque_it {

    arraydeque_t* deque;

    size_t curr;
    size_t end;
    int fwd;
    int started;

};

static int it_next(iterator_t* it) {

    struct arraydeque_it* a = it->istruct;

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

    struct arraydeque_it* a = it->istruct;

    size_t i = (a->deque->pos + a->curr) % a->deque->alloc_length;
    
    return (char *)a->deque->array + (i * a->deque->elem_size);
}

static void it_dispose(iterator_t* it) {

    free(it->istruct);
}

static void resize(arraydeque_t* d) {

    void* new;
    size_t z;
    size_t realloc_size = 1;

    if (d->bound)
        return;

    if (d->length > 0)
        realloc_size = d->length * 2;

    new = malloc(realloc_size * d->elem_size);

    assert(new != NULL);

    /* z = number of elements to copy {i >= pos} (from pos to end) */
    z = d->alloc_length - d->pos < d->length ?
            d->alloc_length - d->pos : d->length;

    /* n-z = number of elements to copy {0 <= i < pos} (from beginning) */

    memcpy(new,
           (char *)d->array + (d->pos * d->elem_size),
           d->elem_size * z);

    memcpy((char *)new + (z * d->elem_size),
           (char *)d->array,
           d->elem_size * (d->length - z));

    free(d->array);

    d->pos          = 0;
    d->array        = new;
    d->alloc_length = realloc_size;
}

void arraydeque_add(arraydeque_t* d, size_t pos, void* elem) {

    size_t i;

    assert((void *)d != NULL);
    assert(elem != NULL);
    assert(pos <= d->length);

    if (d->bound)
        assert(d->length + 1 <= d->alloc_length);

    if (d->length + 1 > d->alloc_length)
        resize(d);

    /* what about rewriting the following shift without loops?
     * memcpy'ing as much contiguous blocks as possible? */
    if (pos < d->length / 2) {

        d->pos = (d->pos == 0) ? d->alloc_length - 1 : d->pos - 1;
        
        if (pos > 0) { /* size_t is often unsigned, pos-1 might break it */
            for (i = 0; i <= pos - 1; ++i) {
                memcpy(
                    (char *)d->array +
                        (((d->pos + i) % d->alloc_length) * d->elem_size),
                    (char *)d->array +
                        (((d->pos + i + 1) % d->alloc_length) * d->elem_size),
                    d->elem_size
                );
            }
        }
    }

    else {
        
        for (i = d->length; i > pos; --i) {
            memcpy(
                (char *)d->array +
                    (((d->pos + i) % d->alloc_length) * d->elem_size),
                (char *)d->array +
                    (((d->pos + i - 1) % d->alloc_length) * d->elem_size),
                d->elem_size
            );
        }
    }

    memcpy(
        (char *)d->array + (((d->pos + pos) % d->alloc_length) * d->elem_size),
        elem,
        d->elem_size
    );

    ++d->length;
}

void arraydeque_clear(arraydeque_t* d) {

    assert((void *)d != NULL);

    d->alloc_length = 1;
    d->length       = 0;
    d->pos          = 0;
    d->array        = realloc(d->array, d->elem_size);

    assert(d->array != NULL);
}

void arraydeque_dispose(arraydeque_t* d) {

    assert((void *)d != NULL);

    free(d->array);
}

void arraydeque_get(arraydeque_t* d, size_t pos, void* elem_out) {

    assert((void *)d != NULL);
    assert(elem_out != NULL);
    assert(d->length > 0);
    assert(pos < d->length);

    memcpy(
        elem_out,
        (char *)d->array + (((d->pos + pos) % d->alloc_length) * d->elem_size),
        d->elem_size
    );
}

void arraydeque_init(arraydeque_t* d, size_t elem_size) {

    assert((void *)d != NULL);
    assert(elem_size > 0);

    d->array = malloc(elem_size);

    assert(d->array != NULL);

    d->alloc_length = 1;
    d->length       = 0;
    d->pos          = 0;
    d->bound        = 0;
    d->elem_size    = elem_size;
}

void arraydeque_init_bound(arraydeque_t* d, size_t elem_size, size_t space) {

    assert((void *)d != NULL);
    assert(elem_size > 0);

    d->array = malloc(space * elem_size);

    assert(d->array != NULL);

    d->alloc_length = space;
    d->length       = 0;
    d->pos          = 0;
    d->bound        = 1;
    d->elem_size    = elem_size;
}

void arraydeque_iterator(arraydeque_t* d, iterator_t* it,
                         size_t start, size_t end) {

    struct arraydeque_it* istruct;

    assert((void *)d != NULL);
    assert((void *)it != NULL);
    assert(start < d->length);
    assert(end < d->length);

    it->next    = it_next;
    it->elem    = it_elem;
    it->dispose = it_dispose;

    istruct = malloc(sizeof(struct arraydeque_it));
    assert((void *)istruct != NULL);

    istruct->deque   = d;
    istruct->curr    = start;
    istruct->end     = end;
    istruct->started = 0;
    istruct->fwd     = 0;

    if (start <= end)
        istruct->fwd = 1;

    it->istruct = istruct;
}

void arraydeque_remove(arraydeque_t* d, size_t pos, void* elem_out) {

    size_t i;

    assert((void *)d != NULL);
    assert(d->length > 0);
    assert(pos < d->length);

    if (elem_out != NULL) {
        memcpy(
            elem_out,
            (char *)d->array +
                (((d->pos + pos) % d->alloc_length) * d->elem_size),
            d->elem_size
        );
    }

    /* what about rewriting the following shift without loops?
     * memcpy'ing as much contiguous blocks as possible? */
    if (pos < d->length / 2) {
        for (i = pos; i > 0; --i) {
            memcpy(
                (char *)d->array +
                    (((d->pos + i) % d->alloc_length) * d->elem_size),
                (char *)d->array +
                    (((d->pos + i - 1) % d->alloc_length) * d->elem_size),
                d->elem_size
            );
        }

        d->pos = (d->pos + 1) % d->alloc_length;
    }

    else {
        for (i = pos; i < d->length - 1; ++i) {
            memcpy(
                (char *)d->array +
                    (((d->pos + i) % d->alloc_length) * d->elem_size),
                (char *)d->array +
                    (((d->pos + i + 1) % d->alloc_length) * d->elem_size),
                d->elem_size
            );
        }
    }

    --d->length;

    if (3 * d->length < d->alloc_length && !d->bound)
        resize(d);
}

void arraydeque_set(arraydeque_t* d, size_t pos, void* elem, void* elem_out) {

    assert((void *)d != NULL);
    assert(elem != NULL);
    assert(d->length > 0);
    assert(pos < d->length);

    if (elem_out != NULL) {
        memcpy(
            elem_out,
            (char *)d->array +
                (((d->pos + pos) % d->alloc_length) * d->elem_size),
            d->elem_size
        );
    }

    memcpy(
        (char *)d->array + (((d->pos + pos) % d->alloc_length) * d->elem_size),
        elem,
        d->elem_size
    );
}
