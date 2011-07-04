package ods;

import java.util.ArrayList;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;


public class Treap<T> extends
		BinarySearchTree<Treap.Node<T>, T> implements SSet<T> {
	/**
	 * A random number source
	 */
	Random rand;

	protected static class Node<T> extends BSTNode<Node<T>,T> {
		int p;
	}
	
	public Treap() {
		sampleNode = new Node<T>();
		rand = new Random();
		c = new DefaultComparator<T>();
	}

	public boolean add(T x) {
		Node<T> u = new Node<T>();
		u.x = x;
		u.p = rand.nextInt();
		if (super.add(u)) {
			bubbleUp(u);
			return true;
		}
		return false;
	}

	protected void bubbleUp(Node<T> u) {
		while (u.parent != null && u.parent.p > u.p) {
			if (u.parent.right == u) {
				rotateLeft(u.parent);
			} else {
				rotateRight(u.parent);
			}
		}
		if (u.parent == null) {
			r = u;
		}
	}

	public boolean remove(T x) {
		Node<T> u = findLast(x);
		if (c.compare(u.x, x) == 0) {
			trickleDown(u);
			splice(u);
			return true;
		}
		return false;
	}

	/**
	 * Do rotations to make u a leaf
	 */
	protected void trickleDown(Node<T> u) {
		while (u.left != null || u.right != null) {
			if (u.left == null) {
				rotateLeft(u);
			} else if (u.right == null) {
				rotateRight(u);
			} else if (u.left.p < u.right.p) {
				rotateRight(u);
			} else {
				rotateLeft(u);
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
