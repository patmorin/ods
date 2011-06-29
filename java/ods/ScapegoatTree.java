package ods;

import java.lang.reflect.Array;


public class ScapegoatTree<T extends Comparable<T>> 
		extends BinarySearchTree<ScapegoatNode<T>,T> {
	/**
	 * An overestimate of the number of n
	 */
	int q;
	
	public ScapegoatTree() {
		super(new ScapegoatNode<T>());
	}
	
	public ScapegoatTree(ScapegoatNode<T> is) {
		super(is);
	}
	
	public int size() {
		return n;
	}
	
	public boolean remove(T x) {
		if (super.remove(x)) {
			if (2*n < q) {
				rebuild(r);
				q = n;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Compute the ceiling of log_{3/2}(q)
	 * @param q
	 * @return the ceiling of log_{3/2}(q)
	 */
	protected static final int log32(int q) {
		final double log23 = 2.4663034623764317;
		return (int)Math.ceil(log23*Math.log(q));
	}
		
	public boolean add(T x) {
		// first do basic insertion keeping track of depth
		ScapegoatNode<T> u = newNode();
		u.x = x;
		int d = 0;
		ScapegoatNode<T> w = r;
		if (w == null) {
			r = u;
			n++; q++;
			return true;
		}
		boolean done = false;
		while (!done) {
			int res = x.compareTo(w.x);
			if (res < 0) {
				if (w.left == null) {
					w.left = u;
					u.parent = w;
					done = true;
				} else {
					w = w.left;
				}
			} else if (res > 0) {
				if (w.right == null) {
					w.right = u;
					u.parent = w;
					done = true;
				}
				w = w.right;
			} else {
				return false;
			}
			d++;
		}
		n++; q++;
		if (d > log32(q)) {
			// depth exceeded, find scapegoat
			while (3*size(w) <= 2*size(w.parent))
				w = w.parent;
			rebuild(w.parent);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	protected void rebuild(ScapegoatNode<T> u) {
		int ns = size(u);
		ScapegoatNode<T> p = u.parent;
		ScapegoatNode<T>[] a = (ScapegoatNode<T>[]) Array.newInstance(
				sampleNode.getClass(), ns);
		packIntoArray(u, a, 0);
		if (p == null) {
			r = buildBalanced(a, 0, ns);
			r.parent = null;
		} else if (p.right == u) {
			p.right = buildBalanced(a, 0, ns);
			p.right.parent = p;
		} else {
			p.left = buildBalanced(a, 0, ns);
			p.left.parent = p;
		}
	}

	/**
	 * A recursive helper that packs the subtree rooted at u into
	 * a[i],...,a[i+size(u)-1]
	 * 
	 * @param u
	 * @param a
	 * @param i
	 * @return size(u)
	 */
	protected int packIntoArray(ScapegoatNode<T> u, ScapegoatNode<T>[] a, int i) {
		if (u == null) {
			return i;
		}
		i = packIntoArray(u.left, a, i);
		a[i++] = u;
		return packIntoArray(u.right, a, i);
	}

	/**
	 * A recursive helper that builds a perfectly balanced subtree out of
	 * a[i],...,a[i+ns-1]
	 * 
	 * @param a
	 * @param i
	 * @param ns
	 * @return the rooted of the newly created subtree
	 */
	protected ScapegoatNode<T> buildBalanced(ScapegoatNode<T>[] a, int i, int ns) {
		if (ns == 0)
			return null;
		int m = ns / 2;
		a[i + m].left = buildBalanced(a, i, m);
		if (a[i + m].left != null)
			a[i + m].left.parent = a[i + m];
		a[i + m].right = buildBalanced(a, i + m + 1, ns - m - 1);
		if (a[i + m].right != null)
			a[i + m].right.parent = a[i + m];
		return a[i + m];
	}


}
