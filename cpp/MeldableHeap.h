/*
 * MeldableHeap.h
 *
 *  Created on: 2011-11-30
 *      Author: morin
 */

#ifndef MELDABLEHEAP_H_
#define MELDABLEHEAP_H_

#include "BinaryTree.h"

namespace ods {

template<class Node, class T> class MeldableHeap;

template<class Node, class T>
class MHeapNode : public BTNode<Node>  {
public:
	T x;
};


template<class Node, class T>
class MeldableHeap: public BinaryTree<Node> {
protected:
	using BinaryTree<Node>::r;
	using BinaryTree<Node>::nil;
	int n;
	Node *merge(Node *h1, Node *h2);
public:
	MeldableHeap();
	virtual ~MeldableHeap();
	bool add(T x);
	T findMin();
	T remove();
	void remove(Node *u);
	int size() {
		return n;
	}
};

template<class T>
class MHeapNode1 : public MHeapNode<MHeapNode1<T>, T> { };

template<class T>
class MeldableHeap1 : public MeldableHeap<MHeapNode1<T>, T> { };


template<class Node, class T>
Node* MeldableHeap<Node,T>::merge(Node *h1, Node *h2) {
	if (h1 == nil) return h2;
	if (h2 == nil) return h1;
	if (compare(h1->x, h2->x) > 0) return merge(h2, h1);
        // now we know h1->x <= h2->x
	if (rand() % 2) {
		h1->left = merge(h1->left, h2);
		if (h1->left != nil) h1->left->parent = h1;
	} else {
		h1->right = merge(h1->right, h2);
		if (h1->right != nil) h1->right->parent = h1;
	}
	return h1;
}

template<class Node, class T>
MeldableHeap<Node,T>::MeldableHeap() {
	n = 0;
}

template<class Node, class T>
MeldableHeap<Node,T>::~MeldableHeap() {
	// nothing to do
}

template<class Node, class T>
bool MeldableHeap<Node,T>::add(T x) {
	Node *u = new Node();
	u->left = u->right = u->parent = nil;
	u->x = x;
	r = merge(u, r);
	r->parent = nil;
	n++;
	return true;
}



template<class Node, class T>
T MeldableHeap<Node,T>::findMin() {
	return r->x;
}



template<class Node, class T>
T MeldableHeap<Node,T>::remove() {
	T x = r->x;
	Node *tmp = r;
	r = merge(r->left, r->right);
	delete tmp;
	if (r != nil) r->parent = nil;
	n--;
	return x;
}



template<class Node, class T>
void MeldableHeap<Node,T>::remove(Node *u) {
	if (u == r) {
		remove();
	} else {
		if (u == u->parent->left) {
			u->parent->left = nil;
		} else {
			u->parent->right = nil;
		}
		u->parent = nil;
		r = merge(r, u->left);
		r = merge(r, u->right);
		r->parent = nil;
		n--;
	}
}


} /* namespace ods */
#endif /* MELDABLEHEAP_H_ */
