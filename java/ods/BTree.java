package ods;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;

/**
 * An implementation of a B Tree
 * @author morin
 *
 * @param <T>
 */
public class BTree<T> implements SSet<T> {

	Factory<T> f;
	protected Comparator<T> c;

	/**
	 * The block size
	 */
	int b;
	
	/**
	 * The height of the tree
	 */
	int h;
	
	/**
	 * The block storage mechanism
	 */
	BlockStore<Block> bs;
	
	/**
	 * The index of the root node
	 */
	int ri;
	
	/**
	 * Find the index at which x should be inserted into the sorted array, a
	 * @param a
	 * @param x
	 * @return
	 */
	protected int findIt(T[] a, T x) {
		for (int i = 0; i < a.length; i++) {
			T z = a[i];
			if (z == null || c.compare(x, z) <= 0) return i;
		}
		return a.length;
	}
	
	/**
	 * A block (node) in a B-tree
	 * @author morin
	 *
	 */
	protected class Block {
		/**
		 * This block's index
		 */
		int id;
		
		/**
		 * The keys stored in this block
		 */
		T[] data;
		
		/**
		 * The indicies of the children of this block (if any)
		 */
		int[] children;
		
		/**
		 * Constructor
		 * @param leaf set to true if this block is a leaf
		 */
		public Block() {
			data = f.newArray(b);
			children = new int[b+1];
			Arrays.fill(children, 0, children.length, -1);
			id = bs.placeBlock(this);
		}
		
		/**
		 * Remove the first key from this block (used after a split)
		 */
		public T removeFirst() {
			T x = data[0];
			System.arraycopy(data, 1, data, 0, data.length-1);
			data[data.length-1] = null;
			return x;
		}
		
		/**
		 * Add the value x to this block
		 * @param x the value to add
		 * @param ci the index of the child associated with x
		 * @return true on success or false if x was not added
		 */
		public boolean add(T x, int ci) {
			int i = findIt(data, x);
			if (data[i] != null && c.compare(x, data[i]) == 0) return false;
			if (i < data.length-1) System.arraycopy(data, i, data, i+1, b-i-1);
			data[i] = x;
			if (i < data.length-1) System.arraycopy(children, i+1, children, i+2, b-i-1);
			children[i+1] = ci;
			return true;
		}

		/**
		 * Remove the value x from this block
		 * @param x
		 * @return
		 */
		public boolean remove(T x) {
			int i = findIt(data, x);
			if (i > b-1 || data[i] == null || c.compare(x, data[i]) != 0) return false;
			System.arraycopy(data, i+1, data, i, b-i-2);
			data[data.length-1] = null;
			return true;
		}
		
		/**
		 * Test if this block is full (contains b keys)
		 * @return true if the block is full
		 */
		public boolean isFull() {
			return data[data.length-1] != null;
		}
		
		/**
		 * Count the number of keys in this block
		 * FIXME: Could be made to run O(log b) time
		 * @return
		 */
		public int size() {
			for (int i = 0; i < b; i++) 
				if (data[i] == null) return i;
			return b;
		}
		
