/*******************************************************************************
 * File         : binarysearchtree.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_BINARYSEARCHTREE_H_
#define ODS_BINARYSEARCHTREE_H_

#include <stdlib.h>

typedef struct btnode_t {
    
    void* data;
    
    struct btnode_t* parent;
    struct btnode_t* left;
    struct btnode_t* right;
    
} btnode_t;

typedef struct  {
    
    btnode_t* root;
    
    size_t elem_size;
    size_t length;
    
    int (*cmp)(void *, void *);
    
} binarysearchtree_t;

/* FUNCTION
 *      binarysearchtree_add
 *
 * DESCRIPTION
 *      Adds an element to the tree.
 *
 * PARAMETERS
 *      tree        A valid pointer to an initialized binarysearchtree_t struct.
 *      elem        The element that will be copied into the tree.
 */
extern void binarysearchtree_add(binarysearchtree_t* tree,
                                 void* elem);

/* FUNCTION
 *      binarysearchtree_dispose
 *
 * DESCRIPTION
 *      Frees allocated memory. Once disposed, a binarysearchtree_t structure
 *      should be re-initialized before use.
 *
 * PARAMETERS
 *      tree        A valid pointer to an initialized binarysearchtree_t struct.
 */
void binarysearchtree_dispose(binarysearchtree_t* tree);

/* FUNCTION
 *      binarysearchtree_find
 *
 * DESCRIPTION
 *      Finds an element in the tree.
 *
 * PARAMETERS
 *      tree        A valid pointer to an initialized binarysearchtree_t struct.
 *      search      The element that is being searched.
 *
 * RETURN VALUES
 *      Returns a non-zero value if the element that was being searched is
 *      found in the tree, zero otherwise.
 */
extern int binarysearchtree_find(binarysearchtree_t* tree,
                                 void* search);

/* FUNCTION
 *      binarysearchtree_init
 *
 * DESCRIPTION
 *      Initializes the binarysearchtree_t struct.
 *
 * PARAMETERS
 *      tree          A valid pointer to a binarysearchtree_t struct.
 *      elem_size     Size of the type that will be inserted in the tree.
 *      comparator    Pointer to a comparator function that takes two void*
 *                    parameters and returns an int. The function must return
 *                    less than zero if the first parameter comes before the
 *                    second parameter and greater than zero if vice versa.
 *                    The function must return zero if arguments are equal.
 */
extern void binarysearchtree_init(binarysearchtree_t* tree,
                                  size_t elem_size,
                                  int(*comparator)(void *, void *));

/* FUNCTION
 *      binarysearchtree_remove
 *
 * DESCRIPTION
 *      Removes an element from the tree.
 *
 * PARAMETERS
 *      tree        A valid pointer to an initialized binarysearchtree_t struct.
 *      search      The element that will be removed from the tree.
 *
 * RETURN VALUES
 *      Returns a non-zero value if the element was found and removed; returns
 *      zero if the element was not found.
 */
extern int binarysearchtree_remove(binarysearchtree_t* tree,
                                   void* elem);

#endif
