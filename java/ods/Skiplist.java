package ods;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Random;

/**
 * An implementation of skiplists for searching
 * 
 * TODO: Add the methods needed to implement the SortedSet interface
 * @author morin
 *
 * @param <T>
 */
public class Skiplist<T extends Comparable<T>> implements Iterable<T> {
	class Node {
		T x;
		Node[] next;
		public Node(T ix, int h) {
			x = ix;
			next = (Node[])Array.newInstance(Node.class, h+1);
		}
		public String toString() {
			return "{" + x.toString() + "," + next.length + "}";
		}
	}
	
	/**
	 * This node sits on the left side of the skiplist
	 */
	protected Node sentinel;
	
	/**
	 * The maximum height of any element
	 */
	int height;
	
	/**
	 * The number of elements stored in the skiplist
	 */
	int n;
	
	/**
	 * A source of random numbers
	 */
	Random r;
	
	public Skiplist() {
		n = 0;
		sentinel = new Node(null, 33);
		height = 0;
		r = new Random();
	}

	/**
	 * Increase the height of the skiplist to h
	 * @param h
	 */
	protected void grow(int h) {
		for (int i = height; i < h; i++)
			assert(sentinel.next[i] != null);
		height = h;
	}
	
	/**
	 * Decrease the height of the skiplist to h
	 * @param h
	 */
	protected void shrink(int h) {
		for (int i = height-1; i >= h; i--)
			assert(sentinel.next[i] == null);
		height = h;
	}
	
	/**
	 * Find the node that precedes the value x in the skiplist.
	 * 
	 * @param x - the value to search for
	 * @return a node u that maximizes u.x subject to
	 * the constraint that u.x < x --- or sentinel if u.x >= x for
	 * all nodes x
	 */
	protected Node findPred(T x) {
		Node u = sentinel;
		int l = height - 1;
		while (l >= 0) {
			while (u.next[l] != null && u.next[l].x.compareTo(x) < 0) {
				u = u.next[l];
			}
			l--;
		}
		return u;
	}

	/**
	 * Insert a new node into the skiplist
	 * @param v the node to insert
	 * @return the node u that precedes v in the skiplist
	 */
	protected Node insert(Node v) {
		if (v.next.length > height)
			grow(v.next.length);
		Node u = sentinel;
		int l = height - 1;
		while (l >= 0) {
			while (u.next[l] != null && u.next[l].x.compareTo(v.x) < 0) {
				u = u.next[l];
			}
			if (l < v.next.length) {
				v.next[l] = u.next[l];
				u.next[l] = v;
			}
			l--;
		}
		n++;
		return u;
	}

	/**
	 * Remove a value from the skiplist
	 * @param x the value to remove
	 * @return the node u that preceded x in the skiplist
	 */
	protected boolean remove(T x) {
		boolean removed = false;
		Node u = sentinel;
		int l = height - 1;
		while (l >= 0) {
			while (u.next[l] != null && u.next[l].x.compareTo(x) < 0) {
				u = u.next[l];
			}
			if (u.next[l] != null && u.next[l].x.equals(x)) {
				removed = true;
				u.next[l] = u.next[l].next[l];
			}
			l--;
		}
		if (removed) n--;
		return removed;
	}

	/**
	 * Simulate repeatedly tossing a coin until it comes up tails
	 * @return the number of coin tosses - 1
	 */
	protected int pickHeight() {
		int j = r.nextInt();
		int i = 0;
		while (((j & 1) == 1)) {
			j >>>= 1;
			i++;
		}
		return i;
	}
	
	public Node insert(T x) {
		Node v = new Node(x, pickHeight());
		return insert(v);
	}
		
	public boolean add(T x) {
		insert(x);
		return true;
	}
	
	public Iterator<T> iterator() {
		class SkiplistIterator implements Iterator<T> {
			Node u, prev;
			public SkiplistIterator() {
				u = sentinel;
				prev = null;
			}
			public boolean hasNext() {
				return u.next[0] != null;
			}
			public T next() {
				prev = u;
				u = u.next[0];
				return u.x;
			}
			public void remove() {
				Skiplist.this.remove(prev.x);
			}
		}
		return new SkiplistIterator();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Skiplist<Integer> l = new Skiplist<Integer>();
		System.out.println("inserting");
		for (int i = 0; i < 100; i++) {
			l.insert(i);
		}
		for (Integer x : l) {
			System.out.print("" + x + ", ");
		}
		System.out.println();
		for (int i = 0; i < 50; i++) {
			l.remove(2*i);
		}
		for (Integer x : l) {
			System.out.print("" + x + ", ");
		}
		System.out.println();
		System.exit(-1);
		for (int i = 0; i < 100; i++) {
			l.insert(1000-i);
		}
		System.out.println("searching");
		for (int i = 0; i < 100; i++) {
		}
		System.out.println(l);
	}
}
