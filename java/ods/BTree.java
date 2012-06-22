package ods;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

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
	 * Number of elements stored in the tree
	 */
	int n;
	
	/**
	 * The block storage mechanism
	 */
	BlockStore<Block> bs;
	
	/**
	 * The index of the root node
	 */
	int ri;
	
	/**
	 * Find the index, i, at which x should be inserted into the null-padded sorted array, a
	 * @param a the sorted array (padded with null entries)
	 * @param x the value to search for
	 * @return i or -i-1 if a[i] equals x
	 */
	protected int findIt(T[] a, T x) {
		int lo = 0, hi = a.length;
		while (hi != lo) {
			int m = (hi+lo)/2;
			int cmp = a[m] == null ? -1 : c.compare(x, a[m]);
			if (cmp < 0)
				hi = m;
			else if (cmp > 0)
				lo = m+1;
			else
				return -m-1;  // found it
		}
		return lo;
	}

	protected int findItSlow(T[] a, T x) {
		for (int i = 0; i < a.length; i++) {
			int cmp = a[i] == null ? 1 : c.compare(x, a[i]);
			if (cmp == 0) return -i-1;
			if (cmp > 0) return i;
		}
		return a.length;
	}
	
	/**
	 * A block (node) in a B-tree
	 * @author morin
	 *return u.data[-(i+1)];
	 */
	protected class Block {
		/**
		 * This block's index
		 */
		int id;
		
		/**
		 * The keys stored in this block
		 */
		T[] keys;
		
		/**
		 * The indicies of the children of this block (if any)
		 */
		int[] children;
		
		/**
		 * Constructor
		 * @param leaf set to true if this block is a leaf
		 */
		public Block() {
			keys = f.newArray(b);
			children = new int[b+1];
			Arrays.fill(children, 0, children.length, -1);
			id = bs.placeBlock(this);
		}
		
		/**
		 * Add the value x to this block
		 * @param x the value to add
		 * @param ci the index of the child associated with x
		 * @return true on success or false if x was not added
		 */
		public boolean add(T x, int ci) {
			int i = findIt(keys, x);
			if (i < 0) return false;
			if (i < keys.length-1) System.arraycopy(keys, i, keys, i+1, b-i-1);
			keys[i] = x;
			if (i < keys.length-1) System.arraycopy(children, i+1, children, i+2, b-i-1);
			children[i+1] = ci;
			return true;
		}
		
		/**
		 * Remove the key x from this block if its exists - don't affect the children
		 * @param x the element to remove
		 * @return the value of the element removed
		 */
		public T removeKey(T x) {
			int i = findIt(keys, x);
			if (i >= 0) return null;
			i = -(i+1);
			return remove(-(i+1));
		}
		
		/**
		 * Remove the i'th value from this block - don't affect this block's children
		 * @param i the index of the element to remove
		 * @return the value of the element removed
		 */
		public T remove(int i) {
			T y = keys[i];
			if (y == null) System.out.println("Poop");
			System.arraycopy(keys, i+1, keys, i, b-i-1);
			keys[keys.length-1] = null;
			// System.arraycopy(children, i+2, children, i+1, b-i-1);
			return y;
		}

		public T remove2(int i) {
			T y = keys[i];
			if (y == null) System.out.println("Poop");
			System.arraycopy(keys, i+1, keys, i, b-i-1);
			keys[keys.length-1] = null;
			System.arraycopy(children, i+2, children, i+1, b-i-1);
			return y;
		}

		
		/**
		 * Test if this block is full (contains b keys)
		 * @return true if the block is full
		 */
		public boolean isFull() {
			return keys[keys.length-1] != null;
		}
		
		/**
		 * Count the number of keys in this block
		 * @return the number of keys in this block
		 */
		public int size() {
			int lo = 0, h = keys.length;
			while (h != lo) {
				int m = (h+lo)/2;
				if (keys[m] == null)
					h = m;
				else
					lo = m+1;
			}
			return lo;
		}

		/**
		 * Split routine 
		 * @return the newly created block
		 */
		protected Block split() {
			Block w = new Block();
			int j = keys.length/2;
			System.arraycopy(keys, j, w.keys, 0, keys.length-j);
			Arrays.fill(keys, j, keys.length, null);
			System.arraycopy(children, j+1, w.children, 0, children.length-j-1);
			Arrays.fill(children, j+1, children.length, -1);
			return w;
		}
		
		/**
		 * Absorb the data and children from this node's right sibling, w 
		 * @param w the node to absorb
		 * @param a value that splits this and w
		 */
		protected void absorb(Block w, T x) {
			int k = size();
			keys[k] = x;
			System.arraycopy(w.keys, 0, keys, k+1, w.size());
			System.arraycopy(w.children, 0, children, k+1, w.size()+1);
		}
		
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for (int i = 0; i < b; i++) {
				sb.append("(" + (children[i] < 0 ? "." : children[i]) + ")");
				sb.append(keys[i] == null ? "_" : keys[i].toString());
//				if (i < b-1)
//					sb.append(",");
			}
			sb.append("(" + (children[b] < 0 ? "." : children[b]) + ")");
			return sb.toString();
		}
		
		public boolean isLeaf() {
			return children[0] < 0;
		}
	}
	
	public BTree(int b, Class<T> clz) {
		this(b, new DefaultComparator<T>(), clz);
	}
	
	public BTree(int b, Comparator<T> c, Class<T> clz) {
		this.c = c;
		b += 1 - (b % 2);
		this.b = b;
		f = new Factory<T>(clz);
		bs = new BlockStore<Block>();
		ri = new Block().id;
	}
	
	public boolean add(T x) {
		Block w;
		try {
			w = addRecursive(x, ri);
		} catch (RuntimeException e) {
			return false;
		}
		if (w != null) {   // root was split
			Block newroot = new Block();
			x = w.remove(0);
			newroot.children[0] = ri;
			newroot.keys[0] = x;
			newroot.children[1] = w.id;
			ri = newroot.id;
		}
		n++;
		return true;
	}
	
	protected Block addRecursive(T x, int ui) {
		Block u = bs.readBlock(ui);
		int i = findIt(u.keys, x);
		if (i < 0) throw new RuntimeException("");
		if (u.children[i] < 0) {
			u.add(x, -1);
		} else {
			Block w = addRecursive(x, u.children[i]);
			if (w != null) {
				x = w.remove(0);
				u.add(x, w.id);
			}
		}
		return u.isFull() ? u.split() : null;
	}
	
	public boolean remove(T x) {
		T y = removeRecursive(x, ri);
		if (y != null) {
			Block r = bs.readBlock(ri);
			if (r.size() == 0) // root has only one child
				ri = r.children[0];  
			n--;
			return true;
		}
		return false;
	}
	
	protected T removeRecursive(T x, int ui) {
		if (ui < 0) return null;
		Block u = bs.readBlock(ui);
		int i = findIt(u.keys, x);
		if (i < 0) { // found it
			i = -(i+1);
			if (u.isLeaf()) {
				return u.remove(i);
			} else {
				T y = u.keys[i];
				u.keys[i] = removeSmallest(u.children[i+1]);
				checkUnderflow(u, i+1);
				return y;  
			}
		}
		T y = removeRecursive(x, u.children[i]); // FIXME: check merge
		checkUnderflow(u, i);
		return y;
	}

	/**
	 * Remove the smallest value in the subtree rooted at ui
	 * @param ui
	 * @return the value that was removed
	 */
	protected T removeSmallest(int ui) {
		Block u = bs.readBlock(ui);
		if (u.isLeaf()) 
			return u.remove(0);
		T y = removeSmallest(u.children[0]);  
		checkUnderflow(u, 0);
		return y;
	}

	/**
	 * Check if an underflow has occurred in the i'th child of u and, if so, fix it 
	 * by borrowing from or merging with a sibling
	 * @param u 
	 * @param i
	 */
	protected void checkUnderflow(Block u, int i) {
		if (u.children[i] < 0) return;
		if (i == 0) 
			checkUnderflowEven(u, i);
		else
			checkUnderflowOdd(u,i);
	}
	
	/**
	 * Check if an underflow has occured in the i'th child of u
	 * @param u
	 * @param i
	 */
	protected void checkUnderflowOdd(Block u, int i) {
		Block w = bs.readBlock(u.children[i]);  // w is child of u
		if (w.size() < b/2) {  // underflow at w
			Block v = bs.readBlock(u.children[i-1]);  // v is left sibling of w
			int sv = v.size();
			if (sv > b/2) {  // we can borrow from v
				// System.out.println(v + " is lending to " + w + " [parent is " + u + "]");
				System.arraycopy(w.keys, 0, w.keys, 1, w.size());
				w.keys[0] = u.keys[i-1];
				System.arraycopy(w.children, 0, w.children, 1, w.size()+1);
				w.children[0] = v.children[sv];
				u.keys[i-1] = v.remove2(sv-1);
				// System.out.println(v + "  splits now   " + w + " [parent is " + u + "]");
			} else { // we have to merge
				// System.out.println(v + " is absorbing  " + w + " [parent is " + u + "]");
				v.absorb(w, u.keys[i-1]);
				u.remove2(i-1);
				// System.out.println(v + "    absorbed   " + w + " [parent is " + u + "]");
			}
		}
	}
	
	protected void checkUnderflowEven(Block u, int i) {
		Block w = bs.readBlock(u.children[i]); // w is child of u
		int sw = w.size();
		if (sw < b/2) {  // underflow at w
			Block v = bs.readBlock(u.children[i+1]);  // v is right sibling of w
			int sv = v.size();
			if (sv > b/2) { // we can borrow
				// System.out.println(w + " is borrowing from " + v + " [parent is " + u + "]");
				// borrow a child from v and key from v (through u)
				w.keys[sw] = u.keys[i];
				w.children[sw+1] = v.children[0];
				u.keys[i] = v.keys[0];
				// delete key from w
				System.arraycopy(v.keys, 1, v.keys, 0, b-1);
				System.arraycopy(v.children, 1, v.children, 0, b-1);
				// System.out.println(w + "    splits now   " + v + " [parent is " + u + "]");
			} else { // we have to merge
				// System.out.println(w + " being absorbed by " + v + " [parent is " + u + "]");
				// copy keys and children from w
				System.arraycopy(v.keys, 0, v.keys, sw+1, b-sw-1);
				System.arraycopy(w.keys, 0, v.keys, 0, sw);
				System.arraycopy(v.children, 0, v.children, sw+1, b-sw-1);
				System.arraycopy(w.children, 0, v.children, 0, sw+1);
				v.keys[sw] = u.keys[i];  // take key from u
				// delete key from u
				System.arraycopy(u.keys, i+1, u.keys, i, b-i-1);
				System.arraycopy(u.children, i+1, u.children, i, b-i);
				// System.out.println(w + " was absorbed by  " + v + " [parent is " + u + "]");
			}
		}
		
	}

	
	public void clear() {
		n = 0;
		bs.clear();
		ri = new Block().id;
	}

	public Comparator<? super T> comparator() {
		return c;
	}

	public T find(T x) {
		T z = null;
		int ui = ri;
		while (ui >= 0) {
			Block u = bs.readBlock(ui);
			int i = findIt(u.keys, x);
			if (i < 0) return u.keys[-(i+1)]; // found it
			if (u.keys[i] != null)
				z = u.keys[i];
			ui = u.children[i];
		}
		return z;
	}

	public T findGE(T x) {
		return find(x);
	}

	public T findLT(T x) {
		T z = null;
		int ui = ri;
		while (ui >= 0) {
			Block u = bs.readBlock(ui);
			int i = findIt(u.keys, x);
			if (i < 0) i = -(i+1);
			if (i > 0)
				z = u.keys[i-1];
			ui = u.children[i];
		}
		return z;
	}

	@Override
	public Iterator<T> iterator(T x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return n;
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
		Block u = bs.readBlock(ui);
		int i = 0;
		while(i < b && u.keys[i] != null) {
			if (u.children[i] >= 0) {
				sb.append("(");
				toString(u.children[i], sb);
				sb.append(")");
			}
			sb.append(u.keys[i++] + ",");
		}
		if (u.children[i] >= 0) {
			sb.append("(");
			toString(u.children[i], sb);
			sb.append(")");
		}
	}
	
	protected static <T> T findGE(SortedSet<T> ss, T x) {
		SortedSet<T> p = ss.tailSet(x);
		return p.isEmpty() ? null : p.first();
	}
	
	protected static <T> T findLT(SortedSet<T> ss, T x) {
		SortedSet<T> p = ss.headSet(x);
		return p.isEmpty() ? null : p.last();
	}

	protected static boolean equals(Object a, Object b) {
		return (a == null && b == null) || a.equals(b);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int b = 6, n = 100000, c = 10, reps = 500;
		BTree<Integer> t = new BTree<Integer>(b, Integer.class);
		SortedSet<Integer> ss = new TreeSet<Integer>();
		for (int seed = 0; seed < reps; seed++) {
			java.util.Random rand = new java.util.Random(seed);
			for (int i = 0; i < n; i++) {
				int x = rand.nextInt(c*n);
				Utils.myassert(t.add(x) == ss.add(x));
			}
			if (n <= 100)
				System.out.println(t);
			System.out.println("ss.size() = " + ss.size());
			System.out.println("t.size()  = " + t.size());
			
			for (int i = 0; i < n; i++) {
				int x = rand.nextInt(c*n);
				// System.out.println(t.findLT(x) + " < " + x + " <= " + t.find(x));
				Utils.myassert(equals(t.find(x),findGE(ss, x)));
				Utils.myassert(equals(t.findLT(x),findLT(ss, x)));
				// System.out.println(t + " (added " + x + ")");
			}
	
			for (int i = 0; i < c*n; i++) {
				int x = rand.nextInt(c*n);
				Utils.myassert(t.remove(x) == ss.remove(x));
				// System.out.println(t + "(removed " + x + ")");
			}
			
			System.out.println("ss.size() = " + ss.size());
			System.out.println("t.size()  = " + t.size());
		}

	}
}
