/*******************************************************************************
 * File         : skiplistsset.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <string.h>
#include <stdlib.h>
#include <assert.h>
#include <stdio.h>

#include <skiplistsset.h>

static skiplistssetnode_t* new_node(void* data, size_t h) {

    skiplistssetnode_t* node = malloc(sizeof(skiplistssetnode_t));
    assert((void *)node != NULL);

    node->next = calloc((h + 1), sizeof(skiplistssetnode_t *));
    assert((void *)node->next != NULL);
    
    node->next_length = h + 1;
    node->data        = data;

    return node;
}

static int pick_height(int (*random)(void)) {

    int z = random();
    int k = 0;
    int m = 1;
    
    while ((z & m) != 0) {
        
        k++;
        m <<= 1;
    }
    
    return k;
}

int skiplistsset_add(skiplistsset_t* s, void* elem) {

    skiplistssetnode_t* node;
    size_t r, i;
    int cmp_result;
    void* elem_cpy;

    assert((void *)s != NULL);
    assert(elem != NULL);

    node = s->sentinel;
    r    = s->height;

    while (1) { /* break if r == 0 */
    
        while (node->next[r] != NULL && 
               (cmp_result = s->cmp(node->next[r]->data, elem)) < 0) {

            node = node->next[r];
        }

        if (node->next[r] != NULL && cmp_result == 0)
            return 0;

        s->stack[r] = node;

        if (r-- == 0)
            break;
    }

    elem_cpy = malloc(s->elem_size);
    assert(elem_cpy != NULL);
    memcpy(elem_cpy, elem, s->elem_size);

    node = new_node(elem_cpy, pick_height(s->rand));

    if (node->next_length > 0)
        while (s->height < node->next_length - 1)
            s->stack[++s->height] = s->sentinel;

    for (i = 0; i < node->next_length; ++i) {

        node->next[i] = s->stack[i]->next[i];
        s->stack[i]->next[i] = node;
    }

    ++s->length;

    return 1;
}

void skiplistsset_init(skiplistsset_t* s, size_t elem_size,
                       int (*comparator)(void*, void*), int (*random)(void)) {

    assert((void *)s != NULL);
    assert(elem_size > 0);
    assert(comparator != NULL);

    s->length     = 0;
    s->elem_size  = elem_size;
    s->cmp        = comparator;
    s->height = 0;
    s->rand       = random == NULL ? rand : random; /* default rand: stdlib */

    /* TODO: 
    s->sentinel   = new_node(NULL, 32);
    s->stack      = calloc(32, sizeof(skiplistssetnode_t *));

    assert((void *)s->stack != NULL);
}
