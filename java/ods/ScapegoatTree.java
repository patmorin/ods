package ods;

import java.lang.reflect.Array;
import java.util.Comparator;


public class ScapegoatTree<T> 
		extends BinarySearchTree<ScapegoatTree.Node<T>,T> {
	/**
	 * An overestimate of n
	 */
	int q;
	
	protected static class Node<T> extends BinarySearchTree.BSTNode<Node<T>,T> {	}
	
	public ScapegoatTree(Comparator<T> c) {
		super(new Node<T>(), c);
	}
	
	public ScapegoatTree() {
		this(new DefaultComparator<T>());
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

	/***
	 * Do a normal BinarySearchTree insertion, but return the depth
	 * of the newly inserted node. 
	 * @param u - the new node to insert
	 * @return the depth of the newly inserted node, or -1 if the node
	 * was not inserted
	 */
	int addWithDepth(Node<T> u) {
		Node<T> w = r;
		if (w == nil) {
			r = u;
			n++; q++;
			return 0;
		}
		boolean done = false;
		int d = 0;
		do {
			int res = c.compare(u.x, w.x);
			if (res < 0) {
				if (w.left == nil) {
					w.left = u;
					u.parent = w;
					done = true;
				} else {
					w = w.left;
				}
			} else if (res > 0) {
				if (w.right == nil) {
					w.right = u;
					u.parent = w;
					done = true;
				}
				w = w.right;
			} else {
				return -1;
			}
			d++;
		} while (!done);
		n++; q++;
		return d;
	}

	public boolean add(T x) {
		// first do basic insertion keeping track of depth
		Node<T> u = newNode(x);
		int d = addWithDepth(u);
		if (d > log32(q)) {
			// depth exceeded, find scapegoat
			Node<T> w = u.parent;
			while (3*size(w) <= 2*size(w.parent))
				w = w.parent;
			rebuild(w.parent);
		}
		return d >= 0;
	}

	@SuppressWarnings("unchecked")
	protected void rebuild(Node<T> u) {
		int ns = size(u);
		Node<T> p = u.parent;
		Node<T>[] a = (Node<T>[]) Array.newInstance(Node.class, ns);
		packIntoArray(u, a, 0);
		if (p == nil) {
			r = buildBalanced(a, 0, ns);
			r.parent = nil;
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
	protected int packIntoArray(Node<T> u, Node<T>[] a, int i) {
		if (u == nil) {
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
	protected Node<T> buildBalanced(Node<T>[] a, int i, int ns) {
		if (ns == 0)
			return nil;
		int m = ns / 2;
		a[i + m].left = buildBalanced(a, i, m);
		if (a[i + m].left != nil)
			a[i + m].left.parent = a[i + m];
		a[i + m].right = buildBalanced(a, i + m + 1, ns - m - 1);
		if (a[i + m].right != nil)
			a[i + m].right.parent = a[i + m];
		return a[i + m];
	}


}
