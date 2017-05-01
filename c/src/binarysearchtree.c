/*******************************************************************************
 * File         : binarysearchtree.c
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include <binarysearchtree.h>

static btnode_t* find_node(binarysearchtree_t* tree, void* data) {
    
    btnode_t* curr_node = tree->root;
    btnode_t* prev_node = NULL;
    int cmp_result;
    
    while (curr_node != NULL) {
        
        prev_node = curr_node;
        
        cmp_result = tree->cmp(data, curr_node->data);
        
        if (cmp_result < 0)
            curr_node = curr_node->left;
        else if (cmp_result > 0)
            curr_node = curr_node->right;
        else
            return curr_node;
    }
    
    return prev_node;
}

static btnode_t* make_node(binarysearchtree_t* tree, btnode_t* p, btnode_t* l,
                           btnode_t* r, void* data) {
    
    btnode_t* new_node = malloc(sizeof(btnode_t));
    assert((void *)new_node != NULL);
    
    new_node->parent = p;
    new_node->left   = l;
    new_node->right  = r;
    new_node->data   = malloc(tree->elem_size);
    
    assert(new_node->data != NULL);
    
    /* copy the data in */
    memcpy(new_node->data, data, tree->elem_size);
    
    return new_node;
}

static void remove_node(binarysearchtree_t* tree, btnode_t* node) {
  
    btnode_t* s = NULL;
    btnode_t* p = NULL;


    if (node->left != NULL)
        s = node->left;
    else
        s = node->right;

    if (node == tree->root) {

        tree->root = s;
        p = NULL;

    } else {

      p = node->parent;

      if (p->left == node)
          p->left = s;
      else
          p->right = s;

    }

    if (s != NULL)
        s->parent = p;

    free(node->data);
    free(node);
}

static void recursive_destroy_node(btnode_t* node) {

  if (node == NULL)
    return;

  recursive_destroy_node(node->left);
  recursive_destroy_node(node->right);

  free(node->data);
  free(node);
}

void binarysearchtree_add(binarysearchtree_t* tree, void* elem) {
    
    btnode_t* ins_node;
    int cmp_result;
    
    assert((void *)tree != NULL);
    assert(elem != NULL);
    
    /* find out where we place the new data */
    ins_node = find_node(tree, elem);
    
    if (ins_node == NULL) {
        
        /* tree is empty, insert to root */
        tree->root = make_node(tree, NULL, NULL, NULL, elem);
        
    } else {
        
        cmp_result = tree->cmp(elem, ins_node->data);
        
        if (cmp_result < 0)
            ins_node->left = make_node(tree, ins_node, NULL, NULL, elem);
        else if (cmp_result > 0)
            ins_node->right = make_node(tree, ins_node, NULL, NULL, elem);
        else /* already in the tree */
            return;
        
    }
    
    ++tree->length;
}

void binarysearchtree_dispose(binarysearchtree_t* tree) {

  assert((void *)tree != NULL);

  recursive_destroy_node(tree->root);
}

int binarysearchtree_find(binarysearchtree_t* tree, void* search) {
    
    btnode_t* node;
    int cmp_result;
    
    assert((void *)tree != NULL);
    assert(search != NULL);
    
    node = tree->root;
    
    while (node != NULL) {
        
        cmp_result = tree->cmp(search, node->data);
        
        if (cmp_result < 0)
            node = node->left;
        else if (cmp_result > 0)
            node = node->right;
        else
            return 1;
    }
    
    return 0;
}

void binarysearchtree_init(binarysearchtree_t* tree, size_t elem_size,
                           int (*comparator)(void *, void *)) {

    assert((void *)tree != NULL);
    assert(comparator != NULL);
    assert(elem_size > 0);
    
    tree->elem_size = elem_size;
    tree->length    = 0;
    tree->cmp       = comparator;
    tree->root      = NULL;
}

int binarysearchtree_remove(binarysearchtree_t* tree, void* elem) {
  
    btnode_t* node = NULL;
    btnode_t* u = NULL;

    assert((void *)tree != NULL);
    assert(elem != NULL);

    node = find_node(tree, elem);

    if (node == NULL || tree->cmp(elem, node->data) != 0)
        return 0;

    if (node->left == NULL || node->right == NULL) {

        remove_node(tree, node);

    } else {

        u = node->right;
        while (u->left != NULL)
          u = u->left;

        memcpy(node->data, u->data, tree->elem_size);

        remove_node(tree, u);
    }

    --tree->length;
}
