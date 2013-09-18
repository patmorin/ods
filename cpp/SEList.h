/*
 * SEList.h
 *
 *  Created on: 2011-11-25
 *      Author: morin
 */

#ifndef SELIST_H_
#define SELIST_H_
#include "ArrayDeque.h"
#include "array.h"


namespace ods {

template<class T>
class SEList {
protected:
	int n;
	int b;
	class BDeque : public ArrayDeque<T> {
		using ArrayDeque<T>::a;
		using ArrayDeque<T>::j;
		using ArrayDeque<T>::n;
	public:
		BDeque(int b) {
			n = 0;
			j = 0;
			array<int> z(b+1);
			a = z;
		}
		virtual ~BDeque() { }
		// C++ Question: Why is this necessary?
		virtual void add(int i, T x) {
			ArrayDeque<T>::add(i, x);
		}
		virtual bool add(T x) {
			ArrayDeque<T>::add(size(), x);
			return true;
		}
		void resize() {}
	};

	class Node {
	public:
		BDeque d;
		Node *prev, *next;
		Node(int b) : d(b) { }
	};

	Node dummy;

	class Location {
	public:
		Node *u;
		int j;
		Location() { }
		Location(Node *u, int j) {
			this->u = u;
			this->j = j;
		}
	};

	Node *addBefore(Node *w) {
		Node *u = new Node(b);
		u->prev = w->prev;
		u->next = w;
		u->next->prev = u;
		u->prev->next = u;
		return u;
	}

	void remove(Node *w) {
		w->prev->next = w->next;
		w->next->prev = w->prev;
		delete w;
	}

	void getLocation(int i, Location &ell) {
		if (i < n / 2) {
			Node *u = dummy.next;
			while (i >= u->d.size()) {
				i -= u->d.size();
				u = u->next;
			}
			ell.u = u;
			ell.j = i;
		} else {
			Node *u = &dummy;
			int idx = n;
			while (i < idx) {
				u = u->prev;
				idx -= u->d.size();
			}
			ell.u = u;
			ell.j = i - idx;
		}
	}

	/**
	 * Call this function on a node u such that u and its b-1 successors
	 * all contain b+1 items.  This adds a new node so that u and its
	 * b successors each contain b items
	 */
	void spread(Node *u) {
		Node *w = u;
		for (int j = 0; j < b; j++) {
			w = w->next;
		}
		w = addBefore(w);
		while (w != u) {
			while (w->d.size() < b)
				w->d.add(0, w->prev->d.remove(w->prev->d.size()-1));
			w = w->prev;
		}
	}

	/**
	 * Call this function on a node u such that u and its b-1 successors
	 * all contain b-1 items.  This removes a node so that u and its
	 * b-2 successors each contain b items
	 */
	void gather(Node *u) {
		Node *w = u;
		for (int j = 0; j < b-1; j++) {
			while (w->d.size() < b)
				w->d.add(w->next->d.remove(0));
			w = w->next;
		}
		remove(w);
	}

	void add(T x) {
		Node *last = dummy.prev;
		if (last == &dummy || last->d.size() == b+1) {
			last = addBefore(&dummy);
		}
		last->d.add(x);
		n++;
	}




public:
	SEList(int b) : dummy(b) {
		this->b = b;
		dummy.next = &dummy;
		dummy.prev = &dummy;
		n  = 0;
	}

	virtual ~SEList() {
		clear();
	}

	virtual void clear() {
		Node *u = dummy.next;
		while (u != &dummy) {
			Node *w = u->next;
			delete u;
			u = w;
		}
		n = 0;
	}

	T get(int i) {
		Location l;
		getLocation(i, l);
		return l.u->d.get(l.j);
	}

	T set(int i, T x) {
		Location l;
		getLocation(i, l);
		T y = l.u->d.get(l.j);
		l.u->d.set(l.j, x);
		return y;
	}

	int size() {
		return n;
	}



	void add(int i, T x) {
		if (i == n) {
			add(x);
			return;
		}
		Location l; getLocation(i, l);
		Node *u = l.u;
		int r = 0;
		while (r < b && u != &dummy && u->d.size() == b+1) {
			u = u->next;
			r++;
		}
		if (r == b) {// b blocks each with b+1 elements
			spread(l.u);
			u = l.u;
		}
		if (u == &dummy) { // ran off the end - add new node
			u = addBefore(u);
		}
		while (u != l.u) { // work backwards, shifting elements
			u->d.add(0, u->prev->d.remove(u->prev->d.size()-1));
			u = u->prev;
		}
		u->d.add(l.j, x);
		n++;
	}

	T remove(int i) {
		Location l; getLocation(i, l);
		T y = l.u->d.get(l.j);
		Node *u = l.u;
		int r = 0;
		while (r < b && u != &dummy && u->d.size() == b - 1) {
			u = u->next;
			r++;
		}
		if (r == b) { // found b blocks each with b-1 elements
			gather(l.u);
		}
		u = l.u;
		u->d.remove(l.j);
		while (u->d.size() < b - 1 && u->next != &dummy) {
			u->d.add(u->next->d.remove(0));
			u = u->next;
		}
		if (u->d.size() == 0)
			remove(u);
		n--;
		return y;
	}
};

} /* namespace ods */
#endif /* SELIST_H_ */
