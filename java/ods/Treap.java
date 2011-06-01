package ods;

import java.util.Random;
import java.util.SortedSet;

public class Treap<T extends Comparable<T>> extends
		BinarySearchTree<TreapNode<T>, T> implements SSet<T> {
	/**
	 * A random number source
	 */
	Random rand;

	public Treap() {
		super(new TreapNode<T>());
		rand = new Random();
		c = new DefaultComparator<T>();
	}

	public boolean add(T x) {
		TreapNode<T> u = new TreapNode<T>();
		u.x = x;
		u.prio = rand.nextInt();
		if (super.add(u)) {
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
			r = u;
		}
	}

	public boolean remove(T x) {
		TreapNode<T> u = findLast((T) x);
		if (c.compare(u.x, (T)x) == 0) {
			trickleDown(u);
			if (u.parent == null) {
				r = null;
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
			if (r == n) {
				r = n.parent;
			}
		}
	}
	
	public static void main(String[] args) {
		SortedSet<Integer> sl = new SortedSSet<Integer>(new Treap<Integer>());
		for (int i = 0; i < 100; i++) {
			sl.add(i);
		}
		System.out.println(sl.size());
		for (int i = 25; i < 75; i++) {
			sl.remove(i);
		}
		System.out.println(sl.size());
	}
}
