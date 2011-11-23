/*
 * ArrayQueue.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef ARRAYQUEUE_H_
#define ARRAYQUEUE_H_

namespace ods {

template<class T>
class ArrayQueue {
protected:
	T *a;
	int j;
	int length;
	int n;
	void resize();
public:
	ArrayQueue();
	virtual ~ArrayQueue();
	virtual bool add(T x);
	virtual T remove();
	int size();
};

} /* namespace ods */
#endif /* ARRAYQUEUE_H_ */
