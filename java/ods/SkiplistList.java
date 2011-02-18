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
 * TODO: remove(i) is not implemented
 * TODO: currently, iteration takes O(log n) time per step
 *       it should use a finger so that it takes O(1) time per step
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
	Random r;
	
	public SkiplistList() {
		n = 0;
		sentinel = new Node(null, 33);
		height = 0;
		r = new Random(0);
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
	 * Find the node that precedes list index i in the skiplist.
	 * 
	 * @param x - the value to search for
	 * @return a node u that maximizes u.x subject to
	 * the constraint that u.x < x --- or sentinel if u.x >= x for
	 * all nodes x
	 */
	protected Node findPred(int i) {
		Node u = sentinel;
		int l = height - 1;
		int s = 0;   // number of nodes in list up to and including u
		while (l >= 0) {
			while (u.next[l] != null && s + u.length[l] < i+1) {
				s += u.length[l];
				u = u.next[l];
			}
			l--;
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
	 * @param v the node to insert
	 * @return the node u that precedes v in the skiplist
	 */
	protected Node insert(int i, Node v) {
		if (v.next.length > height)
			grow(v.next.length);
		Node u = sentinel;
		int l = height - 1;
		int s = 0; // number of nodes in list up to and including u
		while (l >= 0) {
			while (u.next[l] != null && s+u.length[l] < i+1) {
				s += u.length[l];
				u = u.next[l];
			}
			u.length[l]++;
			if (l < v.next.length) {
				v.next[l] = u.next[l];
				u.next[l] = v;
				v.length[l] = u.length[l] - i - 1 + s;
				u.length[l] = i + 1 - s;
			}
			l--;
		}
		n++;
		return u;
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
	
	public Node insert(int i, T x) {
		Node v = new Node(x, pickHeight());
		return insert(i, v);
	}
		
	public void add(int i, T x) {
		insert(i, x);
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
