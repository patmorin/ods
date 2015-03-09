/*******************************************************************************
 * File         : rootisharraystack.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <math.h>

#include <iterator.h>
#include <rootisharraystack.h>

struct rootisharraystack_it {

    rootisharraystack_t* array;

    size_t rem;
    size_t curr_block;      /* current block */
    size_t curr_block_pos;  /* position within current block */
    int fwd;
    int started;

};

static int it_next(iterator_t* it) {

    struct rootisharraystack_it* a = it->istruct;

    if (!a->started)
        return (a->started = 1);

    if (a->rem == 0)
        return 0;

    if (a->fwd) {

        if (a->curr_block_pos == a->curr_block) {
            
            a->curr_block_pos = 0;
            a->curr_block++;
            a->rem--;
            return 1;
        }

        a->curr_block_pos++;
    }

    else {

        if (a->curr_block_pos == 0) {

            a->curr_block--;
            a->curr_block_pos = a->curr_block;
            a->rem--;
            return 1;
        }

        a->curr_block_pos--;
    }

    a->rem--;

    return 1;
}

static void* it_elem(iterator_t* it) {

    struct rootisharraystack_it* a = it->istruct;

    return (char *)(a->array->blocks[a->curr_block]) +
        (a->curr_block_pos * a->array->elem_size);
}

static void it_dispose(iterator_t* it) {

    free(it->istruct);
}

static size_t alloclen(size_t num_blocks) {

    return (num_blocks * (num_blocks + 1)) / 2;
}

static void grow(rootisharraystack_t* r) {

    /* make space for a new pointer */
    r->blocks = realloc(r->blocks, ++r->b_length * sizeof(void *));
    assert(r->blocks != NULL);

    /* make space in the new block */
    r->blocks[r->b_length - 1] = malloc((r->b_length) * r->elem_size);
    assert(r->blocks[r->b_length - 1] != NULL);
}

static size_t pos2block(size_t pos) {

    return (size_t)(ceil((-3.0 + sqrt(9 + (8 * pos))) / 2.0));
}

static void shrink(rootisharraystack_t* r) {

    while (r->b_length > 1 && alloclen(r->b_length - 2) >= r->length) {

        /* free the block */
        free(r->blocks[r->b_length - 1]);
        --r->b_length;
    }

    r->blocks = realloc(r->blocks, (r->b_length * sizeof(void *)));
    assert(r->blocks != NULL);
}

void rootisharraystack_add(rootisharraystack_t* r, size_t pos,
                           void* elem) {

    size_t i;
    void* tmp;

    assert((void *)r != NULL);
    assert(pos <= r->length);
    assert(elem != NULL);

    /* we will use this tmp when shifting elems */
    tmp = malloc(r->elem_size);
    assert(tmp != NULL);

    if (alloclen(r->b_length) < r->length + 1)
        grow(r);

    ++r->length;

    for (i = r->length - 1; i > pos; --i) {
        rootisharraystack_get(r, i - 1, tmp);
        rootisharraystack_set(r, i, tmp, NULL);
    }

    rootisharraystack_set(r, pos, elem, NULL);

    free(tmp);
}

void rootisharraystack_clear(rootisharraystack_t* r) {

    assert((void *)r != NULL);

    r->length = 0;
    shrink(r);
}

void rootisharraystack_dispose(rootisharraystack_t* r) {

    assert((void *)r != NULL);
    
    while (r->b_length > 0)
        free(r->blocks[(r->b_length--) - 1]);

    free(r->blocks);

    r->blocks = NULL;
    r->length = 0;
}

void rootisharraystack_get(rootisharraystack_t* r, size_t pos,
                           void* elem_out) {

    size_t b, subpos;
    void* block; /* this points to the block where pos resides */

    assert((void *)r != NULL);
    assert(pos < r->length);
    assert(elem_out != NULL);

    b     = pos2block(pos);
    block = r->blocks[b];

    /* find the position of pos within the block */
    subpos = pos - alloclen(b); 

    /* rest is trivial, copy element at subpos... */
    memcpy(
        elem_out,
        (char *)block + (subpos * r->elem_size),
        r->elem_size
    );
}

void rootisharraystack_init(rootisharraystack_t* r, size_t elem_size) {

    assert((void *)r != NULL);
    assert(elem_size > 0);
    
    r->length    = 0;
    r->elem_size = elem_size;
    r->b_length        = 1;

    /* allocate space for 1 pointer, which will point to first block */
    r->blocks = malloc(sizeof(void *));
    assert((void *)r->blocks != NULL);

    /* now allocate space for 1 element */
    r->blocks[0] = malloc(elem_size);
    assert(r->blocks[0] != NULL);
}

void rootisharraystack_iterator(rootisharraystack_t* r, iterator_t* it,
                                size_t start, size_t end) {

    struct rootisharraystack_it* istruct;

    assert((void *)r != NULL);
    assert((void *)it != NULL);
    assert(start < r->length);
    assert(end < r->length);

    it->dispose = it_dispose;
    it->elem    = it_elem;
    it->next    = it_next;

    istruct = malloc(sizeof(struct rootisharraystack_it));
    assert((void *)istruct != NULL);

    istruct->array          = r;
    istruct->rem            = start <= end ? end - start : start - end;
    istruct->started        = 0;
    istruct->fwd            = 0;
    istruct->curr_block     = pos2block(start);
    istruct->curr_block_pos = start - alloclen(istruct->curr_block);

    if (start <= end)
        istruct->fwd = 1;

    it->istruct = istruct;
}

void rootisharraystack_remove(rootisharraystack_t* r, size_t pos,
                              void* elem_out) {

    size_t i;
    void* tmp;

    assert((void *)r != NULL);
    assert(pos < r->length);

    tmp = malloc(r->elem_size);
    assert(tmp != NULL);

    if (elem_out != NULL)
        rootisharraystack_get(r, pos, elem_out);

    for (i = pos; i < r->length - 1; ++i) {
        rootisharraystack_get(r, i + 1, tmp);
        rootisharraystack_set(r, i, tmp, NULL);
    }

    --r->length;

    if (alloclen(r->b_length - 2) >= r->length)
        shrink(r);

    free(tmp);
}

void rootisharraystack_set(rootisharraystack_t* r, size_t pos,
                           void* elem, void* elem_out) {

    size_t b, subpos;
    void* block;

    assert((void *)r != NULL);
    assert(pos < r->length);
    assert(elem != NULL);

    b     = pos2block(pos);
    block = r->blocks[b];
    
    subpos = pos - alloclen(b);

    if (elem_out != NULL) {
        memcpy(
            elem_out,
            (char *)block + (subpos * r->elem_size),
            r->elem_size
        );
    }

    memcpy(
        (char *)block + (subpos * r->elem_size),
        elem,
        r->elem_size
    );
}
