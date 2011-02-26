package ods;

import java.util.AbstractSequentialList;
import java.util.ListIterator;

/**
 * An implementation of the List interface as a doubly-linked circular list 
 * of blocks
 * 
 * @author morin
 * 
 * @param <T>
 *            the type of elements stored in the list
 */
public class SEListRaw<T> extends AbstractSequentialList<T> {

	Factory<T> f;

	/**
	 * Number of nodes in the list
	 */
	int n;
	
	/**
	 * The size of blocks in the list
	 */
	int b;

	class Node {
		Node prev;
		T[] a;
		int s;
		Node next;
		public Node(Node prev, T[] a, Node next) {
			this.prev = prev;
			this.a = a;
			this.next = next;
		}
		public Node(T[] a) {
			this.a = a;
		}
	}
	
	/**
	 * The dummy node. We use the convention that dummy.next = first and
	 * dummy.prev = last
	 */
	protected Node dummy;

	protected class Location {
		Node u;
		int j;
		public Location(Node u, int j) {
			this.u = u;
			this.j = j;
		}
	}
	
	public SEListRaw(int b, Class<T> c) {
		this.b = b;
		f = new Factory<T>(c);
		dummy = new Node(f.newArray(1));
		dummy.next = dummy;
		dummy.prev = dummy;
		n = 0;
	}

	/**
	 * Add a new node containing x before the node p
	 * 
	 * @param w
	 *            the node to insert the new node before
	 * @param x
	 *            the value to store in the new node
	 * @return the newly created and inserted node
	 */
	protected Node addBefore(Node w) {
		Node u = new Node(w.prev, f.newArray(b+1), w);
		u.next.prev = u;
		u.prev.next = u;
		return u;
	}

	/**
	 * Remove the node p from the list
	 * 
	 * @param w
	 *            the node to remove
	 */
	protected void remove(Node w) {
		w.prev.next = w.next;
		w.next.prev = w.prev;
	}
	
	protected Location getLocation(int i) {
		if (i < n/2) {
			Node u = dummy.next;
			while (i >= u.s) {
				i -= u.s;
				u = u.next;
			}
			return new Location(u, i);
		} else {
			Node u = dummy;
			int idx = n;
			while (i < idx) {
				u = u.prev;
				idx -= u.s;
			}
			return new Location(u, i-idx);
		}
	}
	
	public T get(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Location l = getLocation(i);
		return l.u.a[l.j];
	}
	
	public T set(int i, T x) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Location l = getLocation(i);
		T y = l.u.a[l.j];
		l.u.a[l.j] = x;
		return y;
	}

	public int size() {
		return n;
	}
	
	/**
	 * Call this function on a node u such that u and its b-1 successors
	 * all contains b+1 items.  This adds a new node so that u and its
	 * b successors each contain b items
	 * @param u
	 * @return
	 */
	public void spread(Node u) {
		T[] saved = f.newArray(b*(b+1));
		Node w = u;
		for (int j = 0; j < b; j++) {
			Utils.myassert(w.s == b+1);
			System.arraycopy(w.a, 0, saved, j*(b+1), b+1);
			w = w.next;
		}
		addBefore(w);
		w = u;
		for (int j = 0; j < b+1; j++) {
			w.s = b;
			System.arraycopy(saved, j*b, w.a, 0, b);
			w.a[b] = null;
			w = w.next;
		}
	}
	
	/**
	 * Call this function on a node u such that u and its b-1 successors
	 * all contains b-1 items.  This removes a node so that u and its
	 * b-2 successors each contain b items
	 * @param u
	 * @return
	 */
	public void gather(Node u) {
		T[] saved = f.newArray(b*(b-1));
		Node w = u;
		for (int j = 0; j < b; j++) {
			Utils.myassert(w.s == b-1);
			System.arraycopy(w.a, 0, saved, j*(b-1), b-1);
			w = w.next;
		}
		remove(w.prev);
		w = u;
		for (int j = 0; j < b-1; j++) {
			w.s = b;
			System.arraycopy(saved, j*b, w.a, 0, b);
			w.a[b] = null;
			w = w.next;
		}
	}

	
	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		if (i == n) {
			add(x);
			return;
		}
		Location l = getLocation(i);
		Node u = l.u;
		int j = 0;
		while (j < b && u != dummy && u.s == b+1) {
			u = u.next;
			j++;
		}
		if (j == b) { // found b blocks each with b+1 elements
			spread(l.u);
			j = 0;
			u = l.u;
		} 
		if (u == dummy) {
			u = addBefore(u);
		}
		while (j > 0) {
			System.arraycopy(u.a, 0, u.a, 1, u.s);
			u.s++;
			u.a[0] = u.prev.a[u.prev.s-1];
			u.prev.s--;
			u = u.prev;
			j--;
		}
		Utils.myassert(u == l.u);
		Utils.myassert(u.s < b+1);
		System.arraycopy(u.a, l.j, u.a, l.j+1, u.s-l.j);
		u.a[l.j] = x;
		u.s++;
		n++;
	}
	
	public T remove(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Location l = getLocation(i);
		T y = l.u.a[l.j];
		Node u = l.u;
		int j = 0;
		while (j < b && u != dummy && u.s == b-1) {
			u = u.next;
			j++;
		}
		if (j == b) {
			gather(l.u);
			j = 0;
		}
		u = l.u;
		System.arraycopy(u.a, l.j+1, u.a, l.j, u.s-l.j-1);
		u.a[--u.s] = null;
		for (int k = 1; k <= j; k++) {
			Utils.myassert(u.s == b-2);
			if (u.next.s > 0) {
				u.a[u.s++] = u.next.a[0];
				u = u.next;
				System.arraycopy(u.a, 1, u.a, 0, --u.s);
				u.a[u.s] = null;
			} else if (u.next != dummy) {
				remove(u.next);
			}
		}
		n--;
		return y;
	}
	
	public boolean add(T x) {
		if (n == 0) {
			addBefore(dummy);
		}
		if (dummy.prev.s == b+1) {
			addBefore(dummy);
		}
		dummy.prev.a[dummy.prev.s++] = x;
		n++;
		return true;
	}
	
	// TODO: unfinished
	public ListIterator<T> listIterator(int i) {
		return null;
	}
	
	public String toString() {
		Node u = dummy.next;
		String s = "";
		while (u != dummy) {
			s += "[";
			for (int i = 0; i < u.s; i++)
				s += u.a[i] + ((i < u.s-1) ? "," : "");
			s += "]";
			u = u.next;
		}
		return s;
	}

	public void clear() {
		dummy.next = dummy;
		dummy.prev = dummy;
		n = 0;
	}
	
