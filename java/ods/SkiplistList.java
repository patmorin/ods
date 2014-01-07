package ods;
import java.lang.reflect.Array;
import java.lang.IllegalStateException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;


/**
 * Implements the List interface as a skiplist so that all the
 * standard operations take O(log n) time
 * 
 * TODO: currently, listIterator() return an iterator that takes O(log n)
 *       time per step
 * @author morin
 *
 * @param <T>
 */
public class SkiplistList<T> extends AbstractList<T> {
	class Node {
		T x;
		Node[] next;
		int[] length;
		@SuppressWarnings("unchecked")
		public Node(T ix, int h) {
			x = ix;
			next = (Node[])Array.newInstance(Node.class, h+1);
			length = new int[h+1];
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
	
	public SkiplistList() {
		n = 0;
		sentinel = new Node(null, 32);
		h = 0;
		rand = new Random(0);
	}
	
	/**
	 * Find the node that precedes list index i in the skiplist.
	 * 
	 * @param x - the value to search for
	 * @return the predecessor of the node at index i or the final
	 * node if i exceeds size() - 1.
	 */
	protected Node findPred(int i) {
		Node u = sentinel;
		int r = h;
		int j = -1;   // index of the current node in list 0
		while (r >= 0) {
			while (u.next[r] != null && j + u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			r--;
		}
		return u;
	}

	public T get(int i) {
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		return findPred(i).next[0].x;
	}

	public T set(int i, T x) {
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		Node u = findPred(i).next[0];
		T y = u.x;
		u.x = x;
		return y;
	}

	/**
	 * Insert a new node into the skiplist
	 * @param i the index of the new node
	 * @param w the node to insert
	 * @return the node u that precedes v in the skiplist
	 */
	protected Node add(int i, Node w) {
		Node u = sentinel;
		int k = w.height();
		int r = h;
		int j = -1; // index of u
		while (r >= 0) {
			while (u.next[r] != null && j+u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			u.length[r]++; // accounts for new node in list 0
			if (r <= k) {
				w.next[r] = u.next[r];
				u.next[r] = w;
				w.length[r] = u.length[r] - (i - j);
				u.length[r] = i - j;
			}
			r--;
		}
		n++;
		return u;
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
	
	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		Node w = new Node(x, pickHeight());
		if (w.height() > h) 
			h = w.height();
		add(i, w);
	}
	
	public T remove(int i) {
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		T x = null;
		Node u = sentinel;
		int r = h;
		int j = -1; // index of node u
		while (r >= 0) {
			while (u.next[r] != null && j+u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			u.length[r]--;  // for the node we are removing
			if (j + u.length[r] + 1 == i && u.next[r] != null) {
				x = u.next[r].x;
				u.length[r] += u.next[r].length[r];
				u.next[r] = u.next[r].next[r];
				if (u == sentinel && u.next[r] == null)
					h--;
			}
			r--;
		}
		n--;
		return x;
	}
	
	public Iterator<T> iterator() {
		class SkiplistIterator implements Iterator<T> {
			Node u;
			int i;
			boolean removable;
			public SkiplistIterator() {
				u = sentinel;
				i = -1;
				removable = false;
			}
			public boolean hasNext() {
				return u.next[0] != null;
			}
			public T next() {
				if (u.next[0] == null)
					throw new NoSuchElementException();
				u = u.next[0];
				i++;
				removable = true;
				return u.x;
			}
			public void remove() {
				if (!removable)
					throw new IllegalStateException();
				SkiplistList.this.remove(i);
				i--;
				removable = false;
			}
		}
		return new SkiplistIterator();
	}
	
	public void clear() {
		n = 0;
		h = 0;
		Arrays.fill(sentinel.length, 0);
		Arrays.fill(sentinel.next, null);
	}

	public int size() {
		return n;
	}
	
	public static void main(String[] args) {
		int n = 20;
		List<Integer> l = new SkiplistList<Integer>();
		for (int i = 0; i < n; i++) {
			l.add(i);
		}
		System.out.println(l);
		for (int i = -1; i > -n; i--) {
			l.add(0,i);
		}
		System.out.println(l);
		for (int i = 0; i < 20; i++) {
			l.add(n+i,1000+i);
		}
		System.out.println(l);

	}

}
