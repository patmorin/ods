/*******************************************************************************
 * File         : skiplist.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <assert.h>
#include <string.h>
#include <stdlib.h>

#include <skiplist.h>
#include <iterator.h>

static int it_next(iterator_t* it) {

    skiplistnode_t* curr_node = it->istruct;

    if (curr_node->next[0] != NULL) {

        it->istruct = curr_node->next[0];
        return 1;
    }

    return 0;
}

static void* it_elem(iterator_t* it) {

    skiplistnode_t* curr_node = it->istruct;

    return curr_node->data;
}

static void it_dispose(iterator_t* it) {

    return;
}

static skiplistnode_t* new_node(void* data, size_t h) {

    skiplistnode_t* node = malloc(sizeof(skiplistnode_t));
    assert((void *)node != NULL);

    node->next = calloc(h + 1, sizeof(skiplistnode_t *));
    assert((void *)node->next != NULL);

    node->length = calloc(h + 1, sizeof(size_t));
    assert((void *)node->length != NULL);

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

static skiplistnode_t* find(skiplist_t* s, size_t pos) {

    skiplistnode_t* node = s->sentinel;
    size_t r = s->height;
    size_t j = 0;

    while (1) {

        while (node->next[r] != NULL && j + node->length[r] < pos) {
            
            j += node->length[r];
            node = node->next[r];
        }
        
        if (r-- == 0)
            break;
    }

    return node;
}

static void add_node(skiplist_t* s, size_t pos, skiplistnode_t* node) {

    skiplistnode_t* u = s->sentinel;
    size_t k = node->next_length - 1;
    size_t r = s->height;
    size_t j = 0;

    while (1) {
        
        while (u->next[r] != NULL && j + u->length[r] < pos) {

            j += u->length[r];
            u = u->next[r];
        }

        u->length[r]++;

        if (r <= k) {

            node->next[r]   = u->next[r];
            u->next[r]      = node;
            node->length[r] = u->length[r] - (pos - j);
            u->length[r]    = pos - j;
        }

        if (r-- == 0)
            break;
    }
}

void skiplist_add(skiplist_t* s, size_t pos, void* elem) {

    skiplistnode_t* node;
    void* elem_cpy;

    assert((void *)s != NULL);
    assert(elem != NULL);
    assert(pos <= s->length);
    
    elem_cpy = malloc(s->elem_size);
    memcpy(elem_cpy, elem, s->elem_size);
    assert(elem_cpy != NULL);

    node = new_node(elem_cpy, pick_height(s->rand));

    if (node->next_length - 1 > s->height) 
        s->height = node->next_length - 1;

    add_node(s, pos, node);

    ++s->length;
}

void skiplist_dispose(skiplist_t* s) {

    skiplistnode_t* node;
    skiplistnode_t* rm;

    assert((void *)s != NULL);

    node = s->sentinel->next[0];

    while (node != NULL) {

        rm = node;
        node = node->next[0];

        free(rm->data);
        free(rm->next);
        free(rm->length);
        free(rm);
    }

    free(s->sentinel->next);
    free(s->sentinel->length);
    free(s->sentinel);
}

void skiplist_get(skiplist_t* s, size_t pos, void* elem_out) {

    assert((void *) s != NULL);
    assert(pos < s->length);

    memcpy(elem_out, find(s, pos)->next[0]->data, s->elem_size);
}

void skiplist_init(skiplist_t* s, size_t elem_size, int (*random)(void)) {

    assert((void *)s != NULL);
    assert(elem_size > 0);

    s->length    = 0;
    s->height    = 0;
    s->elem_size = elem_size;
    s->rand      = random == NULL ? rand : random; /* default rand: stdlib */

    /* TODO: remove hard-coded limit */
    s->sentinel  = new_node(NULL, 32);
}

void skiplist_iterator(skiplist_t* s, iterator_t* it) {

    assert((void *)s != NULL);
    assert((void *)it != NULL);

    it->dispose = it_dispose;
    it->next    = it_next;
    it->elem    = it_elem;
    it->istruct = s->sentinel;
}

void skiplist_remove(skiplist_t* s, size_t pos, void* elem_out) {

    skiplistnode_t* node;
    skiplistnode_t* old_node;
    size_t r, j;

    assert((void *)s != NULL);
    assert(pos < s->length);

    node = s->sentinel;
    r    = s->height;
    j    = 0;

    while (1) {

        while (node->next[r] != NULL && j + node->length[r] < pos) {

            j += node->length[r];
            node = node->next[r];
        }

        --node->length[r];

        if (node->next[r] != NULL && j + node->length[r] + 1 == pos) {

            if (elem_out != NULL)
                memcpy(elem_out, node->next[r]->data, s->elem_size);

            old_node = node->next[r];
            
            node->length[r] += node->next[r]->length[r];
            node->next[r] = node->next[r]->next[r];

            if (r == 0) {

                free(old_node->data);
                free(old_node->next);
                free(old_node->length);
                free(old_node);
            }

            if (node == s->sentinel && node->next[r] == NULL)
                --s->height;
        }

        if (r-- == 0)
            break;
    }

    --s->length;
}

void skiplist_set(skiplist_t* s, size_t pos, void* elem, void* old_elem) {

    skiplistnode_t* node;

    assert((void *)s != NULL);
    assert(pos < s->length);
    assert(elem != NULL);

    node = find(s, pos)->next[0];

    if (old_elem != NULL)
        memcpy(old_elem, node->data, s->elem_size);

    memcpy(node->data, elem, s->elem_size);
}
