package ods;

import java.lang.reflect.Array;
import java.util.Arrays;
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
public class SkiplistSSet<T> implements SSet<T> {
	protected Comparator<T> c;
	
	@SuppressWarnings("unchecked")
	protected static class Node<T> {
		T x;
		Node<T>[] next;
		public Node(T ix, int h) {
			x = ix;
			next = (Node<T>[])Array.newInstance(Node.class, h+1);
		}
		public int height() {
			return next.length - 1;
		}
	}
	
	/**
	 * This node<T> sits on the left side of the skiplist
	 */
	protected Node<T> sentinel;
	
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

	/**
	 * Used by add(x) method
	 */
	protected Node<T>[] stack;
	
	@SuppressWarnings("unchecked")
	public class Finger {
		protected Node<T>[] s;
		public Finger() {
			s = (Node<T>[])Array.newInstance(Node.class, h+1);
			for (int r = 0; r <= h; r++) 
				s[r] = sentinel;
		}
	}

	public Finger getFinger() {
		return new Finger();
	}
	
	public T find(Finger f, T x) {
		int r = 0;
		Node<T> u = f.s[r];
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
	
	@SuppressWarnings("unchecked")
	public SkiplistSSet(Comparator<T> c) {
		this.c = c;
		n = 0;
		sentinel = new Node<T>(null, 32);
		stack = (Node<T>[])Array.newInstance(Node.class, sentinel.next.length);
		h = 0;
		rand = new Random();
	}
	
	public SkiplistSSet() {
		this(new DefaultComparator<T>());
	}
	
	/**
	 * Find the node<T> u that precedes the value x in the skiplist.
	 * 
	 * @param x - the value to search for
	 * @return a node<T> u that maximizes u.x subject to
	 * the constraint that u.x < x --- or sentinel if u.x >= x for
	 * all node<T>s x
	 */
	protected Node<T> findPredNode(T x) {
		Node<T> u = sentinel;
		int r = h;
		while (r >= 0) {
			while (u.next[r] != null && c.compare(u.next[r].x,x) < 0)
				u = u.next[r];   // go right in list r
			r--;               // go down into list r-1
		}
		return u;
	}
	
	public T find(T x) {
		Node<T> u = findPredNode(x);
		return u.next[0] == null ? null : u.next[0].x;
	}
	
	public T findGE(T x) {
		if (x == null) {   // return first node<T>
			return sentinel.next[0] == null ? null : sentinel.next[0].x;
		}
		return find(x);
	}
	
	public T findLT(T x) {
		if (x == null) {  // return last node<T>
			Node<T> u = sentinel;
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

	
	public boolean remove(T x) {
		boolean removed = false;
		Node<T> u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null 
			       && (comp = c.compare(u.next[r].x, x)) < 0) {
				u = u.next[r];
			}
			if (u.next[r] != null && comp == 0) {
				removed = true;
				u.next[r] = u.next[r].next[r];
				if (u == sentinel && u.next[r] == null)
					h--;  // height has gone down
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
		n = 0;
		h = 0;
		Arrays.fill(sentinel.next, null);
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
	protected Iterator<T> iterator(Node<T> u) {
		class SkiplistIterator implements Iterator<T> {
			Node<T> u, prev;
			public SkiplistIterator(Node<T> u) {
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
				SkiplistSSet.this.remove(prev.x);
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

	public boolean add(T x) {
		Node<T> u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null 
			       && (comp = c.compare(u.next[r].x,x)) < 0)
				u = u.next[r];
			if (u.next[r] != null && comp == 0) return false;
			stack[r--] = u;          // going down, store u
		}
		Node<T> w = new Node<T>(x, pickHeight());
		while (h < w.height())
			stack[++h] = sentinel;   // height increased
		for (int i = 0; i < w.next.length; i++) {
			w.next[i] = stack[i].next[i];
			stack[i].next[i] = w;
		}
		n++;
		return true;
	}

	public static void main(String[] args) {
		int n = 100000;
		SkiplistSSet<Integer> sl = new SkiplistSSet<Integer>();
		System.out.println("Adding " + n + " elements");
		for (int i = 0; i < n; i++)
			sl.add(2*i);
		System.out.println("Searching");
		for (int i = 0; i < 2*n; i++) {
			Integer x = sl.find(i);
			Utils.myassert(i > 2*(n-1) || Math.abs(x - i) <= 1);
		}
		System.out.println("Searching (sequential - with finger)");
		SkiplistSSet<Integer>.Finger f = sl.getFinger();
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