//	/**
//	 * Get the i'th node in the list
//	 * 
//	 * @param i
//	 *            the index of the node to get
//	 * @return the node with index i
//	 */
//	protected Node getNode(int i) {
//		Node p = null;
//		if (i < n / 2) {
//			p = dummy.next;
//			for (int j = 0; j < i; j++)
//				p = p.next;
//		} else {
//			p = dummy;
//			for (int j = n; j > i; j--)
//				p = p.prev;
//		}
//		return (p);
//	}
//
//	public ListIterator<T> listIterator(int i) {
//		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
//		return new Iterator(this, i);
//	}
//
//	public int size() {
//		return n;
//	}
//
//	public boolean add(T x) {
//		addBefore(dummy, x);
//		return true;
//	}
//
//	public T remove(int i) {
//		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
//		Node w = getNode(i);
//		remove(w);
//		return w.x;
//	}
//
//	public void add(int i, T x) {
//		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
//		addBefore(getNode(i), x);
//	}
//
//	public T get(int i) {
//		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
//		return getNode(i).x;
//	}
//
//	public T set(int i, T x) {
//		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
//		Node u = getNode(i);
//		T y = u.x;
//		u.x = x;
//		return y;
//	}
//

//	TODO: iterator is not implemented
//	class Iterator implements ListIterator<T> {
//		/**
//		 * The list we are iterating over
//		 */
//		SEList<T> l;
//
//		/**
//		 * The node whose value is returned by next()
//		 */
//		Node p;
//
//		/**
//		 * The last node whose value was returned by next() or previous()
//		 */
//		Node last;
//
//		/**
//		 * The index of p
//		 */
//		int i;
//
//		Iterator(SEList<T> il, int ii) {
//			l = il;
//			i = ii;
//			p = l.getNode(i);
//		}
//
//		public boolean hasNext() {
//			return p != dummy;
//		}
//
//		public T next() {
//			T x = p.x;
//			last = p;
//			p = p.next;
//			i++;
//			return x;
//		}
//
//		public int nextIndex() {
//			return i;
//		}
//
//		public boolean hasPrevious() {
//			return p.prev != dummy;
//		}
//
//		public T previous() {
//			p = p.prev;
//			last = p;
//			i--;
//			return p.x;
//		}
//
//		public int previousIndex() {
//			return i - 1;
//		}
//
//		public void add(T x) {
//			SEList.this.addBefore(p, x);
//		}
//
//		public void set(T x) {
//			last.x = x;
//		}
//
//		public void remove() {
//			if (p == last) {
//				p = p.next;
//			}
//			SEList.this.remove(last);
//		}
//	}
	
	public static void main(String[] args) {
		int n = 51;
		int b = 4;
		SEListRaw<Integer> l = new SEListRaw<Integer>(b, Integer.class);
		for (int i = 0; i < n; i++) {
			l.add(i*10);
		}
		System.out.println(l);
		for (int i = 0; i < n; i++) {
			System.out.print(l.get(i) + ",");
		}
		System.out.println();
		for (int i = 0; i < n; i++) {
			l.set(i, i+1);
		}
		System.out.println();
		System.out.println(l);
		for (int i = 0; i < n; i++) {
			System.out.print(l.get(i) + ",");
		}
		System.out.println();
		// l.add(48, -9);
		System.out.println(l);

		for (int i = 0; i < 15; i++) {
			l.remove(l.size()-1);
			System.out.println(l);
		}

	}
}
