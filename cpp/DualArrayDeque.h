/*
 * DualArrayDeque.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef DUALARRAYDEQUE_H_
#define DUALARRAYDEQUE_H_
#include "ArrayStack.h"

namespace ods {

template<class T>
class DualArrayDeque {
protected:
	ArrayStack<T> front;
	ArrayStack<T> back;
	void balance();
public:
	DualArrayDeque();
	virtual ~DualArrayDeque();
	int size();
	T get(int i);
	T set(int i, T x);
	virtual void add(int i, T x);
	virtual T remove(int i);
};

template<class T> inline
T DualArrayDeque<T>::get(int i) {
	if (i < front.size()) {
		return front.get(front.size() - i - 1);
	} else {
		return back.get(i - front.size());
	}
}

template<class T> inline
T DualArrayDeque<T>::set(int i, T x) {
	if (i < front.size()) {
		return front.set(front.size() - i - 1, x);

	} else {
		return back.set(i - front.size(), x);
	}
}

} /* namespace ods */
#endif /* DUALARRAYDEQUE_H_ */
