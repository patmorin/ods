package ods;

import java.util.Random;

/**
 * A partial implementation of van Emde Boas trees once used on an assignment.
 * Stored here for safekeeping, but not really part of the ods distribution.
 * @author morin
 *
 */
public class VEBTree {
	protected int n;
	protected int w;
	protected Node r;

	protected class Node {
		public int min, max;
		public Node[] children;
		public Node aux;
	}
	
	/**
	 * The number of bits used in the aux tree of a w-bit node
	 * @param w
	 * @return ceiling(w/2)
	 */
	protected final static int auxW(int w) {
		return (w+1)/2;
	}

	/**
	 * The number of bits used in each child of a w-bit node
	 * @param w
	 * @return floor(w/2)
	 */
	protected final static int childW(int w) {
		return w/2;
	}
	
	/**
	 * Return the index of the child that contains i
	 * @param x
	 * @param w
	 * @return
	 */
	protected final static int hibits(int x, int w) {
		return x >>> childW(w);
	}
	
	protected final static int lobits(int x, int w) {
		return x & ((1<<childW(w)) - 1);
	}

	public VEBTree(int w) {
		this.w = w;
		r = initialize(w);
	}
	
	protected Node initialize(int w) {
		if (w == 0) return null;
		Node v = new Node();
		v.max = -1;
		v.min = 1 << w;
		if (w == 1) return v;
		int aw = auxW(w);
		int cw = childW(w);
		int nc = 1 << aw;     // number of children
		v.children = new Node[nc];
		for (int i = 0; i < nc; i++)
			v.children[i] = initialize(cw);
		v.aux = initialize(aw);
		return v;
	}

	/**
	 * Remove the value x from this tree, if x is in the tree
	 * @param x
	 * @return true if x was removed and false otherwise
	 */
	public boolean remove(int x) {
		if (x < 0 || x >= 1 << w) 
			throw new RuntimeException("Invalid argument");
		// TODO: implement this
		return false;
	}
	
	/**
	 * Add the value x to this tree, if it is not already there
	 * @param x
	 * @return true if x was added and false otherwise
	 */
	public boolean add(int x) {
		if (x < 0 || x >= 1 << w) 
			throw new RuntimeException("Invalid argument");
		if (add(x, r, w)) {
			n++;
			return true;
		}
		return false;
	}
	
	protected boolean add(int x, Node v, int w) {
		if (v.max < v.min) {
			v.max = x;
			v.min = x;
			return true;
		}
		if (v.max == v.min) {
			if (x < v.min) {
				v.min = x;
				return true;
			}
			if (x > v.max) {
				v.max = x;
				return true;
			}
			return false;
		}
		if (x < v.min) {
			int tmp = v.min;
			v.min = x;
			x = tmp;
		}
		if (x > v.max) {
			int tmp = v.max;
			v.max = x;
			x = tmp;
		}
		int cw = childW(w);
		int aw = auxW(w);
		int j = hibits(x, w);
		int xx = lobits(x, w);
		if (add(xx, v.children[j], cw)) {
			n++;
			if (v.children[j].min == v.children[j].max) 
				add(j, v.aux, aw);
			return true;
		}
		return false;
	}
	
	public int find(int x) {
		if (x < 0 || x >= 1 << w) 
			throw new RuntimeException("Invalid argument");
		
		return find(x, r, w);
	}
	
	protected int find(int x, Node v, int w) {
		if (x > v.max) return -1;
		if (x == v.max) return v.max; // necessary for when w=1
		if (x <= v.min) return v.min;
		int cw = childW(w);
		int aw = auxW(w);
		int j = hibits(x, w);
		int xx = lobits(x, w);
		int s = find(xx, v.children[j], cw); // search child[j]
		if (s != -1) 
			return (j << cw) | s;  // success
		j = find(j+1, v.aux, aw);  // no luck - search in aux instead
		if (j != -1)
			return (j << cw) | v.children[j].min;
		return v.max;              // no luck yet - it must be v.max
	}
	
	/**
	 * Find the predecessor of x, i.e., the largest value that is less than x
	 * @param x
	 * @return the largest value stored in the tree that is less than x
	 */
	public int findPred(int x) {
		// TODO: implement this
		return -1;
	}

	
	public static void main(String[] args) {
		int w = 5;
		int n = 1 << w;
		VEBTree t = new VEBTree(w);
		Random rand = new Random();
		for (int i = 0; i < n/4; i++) {
			t.add(rand.nextInt(n));
		}
		for (int i = 0; i < n; i++) {
			System.out.println(i + "=>" + t.find(i));
		}
	}
	
	
}
