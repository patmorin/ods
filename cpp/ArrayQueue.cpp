/*
 * ArrayQueue.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#include "utils.h"
#include "ArrayQueue.h"

namespace ods {

template<class T>
ArrayQueue<T>::ArrayQueue() {
	n = 0;
	length = 1;
	a = new T[length];
	j = 0;
}

template<class T>
ArrayQueue<T>::~ArrayQueue() {
	delete a;
}

template<class T>
void ArrayQueue<T>::resize() {
	length = max(1, 2*n);
	T *b = new T[length];
	for (int k = 0; k < n; k++)
		b[k] = a[(j+k)%length];
	delete a;
	a = b;
}

template<class T>
bool ArrayQueue<T>::add(T x) {
	 if (n + 1 > length) resize();
	 a[(j+n) % length] = x;
	 n++;
	 return true;
 }

template<class T>
T ArrayQueue<T>::remove() {
	T x = a[j];
	j = (j + 1) % length;
	n--;
	if (length >= 3*n) resize();
	return x;
}

template<class T>
int ArrayQueue<T>::size() {
	return n;
}

} /* namespace ods */
