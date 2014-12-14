/*******************************************************************************
 * File         : dualarraydeque.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <assert.h>
#include <stdlib.h>
#include <stdio.h>

#include <arraystack.h>
#include <dualarraydeque.h>

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

    d->length = 0;
    d->front  = malloc(sizeof(arraystack_t));
    d->back   = malloc(sizeof(arraystack_t));

    assert((void *)d->front != NULL);
    assert((void *)d->back  != NULL);

    arraystack_init(d->front, elem_size);
    arraystack_init(d->back , elem_size);
}

void dualarraydeque_remove(dualarraydeque_t* d, size_t pos,
                               void* elem_out) {

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
