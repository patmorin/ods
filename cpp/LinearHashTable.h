/*
 * LinearHashTable.h
 *
 *  Created on: 2011-12-16
 *      Author: morin
 */

#ifndef LINEARHASHTABLE_H_
#define LINEARHASHTABLE_H_
#include <climits>
#include <iostream>
using namespace std;
#include "array.h"
namespace ods {

extern unsigned int tab[4][256];

template<class T>
class LinearHashTable {

	static const int w = 32;
	static const int r = 8;
	int n;   // number of values in T
	int q;   // number of non-null entries in T
	int d;   // t.length = 2^d
	T null, del;
	array<T> t;
	void resize();
	int hash(T x) {
		unsigned h = hashCode(x);
		return (tab[0][h&0xff]
				 ^ tab[1][(h>>8)&0xff]
				 ^ tab[2][(h>>16)&0xff]
				 ^ tab[3][(h>>24)&0xff])
				>> (w-d);
	}

public:
	LinearHashTable();
	virtual ~LinearHashTable();
	bool add(T x);
	T remove(T x);
	T find(T x);
	int size() { return n; }
	void clear();
};

template<class T>
LinearHashTable<T>::LinearHashTable() : t(2, INT_MIN) {
	null = INT_MIN;
	del = INT_MIN + 1;
	n = 0;
	q = 0;
	d = 1;
}

template<class T>
LinearHashTable<T>::~LinearHashTable() {
}

template<class T>
void LinearHashTable<T>::resize() {
	d = 1;
	while ((1<<d) < 3*n) d++;
	array<T> tnew(1<<d, null);
	q = n;
	// insert everything in told
	for (int k = 0; k < t.length; k++) {
		if (t[k] != null && t[k] != del) {
			int i = hash(t[k]);
			while (tnew[i] != null)
				i = (i == tnew.length-1) ? 0 : i + 1;
			tnew[i] = t[k];
		}
	}
	t = tnew;
}

template<class T>
void LinearHashTable<T>::clear() {
	n = 0;
	q = 0;
	d = 1;
	array<T> tnew(2, null);
	t = tnew;
}

template<class T>
bool LinearHashTable<T>::add(T x) {
	if (find(x) != null) return false;
	if (2*(q+1) > t.length) resize();   // max 50% occupancy
	int i = hash(x);
	while (t[i] != null && t[i] != del)
		i = (i == t.length-1) ? 0 : i + 1; // increment i (mod t.length)
	if (t[i] == null) q++;
	n++;
	t[i] = x;
	return true;
}

template<class T>
T LinearHashTable<T>::find(T x) {
	int i = hash(x);
	while (t[i] != null) {
		if (t[i] != del && t[i] == x) return t[i];
		i = (i == t.length-1) ? 0 : i + 1; // increment i (mod t.length)
	}
	return null;
}

template<class T>
T LinearHashTable<T>::remove(T x) {
	int i = hash(x);
	while (t[i] != null) {
		T y = t[i];
		if (y != del && x == y) {
			t[i] = del;
			n--;
			if (8*n < t.length) resize(); // min 12.5% occupancy
			return y;
		}
		i = (i == t.length-1) ? 0 : i + 1;  // increment i (mod t.length)
	}
	return null;
}





} /* namespace ods */
#endif /* LINEARHASHTABLE_H_ */
