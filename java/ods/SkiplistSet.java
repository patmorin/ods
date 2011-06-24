package ods;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

/**
 * An implementation of skiplists for searching
 * 
 * @author morin
 *
 * @param <T>
 */
public class SkiplistSet<T> implements SSet<T> {
	protected Comparator<T> c;
	
	protected class Node {
		T x;
		Node[] next;
		public Node(T ix, int h) {
			x = ix;
			next = (Node[])Array.newInstance(Node.class, h+1);
		}
		public int height() {
			return next.length - 1;
		}
	}
	
	/**
	 * This node sits on the left side of the skiplist
	 */
	protected Node sentinel;
	
	/**
	 * The maximum height of any element
	 */
	int h;
	
	/**
	 * The number of elements stored in the skiplist
	 */
	int n;
	
	/**
	 * A source of random numbers
	 */
	Random rand;
	
	public class Finger {
		protected Node[] s;
		public Finger() {
			s = (Node[])Array.newInstance(Node.class, h+1);
			for (int r = 0; r <= h; r++) 
				s[r] = sentinel;
		}
	}

	public Finger getFinger() {
		return new Finger();
	}
	
	public T find(Finger f, T x) {
		int r = 0;
		Node u = f.s[r];
		// find an edge that passes over x
		while (r < h
				&& ((u != sentinel && c.compare(x, u.x) <= 0)
					|| (u.next[r] != null && c.compare(x, u.next[r].x) > 0))) {
			u = f.s[++r];
		}
		r--;
		while (r >= 0) {
			while (u.next[r] != null && c.compare(u.next[r].x,x) < 0)
				u = u.next[r];
			f.s[r] = u;
			r--;              
		}
		return (u.next[0] == null) ? null : u.next[0].x;
	}
	
	public SkiplistSet(Comparator<T> c) {
		this.c = c;
		n = 0;
		sentinel = new Node(null, 32);
		h = 0;
		rand = new Random();
	}
	
	public SkiplistSet() {
		this(new DefaultComparator<T>());
	}
	
	/**
	 * Find the node u that precedes the value x in the skiplist.
	 * 
	 * @param x - the value to search for
	 * @return a node u that maximizes u.x subject to
	 * the constraint that u.x < x --- or sentinel if u.x >= x for
	 * all nodes x
	 */
	protected Node findPredNode(T x) {
		Node u = sentinel;
		int r = h;
		while (r >= 0) {
			while (u.next[r] != null && c.compare(u.next[r].x,x) < 0)
				u = u.next[r];   // go right in list r
			r--;                 // go down into list r-1
		}
		return u;
	}
	
	public T find(T x) {
		Node u = findPredNode(x);
		return u.next[0] == null ? null : u.next[0].x;
	}
	
	public T findGE(T x) {
		if (x == null) {   // return first node
			return sentinel.next[0] == null ? null : sentinel.next[0].x;
		}
		return find(x);
	}
	
	public T findLT(T x) {
		if (x == null) {  // return last node
			Node u = sentinel;
			int r = h;
			while (r >= 0) {
				while (u.next[r] != null)
					u = u.next[r];
				r--;
			}
			return u.x;
		}
		return findPredNode(x).x;
	}

	public boolean add(T x) {
		Node w = new Node(x, pickHeight());
		if (w.height() > h)
			h = w.height();
		return add(w);
	}

	protected boolean add(Node w) {
		int k = w.height();
		Node u = sentinel;
		int r = h;
		int comp = 0;
		boolean dup = false;
		while (r >= 0) {
			while (u.next[r] != null && (comp = c.compare(u.next[r].x,w.x)) < 0)
				u = u.next[r];
			if (u.next[r] != null && comp == 0) // already present
				dup = true;
			if (r <= k) {
				w.next[r] = u.next[r];
				u.next[r] = w;
			}
			r--;
		}
		n++;
		if (dup) {
			remove(w);
			return false;
		}
		return true;
	}
	
