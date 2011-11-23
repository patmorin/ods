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
ArrayStack<T>::ArrayStack() {
	n = 0;
	length = 1;
	a = new T[length];
}

template<class T>
ArrayStack<T>::~ArrayStack() {
	delete a;
}

template<class T>
void ArrayStack<T>::resize()
{
	length = max(1, 2*n);
	T *b = new T[length];
	for (int i = 0; i < n; i++)
		b[i] = a[i];
	delete a;
	a = b;
}

template<class T>
void ArrayStack<T>::add(int i, T x) {
	if (n + 1 > length)	resize();
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
	if (length >= 3 * n) resize();
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