		/**
		 * Split routine 
		 * @return the newly created block
		 */
		protected Block split() {
			System.out.print("split: " + this + " => ");
			Block block2 = new Block();
			int j = data.length/2;
			System.arraycopy(data, j, block2.data, 0, data.length-j);
			Arrays.fill(data, j, data.length, null);
			System.arraycopy(children, j+1, block2.children, 0, children.length-j-1);
			Arrays.fill(children, j+1, children.length, -1);
			System.out.println(this + " , " + block2);
			return block2;
		}
		
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for (int i = 0; i < b; i++) {
				sb.append(data[i] == null ? "_" : data[i].toString());
				if (i < b-1)
					sb.append(",");
			}
			sb.append("]");
			return sb.toString();
		}
	}
	
	public BTree(int b, Class<T> clz) {
		this(b, new DefaultComparator<T>(), clz);
	}
	
	public BTree(int b, Comparator<T> c, Class<T> clz) {
		this.c = c;
		this.b = b;
		f = new Factory<T>(clz);
		bs = new BlockStore<Block>();
		ri = new Block().id;
	}
	
	public boolean add(T x) {
		Block w = addRecursive(x, ri);
		if (w != null) {   // root was split
			Block newroot = new Block();
			x = w.removeFirst();
			newroot.children[0] = ri;
			newroot.data[0] = x;
			newroot.children[1] = w.id;
			ri = newroot.id;
			h++;
		}
		return true;
	}
	
	protected Block addRecursive(T x, int ui) {
		Block u = bs.readBlock(ui);
		int i = findIt(u.data, x);
		if (i < b && u.data[i] != null && c.compare(x, u.data[i]))
			throw new RuntimeException("");
		if (u.children[i] < 0) {
			u.add(x, -1);
		} else {
			Block w = addRecursive(x, u.children[i]);
			if (w != null) {
				x = w.removeFirst();
				u.add(x, w.id);
			}
		}
		return u.isFull() ? u.split() : null;
	}
	
	/**
	 * Add x to the block with index bi
	 * @param bi
	 * @param x
	 */
//	public void addWithSplits(int bi, T x, int ci) {
//		Block block = bs.readBlock(bi);
//		block.add(x, ci);
//		if (block.isFull()) {
//			x = block.data[b/2];
//			Block block2 = block.split();
//			if (block.parent < 0) {  // root was split, make a new root
//				Block newroot = new Block();
//				newroot.children[0] = ri;
//				ri = block.parent = newroot.id;
//				h++;
//			}
//			block2.parent = block.parent;
//			addWithSplits(block2.parent, x, block2.id);
//		}
//	}

	@Override
	public boolean remove(T x) {
		return false;
	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public Comparator<? super T> comparator() {
		return c;
	}

	@Override
	public T find(T x) {
		T z = null;
		int ui = ri;
		for (int j = 0; j <= h; j++) {
			Block block = bs.readBlock(ui);
			int i = findIt(block.data, x);
			if (i < b && block.data[i] != null)
				z = block.data[i];
			ui = block.children[i];
		}
		System.out.println();
		return z;
	}

	@Override
	public T findGE(T x) {
		return find(x);
	}

	@Override
	public T findLT(T x) {
		T z = null;
		int ui = ri;
		for (int j = 0; j <= h; j++) {
			Block block = bs.readBlock(ui);
			int i = findIt(block.data, x);
			if (i > 0)
				z = block.data[i-1];
			ui = block.children[i];
		}
		System.out.println();
		return z;
	}

	@Override
	public Iterator<T> iterator(T x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		toString(ri, sb);
		return sb.toString();
	}

	public void toString(int ui, StringBuffer sb) {
		if (ui < 0) return;
		Block block = bs.readBlock(ui);
		int i = 0;
		while(i < b && block.data[i] != null) {
			if (block.children[i] >= 0) {
				sb.append("(");
				toString(block.children[i], sb);
				sb.append(")");
			}
			sb.append(block.data[i++] + ",");
		}
		if (block.children[i] >= 0) {
			sb.append("(");
			toString(block.children[i], sb);
			sb.append(")");
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BTree<Integer> t = new BTree<Integer>(4, Integer.class);
		System.out.println(t);
		int n = 100;
		java.util.Random rand = new java.util.Random(0);
		for (int i = 0; i < n; i++) {
			int x = rand.nextInt(2*n*n);
			t.add(x);
			// System.out.println(t + " (added " + x + ")");
		}
		System.out.println(t);

		for (int i = 0; i < n; i++) {
			int x = rand.nextInt(2*n*n);
			System.out.println(t.findLT(x) + " < " + x + " <= " + t.find(x));
			// System.out.println(t + " (added " + x + ")");
		}
	
	}

}
