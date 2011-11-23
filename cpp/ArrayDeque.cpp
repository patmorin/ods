/*
 * ArrayDeque.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */
#include "utils.h"
#include "ArrayDeque.h"

namespace ods {

template<class T>
ArrayDeque<T>::ArrayDeque() {
	n = 0;
	length = 1;
	a = new T[length];
	j = 0;
}

template<class T>
ArrayDeque<T>::~ArrayDeque() {
	delete a;
}

template<class T>
void ArrayDeque<T>::resize() {
	length = max(1, 2 * n);
	T *b = new T[length];
	for (int k = 0; k < n; k++)
		b[k] = a[(j+k)%length];
	delete a;
	a = b;
}

template<class T>
int ArrayDeque<T>::size()
{
	return n;
}

template<class T>
void ArrayDeque<T>::add(int i, T x) {
	if (n + 1 > length)	resize();
	if (i < n/2) { // shift a[0],..,a[i-1] left one position
		j = (j == 0) ? length - 1 : j - 1;
		for (int k = 0; k <= i-1; k++)
			a[(j+k)%length] = a[(j+k+1)%length];
	} else { // shift a[i],..,a[n-1] right one position
		for (int k = n; k > i; k--)
			a[(j+k)%length] = a[(j+k-1)%length];
	}
	a[(j+i)%length] = x;
	n++;
}


template<class T>
T ArrayDeque<T>::remove(int i)
{
    T x = a[(j+i)%length];
    if (i < n/2) {  // shift a[0],..,[i-1] right one position
    	for (int k = i; k > 0; k--)
			a[(j+k)%length] = a[(j+k-1)%length];
		j = (j + 1) % length;
    } else {        // shift a[i+1],..,a[n-1] left one position
		for (int k = i; k < n-1; k++)
			a[(j+k)%length] = a[(j+k+1)%length];
    }
    n--;
    if (3*n < length) resize();
    return x;
}

template ArrayDeque<int>::ArrayDeque();
template ArrayDeque<int>::~ArrayDeque();
template void ArrayDeque<int>::add(int,int);
template int ArrayDeque<int>::remove(int);
template int ArrayDeque<int>::size();

} /* namespace ods */
