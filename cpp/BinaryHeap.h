/*
 * BinaryHeap.h
 *
 *  Created on: 2011-11-30
 *      Author: morin
 */

#ifndef BINARYHEAP_H_
#define BINARYHEAP_H_

#include <cstring>
#include "utils.h"
#include "array.h"

namespace ods {

template<class T>
class BinaryHeap {
protected:
	array<T> a;
	int n;
	void resize();
	void bubbleUp(int i);
	void trickleDown(int i);
	static int left(int i) {
		return 2*i + 1;
	}
	static int right(int i) {
		return 2*i + 2;
	}
	static int parent(int i) {
		return (i-1)/2;
	}
public:
	BinaryHeap();
	BinaryHeap(array<T>& b);
	virtual ~BinaryHeap();
	bool add(T x);
	T findMin() {
		return a[0];
	}
	T remove();
	void clear();
	int size() {
		return n;
	}
	static void sort(array<T> &b);
};


template<class T>
void BinaryHeap<T>::resize() {
	array<T> b(max(2*n, 1));
	std::copy(a+0, a+n, b+0);
	a = b;
}

template<class T>
void BinaryHeap<T>::sort(array<T> &b) {
	BinaryHeap<T> h(b);
	while (h.n > 1) {
		h.a.swap(--h.n, 0);
		h.trickleDown(0);
	}
	b = h.a;
	b.reverse();
}


template<class T>
bool BinaryHeap<T>::add(T x) {
	if (n + 1 > a.length) resize();
	a[n++] = x;
	bubbleUp(n-1);
	return true;
}



template<class T>
void BinaryHeap<T>::bubbleUp(int i) {
	int p = parent(i);
	while (i > 0 && compare(a[i], a[p]) < 0) {
		a.swap(i,p);
		i = p;
		p = parent(i);
	}
}



template<class T>
T BinaryHeap<T>::remove() {
	T x = a[0];
	a[0] = a[--n];
	trickleDown(0);
	if (3*n < a.length) resize();
	return x;
}



template<class T>
void BinaryHeap<T>::trickleDown(int i) {
	do {
		int j = -1;
		int r = right(i);
		if (r < n && compare(a[r], a[i]) < 0) {
			int l = left(i);
			if (compare(a[l], a[r]) < 0) {
				j = l;
			} else {
				j = r;
			}
		} else {
			int l = left(i);
			if (l < n && compare(a[l], a[i]) < 0) {
				j = l;
			}
		}
		if (j >= 0)	a.swap(i, j);
		i = j;
	} while (i >= 0);
}



template<class T>
BinaryHeap<T>::BinaryHeap() : a(1) {
	n = 0;
}



template<class T>
BinaryHeap<T>::BinaryHeap(array<T> &b) : a(0) {
	a = b;
	n = a.length;
	for (int i = n/2-1; i >= 0; i--) {
		trickleDown(i);
	}
}



template<class T>
BinaryHeap<T>::~BinaryHeap() {
	// nothing to do
}



template<class T>
void BinaryHeap<T>::clear() {
}

} /* namespace ods */
#endif /* BINARYHEAP_H_ */
