/*
 * main.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */
#include <cmath>
#include <cassert>
#include <ctime>
#include <cstdlib>

#include <iostream>
using namespace std;

#include "ArrayStack.h"
#include "FastArrayStack.h"
#include "ArrayDeque.h"
#include "DualArrayDeque.h"
#include "RootishArrayStack.h"
#include "DLList.h"
#include "SEList.h"
#include "SkiplistList.h"

#include "FastSqrt.h"

#include "SkiplistSSet.h"
#include "BinarySearchTree.h"
#include "Treap.h"
#include "ScapegoatTree.h"
#include "RedBlackTree.h"
#include "BTree.h"

#include "BinaryTrie.h"
#include "XFastTrie.h"
#include "YFastTrie.h"

#include "BinaryHeap.h"
#include "MeldableHeap.h"

#include "BinaryTree.h"

#include "ChainedHashTable.h"
#include "LinearHashTable.h"

#include "Algorithms.h"

#include "AdjacencyMatrix.h"
#include "AdjacencyLists.h"

using namespace ods;

#ifndef CLOCKS_PER_SEC
#define CLOCKS_PER_SEC 1000
#endif

const unsigned RA=0x0001;   // random access
const unsigned FM=0x0002;   // front modifications

template<class Graph1, class Graph2>
void graphCmp(Graph1 &g1, Graph2 &g2) {
	int n = g1.nVertices();
	assert(n == g2.nVertices());
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < n; j++) {
			assert(g1.hasEdge(i,j) == g2.hasEdge(i,j));
		}
	}
	for (int i = 0; i < n; i++) {
		int j;
		ArrayStack<int> l1, l2;
		g1.outEdges(i, l1);
		g2.outEdges(i, l2);
		for (int i = 0; i < l1.size(); i++) {
			for (j = 0; j < l2.size(); j++)
				if (l1.get(i) == l2.get(j)) break;
			assert(j < l2.size());
		}
		for (int i = 0; i < l2.size(); i++) {
			for (j = 0; j < l1.size(); j++)
				if (l2.get(i) == l1.get(j)) break;
			assert(j < l2.size());
		}
	}
}

template<class Graph1, class Graph2>
void graphTests(Graph1 &g1, Graph2 &g2) {
	int n = g1.nVertices();
	for (int k = 0; k < 50*n*n; k++) {
		int i = rand() % n;
		int j = rand() % n;
		if (i != j) {
			if (g1.hasEdge(i,j)) {
				g1.removeEdge(i,j);
				g2.removeEdge(i,j);
			} else {
				g1.addEdge(i,j);
				g2.addEdge(i,j);
			}
		}
		graphCmp(g1, g2);
	}
}


void sqrtTests() {
	int n = 1 << 30;
	clock_t start, stop;

	cout << "Initializing sqrt tables " << n << " inputs...";
	cout.flush();
	start = clock();
	FastSqrt::init();
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

/*
	cout << "Testing correctness of " << n << " inputs...";
	cout.flush();
	start = clock();
	for (int t = 1; t < n; t++) {
		if ((t & 0xfffff) == 0xfffff)
			cout << "."; cout.flush();
		int x = t;
		int logx = FastSqrt::log(x);
		assert(((1 << logx) & x) != 0);
		assert((((1 << (logx + 1)) - 1) & x) == x);
		double ms = sqrt(x);
		double fss = FastSqrt::sqrt(x);
		assert (fabs(ms - fss) <= 2.0);
	}
	stop = clock();
	cout << "passed!" << endl;
*/

	n = 1 << 27;

	cout << "Computing " << n << " sqrt roots...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++) {
		sqrt(i);
	}
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Computing " << n << " FastSqrt::sqrt roots...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++) {
		FastSqrt::sqrt(i);
	}
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
}

template<class Tree>
void btTests(Tree &t) {
	t.size();
	t.size2();
	t.height();
	t.bfTraverse();
	t.traverse();
	t.traverse2();
}

template<class Heap>
void heapTests(Heap &h, int n) {
	clock_t start, stop;
	cout << "Adding " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++) {
		h.add(rand()%100);
	}
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Removing " << n << " elements...";
	cout.flush();
	start = clock();
	int p = h.remove();
	for (int i = 1; i < n; i++) {
		assert(h.size() == n-i);
		int q = h.remove();
		assert(p <= q);
		p = q;
	}
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
}

