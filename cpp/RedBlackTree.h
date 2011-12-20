/*
 * RedBlackTree.h
 *
 * Created on: 2011-11-30
 *   Author: morin
 */

#ifndef REDBLACKTREE_H_
#define REDBLACKTREE_H_

#include "BinarySearchTree.h"

namespace ods {

template<class Node, class T> class RedBlackTree;


template<class Node, class T>
class RedBlackNode : public BSTNode<Node, T> {
protected:
	friend class RedBlackTree<Node, T>;
	char color;
};

template<class Node, class T>
class RedBlackTree : public BinarySearchTree<Node, T> {
protected:
	using BinaryTree<Node>::r;
	using BinaryTree<Node>::nil;
	using BinarySearchTree<Node,T>::n;
	static const int red = 0;
	static const int black = 1;
	void pushBlack(Node *u);
	void pullBlack(Node *u);
	void flipLeft(Node *u);
	void flipRight(Node *u);
	void swapColors(Node *u, Node *w);
	void addFixup(Node *u);
	void removeFixup(Node *u);
	Node *removeFixupCase1(Node *u);
	Node *removeFixupCase2(Node *u);
	Node *removeFixupCase3(Node *u);
	void verify();
	int verify(Node *u);
public:
	RedBlackTree();
	virtual ~RedBlackTree();
	virtual bool add(T x);
	virtual bool remove(T x);
};

template<class T>
class RedBlackNode1 : public RedBlackNode<RedBlackNode1<T>, T> { };

template<class T>
class RedBlackTree1 : public RedBlackTree<RedBlackNode1<T>, T> { };



template<class Node, class T>
void RedBlackTree<Node,T>::pushBlack(Node *u) {
	u->color--;
	u->left->color++;
	u->right->color++;
}



template<class Node, class T>
void RedBlackTree<Node,T>::pullBlack(Node *u) {
	u->color++;
	u->left->color--;
	u->right->color--;
}


template<class Node, class T>
void RedBlackTree<Node,T>::flipLeft(Node *u) {
	swapColors(u, u->right);
	rotateLeft(u);
}


template<class Node, class T>
void RedBlackTree<Node,T>::flipRight(Node *u) {
	swapColors(u, u->left);
	rotateRight(u);
}


template<class Node, class T>
void RedBlackTree<Node,T>::swapColors(Node *u, Node *w) {
	char tmp = u->color;
	u->color = w->color;
	w->color = tmp;
}



template<class Node, class T>
void RedBlackTree<Node,T>::addFixup(Node *u) {
	while (u->color == red) {
		if (u == r) { // u is the root - done
			u->color = black;
			return;
		}
		Node *w = u->parent;
		if (w->left->color == black) { // ensure left-leaning
			flipLeft(w);
			u = w;
			w = u->parent;
		}
		if (w->color == black)
			return; // no red-red edge = done
		Node *g = w->parent; // grandparent of u
		if (g->right->color == black) {
			flipRight(g);
			return;
		} else {
			pushBlack(g);
			u = g;
		}
	}
}



template<class Node, class T>
void RedBlackTree<Node,T>::removeFixup(Node *u) {
	while (u->color > black) {
		if (u == r) {
			u->color = black;
		} else if (u->parent->left->color == red) {
			u = removeFixupCase1(u);
		} else if (u == u->parent->left) {
			u = removeFixupCase2(u);
		} else {
			u = removeFixupCase3(u);
		}
	}
	if (u != r) { // restore left-leaning property, if necessary
		Node *w = u->parent;
		if (w->right->color == red && w->left->color == black) {
			flipLeft(w);
		}
	}
}



template<class Node, class T>
Node* RedBlackTree<Node,T>::removeFixupCase1(Node *u) {
	flipRight(u->parent);
	return u;
}



template<class Node, class T>
Node* RedBlackTree<Node,T>::removeFixupCase2(Node *u) {
	Node *w = u->parent;
	Node *v = w->right;
	pullBlack(w); // w->left
	flipLeft(w); // w is now red
	Node *q = w->right;
	if (q->color == red) { // q-w is red-red
		rotateLeft(w);
		flipRight(v);
		pushBlack(q);
		if (v->right->color == red)
			flipLeft(v);
		return q;
	} else {
		return v;
	}
}



template<class Node, class T>
Node* RedBlackTree<Node,T>::removeFixupCase3(Node *u) {
	Node *w = u->parent;
	Node *v = w->left;
	pullBlack(w);
	flipRight(w);            // w is now red
	Node *q = w->left;
	if (q->color == red) {    // q-w is red-red
		rotateRight(w);
		flipLeft(v);
		pushBlack(q);
		return q;
	} else {
		if (v->left->color == red) {
			pushBlack(v);   // both v's children are red
			return v;
		} else {            // ensure left-leaning
			flipLeft(v);
			return w;
		}
	}
}

template<class Node, class T>
void RedBlackTree<Node,T>::verify() {
	assert (size(r) == n);
	verify(r);
}



template<class Node, class T>
int RedBlackTree<Node,T>::verify(Node *u) {
	if (u == nil)
		return u->color;
	assert(u->color == red || u->color == black);
	if (u->color == red)
		assert(u->left->color == black && u->right->color == black);
	assert(u->right->color == black || u->left->color == red);
	int dl = verify(u->left);
	int dr = verify(u->right);
	if (dl != dr)
	return dl + u->color;
}


template<class Node, class T>
RedBlackTree<Node,T>::RedBlackTree() {
	nil = new Node;
	nil->color = black;
	r = nil;
}



template<class Node, class T>
RedBlackTree<Node,T>::~RedBlackTree() {
	delete nil;
}



template<class Node, class T>
bool RedBlackTree<Node,T>::add(T x) {
	Node *u = new Node();
	u->left = u->right = u->parent = nil;
	u->x = x;
	u->color = red;
	bool added = BinarySearchTree<Node,T>::add(u);
	if (added)
		addFixup(u);
	return added;
}



template<class Node, class T>
bool RedBlackTree<Node,T>::remove(T x) {
	Node *u = findLast(x);
	if (u == nil || compare(u->x, x) != 0)
		return false;
	Node *w = u->right;
	if (w == nil) {
		w = u;
		u = w->left;
	} else {
		while (w->left != nil)
			w = w->left;
		u->x = w->x;
		u = w->right;
	}
	splice(w);
	u->color += w->color;
	u->parent = w->parent;
	delete w;
	removeFixup(u);
	return true;
}

} /* namespace ods */
#endif /* REDBLACKTREE_H_ */
