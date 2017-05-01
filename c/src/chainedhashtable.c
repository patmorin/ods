/*******************************************************************************
 * File         : chainedhashtable.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <string.h>
#include <assert.h>
#include <limits.h>
#include <stdlib.h>

#include <chainedhashtable.h>
#include <dllist.h>
#include <iterator.h>

static size_t hash(chainedhashtable_t* table, int elem_hashcode) {
    
    return (unsigned int)(table->z * elem_hashcode) >> (table->w - table->d);
}

static dllist_t* allocate_table(chainedhashtable_t* table) {
    
    dllist_t*   dllist_array;
    size_t      i;
    size_t      size = 1 << table->d;
    
    /* allocate space and initialize each dllist */
    dllist_array = malloc(sizeof(dllist_t) * size);
    assert((void *)dllist_array != NULL);
    
    for (i = 0; i < size; ++i)
        dllist_init(dllist_array + i, table->elem_size);
        
    return dllist_array;
}

static void resize(chainedhashtable_t* table) {
    
    dllist_t*   list;
    dllist_t*   old_array = table->array;
    size_t      old_array_size = 1 << table->d;
    iterator_t  it;
    size_t      i;
    
    /* determine the new size */
    table->d = 1;
    while ((1 << table->d) <= table->length)
        ++table->d;
    
    /* allocate a new array for this table */
    table->array = allocate_table(table);
    
    /* iterate over the old array and copy elements */
    for (i = 0; i < old_array_size; ++i) {
        
        list = old_array + i;
        
        if (list->length > 0) {
            
            dllist_iterator(list, &it, 0, list->length - 1);
            while (it.next(&it))
                chainedhashtable_add(table, it.elem(&it));
                
            it.dispose(&it);
        }
        
        dllist_dispose(list);
    }
    
    free(old_array);
}

int chainedhashtable_add(chainedhashtable_t* table, void* elem) {
    
    dllist_t* list;
    
    assert((void *)table != NULL);
    assert(elem != NULL);
    
    /* check if element exist, return false if so */
    if (chainedhashtable_find(table, elem))
        return 0;
        
    if (table->length + 1 > (1 << table->d))
        resize(table);
        
    /* find which list we are supposed to insert the element */
    list = table->array + hash(table, table->hashcode(elem));    
    
    /* add the element */
    dllist_add(list, list->length, elem);
    ++table->length;
    
    return 1;
}

void chainedhashtable_clear(chainedhashtable_t* table) {
    
    assert((void *)table != NULL);
    
    table->d        = 1;
    table->length   = 0;
    table->array    = allocate_table(table);
}

void chainedhashtable_dispose(chainedhashtable_t* table) {

    size_t  i;
    size_t  array_size;
    
    assert((void *)table != NULL);
    
    array_size = 1 << table->d;
    
    /* iterate over the array of lists and dispose each */
    for (i = 0; i < array_size; ++i)
        dllist_dispose(table->array + i);
        
    free(table->array);
}

int chainedhashtable_find(chainedhashtable_t* table, void* elem) {
    
    dllist_t*   list;
    iterator_t  it;
    
    assert((void *)table != NULL);
    assert(elem != NULL);
    
    list = table->array + hash(table, table->hashcode(elem));
    
    /* find the element in the list */
    if (list->length > 0) {
        
        dllist_iterator(list, &it, 0, list->length - 1);
        while (it.next(&it)) {
            
            if (!memcmp(elem, it.elem(&it), table->elem_size)){
                
                it.dispose(&it);
                return 1;
            }
        }
        
        it.dispose(&it);
    }
    
    return 0;
}

void chainedhashtable_init(chainedhashtable_t* table, size_t elem_size,
                           int (*hashcode)(void *), int (*random)(void)) {

    assert((void *)table != NULL);
    assert(hashcode != NULL);
    assert(elem_size > 0);
    
    table->elem_size    = elem_size;
    table->w            = sizeof(int) * CHAR_BIT;
    table->d            = 1;
    table->hashcode     = hashcode;
    table->length       = 0;
    
    /* generate a random odd integer z */
    table->z            = (random != NULL ? random() : rand()) | 1;

    table->array = allocate_table(table);
}

void chainedhashtable_remove(chainedhashtable_t* table, void* elem) {
    
    dllist_t*   list;
    iterator_t  it;
    size_t      i;
    
    assert((void *)table != NULL);
    assert(elem != NULL);
    
    /* find the list where the element should be */
    list = table->array + hash(table, table->hashcode(elem));
    
    /* look for the element in the list */
    if (list->length > 0) {
        
        i = 0;
        
        dllist_iterator(list, &it, 0, list->length - 1);
        while (it.next(&it)) {
            
            if (!memcmp(it.elem(&it), elem, table->elem_size)) {
                
                it.dispose(&it);
                dllist_remove(list, i, NULL);
                --table->length;
                return;
            }
            
            i++;
        }
    }
}
