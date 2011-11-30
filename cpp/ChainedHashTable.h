/*
 * ChainedHashTable.h
 *
 *  Created on: 2011-11-30
 *      Author: morin
 */

#ifndef CHAINEDHASHTABLE_H_
#define CHAINEDHASHTABLE_H_
#include "array.h"
#include "ArrayStack.h"

namespace ods {

template<class T>
class ChainedHashTable {
protected:
	int n;
	int d;
	int z;
	static const int w = 32; //sizeof(int)*8;
	array<ArrayStack<T> > t;
	void allocTable(int m);
	void resize();
	int hash(T x) {
		return (z * x.hashCode()) >> (w-d);
	}

public:
	ChainedHashTable();
	virtual ~ChainedHashTable();
	bool add(T x);
	T remove(T x);
	T find(T x);
	int size() {
		return n;
	}
	void clear();
};

} /* namespace ods */
#endif /* CHAINEDHASHTABLE_H_ */
