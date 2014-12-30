/*******************************************************************************
 * File         : skiplistsset.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <string.h>
#include <stdlib.h>
#include <assert.h>

#include <iterator.h>
#include <skiplistsset.h>

static int it_next(iterator_t* it) {

    skiplistssetnode_t* curr_node = it->istruct;

    if (curr_node->next[0] != NULL) {

        it->istruct = curr_node->next[0];
        return 1;
    }

    return 0;
}

static void* it_elem(iterator_t* it) {

    skiplistssetnode_t* curr_node = it->istruct;

    return curr_node->data;
}

static void it_dispose(iterator_t* it) {

    return;
}

static skiplistssetnode_t* new_node(void* data, size_t h) {

    skiplistssetnode_t* node = malloc(sizeof(skiplistssetnode_t));
    assert((void *)node != NULL);

    node->next = calloc(h + 1, sizeof(skiplistssetnode_t *));
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

    while (1) {
    
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

void skiplistsset_dispose(skiplistsset_t* s) {

    skiplistssetnode_t* node;
    skiplistssetnode_t* rm;

    assert((void *)s != NULL);

    node = s->sentinel->next[0];

    while (node != NULL) {

        rm = node;
        node = node->next[0];

        free(rm->data);
        free(rm->next);
        free(rm);
    }

    free(s->sentinel->next);
    free(s->sentinel);
    free(s->stack);
}

int skiplistsset_contains(skiplistsset_t* s, void* elem) {

    skiplistssetnode_t* node;
    size_t r;

    assert((void *)s != NULL);
    assert(elem != NULL);

    node = s->sentinel;
    r    = s->height;

    while (1) {

        while (node->next[r] != NULL && s->cmp(node->next[r]->data, elem) < 0)
            node = node->next[r];

        if (r-- == 0)
            break;
    }

    return node->next[0] == NULL ? 0 :
        (s->cmp(node->next[0]->data, elem) == 0 ? 1 : 0);
}

void skiplistsset_init(skiplistsset_t* s, size_t elem_size,
                       int (*comparator)(void*, void*), int (*random)(void)) {

    assert((void *)s != NULL);
    assert(elem_size > 0);
    assert(comparator != NULL);

    s->length     = 0;
    s->elem_size  = elem_size;
    s->cmp        = comparator;
    s->height     = 0;
    s->rand       = random == NULL ? rand : random; /* default rand: stdlib */

    /* TODO: remove hard-coded limit */
    s->sentinel   = new_node(NULL, 32);
    
    s->stack      = calloc(32, sizeof(skiplistssetnode_t *));
    assert((void *)s->stack != NULL);
}

void skiplistsset_iterator(skiplistsset_t* s, iterator_t* it) {

    assert((void *)s != NULL);
    assert((void *)it != NULL);

    it->dispose = it_dispose;
    it->next    = it_next;
    it->elem    = it_elem;
    it->istruct = s->sentinel;
}

int skiplistsset_remove(skiplistsset_t* s, void* elem) {

    skiplistssetnode_t* node;
    skiplistssetnode_t* old_node;
    size_t r;
    int cmp_result = 0;
    int rm = 0;

    assert((void *)s != NULL);
    assert(elem != NULL);

    node = s->sentinel;
    r    = s->height;

    while (1) {
        
        while (node->next[r] != NULL &&
               (cmp_result = s->cmp(node->next[r]->data, elem)) < 0) {

            node = node->next[r];
        }
        
        if (node->next[r] != NULL && cmp_result == 0) {

            rm = 1;

            old_node = node->next[r];
            node->next[r] = node->next[r]->next[r];

            if (r == 0) {

                /* at r = 0, we're removing the last reference to the node
                 * that's being removed (old_node), hence we can free it */
                free(old_node->data);
                free(old_node->next);
                free(old_node);
            }

            if (node == s->sentinel && node->next[r] == NULL)
                s->height--;
        }

        if (r-- == 0)
            break;
    }

    if (rm)
        s->length--;

    return rm;
}
