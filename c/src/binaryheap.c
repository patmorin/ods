/*******************************************************************************
 * File         : binaryheap.c
 * Author(s)    : Andrii Lastivka <andriilastivka@gmail.com>
 ******************************************************************************/

#include <stdlib.h>
#include <assert.h>
#include <string.h>

#include <binaryheap.h>

#define binaryheap_left(i) (2*i + 1)
#define binaryheap_right(i) (2*i + 2)
#define binaryheap_parent(i) (i-1)/2

static void resize(binaryheap_t *h) {

    size_t realloc_size = 1;
    
    if (h->length > 0)
        realloc_size = h->length * 2;

    h->array = realloc(h->array, realloc_size * h->elem_size);

    assert(h->array != NULL);

    h->alloc_length = realloc_size;
}

void binaryheap_clear(binaryheap_t* h) {

    assert((void *)h != NULL);

    h->length       = 0;
    h->alloc_length = 1;
    h->array        = realloc(h->array, h->elem_size);

    assert(h->array != NULL);
}

void binaryheap_add(binaryheap_t* h, void* elem) {	
	size_t i, p;
	void *temp;
	
	assert((void *)h != NULL);
    assert(elem != NULL);

    if (1 + h->length > h->alloc_length)
        resize(h);
	
	memcpy((char *)h->array + (h->length)*h->elem_size, elem, h->elem_size);
	
	/* bubble up */
	i = h->length;
	p = binaryheap_parent(i);
	temp = malloc(h->elem_size);
	while(i > 0 && h->cmp((char *)h->array + (i * h->elem_size), (char *)h->array + (p * h->elem_size)) < 0)
	{
		
		memcpy(temp, (char *)h->array + (i * h->elem_size), h->elem_size);
		memcpy((char *)h->array + (i * h->elem_size), (char *)h->array + (p * h->elem_size), h->elem_size);
		memcpy((char *)h->array + (p * h->elem_size), temp, h->elem_size);
		i = p;
		p = binaryheap_parent(i);
		
	}	
	free(temp);
	h->length++;
}

void binaryheap_dispose(binaryheap_t* h) {

    assert((void *)h != NULL);
    
    free(h->array);
}

extern void binaryheap_init(binaryheap_t* h,
                                  size_t elem_size,
                                  int(*comparator)(void *, void *)){

    assert((void *)h != NULL);
    assert(h->elem_size > 0);

    h->array = malloc(h->elem_size);

    assert(h->array != NULL);

    h->length       = 0;
    h->alloc_length = 1;
    h->elem_size    = elem_size;
	h->cmp = comparator;
}

void binaryheap_remove(binaryheap_t* h,
                                   void* elem_out)  {
	int j, i, l, r;
	void *temp;
	
	assert((void *)h != NULL);
    assert(h->length > 0);

    /* copy the removed data */
    if (elem_out != NULL)
        memcpy(elem_out, h->array, h->elem_size);

	memcpy(h->array, (char *)h->array + (h->length-1)*h->elem_size, h->elem_size);
	h->length--;
    /* resize if necessary */
    if (h->length * 3 < h->alloc_length)
        resize(h);
	
	/* trickle down */
	i = 0;
	temp = malloc(h->elem_size);
	while(i >= 0)
	{
		j = -1;
		r = binaryheap_right(i);
		l = binaryheap_left(i);
		if(r < h->length && h->cmp((char *)h->array + (r * h->elem_size), (char *)h->array + (i * h->elem_size)) < 0)
		{
			if(h->cmp((char *)h->array + (l * h->elem_size), (char *)h->array + (r * h->elem_size)) < 0)
				j = l;
			else 
				j = r;
		}
		else
		{
			if(l < h->length && h->cmp((char *)h->array + (l * h->elem_size), (char *)h->array + (i * h->elem_size)) < 0)
				j = l;
		}
		if(j >= 0)
		{
			memcpy(temp, (char *)h->array + (i * h->elem_size), h->elem_size);
			memcpy((char *)h->array + (i * h->elem_size), (char *)h->array + (j * h->elem_size), h->elem_size);
			memcpy((char *)h->array + (j * h->elem_size), temp, h->elem_size);
		}
		i = j;
	}
	
	free(temp);
}
