/*
 * RootishArrayStack.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef ROOTISHARRAYSTACK_H_
#define ROOTISHARRAYSTACK_H_
#include <math.h>
#include "ArrayStack.h"

namespace ods {

template<class T>
class RootishArrayStack {
protected:
	ArrayStack<T*> blocks;
	int n;

	int i2b(int i);
	void grow();
	void shrink();
public:
	RootishArrayStack();
	virtual ~RootishArrayStack();
	int size();
	T get(int i);
	T set(int i, T x);
	virtual void add(int i, T x);
	virtual T remove(int i);
};



template<class T> inline int RootishArrayStack<T>::size()
{
	return n;
}

template<class T> inline T RootishArrayStack<T>::get(int i)
{
    int b = i2b(i);
    int j = i - b*(b+1)/2;
    return blocks.get(b)[j];
}

template<class T> inline T RootishArrayStack<T>::set(int i, T x)
{
    int b = i2b(i);
    int j = i - b*(b+1)/2;
    T y = blocks.get(b)[j];
    blocks.get(b)[j] = x;
    return y;
}

template<class T> inline int RootishArrayStack<T>::i2b(int i)
{
    double db = (-3.0 + sqrt(9 + 8*i)) / 2.0;
    int b = (int)ceil(db);
    return b;
}


} /* namespace ods */
#endif /* ROOTISHARRAYSTACK_H_ */
