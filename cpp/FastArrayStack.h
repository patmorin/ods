/*
 * FastArrayStack.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef FASTARRAYSTACK_H_
#define FASTARRAYSTACK_H_

#include "ArrayStack.h"

namespace ods {

template<class T>
class FastArrayStack : public ArrayStack<T> {
protected:
	using ArrayStack<T>::a;
	using ArrayStack<T>::n;
	virtual void resize();
public:
	virtual ~FastArrayStack();
	FastArrayStack();
	virtual void add(int i, T x);
	virtual T remove(int i);
};

template<class T>
FastArrayStack<T>::FastArrayStack() : ArrayStack<T>() {
}

template<class T>
FastArrayStack<T>::~FastArrayStack() {
}

template<class T>
void FastArrayStack<T>::resize() {
	array<T> b(max(1, 2*n));
	memcpy(b+0, a+0, n*sizeof(T));
	a = b;
}

template<class T>
void FastArrayStack<T>::add(int i, T x) {
	if (n + 1 > a.length) resize();
	memcpy(a + i + 1, a + i, (n-i)*sizeof(T));
	a[i] = x;
	n++;
}

template<class T>
T FastArrayStack<T>::remove(int i)
{
    T x = a[i];
	memcpy(a + i, a + i - 1, (n-i-1)*sizeof(T));
	n--;
	if (a.length >= 3 * n) resize();
	return x;
}


} /* namespace ods */
#endif /* FASTARRAYSTACK_H_ */
