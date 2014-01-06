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
	array<T> t;
	int n;   // number of values in T
	int q;   // number of non-null entries in T
	int d;   // t.length = 2^d
	T null, del;
	void resize();
	int hash(T x) {
		unsigned h = hashCode(x);
		return (tab[0][h&0xff]
				 ^ tab[1][(h>>8)&0xff]
				 ^ tab[2][(h>>16)&0xff]
				 ^ tab[3][(h>>24)&0xff])
				>> (w-d);
	}
	// Sample code for the book only -- never use this
	/*
	int idealHash(T x) {
		return tab[hashCode(x) >> w-d];
	}
	*/


public:
	// FIXME: get rid of default constructor
	LinearHashTable();
	LinearHashTable(T null, T del);
	virtual ~LinearHashTable();
	bool add(T x);
	bool addSlow(T x);
	T remove(T x);
	T find(T x);
	int size() { return n; }
	void clear();
	// FIXME: yuck
	void setNull(T null) { this->null = null; t.fill(null); }
	void setDel(T del) { this->del = del; }
};

/*
template<>
LinearHashTable<int>::LinearHashTable() : t(2, INT_MIN) {
	null = INT_MIN;
	del = INT_MIN + 1;
	n = 0;
	q = 0;
	d = 1;
}
*/

/**
 * FIXME: Dangerous - leaves null and del uninitialized
 */
template<class T>
LinearHashTable<T>::LinearHashTable() : t(2) {
/*
	this->null = null;
	this->del = del;
*/
	n = 0;
	q = 0;
	d = 1;
}


template<class T>
LinearHashTable<T>::LinearHashTable(T null, T del) : t(2, null) {
	this->null = null;
	this->del = del;
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
	// insert everything into tnew
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
		i = (i == t.length-1) ? 0 : i + 1; // increment i
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
		i = (i == t.length-1) ? 0 : i + 1; // increment i
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
		i = (i == t.length-1) ? 0 : i + 1;  // increment i
	}
	return null;
}

template<class T>
bool LinearHashTable<T>::addSlow(T x) {
	if (2*(q+1) > t.length) resize();   // max 50% occupancy
	int i = hash(x);
	while (t[i] != null) {
			if (t[i] != del && x.equals(t[i])) return false;
			i = (i == t.length-1) ? 0 : i + 1; // increment i
	}
	t[i] = x;
	n++; q++;
	return true;
}


} /* namespace ods */
#endif /* LINEARHASHTABLE_H_ */