template <class SSet>
void ssetTests(SSet &ss, int n, unsigned flags) {
	clock_t start, stop;
	cout << "Adding " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++) {
		ss.add(rand());
	}
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Finding " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++) {
		int z = rand();
		int y = ss.find(z);
		if (i % 300000 == 0) {
			cout << "[" << z << "=>" << y << "]";
		}
	}
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

//	cout << "Running binary tree tests...";
//	cout.flush();
//	start = clock();
//	btTests(ss);
//	stop = clock();
//	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Removing " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++) {
		ss.remove(rand());
	}
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Final size is " << ss.size() << endl;

	cout << "Clearing " << ss.size() << " elements...";
	cout.flush();
	start = clock();
	ss.clear();
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
}

template <class USet>
void usetTests(USet &us, int n) {
	clock_t start, stop;
	cout << "Adding " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++) {
		us.add(rand() % (2*n));
	}
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Finding " << n << " elements...";
	cout.flush();
	start = clock();
	int success = 0;
	for (int i = 0; i < n; i++) {
		int z = rand() % (2*n);
		int y = us.find(z);
		if (y == z) success++;
		if (i % 300000 == 0) {
			cout << "[" << z << "=>" << y << "]";
		}
	}
	stop = clock();
	cout << "done [" << success << "/" << n << " (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Removing " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++) {
		us.remove(rand() % (2*n));
	}
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Final size is " << us.size() << endl;

	cout << "Clearing " << us.size() << " elements...";
	cout.flush();
	start = clock();
	us.clear();
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
}


template <class List>
void listTests(List &ell, int n, unsigned flags)
{
	cout.setf(ios::fixed,ios::floatfield);
	cout.precision(5);

	ell.clear();

	clock_t start, stop;
	cout << "Adding " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++)
		ell.add(ell.size(), i);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	if (flags & RA) {
		cout << "Reading " << n << " elements...";
		cout.flush();
		start = clock();
		for (int i = 0; i < n; i++)
			assert(ell.get(i) == i);
		stop = clock();
		cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

		cout << "Setting " << n << " elements...";
		cout.flush();
		start = clock();
		for (int i = 0; i < n; i++)
			ell.set(i, 2*i);
		stop = clock();
		cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
	} else {
		cout << "Reading 2log(" << n << ") elements...";
		cout.flush();
		start = clock();
		for (int i = 1; i < n; i*=2) {
			assert(ell.get(i) == i);
			assert(ell.get(n-i-1) == n-i-1);
		}
		stop = clock();
		cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

		cout << "Setting 2log(" << n << ") elements...";
		cout.flush();
		start = clock();
		for (int i = 1; i < n; i*=2) {
			ell.set(i, 2*i);
			ell.set(n-i-1, 2*(n-i-1));
		}
		stop = clock();
		cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	}

	if (flags & FM) {
		cout << "Adding " << n << " elements at front...";
		cout.flush();
		start = clock();
		for (int i = 0; i < n; i++)
			ell.add(0, i);
		stop = clock();
		cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

		cout << "Removing " << n << " elements at front...";
		cout.flush();
		start = clock();
		for (int i = 0; i < n; i++)
			ell.remove(0);
		stop = clock();
		cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
	} else {
		cout << "Adding log(" << n << ") elements at front...";
		cout.flush();
		start = clock();
		for (int i = 1; i < n; i*=2)
			ell.add(0, i);
		stop = clock();
		cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

		cout << "Removing log(" << n << ") elements at front...";
		cout.flush();
		start = clock();
		for (int i = 1; i < n; i*=2)
			ell.remove(0);
		stop = clock();
		cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
	}

	cout << "Removing " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++)
		ell.remove(ell.size()-1);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
}

void sortTests(int n) {
	clock_t start, stop;
	array<int> a(n);

	for (int i = 0; i < n; i++)
		a[i] = rand();
	cout << "Sorting " << n << " elements using quickSort...";
	cout.flush();
	start = clock();
	quickSort(a);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
	for (int i = 1; i < n; i++)
		assert(a[i-1] <= a[i]);

	for (int i = 0; i < n; i++)
		a[i] = rand();
	cout << "Sorting " << n << " elements using heapSort...";
	cout.flush();
	start = clock();
	heapSort(a);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
	for (int i = 1; i < n; i++)
		assert(a[i-1] <= a[i]);

	for (int i = 0; i < n; i++)
		a[i] = rand();
	cout << "Sorting " << n << " elements using mergeSort...";
	cout.flush();
	start = clock();
	mergeSort(a);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
	for (int i = 1; i < n; i++)
		assert(a[i-1] <= a[i]);

	for (int i = 0; i < n; i++)
		a[i] = rand();
	cout << "Sorting " << n << " elements using radixSort...";
	cout.flush();
	start = clock();
	radixSort(a);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
	for (int i = 1; i < n; i++)
		assert(a[i-1] <= a[i]);

}


