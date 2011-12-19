/*
 * array.h
 *
 *  Created on: 2011-11-24
 *      Author: morin
 */

#ifndef ARRAY_H_
#define ARRAY_H_
#include <stdlib.h>
#include <assert.h>

namespace ods {

/**
 * A simple array class that simulates Java's arrays implementation - kind of
 * TODO: Make a reference-counted version so that the = operator doesn't have
 * to destroy its right-hand side.
 */
template<class T>
class array {
protected:
	T *a;
public:
	int length;
	array(int len) {
		length = len;
		a = new T[length];
	}
	array(int len, T init) {
		length = len;
		a = new T[length];
		for (int i = 0; i < length; i++)
			a[i] = init;
	}
	virtual ~array() {
		if (a != NULL) delete[] a;
	}
	array<T>& operator=(array<T> &b) {
		if (a != NULL) delete[] a;
		a = b.a;
		b.a = NULL;
		length = b.length;
		return *this;
	}
	T& operator[](int i) {
		assert(i >= 0 && i < length);
		return a[i];
	}
	T* operator+(int i) {
		return &a[i];
	}
	void swap(int i, int j) {
		T x = a[i];
		a[i] = a[j];
		a[j] = x;
	}
	static void copyOfRange(array<T> &a0, array<T> &a, int i, int j);
	virtual void reverse();
};

template<class T>
void array<T>::reverse() {
	for (int i = 0; i < length/2; i++) {
		swap(i, length-i-1);
	}
}

template<class T>
void array<T>::copyOfRange(array<T> &a0, array<T> &a, int i, int j) {
	array<T> b(j-i);
	memcpy(b.a, a.a, (j-i)*sizeof(T));
	a0 = b;
}

} /* namespace ods */
#endif /* ARRAY_H_ */
