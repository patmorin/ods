/*******************************************************************************
 * File         : dualarraydeque.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <assert.h>
#include <stdlib.h>

#include <iterator.h>
#include <arraystack.h>
#include <dualarraydeque.h>

struct dualarraydeque_it {

    dualarraydeque_t* deque;

    size_t curr;
    size_t end;
    int fwd;
    int started;

};

static int it_next(iterator_t* it) {

    struct dualarraydeque_it* a = it->istruct;

    if (!a->started)
        return (a->started = 1);

    if (a->curr == a->end)
        return 0;

    a->fwd ? a->curr++ : a->curr--;

    return 1;
}

static void* it_elem(iterator_t* it) {

    struct dualarraydeque_it* a = it->istruct;

    if (a->curr < a->deque->front->length)
        return (char *)a->deque->front->array +
            ((a->deque->front->length - a->curr - 1) * a->deque->elem_size);

    return (char *)a->deque->back->array +
        ((a->curr - a->deque->front->length) * a->deque->elem_size);
}

static void it_dispose(iterator_t* it) {

    free(it->istruct);
}

static void balance(dualarraydeque_t* d) {

    size_t s;

    if (3 * d->front->length < d->back->length) {
        
        s = (d->length / 2) - d->front->length;
        
        /* move s elements from the beginning of d->back to d->front */
        arraystack_copy(d->front, 0, d->back, 0, s);
        
        /* reverse the newly added range in d->front */
        arraystack_reverse(d->front, 0, s);

        /* truncate the d->back arraystack by s elements, starting from 0 */
        arraystack_truncate(d->back, 0, s);
    }

    else if (3 * d->back->length < d->front->length) {
        
        s = d->front->length - (d->length / 2);
        
        /* same idea as above... */
        arraystack_copy(d->back, 0, d->front, 0, s);
        arraystack_reverse(d->back, 0, s);
        arraystack_truncate(d->front, 0, s);
    }
}

void dualarraydeque_add(dualarraydeque_t* d, size_t pos, void* elem) {

    assert((void *)d != NULL);
    assert(elem != NULL);
    assert(pos <= d->length);    

    if (pos < d->front->length)
        arraystack_add(d->front, d->front->length - pos, elem);
    else
        arraystack_add(d->back, pos - d->front->length, elem);

    ++d->length;

    balance(d);
}

void dualarraydeque_dispose(dualarraydeque_t* d) {

    assert((void *)d != NULL);

    d->length = 0;

    arraystack_dispose(d->front);
    arraystack_dispose(d->back);
    
    free(d->front);
    free(d->back);
}

void dualarraydeque_get(dualarraydeque_t* d, size_t pos, void* elem_out) {

    assert((void *)d != NULL);
    assert(elem_out != NULL);
    assert(pos < d->length);

    if (pos < d->front->length)
        arraystack_get(d->front, d->front->length - pos - 1, elem_out);
    else
        arraystack_get(d->back, pos - d->front->length, elem_out);
}

void dualarraydeque_init(dualarraydeque_t* d, size_t elem_size) {

    assert((void *)d != NULL);
    assert(elem_size > 0);

    d->length    = 0;
    d->elem_size = elem_size;
    d->front     = malloc(sizeof(arraystack_t));
    d->back      = malloc(sizeof(arraystack_t));

    assert((void *)d->front != NULL);
    assert((void *)d->back  != NULL);

    arraystack_init(d->front, elem_size);
    arraystack_init(d->back , elem_size);
}

void dualarraydeque_iterator(dualarraydeque_t* d, iterator_t* it,
                             size_t start, size_t end) {

    struct dualarraydeque_it* istruct;

    assert((void *)d != NULL);
    assert((void *)it != NULL);
    assert(start < d->length);
    assert(end < d->length);

    it->next    = it_next;
    it->elem    = it_elem;
    it->dispose = it_dispose;

    istruct = malloc(sizeof(struct dualarraydeque_it));
    assert((void *)istruct != NULL);

    istruct->deque   = d;
    istruct->end     = end;
    istruct->curr    = start;
    istruct->started = 0;
    istruct->fwd     = 0;

    if (start <= end)
        istruct->fwd = 1;

    it->istruct = istruct;
}

void dualarraydeque_remove(dualarraydeque_t* d, size_t pos, void* elem_out) {

    assert((void *)d != NULL);
    assert(pos < d->length);

    if (pos < d->front->length)
        arraystack_remove(d->front, d->front->length - pos - 1, elem_out);
    else
        arraystack_remove(d->back, pos - d->front->length, elem_out);

    --d->length;

    balance(d);
}

void dualarraydeque_set(dualarraydeque_t* d, size_t pos,
                        void* elem, void* old_elem) {

    assert((void *)d != NULL);
    assert(elem != NULL);
    assert(pos < d->length);

    if (pos < d->front->length) {
        arraystack_set(
            d->front,
            d->front->length - pos - 1,
            elem,
            old_elem
        );
    }

    else {
        arraystack_set(
            d->back,
            pos - d->front->length,
            elem,
            old_elem
        );
    }
}
