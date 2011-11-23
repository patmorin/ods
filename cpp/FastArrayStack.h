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

template<typename T>
class FastArrayStack : public ArrayStack<T> {
protected:
	using ArrayStack<T>::a;
	using ArrayStack<T>::length;
	using ArrayStack<T>::n;
	virtual void resize();
public:
	virtual ~FastArrayStack();
	FastArrayStack();
	virtual void add(int i, T x);
	virtual T remove(int i);
};



} /* namespace ods */
#endif /* FASTARRAYSTACK_H_ */
