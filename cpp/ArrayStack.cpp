/*
 * ArrayStack.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#include "ArrayStack.h"
#include "utils.h"


namespace ods {

template <class T>
ArrayStack<T>::ArrayStack() : a(1) {
	n = 0;
}

template<class T>
ArrayStack<T>::~ArrayStack() {
}

template<class T>
void ArrayStack<T>::resize()
{
	array<T> b(max(2 * n, 1));
	for (int i = 0; i < n; i++)
		b[i] = a[i];
	a = b;
}

template<class T>
void ArrayStack<T>::add(int i, T x) {
	if (n + 1 > a.length)	resize();
	for (int j = n; j > i; j--)
		a[j] = a[j - 1];
	a[i] = x;
	n++;
}

template<class T>
T ArrayStack<T>::remove(int i)
{
    T x = a[i];
	for (int j = i; j < n - 1; j++)
		a[j] = a[j + 1];
	n--;
	if (a.length >= 3 * n) resize();
	return x;
}

template ArrayStack<int>::ArrayStack();
template ArrayStack<int>::~ArrayStack();
template void ArrayStack<int>::add(int,int);
template int ArrayStack<int>::remove(int);

template ArrayStack<int*>::ArrayStack();
template ArrayStack<int*>::~ArrayStack();
template void ArrayStack<int*>::add(int,int*);
template int* ArrayStack<int*>::remove(int);


//void pfft() {
//	ArrayStack<int> asi;
//	asi.size();
//}

} /* namespace ods */


