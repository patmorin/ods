/*
 * Algorithms.h
 *
 *  Created on: 2011-11-30
 *      Author: morin
 */

#ifndef ALGORITHMS_H_
#define ALGORITHMS_H_

#include "utils.h"
#include "array.h"

namespace ods {

template<class T>
void merge(array<T> &a0, array<T> &a1, array<T> &a) {
	int i0 = 0, i1 = 0;
	for (int i = 0; i < a.length; i++) {
		if (i0 == a0.length)
			a[i] = a1[i1++];
		else if (i1 == a1.length)
			a[i] = a0[i0++];
		else if (compare(a0[i0], a1[i1]) < 0)
			a[i] = a0[i0++];
		else
			a[i] = a1[i1++];
	}
}

template<class T>
void mergeSort(array<T> &a) {
	if (a.length <= 1) return;
	array<T> a0(0);
	array<T>::copyOfRange(a0, a, 0, a.length/2);
	array<T> a1(0);
	array<T>::copyOfRange(a1, a, a.length/2, a.length);
	mergeSort(a0);
	mergeSort(a1);
	merge(a0, a1, a);
}

template<class T>
void quickSort(array<T> &a) {
	quickSort(a, 0, a.length);
}

template<class T>
void quickSort(array<T> &a, int i, int n) {
	if (n <= 1) return;
	T x = a[i + rand()%n];
	int p = i-1, j = i, q = i+n;
	// a[i..p]<x,  a[p+1..q-1]??x, a[q..i+n-1]>x
	while (j < q) {
		int comp = compare(a[j], x);
		if (comp < 0) {       // move to beginning of array
			a.swap(j++, ++p);
		} else if (comp > 0) {
			a.swap(j, --q);  // move to end of array
		} else {
			j++;              // keep in the middle
		}
	}
	// a[i..p]<x,  a[p+1..q-1]=x, a[q..i+n-1]>x
	quickSort(a, i, p-i+1);
	quickSort(a, q, n-(q-i));
}

template<class T>
void heapSort(array<T> &a) {
	BinaryHeap<T>::sort(a);
}


} /* namespace ods */

#endif /* ALGORITHMS_H_ */
