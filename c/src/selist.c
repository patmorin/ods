/*******************************************************************************
 * File         : selist.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <assert.h>
#include <stdlib.h>

#include <iterator.h>
#include <arraydeque.h>
#include <selist.h>

struct location {

    senode_t* node;
    size_t local_pos;

};

struct selist_it {

    senode_t* node;
    size_t local_pos;
    
    size_t rem;
    int fwd;
    int started;

};

static int it_next(iterator_t* it) {

    struct selist_it* a = it->istruct;

    if (!a->started)
        return (a->started = 1);

    if (a->rem == 0)
        return 0;

    if (a->fwd) {

        if (a->local_pos == a->node->bdeque->length - 1) {

            a->local_pos = 0;
            a->node = a->node->next;
        }

        else {
            a->local_pos++;
        }
    }

    else {

        if (a->local_pos == 0) {

            a->node = a->node->prev;
            a->local_pos = a->node->bdeque->length - 1;
        }

        else {
            a->local_pos--;
        }
    }

    a->rem--;
    return 1;
}

static void* it_elem(iterator_t* it) {

    struct selist_it* a = it->istruct;

    size_t i = (a->node->bdeque->pos + a->local_pos) %
                    a->node->bdeque->alloc_length;

    return (char *)a->node->bdeque->array + (i * a->node->bdeque->elem_size);
}

static void it_dispose(iterator_t* it) {

    free(it->istruct);
}

static senode_t* add_before(selist_t* l, senode_t* node) {

    senode_t* new_node;

    /* allocate space for the node */
    new_node = malloc(sizeof(senode_t));
    assert((void *)new_node != NULL);

    /* allocate space for the bdeque */
    new_node->bdeque = malloc(sizeof(arraydeque_t));
    assert((void *)(new_node->bdeque) != NULL);
    
    arraydeque_init_bound(new_node->bdeque, l->elem_size, l->block_size + 1);

    new_node->prev = node->prev;
    new_node->next = node;
    
    new_node->next->prev = new_node;
    new_node->prev->next = new_node;

    return new_node;
}

static struct location find(selist_t* l, size_t pos) {

    senode_t* u;
    struct location loc;
    size_t i;

    if (pos < l->length / 2) {

        for (u = l->dummy->next; pos >= u->bdeque->length; u = u->next)
            pos -= u->bdeque->length;

        loc.node      = u;
        loc.local_pos = pos;
    }

    else {

        u = l->dummy;

        for (i = l->length; pos < i;) {
            u = u->prev;
            i -= u->bdeque->length;
        }

        loc.node      = u;
        loc.local_pos = pos - i;
    }

    return loc;
}

static void remove_node(senode_t* node) {

    node->prev->next = node->next;
    node->next->prev = node->prev;

    /* some cleanup... */
    arraydeque_dispose(node->bdeque);
    free(node->bdeque);
    free(node);
}

static void gather(selist_t* l, senode_t* u) {

    void* tmp;
    senode_t* w = u;
    size_t i;

    tmp = malloc(l->elem_size);
    assert(tmp != NULL);

    for (i = 0; i < l->block_size - 1; ++i, w = w->next) {

        while (w->bdeque->length < l->block_size) {

            arraydeque_remove(w->next->bdeque, 0, tmp);
            arraydeque_add(w->bdeque, w->bdeque->length, tmp);
        }
    }

    remove_node(w);
    
    free(tmp);
}

static void spread(selist_t* l, senode_t* u) {

    void* tmp;
    senode_t* w = u;
    size_t i;

    tmp = malloc(l->elem_size);
    assert(tmp != NULL);

    for (i = 0; i < l->block_size; ++i, w = w->next);

    w = add_before(l, w);

    for (;w != u; w = w->prev) {
        
        while (w->bdeque->length < l->block_size) {

            arraydeque_remove(w->prev->bdeque, w->prev->bdeque->length-1, tmp);
            arraydeque_add(w->bdeque, 0, tmp);
        }
    }

    free(tmp);
}

