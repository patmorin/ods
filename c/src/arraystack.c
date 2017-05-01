/*******************************************************************************
 * File         : arraystack.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <stdlib.h>
#include <assert.h>
#include <string.h>

#include <arraystack.h>

struct arraystack_it {

    arraystack_t* stack;

    size_t end;
    size_t curr;
    int started;
    int fwd;

};

static int it_next(iterator_t* it) {

    struct arraystack_it* a = it->istruct;

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

    struct arraystack_it* a = it->istruct;

    return (char *)a->stack->array + (a->curr * a->stack->elem_size);
}

static void it_dispose(iterator_t* it) {

    free(it->istruct);
}

static void resize(arraystack_t* s) {

    size_t realloc_size = 1;
    
    if (s->length > 0)
        realloc_size = s->length * 2;

    s->array = realloc(s->array, realloc_size * s->elem_size);

    assert(s->array != NULL);

    s->alloc_length = realloc_size;
}

void arraystack_add(arraystack_t* s, size_t pos, void* elem) {

    assert((void *)s != NULL);
    assert(elem != NULL);
    assert(pos <= s->length);

    if (1 + s->length > s->alloc_length)
        resize(s);

    /* shift elements to the right according to insertion position */
    memmove((char *)s->array + ((1 + pos) * s->elem_size),
            (char *)s->array + (pos * s->elem_size),
            (s->length - pos) * s->elem_size);

    /* add the element in */
    memcpy((char *)s->array + (pos * s->elem_size), elem, s->elem_size);

    ++s->length;
}

void arraystack_clear(arraystack_t* s) {

    assert((void *)s != NULL);

    s->length       = 0;
    s->alloc_length = 1;
    s->array        = realloc(s->array, s->elem_size);

    assert(s->array != NULL);
}

void arraystack_copy(arraystack_t* dest, size_t dest_pos,
                         arraystack_t* src, size_t src_pos,
                         size_t num_elems) {

    size_t elem_size;

    if (num_elems < 1)
        return;

    assert((void *)dest != NULL);
    assert((void *)src  != NULL);
    assert(dest->elem_size == src->elem_size); /* elem size mismatch */
    assert(dest_pos <= dest->length);
    assert(src_pos + num_elems <= src->length);

    elem_size = dest->elem_size;

    if (dest->length + num_elems > dest->alloc_length)
        arraystack_reserve(dest, dest->length + num_elems);

    memmove(
        (char *)dest->array + ((dest_pos + num_elems) * elem_size),
        (char *)dest->array + (dest_pos * elem_size),
        (dest->length - dest_pos) * elem_size
    );

    memcpy(
        (char *)dest->array + (dest_pos * elem_size),
        (char *)src->array  + (src_pos  * elem_size),
        num_elems * elem_size
    );

    dest->length += num_elems;
}

void arraystack_dispose(arraystack_t* s) {

    assert((void *)s != NULL);
    
    free(s->array);
}

void arraystack_get(arraystack_t* s, size_t pos, void* elem) {

    assert((void *)s != NULL);
    assert(elem != NULL);
    assert(s->length > 0);
    assert(pos < s->length);

    memcpy(elem, (char *)s->array + (pos * s->elem_size), s->elem_size);
}

void arraystack_init(arraystack_t* s, size_t elem_size) {

    assert((void *)s != NULL);
    assert(elem_size > 0);

    s->array = malloc(elem_size);

    assert(s->array != NULL);

    s->length       = 0;
    s->alloc_length = 1;
    s->elem_size    = elem_size;
}

void arraystack_iterator(arraystack_t* s, iterator_t* it,
                         size_t start, size_t end) {

    struct arraystack_it* istruct;

    assert((void *)s != NULL);
    assert(start < s->length);
    assert(end < s->length);

    it->next    = it_next;
    it->elem    = it_elem;
    it->dispose = it_dispose;

    istruct = malloc(sizeof(struct arraystack_it));
    assert(istruct != NULL);

    istruct->stack   = s;
    istruct->end     = end;
    istruct->curr    = start;
    istruct->started = 0;
    istruct->fwd     = 0;

    if (start <= end)
        istruct->fwd = 1;

    it->istruct = istruct;
}

void arraystack_remove(arraystack_t* s, size_t pos, void* elem_out) {

    assert((void *)s != NULL);
    assert(s->length > 0);
    assert(pos < s->length);

    /* copy the removed data */
    if (elem_out != NULL)
        memcpy(elem_out, (char *)s->array + (pos * s->elem_size), s->elem_size);

    /* shift the elements to the left */
    memmove((char *)s->array + (pos * s->elem_size),
            (char *)s->array + ((1 + pos) * s->elem_size),
            (s->length - pos - 1) * s->elem_size);

    --s->length;

    /* resize if necessary */
    if (s->length * 3 < s->alloc_length)
        resize(s);
}

void arraystack_reserve(arraystack_t* s, size_t n) {

    assert((void *)s != NULL);
    assert(n >= s->length);

    if (n == s->length)
        return;

    s->array = realloc(s->array, n * s->elem_size);

    assert(s->array != NULL);

    s->alloc_length = n;
}

void arraystack_reverse(arraystack_t* s, size_t pos, size_t num_elems) {

    size_t i, j;
    void* tmp;

    if (num_elems < 2)
        return;

    assert((void *)s != NULL);
    assert(pos + num_elems <= s->length);

    tmp = malloc(s->elem_size);
    assert(tmp != NULL);

    for (i = pos, j = (pos + num_elems) - 1; i < j; ++i, --j) {

        memcpy(tmp, (char *)s->array + (i * s->elem_size), s->elem_size);
        
        memcpy(
            (char *)s->array + (i * s->elem_size),
            (char *)s->array + (j * s->elem_size),
            s->elem_size
        );

        memcpy((char *)s->array + (j * s->elem_size), tmp, s->elem_size);
    }

    free(tmp);
}

void arraystack_set(arraystack_t* s, size_t pos,
                        void* elem, void* old_elem) {

    assert((void *)s != NULL);
    assert(elem != NULL);
    assert(s->length > 0);
    assert(pos < s->length);

    /* first copy the old data*/
    if (old_elem != NULL)
        memcpy(old_elem, (char *)s->array + (pos * s->elem_size), s->elem_size);

    memcpy((char *)s->array + (pos * s->elem_size), elem, s->elem_size);
}

void arraystack_truncate(arraystack_t* s, size_t pos, size_t num_elems) {

    assert((void *)s != NULL);
    assert(pos + num_elems <= s->length);

    if (num_elems == 0)
        return;

    /* there is no need to move anything if truncating end of array */
    if (pos + num_elems < s->length) {
        memmove(
            (char *)s->array + (pos * s->elem_size),
            (char *)s->array + ((pos + num_elems) * s->elem_size),
            (s->length - (pos + num_elems)) * s->elem_size
        );
    }

    s->length -= num_elems;

    if (s->length * 3 < s->alloc_length)
        resize(s);
}
