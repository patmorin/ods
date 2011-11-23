/*
 * FastArrayStack.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */
#include <string.h>
#include "FastArrayStack.h"
#include "utils.h"

namespace ods {

template<class T>
FastArrayStack<T>::FastArrayStack() : ArrayStack<T>() {
}

template<class T>
FastArrayStack<T>::~FastArrayStack() {
}

template<class T>
void FastArrayStack<T>::resize()
{
	length = max(1, 2*n);
	T *b = new T[length];
	memcpy(b, a, n*sizeof(T));
	delete a;
	a = b;
}

template<class T>
void FastArrayStack<T>::add(int i, T x) {
	if (n + 1 > length) resize();
	memcpy(&a[i+1], &a[i], (n-i)*sizeof(T));
	a[i] = x;
	n++;
}

template<class T>
T FastArrayStack<T>::remove(int i)
{
    T x = a[i];
	memcpy(&a[i], &a[i+1], (n-i-1)*sizeof(T));
	n--;
	if (length >= 3 * n) resize();
	return x;
}

template FastArrayStack<int>::FastArrayStack();
template FastArrayStack<int>::~FastArrayStack();
template void FastArrayStack<int>::add(int,int);
template int FastArrayStack<int>::remove(int);



} /* namespace ods */