void selist_add(selist_t* l, size_t pos, void* elem) {

    struct location loc;
    senode_t* u;
    size_t r;
    void* tmp;

    assert((void *)l != NULL);
    assert(pos <= l->length);
    assert(elem != NULL);

    if (pos == l->length) {
        
        u = l->dummy->prev;
        if (u == l->dummy || u->bdeque->length == l->block_size + 1)
            u = add_before(l, l->dummy);

        arraydeque_add(u->bdeque, u->bdeque->length, elem);
        ++l->length;
        return;
    }

    loc  = find(l, pos);
    u    = loc.node;
    r    = 0;

    while (r < l->block_size && u != l->dummy &&
           u->bdeque->length == l->block_size + 1) {

        u = u->next;
        r++;
    }

    if (r == l->block_size) {
        
        spread(l, loc.node);
        u = loc.node;
    }
        
    if (u == l->dummy)
        u = add_before(l, u);

    /* need to allocate space for shifting */
    tmp = malloc(l->elem_size);
    assert(tmp != NULL);

    while (u != loc.node) {

        arraydeque_remove(u->prev->bdeque, u->prev->bdeque->length - 1, tmp);
        arraydeque_add(u->bdeque, 0, tmp);

        u = u->prev;
    }

    arraydeque_add(u->bdeque, loc.local_pos, elem);

    ++l->length;
    free(tmp);
}

void selist_dispose(selist_t* l) {

    senode_t* old_node;
    senode_t* node;

    assert((void *)l != NULL);

    for (node = l->dummy->next; node != l->dummy;) {

        arraydeque_dispose(node->bdeque);
        free(node->bdeque);

        old_node = node;
        node = node->next;

        free(old_node);
    }

    free(l->dummy);
}

void selist_get(selist_t* l, size_t pos, void* elem_out) {

    struct location loc;

    assert((void *)l != NULL);
    assert(pos < l->length);
    assert(elem_out != NULL);

    loc = find(l, pos);
    arraydeque_get(loc.node->bdeque, loc.local_pos, elem_out);
}

void selist_init(selist_t* l, size_t elem_size, size_t block_size) {

    assert((void *)l != NULL);
    assert(elem_size > 0);

    l->dummy = malloc(sizeof(senode_t));
    assert(l->dummy != NULL);

    l->dummy->bdeque = NULL;
    l->dummy->next   = l->dummy;
    l->dummy->prev   = l->dummy;

    l->elem_size  = elem_size;
    l->length     = 0;
    l->block_size = block_size;
}

void selist_iterator(selist_t* l, iterator_t* it, size_t start, size_t end) {

    struct selist_it* istruct;
    struct location loc;

    assert((void *)l != NULL);
    assert((void *)it != NULL);
    assert(start < l->length);
    assert(end < l->length);

    it->dispose = it_dispose;
    it->next    = it_next;
    it->elem    = it_elem;

    istruct = malloc(sizeof(struct selist_it));
    assert((void *)istruct != NULL);

    loc = find(l, start);

    istruct->node      = loc.node;
    istruct->local_pos = loc.local_pos;
    istruct->started   = 0;
    istruct->rem       = start <= end ? end - start : start - end;
    istruct->fwd       = start <= end ? 1 : 0;

    it->istruct = istruct;
}

void selist_remove(selist_t* l, size_t pos, void* elem_out) {

    void* tmp;
    struct location loc;
    senode_t* u;
    size_t r;

    assert((void *)l != NULL);
    assert(pos < l->length);

    loc = find(l, pos);

    if (elem_out != NULL)
        arraydeque_get(loc.node->bdeque, loc.local_pos, elem_out);

    u = loc.node;
    r = 0;

    while (r < l->block_size && u != l->dummy &&
           u->bdeque->length == l->block_size - 1) {

        u = u->next;
        ++r;
    }

    if (r == l->block_size)
        gather(l, loc.node);

    u = loc.node;

    arraydeque_remove(u->bdeque, loc.local_pos, NULL);

    tmp = malloc(l->elem_size);
    assert(tmp != NULL);

    while (u->bdeque->length < l->block_size - 1 && u->next != l->dummy) {

        arraydeque_remove(u->next->bdeque, 0, tmp);
        arraydeque_add(u->bdeque, u->bdeque->length, tmp);
        u = u->next;
    }

    if (u->bdeque->length == 0)
        remove_node(u);

    --l->length;
    free(tmp);
}

void selist_set(selist_t* l, size_t pos, void* elem, void* old_elem) {

    struct location loc;

    assert((void *)l != NULL);
    assert(pos < l->length);
    assert(elem != NULL);

    loc = find(l, pos);
    arraydeque_set(loc.node->bdeque, loc.local_pos, elem, old_elem);
}
