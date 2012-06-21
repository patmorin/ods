package ods;

import java.util.ArrayList;
import java.util.List;

/**
 * This class fakes an external memory block storage system.  It's actually implemented as
 * a list of blocks.
 * @author morin
 *
 * @param <T>
 */
class BlockStore<T> {
	
	/**
	 * A list of blocks
	 */
	List<T> blocks;
	
	/**
	 * A list if available block indices (indices into the blocks list)
	 */
	List<Integer> free;

	/**
	 * Initialise a BlockStore with block size b
	 * @param b the block size
	 * @param clz needed so that we can allocate arrays of type T
	 */
	public BlockStore() {
		blocks = new ArrayList<T>();
		free = new ArrayList<Integer>();
	}
	
	public void clear() {
		blocks.clear();
		free.clear();		
	}
	
	/**
	 * Allocate a new block and return its index
	 * @return the index of the newly allocated block
	 */
	public int placeBlock(T block) {
		int i;
		if (!free.isEmpty()) {
			i = free.remove(free.size());
			blocks.set(i, block);
		} else { 
			i = blocks.size();
			blocks.add(i, block);
		}
		return i;
	}
	
	/**
	 * Free a block, adding its index to the free list
	 * @param i the block index to free
	 */
	public void freeBlock(int i) {
		blocks.set(i, null);
		free.add(i);
	}
	
	/**
	 * Read a block
	 * @param i the index of the block to read
	 * @return the block
	 */
	public T readBlock(int i) {
		return blocks.get(i);
	}
	
	/**
	 * Write a block
	 * @param i the index of the block
	 * @param block the block
	 */
	public void writeBlock(int i, T block) {
		blocks.set(i, block);
	}
}
