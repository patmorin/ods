/*
 * ArrayStack.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef ARRAYSTACK_H_
#define ARRAYSTACK_H_
#include "array.h"

namespace ods {

template<class T>
class DualArrayDeque;

template<class T>
class ArrayStack {
protected:
	friend class DualArrayDeque<T>;
	array<T> a;
	int n;
	virtual void resize();
public:
	ArrayStack();
	virtual ~ArrayStack();
	int size();
	T get(int i);
	T set(int i, T x);
	virtual void add(int i, T x);
	virtual T remove(int i);
	virtual void clear();
};

template<class T> inline
int ArrayStack<T>::size() {
	return n;
}

template<class T> inline
T ArrayStack<T>::get(int i)
{
	return a[i];
}

template<class T> inline
T ArrayStack<T>::set(int i, T x)
{
	T y = a[i];
	a[i] = x;
	return y;
}

template<class T>
void ods::ArrayStack<T>::clear() {
	n = 0;
	array<T> b(1);
	a = b;
}


} /* namespace ods */

#endif /* ARRAYSTACK_H_ */
