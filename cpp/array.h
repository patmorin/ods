/*
 * array.h
 *
 *  Created on: 2011-11-24
 *      Author: morin
 */

#ifndef ARRAY_H_
#define ARRAY_H_
#include <iostream>
#include <algorithm>

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
	array(int len);
	array(int len, T init);
	void fill(T x);
	virtual ~array();

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
array<T>::array(int len) {
	length = len;
	a = new T[length];
}

template<class T>
array<T>::array(int len, T init) {
	length = len;
	a = new T[length];
	for (int i = 0; i < length; i++)
		a[i] = init;
}

template<class T>
array<T>::~array() {
	if (a != NULL) delete[] a;
}

template<class T>
void array<T>::reverse() {
	for (int i = 0; i < length/2; i++) {
		swap(i, length-i-1);
	}
}

template<class T>
void array<T>::copyOfRange(array<T> &a0, array<T> &a, int i, int j) {
	array<T> b(j-i);
	std::copy(a.a, a.a+j-i, b.a);
	a0 = b;
}

template<class T>
void array<T>::fill(T x) {
	std::fill(a, a+length, x);
}


} /* namespace ods */
#endif /* ARRAY_H_ */
