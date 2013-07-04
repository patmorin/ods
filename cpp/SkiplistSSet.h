/*
 * SkiplistSSet.h
 *
 *  Created on: 2011-11-28
 *      Author: morin
 */

#ifndef SKIPLISTSSET_H_
#define SKIPLISTSSET_H_
#include <cstdlib>
#include <cstring>

#include "utils.h"

namespace ods {

template<class T>
class SkiplistSSet {
protected:
	T null;
	struct Node {
		T x;
		int height;     // length of next
		Node *next[];
	};
	Node *sentinel;
	int h;
	int n;
	Node** stack;

	Node *newNode(T x, int h);
	void deleteNode(Node *u);
	Node* findPredNode(T x);

public:
	SkiplistSSet();

	virtual ~SkiplistSSet();

	T find(T x);
	bool remove(T x);
	bool add(T x);
	int pickHeight();
	void clear();
	int size() { return n;	}
};

template<class T>
typename SkiplistSSet<T>::Node* SkiplistSSet<T>::newNode(T x, int h) {
	Node *u = (Node*)malloc(sizeof(Node)+(h+1)*sizeof(Node*));
	u->x = x;
	u->height = h;
	return u;
}

template<class T>
void SkiplistSSet<T>::deleteNode(Node *u) {
	free(u);
}

template<class T>
typename SkiplistSSet<T>::Node* SkiplistSSet<T>::findPredNode(T x) {
	Node *u = sentinel;
	int r = h;
	while (r >= 0) {
		while (u->next[r] != NULL 
               && compare(u->next[r]->x, x) < 0)
			u = u->next[r]; // go right in list r
		r--; // go down into list r-1
	}
	return u;
}

template<class T>
SkiplistSSet<T>::SkiplistSSet() {
	null = (T)NULL;
	n = 0;
	sentinel = newNode(null, sizeof(int)*8);
	memset(sentinel->next, '\0', sizeof(Node*)*sentinel->height);
	stack = (Node**)new Node*[sentinel->height];
	h = 0;
}

template<class T>
SkiplistSSet<T>::~SkiplistSSet() {
	clear();
	deleteNode(sentinel);
	delete stack;
}

template<class T>
T SkiplistSSet<T>::find(T x) {
	Node *u = findPredNode(x);
	return u->next[0] == NULL ? null : u->next[0]->x;
}

template<class T>
bool SkiplistSSet<T>::remove(T x) {
	bool removed = false;
	Node *u = sentinel, *del;
	int r = h;
	int comp = 0;
	while (r >= 0) {
		while (u->next[r] != NULL 
               && (comp = compare(u->next[r]->x, x)) < 0) {
			u = u->next[r];
		}
		if (u->next[r] != NULL && comp == 0) {
			removed = true;
			del = u->next[r];
			u->next[r] = u->next[r]->next[r];
			if (u == sentinel && u->next[r] == NULL)
				h--; // skiplist height has gone down
		}
		r--;
	}
	if (removed) {
		delete del;
		n--;
	}
	return removed;
}

template<class T>
bool SkiplistSSet<T>::add(T x) {
	Node *u = sentinel;
	int r = h;
	int comp = 0;
	while (r >= 0) {
		while (u->next[r] != NULL 
               && (comp = compare(u->next[r]->x, x)) < 0)
			u = u->next[r];
		if (u->next[r] != NULL && comp == 0)
			return false;
		stack[r--] = u;        // going down, store u
	}
	Node *w = newNode(x, pickHeight());
	while (h < w->height)
		stack[++h] = sentinel; // height increased
	for (int i = 0; i < w->height; i++) {
		w->next[i] = stack[i]->next[i];
		stack[i]->next[i] = w;
	}
	n++;
	return true;
}

template<class T>
int SkiplistSSet<T>::pickHeight() {
	int z = rand();
	int k = 0;
	int m = 1;
	while ((z & m) != 0) {
		k++;
		m <<= 1;
	}
	return k;
}

template<class T>
void SkiplistSSet<T>::clear() {
	Node *u = sentinel->next[0];
	while (u != NULL) {
		Node *n = u->next[0];
		deleteNode(u);
		u = n;
	}
	memset(sentinel->next, '\0', sizeof(Node*)*h);
	h = 0;
	n = 0;
}



} /* namespace ods */

#endif /* SKIPLISTSSET_H_ */
