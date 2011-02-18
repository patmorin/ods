package ods;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Treap<T extends Comparable<T>> extends
		BinarySearchTree<TreapNode<T>, T> {
	/**
	 * A random number source
	 */
	Random r;

	/**
	 * The number of nodes (elements) currently in the treap
	 */
	int n;

	public Treap(TreapNode<T> sn) {
		super(sn);
		r = new Random();
	}

	public boolean add(T x) {
		TreapNode<T> u = new TreapNode<T>();
		u.x = x;
		u.prio = r.nextInt();
		if (super.add(u)) {
			n++;
			bubbleUp(u);
			return true;
		}
		return false;
	}

	protected void bubbleUp(TreapNode<T> u) {
		while (u.parent != null && u.parent.prio > u.prio) {
			if (u.parent.right == u) {
				leftRotate(u.parent);
			} else {
				rightRotate(u.parent);
			}
		}
		if (u.parent == null) {
			root = u;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object x) {
		TreapNode<T> u = findNode((T) x);
		if (u.x.compareTo((T) x) == 0) {
			trickleDown(u);
			if (u.parent == null) {
				root = null;
			} else if (u.parent.left == u) {
				u.parent.left = null;
			} else {
				u.parent.right = null;
			}
			n--;
			return true;
		}
		return false;
	}

	/**
	 * Do rotations to make u a leaf
	 */
	protected void trickleDown(TreapNode<T> n) {
		while (!n.isLeaf()) {
			if (n.left != null) {
				if (n.right == null || n.left.prio < n.right.prio) {
					rightRotate(n);
				} else {
					leftRotate(n);
				}
			} else {
				leftRotate(n);
			}
			if (root == n) {
				root = n.parent;
			}
		}
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return n == 0;
	}
	
	public void clear() {
		super.clear();
		n = 0;
	}

	/**
	 * Return the node at index i
	 * @param i
	 * @return
	 */
	public T get(int i) {
		if (i < 0 || i > size())
			throw new IndexOutOfBoundsException();
		TreapNode<T> u = root;
		while (1 < 2) {
			int l = (u.left == null) ? 0 : u.left.size;
			if (l == i) {
				return u.x;
			} else if (l > i) {
				u = u.left;
			} else {
				u = u.right;
				i = i - l - 1;
			}
		}
	}
	
	/**
	 * Compute all sizes of subtrees - for testing purposes only
	 * this is not the basis for a fast implementation
	 * @param u
	 * @return
	 */
	protected int computeSizes(TreapNode<T> u) {
		if (u == null) return 0;
		u.size = computeSizes(u.left) + computeSizes(u.right) + 1;
		return u.size;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Treap<Integer> t = new Treap<Integer>(new TreapNode<Integer>());
		int n = 100000;
		correctnessTests(t, n);
		t.clear();
		performanceTests(t);
		t.clear();
		n = 10000;
		Integer[] a = new Integer[n];
		Random r = new Random();
		for (int i = 0; i < n; i++) {
			a[i] = r.nextInt();
			t.add(a[i]);
		}
		t.computeSizes(t.root);   // this code should not be needed once you're done
		Collections.sort(Arrays.asList(a));
		for (int i = 0; i < n; i++) {
			Integer x = t.get(i);
			Integer y = a[i];
			Utils.myassert(x.equals(y));
		}
		// add some of your own testing for performance as well as interleaving
		// add(x)/remove(x) and get(i) operations
	}
}