	public boolean remove(T x) {
		boolean removed = false;
		Node u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null && (comp = c.compare(u.next[r].x, x)) < 0) {
				u = u.next[r];
			}
			if (u.next[r] != null && comp == 0) {
				removed = true;
				u.next[r] = u.next[r].next[r];
				if (u == sentinel && u.next[r] == null)
					h--;
			}
			r--;
		}
		if (removed) n--;
		return removed;
	}
	
	/**
	 * Remove the first instance of w.x, if it is stored in the node w.
	 */
	public boolean remove(Node w) {
		boolean removed = false;
		Node u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null && (comp = c.compare(u.next[r].x, w.x)) < 0) {
				u = u.next[r];
			}
			if (u.next[r] == w && comp == 0) {
				removed = true;
				u.next[r] = u.next[r].next[r];
				if (u == sentinel && u.next[r] == null)
					h--;
			}
			r--;
		}
		if (removed) n--;
		return removed;
	}

	/**
	 * Simulate repeatedly tossing a coin until it comes up tails.
	 * Note, this code will never generate a height greater than 32
	 * @return the number of coin tosses - 1
	 */
	protected int pickHeight() {
		int z = rand.nextInt();
		int k = 0;
		int m = 1;
		while ((z & m) != 0) {
			k++;
			m <<= 1;
		}
		return k;
	}
	
	public void clear() {
		for (int i = 0; i < h; i++) {
			sentinel.next[i] = null;
		}
		h = 0;
		n = 0;
	}
	
	public int size() {
		return n;
	}

	public Comparator<T> comparator() {
		return c;
	}

	/**
	 * Create a new iterator in which the next value in the iteration is u.next.x
	 * TODO: Constant time removal requires the use of a skiplist finger (a stack)
	 * @param u
	 * @return
	 */
	protected Iterator<T> iterator(Node u) {
		class SkiplistIterator implements Iterator<T> {
			Node u, prev;
			public SkiplistIterator(Node u) {
				this.u = u;
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
				// TODO: Not constant time
				SkiplistSet.this.remove(prev.x);
			}
		}
		return new SkiplistIterator(u);
	}
	
	public Iterator<T> iterator() {
		return iterator(sentinel);
	}
	
	public Iterator<T> iterator(T x) {
		return iterator(findPredNode(x));
	}

	public static void main(String[] args) {
		int n = 100000;
		SkiplistSet<Integer> sl = new SkiplistSet<Integer>();
		System.out.println("Adding " + n + " elements");
		for (int i = 0; i < n; i++)
			sl.add(2*i);
		System.out.println("Searching");
		for (int i = 0; i < 2*n; i++) {
			Integer x = sl.find(i);
			Utils.myassert(i > 2*(n-1) || Math.abs(x - i) <= 1);
		}
		System.out.println("Searching (sequential - with finger)");
		SkiplistSet<Integer>.Finger f = sl.getFinger();
		for (int i = 0; i < 2*n; i++) {
			Integer x = sl.find(f, i);
			Utils.myassert(i > 2*(n-1) || Math.abs(x - i) <= 1);
		}
		System.out.println("Searching (random - with finger)");
		Random r = new Random();
		for (int i = 0; i < 2*n; i++) {
			int j = r.nextInt(2*n);
			Integer x = sl.find(f, j);
			Utils.myassert(j > 2*(n-1) || Math.abs(x - j) <= 1);
		}
		System.out.println("Searching (backwards - with finger)");
		for (int i = 2*n; i >= 0; i--) {
			Integer x = sl.find(f, i);
			Utils.myassert(i > 2*(n-1) || Math.abs(x - i) <= 1);
		}

		System.out.println("Removing");
		for (int i = 0; i < n/2; i++) {
			sl.remove(4*i);
		}
		System.out.println("Verifying");
		for (int i = 0; i < 2*n; i++) {
			Integer x = sl.find(i);
			Utils.myassert(i > 2*(n-1) || Math.abs(x - i) <= 3);
		}
		System.out.println("Done - size() = " + sl.size());

//		for (Integer x : sl) {
//			System.out.print(x + ",");
//		}		
	}
}
