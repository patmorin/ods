/*******************************************************************************
 * File         : binarysearchtree.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_BINARYSEARCHTREE_H_
#define ODS_BINARYSEARCHTREE_H_

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

#endif
