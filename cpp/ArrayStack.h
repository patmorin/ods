/*
 * ArrayStack.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef ARRAYSTACK_H_
#define ARRAYSTACK_H_

namespace ods {

template<class T>
class DualArrayDeque;

template<class T>
class ArrayStack {
protected:
	friend class DualArrayDeque<T>;
	T *a;
	int length;
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


} /* namespace ods */

#endif /* ARRAYSTACK_H_ */
