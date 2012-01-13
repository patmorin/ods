/*
 * Algorithms.h
 *
 *  Created on: 2011-11-30
 *      Author: morin
 */

#ifndef ALGORITHMS_H_
#define ALGORITHMS_H_

#include "SLList.h"

#include "utils.h"
#include "array.h"

namespace ods {


template<class Graph>
void bfs(Graph &g, int r) {
	bool *seen = new bool[g.nVertices()];
	SLList<int> q;
	q.add(r);
	seen[r] = true;
	while (q.size() > 0) {
		int i = q.remove();
		ArrayStack<int> edges;
		g.outEdges(i, edges);
		for (int k = 0; k < edges.size(); k++) {
			int j = edges.get(k);
			if (!seen[j]) {
				q.add(j);
				seen[j] = true;
			}
		}
	}
	delete[] seen;
}

enum { white, grey, black };

template<class Graph>
void dfs(Graph &g, int i, char *c) {
	c[i] = grey;  // currently visiting i
	ArrayStack<int> edges;
	g.outEdges(i, edges);
	for (int k = 0; k < edges.size(); k++) {
		int j = edges.get(k);
		if (c[j] == white) {
			c[j] = grey;
			dfs(g, j, c);
		}
	}
	c[i] = black; // done visiting i
}

template<class Graph>
void dfs(Graph &g, int r) {
	char *c = new char[g.nVertices()];
	dfs(g, r, c);
	delete[] c;
}

template<class Graph>
void dfs2(Graph &g, int r) {
	char *c = new char[g.nVertices()];
	SLList<int> s;
	s.push(r);
	while (s.size() > 0) {
		int i = s.pop();
		if (c[i] == white) {
			c[i] = grey;
			ArrayStack<int> edges;
			g.outEdges(i, edges);
			for (int k = 0; k < edges.size(); k++)
				s.push(edges.get(k));
		}
	}
	delete[] c;
}


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

void countingSort(array<int> &a, int k) {
	array<int> c(k, 0);
	for (int i = 0; i < a.length; i++)
		c[a[i]]++;
	for (int i = 1; i < k; i++)
		c[i] += c[i-1];
	array<int> b(a.length);
	for (int i = a.length-1; i >= 0; i--)
		b[--c[a[i]]] = a[i];
	a = b;
}

void radixSort(array<int> &a) {
	const int d = 8, w = 32;
	for (int p = 0; p < w/d; p++) {
		array<int> c(1<<d, 0);
		// the next three for loops implement counting-sort
		array<int> b(a.length);
		for (int i = 0; i < a.length; i++)
			c[(a[i] >> d*p)&((1<<d)-1)]++;
		for (int i = 1; i < 1<<d; i++)
			c[i] += c[i-1];
		for (int i = a.length-1; i >= 0; i--)
			b[--c[(a[i] >> d*p)&((1<<d)-1)]] = a[i];
		a = b;
	}
}


} /* namespace ods */

#endif /* ALGORITHMS_H_ */
