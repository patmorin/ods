/*
 * main.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */
#include <time.h>
#include <assert.h>

#include <iostream>
using namespace std;

#include "ArrayStack.h"
#include "FastArrayStack.h"
#include "ArrayDeque.h"
#include "DualArrayDeque.h"
#include "RootishArrayStack.h"

using namespace ods;

#ifndef CLOCKS_PER_SEC
#define CLOCKS_PER_SEC 1000
#endif

template <class List>
void listTests(List &as, int n)
{
	cout.setf(ios::fixed,ios::floatfield);
	cout.precision(5);

	clock_t start, stop;
	int sn = n/3333;
	cout << "Adding " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++)
		as.add(as.size(), i);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Reading " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++)
		assert(as.get(i) == i);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Setting " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++)
		as.set(i, 2*i);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

/*
	cout << "Adding " << sn << " elements at front...";
	cout.flush();
	start = clock();
	for (int i = 0; i < sn; i++)
		as.add(0, i);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;

	cout << "Removing " << sn << " elements at front...";
	cout.flush();
	start = clock();
	for (int i = 0; i < sn; i++)
		as.remove(0);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
*/

	cout << "Removing " << n << " elements...";
	cout.flush();
	start = clock();
	for (int i = 0; i < n; i++)
		as.remove(as.size()-1);
	stop = clock();
	cout << "done (" << ((double)(stop-start))/CLOCKS_PER_SEC << "s)" << endl;
}


int main(int argc, char **argv)
{
	int n = 10000000;

	cout << endl << "ArrayStack<int>:" << endl;
	ArrayStack<int> as;
	listTests(as, n);

	cout << endl << "FastArrayStack<int>:" << endl;
	FastArrayStack<int> fas;
	listTests(fas, n);

	cout << endl << "ArrayDeque<int>:" << endl;
	ArrayDeque<int> ad;
	listTests(ad, n);

	cout << endl << "DualArrayDeque<int>:" << endl;
	DualArrayDeque<int> dad;
	listTests(dad, n);

	cout << endl << "RootishArrayStack<int>:" << endl;
	RootishArrayStack<int> ras;
	listTests(ras, n);

	return 0;
}

