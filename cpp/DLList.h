/*
 * DLList.h
 *
 *  Created on: 2011-11-24
 *      Author: morin
 */

#ifndef DLLIST_H_
#define DLLIST_H_

namespace ods {

template<class T>
class DLList {
protected:
	struct Node {
		Node *prev, *next;
		T x;
	};
	Node dummy;
	int n;
	Node* addBefore(Node *w, T x) {
		Node *u = new Node;
		u->x = x;
		u->prev = w->prev;
		u->next = w;
		u->next->prev = u;
		u->prev->next = u;
		n++;
		return u;
	}
	void remove(Node *w) {
		w->prev->next = w->next;
		w->next->prev = w->prev;
		delete w;
		n--;
	}
	Node* getNode(int i) {
		Node* p;
		if (i < n / 2) {
			p = dummy.next;
			for (int j = 0; j < i; j++)
				p = p->next;
		} else {
			p = &dummy;
			for (int j = n; j > i; j--)
				p = p->prev;
		}
		return (p);
	}

public:
	DLList();
	virtual ~DLList();
	int size() {
		return n;
	}
	T get(int i) {
        return getNode(i)->x;
	}
	T set(int i, T x) {
		Node* u = getNode(i);
		T y = u->x;
		u->x = x;
		return y;
	}
	virtual void add(int i, T x) {
        addBefore(getNode(i), x);
	}
	virtual void add(T x) { add(size(), x); }
	virtual T remove(int i) {
		Node *w = getNode(i);
		T x = w->x;
		remove(w);
		return x;
	}
	virtual void clear();
};

template<class T>
DLList<T>::DLList() {
	dummy.next = &dummy;
	dummy.prev = &dummy;
	n = 0;
}

template<class T>
DLList<T>::~DLList() {
	clear();
}

template<class T>
void DLList<T>::clear() {
	Node *u = dummy.next;
	while (u != &dummy) {
		Node *w = u->next;
		delete u;
		u = w;
	}
	n = 0;
}

}

 /* namespace ods */
#endif /* DLLIST_H_ */
