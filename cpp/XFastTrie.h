/*
 * XFastTrie.h
 *
 *  Created on: 2012-01-26
 *      Author: morin
 * WARNING: This class can not store the unsigned integers UINT_MAX-1 or UINT_MAX-2
 */

#ifndef XFASTTRIE_H_
#define XFASTTRIE_H_

#include <climits>

#include "LinearHashTable.h"
#include "BinaryTrie.h"

namespace ods {

template<class Node, class T>
class XFastTrieNode : public BinaryTrieNode<Node,T> {
public:
};


template<class Node>
class XPair {
public:
	unsigned prefix;
	Node *u;
	XPair() { };
	XPair(int prefix0) { prefix = prefix0; u = NULL; }
	XPair(int prefix0, Node *u0) { prefix = prefix0; u = u0; }
	bool operator==(XPair &p) { return prefix == p.prefix; }
	bool operator!=(XPair &p) { return prefix != p.prefix; }
};

template<class Node>
int hashCode(XPair<Node> &p) {
	return p.prefix;
}

template<class Node, class T>
class XFastTrie : public BinaryTrie<Node,T> {
protected:
	T nullt;
	using BinaryTrie<Node,T>::w;
	using BinaryTrie<Node,T>::n;
	using BinaryTrie<Node,T>::r;
	using BinaryTrie<Node,T>::right;
	using BinaryTrie<Node,T>::left;
	using BinaryTrie<Node,T>::dummy;
	XPair<Node> null;
	LinearHashTable<XPair<Node> > t[w+1];
public:
	XFastTrie();
	virtual ~XFastTrie();
	T find(T x);
	bool add(T x);
	bool remove(T x);
	void clear();
	Node* findNode(unsigned ix); // FIXME: should be protected
};



template<class Node, class T>
XFastTrie<Node,T>::XFastTrie() : BinaryTrie<Node,T>(), null(UINT_MAX-1, NULL) {
	// FIXME: make this work --- nullt = (T)NULL;
	XPair<Node> del(UINT_MAX-2);
	for (int i = 0; i < w+1; i++) {
		t[i].setNull(null);
		t[i].setDel(del);
	}
}

template<class Node, class T>
XFastTrie<Node,T>::~XFastTrie() {
	clear();
}

template<class Node, class T>
void XFastTrie<Node,T>::clear() {
	BinaryTrie<Node,T>::clear();
	for (int i = 0; i <=w; i++)
		t[i].clear();
}

template<class Node, class T>
T XFastTrie<Node,T>::find(T x) {
	int l = 0, h = w+1;
	unsigned ix = intValue(x);
	Node *v, *u = &r;
	while (h-l > 1) {
		int i = (l+h)/2;
		XPair<Node> p(ix >> (w-i));
		if ((v = t[i].find(p).u) == NULL) {
			h = i;
		} else {
			u = v;
			l = i;
		}
	}
	if (l == w) return u->x;
	Node *pred = (((ix >> (w-l-1)) & 1) == 1)
	              ? u->jump : u->jump->prev;
	return (pred->next == &dummy) ? nullt : pred->next->x;
}

template<class Node, class T>
Node* XFastTrie<Node,T>::findNode(unsigned ix) {
	int l = 0, h = w+1;
	Node *v, *u = &r;
	while (h-l > 1) {
		int i = (l+h)/2;
		XPair<Node> p(ix >> (w-i));
		if ((v = t[i].find(p).u) == NULL) {
			h = i;
		} else {
			u = v;
			l = i;
		}
	}
	if (l == w) return u;
	Node *pred = (((ix >> (w-l-1)) & 1) == 1) ? u->jump : u->jump->prev;
	return (pred->next == &dummy) ? NULL : pred->next;
}

template<class Node, class T>
bool XFastTrie<Node,T>::add(T x) {
	int i, c = 0;
	unsigned ix = intValue(x);
	Node *u = &r;
	// 1 - search for ix until falling out of the trie
	for (i = 0; i < w; i++) {
		c = (ix >> (w-i-1)) & 1;
		if (u->child[c] == NULL) break;
		u = u->child[c];
	}
	if (i == w) return false; // trie already contains x - abort
	Node *pred = (c == right) ? u->jump : u->jump->left; // save for step 3
	u->jump = NULL;  // u will have two children shortly
	// 2 - add path to ix
	for (; i < w; i++) {
		c = (ix >> (w-i-1)) & 1;
		u->child[c] = new Node();
		t[i+1].add(XPair<Node>(ix >> (w-i-1), u->child[c]));
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
bool XFastTrie<Node,T>::remove(T x) {
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
		t[i].remove(XPair<Node>(w-i));
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
class XFastTrieNode1 : public XFastTrieNode<XFastTrieNode1<T>, T> { };

template<class T>
class XFastTrie1 : public XFastTrie<XFastTrieNode1<T>, T> { };






} /* namespace ods */
#endif /* XFASTTRIE_H_ */
