/*
 * SkiplistList.h
 *
 *  Created on: 2011-11-28
 *      Author: morin
 */

#ifndef SkiplistList_H_
#define SkiplistList_H_
#include <cstdlib>
#include <cstring>

#include "utils.h"

namespace ods {

template<class T>
class SkiplistList {
protected:
	T null;
	struct Node {
		T x;
		int height;     // length of next
		int *length;
		Node **next;
	};
	Node *sentinel;
	int h;
	int n;

	Node *newNode(T x, int h) {
		Node *u = new Node;
		u->x = x;
		u->height = h;
		u->length = new int[h+1];
		u->next = new Node*[h+1];
		return u;
	}
	void deleteNode(Node *u) {
		delete[] u->length;
		delete[] u->next;
		delete u;
	}

	Node* findPred(int i) {
		Node *u = sentinel;
		int r = h;
		int j = -1;   // the index of the current node in list 0
		while (r >= 0) {
			while (u->next[r] != NULL && j + u->length[r] < i) {
				j += u->length[r];
				u = u->next[r];
			}
			r--;
		}
		return u;
	}

	Node* add(int i, Node *w) {
		Node *u = sentinel;
		int k = w->height;
		int r = h;
		int j = -1; // index of u
		while (r >= 0) {
			while (u->next[r] != NULL && j + u->length[r] < i) {
				j += u->length[r];
				u = u->next[r];
			}
			u->length[r]++; // to account for new node in list 0
			if (r <= k) {
				w->next[r] = u->next[r];
				u->next[r] = w;
				w->length[r] = u->length[r] - (i - j);
				u->length[r] = i - j;
			}
			r--;
		}
		n++;
		return u;
	}

public:
	SkiplistList();
	virtual ~SkiplistList();


	T get(int i) {
		return findPred(i)->next[0]->x;
	}

	T set(int i, T x) {
		Node *u = findPred(i)->next[0];
		T y = u->x;
		u->x = x;
		return y;
	}

	void add(int i, T x) {
		Node *w = newNode(x, pickHeight());
		if (w->height > h)
			h = w->height;
		add(i, w);
	}

	T remove(int i) {
		T x = null;
		Node *u = sentinel, *del;
		int r = h;
		int j = -1; // index of node u
		while (r >= 0) {
			while (u->next[r] != NULL && j + u->length[r] < i) {
				j += u->length[r];
				u = u->next[r];
			}
			u->length[r]--; // for the node we are removing
			if (j + u->length[r] + 1 == i && u->next[r] != NULL) {
				x = u->next[r]->x;
				u->length[r] += u->next[r]->length[r];
				del = u->next[r];
				u->next[r] = u->next[r]->next[r];
				if (u == sentinel && u->next[r] == NULL)
					h--;
			}
			r--;
		}
		deleteNode(del);
		n--;
		return x;
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
SkiplistList<T>::SkiplistList() {
	null = (T)NULL;
	n = 0;
	sentinel = newNode(null, sizeof(int)*8);
	memset(sentinel->next, '\0', sizeof(Node*)*sentinel->height);
	h = 0;
}

template<class T>
SkiplistList<T>::~SkiplistList() {
	clear();
	deleteNode(sentinel);
}


} /* namespace ods */

#endif /* SkiplistList_H_ */
