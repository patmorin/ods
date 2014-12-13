/*******************************************************************************
 * File         : rootisharraystack.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <math.h>

#include <rootisharraystack.h>

static size_t alloclen(size_t num_blocks) {

    return (num_blocks * (num_blocks + 1)) / 2;
}

static void grow(rootisharraystack_t* r) {

    /* make space for a new pointer */
    r->blocks = realloc(r->blocks, ++r->bs * sizeof(void *));
    assert(r->blocks != NULL);

    /* make space in the new block */
    r->blocks[r->bs - 1] = malloc((r->bs - 1) * r->elem_size);
    assert(r->blocks[r->bs - 1] != NULL);
}

static size_t pos2block(size_t pos) {

    return (size_t)(ceil((-3.0 + sqrt(9 + (8 * pos))) / 2.0));
}

static void shrink(rootisharraystack_t* r) {

    while (r->bs > 1 &&
           alloclen(r->bs - 2) >= r->length) {

        /* free the block */
        free(r->blocks[r->bs - 1]);
        --r->bs;
    }

    r->blocks = realloc(r->blocks, (r->bs * sizeof(void *)));
    assert(r->blocks != NULL);
}

void ods_rootisharraystack_add(rootisharraystack_t* r, size_t pos,
                               void* elem) {

    size_t i;
    void* tmp;

    assert((void *)r != NULL);
    assert(pos <= r->length);
    assert(elem != NULL);

    /* we will use this tmp when shifting elems */
    tmp = malloc(r->elem_size);
    assert(tmp != NULL);

    if (alloclen(r->bs) < r->length + 1)
        grow(r);

    ++r->length;

    for (i = r->length - 1; i > pos; --i) {
        ods_rootisharraystack_get(r, i - 1, tmp);
        ods_rootisharraystack_set(r, i, tmp, NULL);
    }

    ods_rootisharraystack_set(r, pos, elem, NULL);
}

void ods_rootisharraystack_dispose(rootisharraystack_t* r) {

    assert((void *)r != NULL);
    
    while (r->bs > 0)
        free(r->blocks[(r->bs--) - 1]);

    free(r->blocks);

    r->blocks = NULL;
    r->length = 0;
}

void ods_rootisharraystack_get(rootisharraystack_t* r, size_t pos,
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

void ods_rootisharraystack_init(rootisharraystack_t* r, size_t elem_size) {

    assert((void *)r != NULL);
    assert(elem_size > 0);
    
    r->length    = 0;
    r->elem_size = elem_size;
    r->bs        = 1;

    /* allocate space for 1 pointer, which will point to first block */
    r->blocks = malloc(sizeof(void *));
    assert((void *)r->blocks != NULL);

    /* now allocate space for 1 element */
    r->blocks[0] = malloc(elem_size);
    assert(r->blocks[0] != NULL);
}

void ods_rootisharraystack_remove(rootisharraystack_t* r, size_t pos,
                                   void* elem_out) {

    size_t i;
    void* tmp;

    assert((void *)r != NULL);
    assert(pos < r->length);

    tmp = malloc(r->elem_size);
    assert(tmp != NULL);

    if (elem_out != NULL)
        ods_rootisharraystack_get(r, pos, elem_out);

    for (i = pos; i < r->length - 1; ++i) {
        ods_rootisharraystack_get(r, i + 1, tmp);
        ods_rootisharraystack_set(r, i, tmp, NULL);
    }

    --r->length;

    if (alloclen(r->bs - 2) >= r->length)
        shrink(r);
}

void ods_rootisharraystack_set(rootisharraystack_t* r, size_t pos,
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
