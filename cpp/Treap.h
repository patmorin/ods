/*
 * Treap.h
 *
 *  Created on: 2011-11-28
 *      Author: morin
 */

#ifndef TREAP_H_
#define TREAP_H_
#include "BinarySearchTree.h"
namespace ods {

template<class Node, class T> class Treap;

template<class Node, class T>
class TreapNode : public BSTNode<Node, T> {
protected:
	friend class Treap<Node,T>;
	int p;
};

template<class Node, class T>
class Treap : public BinarySearchTree<Node, T> {
protected:
	using BinaryTree<Node>::r;
	using BinaryTree<Node>::nil;
	void bubbleUp(Node *u);
	void trickleDown(Node *u);
public:
	Treap();
	virtual ~Treap();
	virtual bool add(T x);
	virtual bool remove(T x);
};

template<class T>
class TreapNode1 : public TreapNode<TreapNode1<T>, T> { };

template<class T>
class Treap1 : public Treap<TreapNode1<T>, T> { };

template<class Node, class T>
Treap<Node, T>::Treap() {
	// nothing to do
}

template<class Node, class T>
Treap<Node, T>::~Treap() {
	// nothing to do
}

template<class Node, class T>
void Treap<Node, T>::bubbleUp(Node *u) {
	while (u->parent != nil && u->parent->p > u->p) {
		if (u->parent->right == u) {
			rotateLeft(u->parent);
		} else {
			rotateRight(u->parent);
		}
	}
	if (u->parent == nil) {
		r = u;
	}
}

template<class Node, class T>
bool Treap<Node, T>::add(T x) {
	Node *u = new Node;
	u->x = x;
	u->p = rand();
	if (BinarySearchTree<Node,T>::add(u)) {
		bubbleUp(u);
		return true;
	}
	return false;
}

template<class Node, class T>
bool Treap<Node, T>::remove(T x) {
	Node *u = findLast(x);
	if (u != nil && compare(u->x, x) == 0) {
		trickleDown(u);
		splice(u);
		delete u;
		return true;
	}
	return false;
}


template<class Node, class T>
void Treap<Node, T>::trickleDown(Node *u) {
	while (u->left != nil || u->right != nil) {
		if (u->left == nil) {
			rotateLeft(u);
		} else if (u->right == nil) {
			rotateRight(u);
		} else if (u->left->p < u->right->p) {
			rotateRight(u);
		} else {
			rotateLeft(u);
		}
		if (r == u) {
			r = u->parent;
		}
	}
}


} /* namespace ods */
#endif /* TREAP_H_ */
