package ods;

import java.util.ArrayList;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;


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
		u.p = rand.nextInt();
		if (super.add(u)) {
			bubbleUp(u);
			return true;
		}
		return false;
	}

	protected void bubbleUp(TreapNode<T> u) {
		while (u.parent != null && u.parent.p > u.p) {
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
			splice(u);
			return true;
		}
		return false;
	}

	/**
	 * Do rotations to make u a leaf
	 */
	protected void trickleDown(TreapNode<T> u) {
		while (u.left != null || u.right != null) {
			if (u.left == null) {
				leftRotate(u);
			} else if (u.right == null) {
				rightRotate(u);
			} else if (u.left.p < u.right.p) {
				rightRotate(u);
			} else {
				leftRotate(u);
			}
			if (r == u) {
				r = u.parent;
			}
		}
	}
	
	public static void main(String[] args) {
		List<SortedSet<Integer>> c = new ArrayList<SortedSet<Integer>>();
		c.add(new TreeSet<Integer>());
		c.add(new SortedSSet<Integer>(new Treap<Integer>()));
		c.add(new SortedSSet<Integer>(new SkiplistSet<Integer>()));
		c.add(new TreeSet<Integer>());
		Testum.sortedSetSpeedTests(c, 1000000);
	}
}
