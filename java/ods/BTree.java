package ods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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
	 * The maximum number of children of a node (an odd number)
	 */
	int b;

    /** b div 2
     */
	int B; 
	
	/**
	 * Number of elements stored in the tree
	 */
	int n;
	
	/**
	 * The block storage mechanism
	 */
	BlockStore<Node> bs;
	
	/**
	 * The index of the root node
	 */
	int ri;
	
	/**
	 * Find the index, i, at which x should be inserted into the null-padded
	 * sorted array, a
	 * 
	 * @param a
	 *            the sorted array (padded with null entries)
	 * @param x
	 *            the value to search for
	 * @return i or -i-1 if a[i] equals x
	 */
	protected int findIt(T[] a, T x) {
		int lo = 0, hi = a.length;
		while (hi != lo) {
			int m = (hi+lo)/2;
			int cmp = a[m] == null ? -1 : c.compare(x, a[m]);
			if (cmp < 0)
				hi = m;      // look in first half
			else if (cmp > 0)
				lo = m+1;    // look in second half
			else
				return -m-1; // found it
		}
		return lo;
	}

	static class DuplicateValueException extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
	/**
	 * A node in a B-tree which has an array of up to b keys and up to b children
	 */
	protected class Node {
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
		 */
		public Node() {
			keys = f.newArray(b);
			children = new int[b+1];
			Arrays.fill(children, 0, children.length, -1);
			id = bs.placeBlock(this);
		}
		
		public boolean isLeaf() {
			return children[0] < 0;
		}

		/**
		 * Test if this block is full (contains b keys)
		 * 
		 * @return true if the block is full
		 */
		public boolean isFull() {
			return keys[keys.length-1] != null;
		}

		/**
		 * Count the number of keys in this block, using binary search
		 * 
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
		 * Add the value x to this block
		 * 
		 * @param x
		 *            the value to add
		 * @param ci
		 *            the index of the child associated with x
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
		 * Remove the i'th value from this block - don't affect this block's
		 * children
		 * 
		 * @param i
		 *            the index of the element to remove
		 * @return the value of the element removed
		 */
		public T remove(int i) {
			T y = keys[i];
			System.arraycopy(keys, i+1, keys, i, b-i-1);
			keys[keys.length-1] = null;
			return y;
		}
		
		/**
		 * Split this node into two nodes
		 * 
		 * @return the newly created block, which has the larger keys
		 */
		protected Node split() {
			Node w = new Node();
			int j = keys.length/2;
			System.arraycopy(keys, j, w.keys, 0, keys.length-j);
			Arrays.fill(keys, j, keys.length, null);
			System.arraycopy(children, j+1, w.children, 0, children.length-j-1);
			Arrays.fill(children, j+1, children.length, -1);
			bs.writeBlock(id, this);
			return w;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for (int i = 0; i < b; i++) {
				sb.append("(" + (children[i] < 0 ? "." : children[i]) + ")");
				sb.append(keys[i] == null ? "_" : keys[i].toString());
			}
			sb.append("(" + (children[b] < 0 ? "." : children[b]) + ")");
			return sb.toString();
		}
	}
	
	/**
	 * Construct an empty BTree that uses a DefaultComparator 
	 * @param b the block size
	 * @param clz the class of objects stored in this BTree
	 */
	public BTree(int b, Class<T> clz) {
		this(b, new DefaultComparator<T>(), clz);
	}
	
	/**
	 * Construct an empty BTree
	 * @param b the block size
	 * @param c the comparator to use
	 * @param clz the class of objects stored in this BTree
	 */
	public BTree(int b, Comparator<T> c, Class<T> clz) {
		this.c = c;
		b += 1 - (b % 2);
		this.b = b;
		B = b/2;
		f = new Factory<T>(clz);
		bs = new BlockStore<Node>();
		ri = new Node().id;
		n = 0;
	}
	
	public boolean add(T x) {
		Node w;
		try {
			w = addRecursive(x, ri);
		} catch (DuplicateValueException e) {
			return false;
		}
		if (w != null) {   // root was split, make new root
			Node newroot = new Node();
			x = w.remove(0);
			bs.writeBlock(w.id, w);
			newroot.children[0] = ri;
			newroot.keys[0] = x;
			newroot.children[1] = w.id;
			ri = newroot.id;
			bs.writeBlock(ri, newroot);
		}
		n++;
		return true;
	}
	
	/**
	 * Add the value x in the subtree rooted at the node with index ui
	 * 
	 * This method adds x into the subtree rooted at the node u whose index is
	 * ui. If u is split by this operation then the return value is the Node
	 * that was created when u was split
	 * 
	 * @param x
	 *            the element to add
	 * @param ui
	 *            the index of the node, u, at which to add x
	 * @return a new node that was created when u was split, or null if u was
	 *         not split
	 */
	protected Node addRecursive(T x, int ui) throws DuplicateValueException {
		Node u = bs.readBlock(ui);
		int i = findIt(u.keys, x);
		if (i < 0) throw new DuplicateValueException();
		if (u.children[i] < 0) { // leaf node, just add it
			u.add(x, -1);
			bs.writeBlock(u.id, u);
		} else {
			Node w = addRecursive(x, u.children[i]);
			if (w != null) {  // child was split, w is new child 
				x = w.remove(0);
				bs.writeBlock(w.id, w);
				u.add(x, w.id);
				bs.writeBlock(u.id, u);
			}
		}
		return u.isFull() ? u.split() : null;
	}
	
	public boolean remove(T x) {
		if (removeRecursive(x, ri)) {
			n--;
			Node r = bs.readBlock(ri);
			if (r.size() == 0 && n > 0) // root has only one child
				ri = r.children[0];  
			return true;
		}
		return false;
	}

	/**
	 * Remove the value x from the subtree rooted at the node with index ui
	 * 
	 * @param x
	 *            the value to remove
	 * @param ui
	 *            the index of the subtree to remove x from
	 * @return true if x was removed and false otherwise
	 */
	protected boolean removeRecursive(T x, int ui) {
		if (ui < 0) return false;  // didn't find it
		Node u = bs.readBlock(ui);
		int i = findIt(u.keys, x);
		if (i < 0) { // found it
			i = -(i+1);
			if (u.isLeaf()) {
				u.remove(i);
			} else {
				u.keys[i] = removeSmallest(u.children[i+1]);
				checkUnderflow(u, i+1);
			}
			return true;  
		} else if (removeRecursive(x, u.children[i])) {
			checkUnderflow(u, i);
			return true;
		}
		return false;
	}

	/**
	 * Remove the smallest value in the subtree rooted at the node with index ui
	 * 
	 * @param ui
	 *            the index of a subtree
	 * @return the value that was removed
	 */
	protected T removeSmallest(int ui) {
		Node u = bs.readBlock(ui);
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
	protected void checkUnderflow(Node u, int i) {
		if (u.children[i] < 0) return;
		if (i == 0) 
			checkUnderflowZero(u, i); // use u's right sibling
		else
			checkUnderflowNonZero(u,i);
	}
	
	protected void merge(Node u, int i, Node v, Node w) {
		Utils.myassert(v.id == u.children[i]);
		Utils.myassert(w.id == u.children[i+1]);
		int sv = v.size();
		int sw = w.size();
		// copy keys from w to v
		System.arraycopy(w.keys, 0, v.keys, sv+1, sw);
		System.arraycopy(w.children, 0, v.children, sv+1, sw+1);
		// add key to v and remove it from u
		v.keys[sv] = u.keys[i];
		System.arraycopy(u.keys, i+1, u.keys, i, b-i-1);
		u.keys[b-1] = null;
		System.arraycopy(u.children, i+2, u.children, i+1, b-i-1);
		u.children[b] = -1;
	}
	
	/**
	 * Check if an underflow has occured in the i'th child of u and, if so, fix
	 * it
	 * 
	 * @param u
	 *            a node
	 * @param i
	 *            the index of a child in u
	 */
	protected void checkUnderflowNonZero(Node u, int i) {
		Node w = bs.readBlock(u.children[i]);  // w is child of u
		if (w.size() < B-1) {  // underflow at w
			Node v = bs.readBlock(u.children[i-1]); // v left of w
			if (v.size() > B) {  // w can borrow from v
				shiftLR(u, i-1, v, w);
			} else { // v will absorb w
				merge(u, i-1, v, w);
			}
		}
	}
	
	/**
	 * Shift keys from v into w
	 * 
	 * @param u
	 *            the parent of v and w
	 * @param i
	 *            the index w in u.children
	 * @param v
	 *            the right sibling of w
	 * @param w
	 *            the left sibling of v
	 */
	protected void shiftLR(Node u, int i, Node v, Node w) {
		int sw = w.size();
		int sv = v.size();
		int shift = ((sw+sv)/2) - sw;  // num. keys to shift from v to w
		// make space for new keys in w
		System.arraycopy(w.keys, 0, w.keys, shift, sw);
		System.arraycopy(w.children, 0, w.children, shift, sw+1);
		// move keys and children out of v and into w (and u)
		w.keys[shift-1] = u.keys[i];
		u.keys[i] = v.keys[sv-shift];
		System.arraycopy(v.keys, sv-shift+1, w.keys, 0, shift-1);
		Arrays.fill(v.keys, sv-shift, sv, null);
		System.arraycopy(v.children, sv-shift+1, w.children, 0, shift);
		Arrays.fill(v.children, sv-shift+1, sv+1, -1);
	}

	
	protected void checkUnderflowZero(Node u, int i) {
		Node w = bs.readBlock(u.children[i]); // w is child of u
		if (w.size() < B-1) {  // underflow at w
			Node v = bs.readBlock(u.children[i+1]); // v right of w
			if (v.size() > B) { // w can borrow from v
				shiftRL(u, i, v, w);
			} else { // w will absorb w
				merge(u, i, w, v);
				u.children[i] = w.id;
			}
		}
	}

	/**
	 * Shift keys from node v into node w
	 * @param u the parent of v and w
	 * @param i the index w in u.children
	 * @param v the left sibling of w
	 * @param w the right sibling of v
	 */
	protected void shiftRL(Node u, int i, Node v, Node w) {
		int sw = w.size();
		int sv = v.size();
		int shift = ((sw+sv)/2) - sw;  // num. keys to shift from v to w
		// shift keys and children from v to w
		w.keys[sw] = u.keys[i];
		System.arraycopy(v.keys, 0, w.keys, sw+1, shift-1);
		System.arraycopy(v.children, 0, w.children, sw+1, shift);
		u.keys[i] = v.keys[shift-1];
		// delete keys and children from v
		System.arraycopy(v.keys, shift, v.keys, 0, b-shift);
		Arrays.fill(v.keys, sv-shift, b, null);
		System.arraycopy(v.children, shift, v.children, 0, b-shift+1);
		Arrays.fill(v.children, sv-shift+1, b+1, -1);
	}

	public void clear() {
		n = 0;
		bs.clear();
		ri = new Node().id;
	}

	public Comparator<? super T> comparator() {
		return c;
	}

	public T find(T x) {
		T z = null;
		int ui = ri;
		while (ui >= 0) {
			Node u = bs.readBlock(ui);
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
			Node u = bs.readBlock(ui);
			int i = findIt(u.keys, x);
			if (i < 0) i = -(i+1);
			if (i > 0)
				z = u.keys[i-1];
			ui = u.children[i];
		}
		return z;
	}
	
	protected class BTIterator implements Iterator<T> {
		protected List<Node> nstack;
		protected List<Integer> istack;

		public BTIterator() {
			nstack = new ArrayList<Node>(); // <Node>(Node.class);
			istack = new ArrayList<Integer>(); // <Integer>(Integer.class);
			if (n == 0) return;
			int ui = ri;		
			do {
				Node u = bs.readBlock(ui);
				nstack.add(u);
				istack.add(0);
				ui = u.children[0];
			} while (ui >= 0);
		}

		public BTIterator(T x) {
			Node u; int i;
			nstack = new ArrayList<Node>(); // <Node>(Node.class);
			istack = new ArrayList<Integer>(); // <Integer>(Integer.class);
			if (n == 0) return;
			int ui = ri;
			do {
				u = bs.readBlock(ui);
				i = findIt(u.keys, x);
				nstack.add(u);
				if (i < 0) {
					istack.add(-(i+1));
					return;
				}
				istack.add(i);
				ui = u.children[i];
			} while (ui >= 0);
			if (i == u.size())
				advance();
		}

		public boolean hasNext() {
			return !nstack.isEmpty();
		}

		public T next() {
			Node u = nstack.get(nstack.size()-1);
			int i = istack.get(istack.size()-1);
			T y = u.keys[i++];
			istack.set(istack.size()-1, i);
			advance();
			return y;
		}
		
		protected void advance() {
			Node u = nstack.get(nstack.size()-1);
			int i = istack.get(istack.size()-1);
			if (u.isLeaf()) { // this is a leaf, walk up
				while (!nstack.isEmpty() && i == u.size()) {
					nstack.remove(nstack.size()-1);
					istack.remove(istack.size()-1);
					if (!nstack.isEmpty()) {
						u = nstack.get(nstack.size()-1);
						i = istack.get(istack.size()-1);					
					}
				}
			} else { // this is an internal node, walk down
				int ui = u.children[i];
				do {
					u = bs.readBlock(ui);
					nstack.add(u);
					istack.add(0);
					ui = u.children[0];
				} while (ui >= 0);
			}
		}


		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	public Iterator<T> iterator(T x) {
		return new BTIterator(x);
	}

	public int size() {
		return n;
	}

	public Iterator<T> iterator() {
		return new BTIterator();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		toString(ri, sb);
		return sb.toString();
	}

	/**
	 * A recursive algorithm for converting this tree into a string
	 * 
	 * @param ui
	 *            the subtree to add to the the string
	 * @param sb
	 *            a StringBuffer for building the string
	 */
	public void toString(int ui, StringBuffer sb) {
		if (ui < 0) return;
		Node u = bs.readBlock(ui);
		int i = 0;
		while(i < b && u.keys[i] != null) {
			toString(u.children[i], sb);
			sb.append(u.keys[i++] + ", ");
		}
		toString(u.children[i], sb);
	}
	

	/**
	 * Simple test method
	 * @param args
	 */
	public static void main(String[] args) {
		int b = 60, n = 100000, c = 10, reps = 500;
		BTree<Integer> t = new BTree<Integer>(b, Integer.class);
		SortedSet<Integer> ss = new TreeSet<Integer>();
		for (int seed = 0; seed < reps; seed++) {
			System.out.println("Adding " + n + " elements");
			java.util.Random rand = new java.util.Random(seed);
			for (int i = 0; i < n; i++) {
				int x = rand.nextInt(c*n);
				Utils.myassert(t.add(x) == ss.add(x));
			}
			if (n <= 100) {
				System.out.println(t);
				for (Integer xx : t)
					System.out.print(xx + ", ");
				System.out.println();
				Iterator<Integer> it = t.iterator(c*n/2); 
				while (it.hasNext()) {
					System.out.print(it.next() + ", ");
				}
				System.out.println();
			}
			System.out.println("ss.size() = " + ss.size());
			System.out.println("t.size()  = " + t.size());
			
			System.out.println("Checking equality");
			for (int i = 0; i < n; i++) {
				int x = rand.nextInt(c*n);
				// System.out.println(t.findLT(x) + " < " + x + " <= " + t.find(x));
				Utils.myassert(Utils.equals(t.find(x),Utils.findGE(ss, x)));
				Utils.myassert(Utils.equals(t.findLT(x),Utils.findLT(ss, x)));
				// System.out.println(t + " (added " + x + ")");
			}
	
			System.out.println("Removing elements");
			for (int i = 0; i < 10*c*n; i++) {
				int x = rand.nextInt(c*n);
				Utils.myassert(t.remove(x) == ss.remove(x));
				// System.out.println(t + "(removed " + x + ")");
			}

			System.out.println("Checking equality");
			for (int i = 0; i < n; i++) {
				int x = rand.nextInt(c*n);
				// System.out.println(t.findLT(x) + " < " + x + " <= " + t.find(x));
				Utils.myassert(Utils.equals(t.find(x),Utils.findGE(ss, x)));
				Utils.myassert(Utils.equals(t.findLT(x),Utils.findLT(ss, x)));
				// System.out.println(t + " (added " + x + ")");
			}

			System.out.println("ss.size() = " + ss.size());
			System.out.println("t.size()  = " + t.size());
		}

	}
}
