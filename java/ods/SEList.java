package ods;

import java.util.AbstractSequentialList;
import java.util.ListIterator;

/**
 * An implementation of the List interface as a doubly-linked circular list 
 * of blocks of size b
 * 
 * @author morin
 * 
 * @param <T>
 *            the type of elements stored in the list
 */
public class SEList<T> extends AbstractSequentialList<T> {
	Factory<T> f;

	/**
	 * Number of nodes in the list
	 */
	int n;
	
	/**
	 * The size of blocks in the list
	 */
	int b;

	protected class BDeque extends ArrayDeque<T> {
		public BDeque() {
			super(SEList.this.f.type());
			a = f.newArray(b+1);
		}
		protected void resize() { }
	}
	
	protected class Node {
		BDeque d;
		Node prev, next;
	}
	
	protected Node dummy;

	protected class Location {
		Node u;
		int j;
		public Location(Node u, int j) {
			this.u = u;
			this.j = j;
		}
	}
	
	public SEList(int b, Class<T> c) {
		this.b = b;
		f = new Factory<T>(c);
		dummy = new Node();
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
		Node u = new Node();
		u.d = new BDeque();
		u.prev = w.prev;
		u.next = w;
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
			while (i >= u.d.size()) {
				i -= u.d.size();
				u = u.next;
			}
			return new Location(u, i);
		} else {
			Node u = dummy;
			int idx = n;
			while (i < idx) {
				u = u.prev;
				idx -= u.d.size();
			}
			return new Location(u, i-idx);
		}
	}
	
	public T get(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Location l = getLocation(i);
		return l.u.d.get(l.j);
	}
	
	public T set(int i, T x) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Location l = getLocation(i);
		T y = l.u.d.get(l.j);
		l.u.d.set(l.j,x);
		return y;
	}

	public int size() {
		return n;
	}
	
	/**
	 * Call this function on a node u such that u and its b-1 successors
	 * all contain b+1 items.  This adds a new node so that u and its
	 * b successors each contain b items
	 * @param u
	 * @return
	 */
	protected void spread(Node u) {
		Node w = u;
		for (int j = 0; j < b; j++) {
			w = w.next;
		}
		w = addBefore(w);
		while (w != u) {
			while (w.d.size() < b)
				w.d.add(0,w.prev.d.remove(w.prev.d.size()-1));
			w = w.prev;
		}
	}
	
	/**
	 * Call this function on a node u such that u and its b-1 successors
	 * all contain b-1 items.  This removes a node so that u and its
	 * b-2 successors each contain b items
	 * @param u
	 * @return
	 */
	protected void gather(Node u) {
		Node w = u;
		for (int j = 0; j < b-1; j++) {
			while (w.d.size() < b)
				w.d.add(w.next.d.remove(0));
			w = w.next;
		}
		remove(w);
	}

	
	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		if (i == n) {
			add(x);
			return;
		}
		Location l = getLocation(i);
		Node u = l.u;
		int r = 0;
		while (r < b && u != dummy && u.d.size() == b+1) {
			u = u.next;
			r++;
		}
		if (r == b) {      // b blocks each with b+1 elements
			spread(l.u);
			u = l.u;
		} 
		if (u == dummy) {  // ran off the end - add new node
			u = addBefore(u);
		}
		while (u != l.u) { // work backwards, shifting elements
			u.d.add(0, u.prev.d.remove(u.prev.d.size()-1));
			u = u.prev;
		}
		u.d.add(l.j, x);
		n++;
	}
	
	public T remove(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Location l = getLocation(i);
		T y = l.u.d.get(l.j);
		Node u = l.u;
		int r = 0;
		while (r < b && u != dummy && u.d.size() == b-1) {
			u = u.next;
			r++;
		}
		if (r == b) {  // b blocks each with b-1 elements
			gather(l.u);
		}
		u = l.u;
		u.d.remove(l.j);
		while (u.d.size() < b-1 && u.next != dummy) {
			u.d.add(u.next.d.remove(0));
			u = u.next;
		}
		if (u.d.isEmpty()) remove(u);
		n--;
		return y;
	}
	
	public boolean add(T x) {
		Node last = dummy.prev;
		if (last == dummy || last.d.size() == b+1) {
			last = addBefore(dummy);
		}
		last.d.add(x);
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
			s += u.d.toString();
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
		SEList<Integer> l = new SEList<Integer>(b, Integer.class);
		for (int i = 0; i < n; i++) {
			l.add(i*10);
		}
		System.out.println(l);
		for (int i = 0; i < n; i++) {
			l.remove(l.size()-1);
			System.out.println(l);
		}
		System.out.println(l);
		System.exit(-1);
		
		System.out.println(l);
		for (int i = 0; i < n; i++) {
			l.set(i, i+1);
		}
		System.out.println();
		System.out.println(l);
		for (int i = 0; i < 15; i++) {
			l.remove(l.size()-1);
			System.out.println(l);
		}

	}
}
