/*
 * BinaryTrie.h
 *
 *  Created on: 2012-01-26
 *      Author: morin
 */

#ifndef BINARYTRIE_H_
#define BINARYTRIE_H_

#include<cstdlib>

namespace ods {

template<class N, class T>
class BinaryTrieNode {
public:
	T x;
	N *jump;
	N *parent;
	union {
		struct {
			N *left;
			N *right;
		};
		struct {
			N *prev;
			N *next;
		};
		N* child[2];
	};
	BinaryTrieNode() {
		left = right = parent = NULL;
	}
};

template<class Node, class T>
class BinaryTrie {
protected:
	T null;
	enum { left, right };
	enum { prev, next };
	Node r;
	Node dummy;
	int n;
	static const int w = 32;  // FIXME: portability
	void clear(Node *u, int d);

public:
	BinaryTrie();
	virtual ~BinaryTrie();
	bool add(T x);
	bool remove(T x);
	T find(T x);
	int size() { return n; }
	void clear();
};



template<class Node, class T>
BinaryTrie<Node,T>::BinaryTrie() {
	// FIXME: we have to initialize to something reasonable, i.e., null = (T)NULL;
	dummy.next = &dummy;
	dummy.prev = &dummy;
	r.jump = &dummy;
	n = 0;
}

template<class Node, class T>
BinaryTrie<Node,T>::~BinaryTrie() {
	clear();
}

template<class Node, class T>
void BinaryTrie<Node,T>::clear() {
	clear(&r, 0);
	dummy.next = &dummy;
	dummy.prev = &dummy;
	r.jump = &dummy;
	r.left = NULL;
	r.right = NULL;
	n = 0;
	// TODO: recursive cleanup
}

template<class Node, class T>
void BinaryTrie<Node,T>::clear(Node *u, int d) {
	if (u == NULL) return;
	if (d < w) {
		clear(u->left, d+1);
		clear(u->right, d+1);
	}
	if (d > 0)
		delete u;
}

template<class Node, class T>
bool BinaryTrie<Node,T>::add(T x) {
	int i, c = 0;
	unsigned ix = intValue(x);
	Node *u = &r;
	// 1 - search for ix until falling out of the trie
	for (i = 0; i < w; i++) {
		c = (ix >> (w-i-1)) & 1;
		if (u->child[c] == NULL) break;
		u = u->child[c];
	}
	if (i == w) return false; // already contains x - abort
	Node *pred = (c == right) ? u->jump : u->jump->left;
	u->jump = NULL;  // u will have two children shortly
	// 2 - add path to ix
	for (; i < w; i++) {
		c = (ix >> (w-i-1)) & 1;
		u->child[c] = new Node();
		u->child[c]->parent = u;
		u = u->child[c];
	}
	u->x = x;
	// 3 - add u to linked list
	u->prev = pred;
	u->next = pred->next;;
	u->prev->next = u;
	u->next->prev = u;
	// 4 - walk back up, updating jump pointers
	Node *v = u->parent;
	while (v != NULL) {
		if ((v->left == NULL
				&& (v->jump == NULL || intValue(v->jump->x) > ix))
		|| (v->right == NULL
				&& (v->jump == NULL || intValue(v->jump->x) < ix)))
			v->jump = u;
		v = v->parent;
	}
	n++;
	return true;
}

template<class Node, class T>
T BinaryTrie<Node,T>::find(T x) {
	int i, c = 0;
	unsigned ix = intValue(x);
	Node *u = &r;
	for (i = 0; i < w; i++) {
		c = (ix >> (w-i-1)) & 1;
		if (u->child[c] == NULL) break;
		u = u->child[c];
	}
	if (i == w) return u->x;  // found it
	u = (c == 0) ? u->jump : u->jump->next;
	return u == &dummy ? null : u->x;
}


template<class Node, class T>
bool BinaryTrie<Node,T>::remove(T x) {
	// 1 - find leaf, u, containing x
	int i = 0, c;
	unsigned ix = intValue(x);
	Node *u = &r;
	for (i = 0; i < w; i++) {
		c = (ix >> (w-i-1)) & 1;
		if (u->child[c] == NULL) return false;
		u = u->child[c];
	}
	// 2 - remove u from linked list
	u->prev->next = u->next;
	u->next->prev = u->prev;
	Node *v = u;
	// 3 - delete nodes on path to u
	for (i = w-1; i >= 0; i--) {
		c = (ix >> (w-i-1)) & 1;
		v = v->parent;
		delete v->child[c];
		v->child[c] = NULL;
		if (v->child[1-c] != NULL) break;
	}
	// 4 - update jump pointers
	v->jump = u;
	for (; i >= 0; i--) {
		c = (ix >> (w-i-1)) & 1;
		if (v->jump == u)
			v->jump = u->child[1-c];
		v = v->parent;
	}
	n--;
	return true;
}


template<class T>
class BinaryTrieNode1 : public BinaryTrieNode<BinaryTrieNode1<T>, T> { };

template<class T>
class BinaryTrie1 : public BinaryTrie<BinaryTrieNode1<T>, T> { };



} /* namespace ods */
#endif /* BINARYTRIE_H_ */
