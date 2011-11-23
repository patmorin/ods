/*
 * ArrayDeque.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef ARRAYDEQUE_H_
#define ARRAYDEQUE_H_

namespace ods {

template<class T>
class ArrayDeque {
protected:
	T *a;
	int j;
	int length;
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
};

template<class T> inline T ods::ArrayDeque<T>::get(int i)
{
    return a[(j+i)%length];
}

template<class T> inline T ods::ArrayDeque<T>::set(int i, T x)
{
    T y = a[(j+i)%length];
    a[(j+i)%length] = x;
    return y;
}

} /* namespace ods */
#endif /* ARRAYDEQUE_H_ */
