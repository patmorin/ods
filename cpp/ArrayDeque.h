/*
 * ArrayDeque.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef ARRAYDEQUE_H_
#define ARRAYDEQUE_H_
#include "array.h"
#include "utils.h"

namespace ods {

template<class T>
class ArrayDeque {
protected:
	array<T> a;
	int j;
	int n;
	void resize();
public:
	ArrayDeque();
	virtual ~ArrayDeque();
	int size();
	T get(int i);
	T set(int i, T x);
	virtual void add(int i, T x);
	virtual T remove(int i);
	virtual void clear();
};

template<class T> inline
T ArrayDeque<T>::get(int i) {
	return a[(j + i) % a.length];
}

template<class T> inline
T ArrayDeque<T>::set(int i, T x) {
	T y = a[(j + i) % a.length];
	a[(j + i) % a.length] = x;
	return y;
}

template<class T>
void ArrayDeque<T>::clear() {
	n = 0;
	j = 0;
	array<T> b(1);
	a = b;
}

template<class T>
ArrayDeque<T>::ArrayDeque() : a(1) {
	n = 0;
	j = 0;
}

template<class T>
ArrayDeque<T>::~ArrayDeque() {
}

template<class T>
void ArrayDeque<T>::resize() {
	array<T> b(max(1, 2*n));
	for (int k = 0; k < n; k++)
		b[k] = a[(j+k)%a.length];
	a = b;
    j = 0;
}

template<class T>
int ArrayDeque<T>::size() {
	return n;
}

template<class T>
void ArrayDeque<T>::add(int i, T x) {
	if (n + 1 > a.length)	resize();
	if (i < n/2) { // shift a[0],..,a[i-1] left one position
		j = (j == 0) ? a.length - 1 : j - 1;
		for (int k = 0; k <= i-1; k++)
			a[(j+k)%a.length] = a[(j+k+1)%a.length];
	} else { // shift a[i],..,a[n-1] right one position
		for (int k = n; k > i; k--)
			a[(j+k)%a.length] = a[(j+k-1)%a.length];
	}
	a[(j+i)%a.length] = x;
	n++;
}

template<class T>
T ArrayDeque<T>::remove(int i) {
    T x = a[(j+i)%a.length];
    if (i < n/2) { // shift a[0],..,[i-1] right one position
    	for (int k = i; k > 0; k--)
			a[(j+k)%a.length] = a[(j+k-1)%a.length];
		j = (j + 1) % a.length;
    } else { // shift a[i+1],..,a[n-1] left one position
		for (int k = i; k < n-1; k++)
			a[(j+k)%a.length] = a[(j+k+1)%a.length];
    }
    n--;
    if (3*n < a.length) resize();
    return x;
}

} /* namespace ods */


#endif /* ARRAYDEQUE_H_ */
