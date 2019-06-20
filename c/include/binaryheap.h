/*******************************************************************************
 * File         : binaryheap.h
 * Author(s)    : Andrii Lastivka <andriilastivka@gmail.com>
 ******************************************************************************/

#ifndef ODS_BINARYHEAP_H_
#define ODS_BINARYHEAP_H_

#include <stdlib.h>

typedef struct  {
    
    size_t alloc_length;
    size_t length;
    size_t elem_size;
    void* array;
    
    int (*cmp)(void *, void *);
    
} binaryheap_t;

/* FUNCTION
 *      binaryheap_add
 *
 * DESCRIPTION
 *      Adds an element to the h.
 *
 * PARAMETERS
 *      h           A valid pointer to an initialized binaryheap_t struct.
 *      elem        The element that will be copied into the h.
 */
extern void binaryheap_add(binaryheap_t* h,
                                 void* elem);

/* FUNCTION
 *      binaryheap_dispose
 *
 * DESCRIPTION
 *      Frees allocated memory. Once disposed, a binaryheap_t structure
 *      should be re-initialized before use.
 *
 * PARAMETERS
 *      h           A valid pointer to an initialized binaryheap_t struct.
 */
void binaryheap_dispose(binaryheap_t* h);

/* FUNCTION
 *      binaryheap_init
 *
 * DESCRIPTION
 *      Initializes the binaryheap_t struct.
 *
 * PARAMETERS
 *      h     		  A valid pointer to a binaryheap_t struct.
 *      elem_size     Size of the type that will be inserted in the h.
 *      comparator    Pointer to a comparator function that takes two void*
 *                    parameters and returns an int. The function must return
 *                    less than zero if the first parameter comes before the
 *                    second parameter and greater than zero if vice versa.
 *                    The function must return zero if arguments are equal.
 */
extern void binaryheap_init(binaryheap_t* h,
                                  size_t elem_size,
                                  int(*comparator)(void *, void *));

/* FUNCTION
 *      binaryheap_remove
 *
 * DESCRIPTION
 *      Removes an element from the h.
 *
 * PARAMETERS
 *      h        A valid pointer to an initialized binaryheap_t struct.
 *      elem_out    If not a null pointer, the removed element will be copied
 *                  into the memory pointed to by this pointer.
 */
extern void binaryheap_remove(binaryheap_t* h,
                                   void* elem_out);

#endif
