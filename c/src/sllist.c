/*******************************************************************************
 * File         : sllist.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <string.h>
#include <stdlib.h>
#include <assert.h>

#include <iterator.h>
#include <sllist.h>

struct sllist_it {

    slnode_t* curr_node;
    
    size_t rem;
    int started;

};

static int it_next(iterator_t* it) {

    struct sllist_it* a = it->istruct;

    if (!a->started)
        return (a->started = 1);

    if (a->rem == 0)
        return 0;

    a->rem--;
    a->curr_node = a->curr_node->next;
    
    return 1;
}

static void* it_elem(iterator_t* it) {

    struct sllist_it* a = it->istruct;

    return a->curr_node->data;
}

static void it_dispose(iterator_t* it) {

    free(it->istruct);
}

void sllist_dispose(sllist_t* l) {

    slnode_t* node     = NULL;
    slnode_t* old_node = NULL;

    assert((void *)l != NULL);

    for (node = l->head; node != NULL;) {

        free(node->data);

        old_node = node;
        node = node->next;

        free(old_node);
    }
}

void sllist_enqueue(sllist_t* l, void* elem) {

    slnode_t* node;

    assert((void *)l != NULL);
    assert(elem != NULL);

    node = malloc(sizeof(slnode_t));
    assert((void *)node != NULL);

    node->data = malloc(l->elem_size);
    assert(node->data != NULL);

    memcpy(node->data, elem, l->elem_size);

    node->next = NULL;

    if (l->length == 0)
        l->head = node;
    else
        l->tail->next = node;

    l->tail = node;

    ++l->length;
}

void sllist_init(sllist_t* l, size_t elem_size) {

    assert((void *)l != NULL);

    l->length    = 0;
    l->elem_size = elem_size;
    l->head      = NULL;
    l->tail      = NULL;
}

void sllist_iterator(sllist_t* l, iterator_t* it, size_t start, size_t end) {

    struct sllist_it* istruct;
    slnode_t* node;
    size_t i;

    assert((void *)l != NULL);
    assert((void *)it != NULL);
    assert(start < l->length);
    assert(end < l->length);
    assert(start <= end);

    it->dispose = it_dispose;
    it->next    = it_next;
    it->elem    = it_elem;

    istruct = malloc(sizeof(struct sllist_it));
    assert((void *)istruct != NULL);

    istruct->rem     = end - start;
    istruct->started = 0;

    /* find the starting node */
    for (i = 0, node = l->head; i < start; node = node->next, i++);

    istruct->curr_node = node;

    it->istruct = istruct;
}

void sllist_pop(sllist_t* l, void* elem_out) {

    slnode_t* node;

    assert((void *)l != NULL);
    assert(l->length > 0);

    node = l->head;

    if (elem_out != NULL)
        memcpy(elem_out, node->data, l->elem_size);

    l->head = node->next;

    if (l->length == 0)
        l->tail = NULL;

    --l->length;

    free(node->data);
    free(node);
}

void sllist_push(sllist_t* l, void* elem) {

    slnode_t* node;

    assert((void *)l != NULL);
    assert(elem != NULL);

    /* make new node */
    node = malloc(sizeof(slnode_t));
    assert((void *)node != NULL);

    /* make space for data */
    node->data = malloc(l->elem_size);
    assert(node->data != NULL);

    memcpy(node->data, elem, l->elem_size);

    node->next = l->head;
    l->head = node;

    if (l->length == 0)
        l->tail = node;

    ++l->length;
}