int main(int argc, char **argv)
{
	int n = 1000000;

	srand(0);


	/* {
		for (int i = 1; i < 10; i++) {
			int b = i*10;
			cout << endl << "BTree<int>(" << b << "):" << endl;
			BTree<int> t(b);
			ssetTests(t, n, 0x0);
		}
	} */

	{
		cout << endl << "Treap<int>:" << endl;
		Treap1<int> t;
		btTests(t);
		ssetTests(t, n, 0x0);
	}

	{
		cout << endl << "YFastTrie<int>:" << endl;
		YFastTrie<int> t;
		ssetTests(t, n, 0x0);
	}

	{
		cout << endl << "XFastTrie<int>:" << endl;
		XFastTrie1<int> t;
		ssetTests(t, n, 0x0);
	}

	{
		cout << endl << "BinaryTrie<int>:" << endl;
		BinaryTrie1<int> t;
		ssetTests(t, n, 0x0);
	}



	{
		int n = 20;
		cout << endl << "Graph Tests" << endl;
		AdjacencyMatrix am(n);
		AdjacencyLists al(n);
		graphTests(am, al);
		bfs(al, 0);
		dfs(al, 0);
		dfs2(al, 0);
	}

	sortTests(n);

	{
		cout << endl << "ChainedHashTable<int>:" << endl;
		ChainedHashTable<int> t;
		usetTests(t, n);
	}

	{
		cout << endl << "LinearHashTable<int>:" << endl;
		LinearHashTable<int> t(INT_MIN, INT_MIN+1);
		usetTests(t, n);
	}

	{
		cout << endl << "BinaryTree:" << endl;
		BinaryTree<BTNode1> t;
		btTests(t);
	}


	{
		cout << endl << "BinaryHeap<int>:" << endl;
		BinaryHeap<int> h;
		heapTests(h, n);
	}

	{
		cout << endl << "MeldableHeap<int>:" << endl;
		MeldableHeap1<int> h;
		heapTests(h, n);
	}

	{
		cout << endl << "RedBlackTree<int>:" << endl;
		RedBlackTree1<int> t;
		btTests(t);
		ssetTests(t, n, 0x0);
	}

	{
		cout << endl << "ScapegoatTree<int>:" << endl;
		ScapegoatTree1<int> t;
		btTests(t);
		ssetTests(t, n, 0x0);
	}

	{
		cout << endl << "BinarySearchTree<int>:" << endl;
		BinarySearchTree1<int> t;
		btTests(t);
		ssetTests(t, n, 0x0);
	}

	{
		cout << endl << "Treap<int>:" << endl;
		Treap1<int> t;
		btTests(t);
		ssetTests(t, n, 0x0);
	}



	{
		cout << endl << "SkiplistSet<int>:" << endl;
		SkiplistSSet<int> s;
		ssetTests(s, n, 0x0);
	}


	{
		BinarySearchTree<BSTNode1<int>,int> s;
		ssetTests(s, n, 0x0);
	}

	// sqrtTests();

	cout << endl << "ArrayStack<int>:" << endl;
	ArrayStack<int> as;
	listTests(as, n, RA);

	cout << endl << "FastArrayStack<int>:" << endl;
	FastArrayStack<int> fas;
	listTests(fas, n, RA);

	cout << endl << "ArrayDeque<int>:" << endl;
	ArrayDeque<int> ad;
	listTests(ad, n, RA|FM);

	cout << endl << "DualArrayDeque<int>:" << endl;
	DualArrayDeque<int> dad;
	listTests(dad, n, RA|FM);

	cout << endl << "RootishArrayStack<int>:" << endl;
	RootishArrayStack<int> ras;
	listTests(ras, n, RA);

	cout << endl << "DLList<int>:" << endl;
	DLList<int> dll;
	listTests(dll, n, FM);

	{
	cout << endl << "SEList<int>:" << endl;
	SEList<int> sel(50);
	listTests(sel, n, FM);
	}

	{
	cout << endl << "SkiplistList<int>:" << endl;
	SkiplistList<int> sel;
	listTests(sel, n, RA|FM);
	}


	return 0;
}

