package ods;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 * Implements the List interface as a skiplist so that all the
 * standard operations take O(log n) time
 * 
 * TODO: currently, listIterator() return an iterator that takes O(log n)
 *       time per step
 * TODO: height never decreases
 * @author morin
 *
 * @param <T>
 */
public class SkiplistList<T> extends AbstractList<T> {
	class Node {
		T x;
		Node[] next;
		int[] length;
		public Node(T ix, int h) {
			x = ix;
			next = (Node[])Array.newInstance(Node.class, h+1);
			length = new int[h+1];
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
	Random rand;
	
	public SkiplistList() {
		n = 0;
		sentinel = new Node(null, 33);
		height = 0;
		rand = new Random(0);
	}
	
	/**
	 * Find the node that precedes list index i in the skiplist.
	 * 
	 * @param x - the value to search for
	 * @return a node u that maximizes u.x subject to
	 * the constraint that u.x < x --- or sentinel if u.x >= x for
	 * all nodes x
	 */
	protected Node findPred(int i) {
		Node u = sentinel;
		int r = height - 1;
		int j = -1;   // the index of the current node in list 0
		while (r >= 0) {
			while (u.next[r] != null && j + u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			r--;
		}
		return u;
	}

	public T set(int i, T x) {
		Node u = findPred(i).next[0];
		T y = u.x;
		u.x = x;
		return y;
	}

	public T get(int i) {
		return findPred(i).next[0].x;
	}

	/**
	 * Insert a new node into the skiplist
	 * @param i the index of the new node
	 * @param w the node to insert
	 * @return the node u that precedes v in the skiplist
	 */
	protected Node add(int i, Node w) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		Node u = sentinel;
		int k = w.next.length - 1;
		int r = height - 1;
		int j = -1; // index of u
		while (r >= 0) {
			while (u.next[r] != null && j+u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			u.length[r]++;    // to account for new node in list 0
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
	 * Simulate repeatedly tossing a coin until it comes up tails
	 * @return the number of coin tosses - 1
	 */
	protected int pickHeight() {
		int z = rand.nextInt();
		int k = 0;
		while ((z & (1 << k)) != 0)
			k++;
		return k;
	}
	
	public void add(int i, T x) {
		Node w = new Node(x, pickHeight());
		if (w.next.length > height) 
			height = w.next.length;
		add(i, w);
	}
	
	public T remove(int i) {
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		T x = null;
		Node u = sentinel;
		int r = height - 1;
		int j = -1; // index of node u
		while (r >= 0) {
			while (u.next[r] != null && j+u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			u.length[r]--;  // for the node we are removing
			if (j + u.length[r] == i && u.next[r] != null) {
				x = u.next[r].x;
				u.length[r] += u.next[r].length[r];
				u.next[r] = u.next[r].next[r];
			}
			r--;
		}
		n--;
		return x;
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
				SkiplistList.this.remove(prev.x);
			}
		}
		return new SkiplistIterator();
	}
	
	public void clear() {
		n = 0;
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
