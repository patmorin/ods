/*
 * BlockStore.h
 *
 *  Created on: 2013-07-03
 *      Author: morin
 */

#ifndef BLOCKSTORE_H_
#define BLOCKSTORE_H_

#include "ArrayStack.h"

namespace ods {

/**
 * This class fakes an external memory block storage system. It's just a list
 * of blocks.
 */
template<class T>
class BlockStore {
protected:
	ArrayStack<T> blocks;      // list of blocks
	ArrayStack<unsigned> free; // unused block indices
public:
	BlockStore() : blocks(), free() {	}
	virtual void clear() {
		blocks.clear();
		free.clear();
	}
	virtual ~BlockStore() {
		clear();
	}
	virtual int placeBlock(T block) {
		int i;
		if (free.size() > 0) {
			i = free.remove(free.size());
		} else {
			i = blocks.size();
			blocks.add(i, block);
		}
		return i;
	}
	virtual void freeBlock(int i) {
		blocks.set(i, NULL);
		free.add(i);
	}
	virtual T readBlock(int i) {
		return blocks.get(i);
	}
	void writeBlock(int i, T block) {
		blocks.set(i, block);
	}
};

} /* namespace ods */
#endif /* BLOCKSTORE_H_ */
