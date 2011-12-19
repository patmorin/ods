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
	struct Node {
		T x;
		int height;     // length of next
		Node *next[];
	};
	Node *sentinel;
	int h;
	int n;
	Node** stack;

	Node *newNode(T x, int h) {
		Node *u = (Node*)malloc(sizeof(Node)+(h+1)*sizeof(Node*));
		u->x = x;
		u->height = h;
		return u;
	}

	void deleteNode(Node *u) {
		free(u);
	}
public:
	SkiplistSSet();

	virtual ~SkiplistSSet();

	Node* findPredNode(T x) {
		Node *u = sentinel;
		int r = h;
		while (r >= 0) {
			while (  u->next[r] != NULL && compare(u->next[r]->x, x) < 0)
				u = u->next[r]; // go right in list r
			r--; // go down into list r-1
		}
		return u;
	}

	 T find(T x) {
		Node *u = findPredNode(x);
		return u->next[0] == NULL ? NULL : u->next[0]->x;
	}

	 bool remove(T x) {
		bool removed = false;
		Node *u = sentinel, *del;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u->next[r] != NULL && (comp = compare(u->next[r]->x, x)) < 0) {
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

	 bool add(T x) {
		Node *u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u->next[r] != NULL && (comp = compare(u->next[r]->x, x)) < 0)
				u = u->next[r];
			if (u->next[r] != NULL && comp == 0)
				return false;
			stack[r--] = u; // going down, store u
		}
		Node *w = newNode(x, pickHeight());
		while (h < w->height)
			stack[++h] = sentinel; // increasing height of skiplist
		for (int i = 0; i < w->height; i++) {
			w->next[i] = stack[i]->next[i];
			stack[i]->next[i] = w;
		}
		n++;
		return true;
	}

	int pickHeight() {
		int z = rand();
		int k = 0;
		int m = 1;
		while ((z & m) != 0) {
			k++;
			m <<= 1;
		}
		return k;
	}

	void clear() {
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

	int size() {
		return n;
	}
};

template<class T>
SkiplistSSet<T>::SkiplistSSet() {
	n = 0;
	sentinel = newNode(NULL, sizeof(int)*8);
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


} /* namespace ods */

#endif /* SKIPLISTSSET_H_ */
