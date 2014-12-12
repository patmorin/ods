    /*******************************************************************************
 * File         : dualarraydeque.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <assert.h>
#include <stdlib.h>
#include <stdio.h>

#include <arraystack.h>
#include <dualarraydeque.h>

static void ods_dualarraydeque_balance(dualarraydeque_t* d) {

    size_t s;

    if (3 * d->front->length < d->back->length) {
        
        s = (d->length / 2) - d->front->length;
        
        /* move s elements from the beginning of d->back to d->front */
        ods_arraystack_copy(d->front, 0, d->back, 0, s);
        
        /* reverse the newly added range in d->front */
        ods_arraystack_reverse(d->front, 0, s);

        /* truncate the d->back arraystack by s elements, starting from 0 */
        ods_arraystack_truncate(d->back, 0, s);
    }

    else if (3 * d->back->length < d->front->length) {
        
        s = d->front->length - (d->length / 2);
        
        /* same idea as above... */
        ods_arraystack_copy(d->back, 0, d->front, 0, s);
        ods_arraystack_reverse(d->back, 0, s);
        ods_arraystack_truncate(d->front, 0, s);
    }
}

void ods_dualarraydeque_add(dualarraydeque_t* d, size_t pos, void* elem) {

    assert((void *)d > NULL);
    assert(elem > NULL);
    assert(pos <= d->length);    

    if (pos < d->front->length)
        ods_arraystack_add(d->front, d->front->length - pos, elem);
    else
        ods_arraystack_add(d->back, pos - d->front->length, elem);

    ++d->length;

    ods_dualarraydeque_balance(d);
}

void ods_dualarraydeque_dispose(dualarraydeque_t* d) {

    assert((void *)d > NULL);

    d->length = 0;
    
    free(d->front);
    free(d->back);
}

void ods_dualarraydeque_get(dualarraydeque_t* d, size_t pos, void* elem_out) {

    assert((void *)d > NULL);
    assert(elem_out > NULL);
    assert(pos < d->length);

    if (pos < d->front->length)
        ods_arraystack_get(d->front, d->front->length - pos - 1, elem_out);
    else
        ods_arraystack_get(d->back, pos - d->front->length, elem_out);
}

void ods_dualarraydeque_init(dualarraydeque_t* d, size_t elem_size) {

    assert((void *)d > NULL);
    assert(elem_size > 0);

    d->length = 0;
    d->front  = malloc(sizeof(arraystack_t));
    d->back   = malloc(sizeof(arraystack_t));

    assert((void *)d->front > NULL);
    assert((void *)d->back  > NULL);

    ods_arraystack_init(d->front, elem_size);
    ods_arraystack_init(d->back , elem_size);
}

void ods_dualarraydeque_remove(dualarraydeque_t* d, size_t pos,
                               void* elem_out) {

    assert((void *)d > NULL);
    assert(pos < d->length);

    if (pos < d->front->length)
        ods_arraystack_remove(d->front, d->front->length - pos - 1, elem_out);
    else
        ods_arraystack_remove(d->back, pos - d->front->length, elem_out);

    --d->length;

    ods_dualarraydeque_balance(d);
}

void ods_dualarraydeque_set(dualarraydeque_t* d, size_t pos,
                            void* elem, void* old_elem) {

    assert((void *)d > NULL);
    assert(elem > NULL);
    assert(pos < d->length);

    if (pos < d->front->length) {
        ods_arraystack_set(
            d->front,
            d->front->length - pos - 1,
            elem,
            old_elem
        );
    }

    else {
        ods_arraystack_set(
            d->back,
            pos - d->front->length,
            elem,
            old_elem
        );
    }
}
