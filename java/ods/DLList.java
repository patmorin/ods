package ods;

import java.util.AbstractSequentialList;
import java.util.ListIterator;

/**
 * An implementation of the List interface as a doubly-linked circular list. A
 * dummy node is used to simplify the code.
 * 
 * @author morin
 * 
 * @param <T>
 *            the type of elements stored in the list
 */
public class DLList<T> extends AbstractSequentialList<T> {
	class Node {
		T x;
		Node prev, next;
	}
	
	/**
	 * Number of nodes in the list
	 */
	int n;

	/**
	 * The dummy node. We use the convention that dummy.next = first and
	 * dummy.prev = last
	 */
	protected Node dummy;

	public DLList() {
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
	protected Node addBefore(Node w, T x) {
		Node u = new Node();
		u.x = x;
		u.prev = w.prev;
		u.next = w;
		u.next.prev = u;
		u.prev.next = u;
		n++;
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
		n--;
	}

	/**
	 * Get the i'th node in the list
	 * 
	 * @param i
	 *            the index of the node to get
	 * @return the node with index i
	 */
	protected Node getNode(int i) {
		Node p = null;
		if (i < n / 2) {
			p = dummy.next;
			for (int j = 0; j < i; j++)
				p = p.next;
		} else {
			p = dummy;
			for (int j = n; j > i; j--)
				p = p.prev;
		}
		return p;
	}

	public ListIterator<T> listIterator(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		return new Iterator(this, i);
	}

	public int size() {
		return n;
	}

	public boolean add(T x) {
		addBefore(dummy, x);
		return true;
	}

	public T remove(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Node w = getNode(i);
		remove(w);
		return w.x;
	}

	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		addBefore(getNode(i), x);
	}

	public T get(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		return getNode(i).x;
	}

	public T set(int i, T x) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Node u = getNode(i);
		T y = u.x;
		u.x = x;
		return y;
	}

	public void clear() {
		dummy.next = dummy;
		dummy.prev = dummy;
		n = 0;
	}

	class Iterator implements ListIterator<T> {
		/**
		 * The list we are iterating over
		 */
		DLList<T> l;

		/**
		 * The node whose value is returned by next()
		 */
		Node p;

		/**
		 * The last node whose value was returned by next() or previous()
		 */
		Node last;

		/**
		 * The index of p
		 */
		int i;

		Iterator(DLList<T> il, int ii) {
			l = il;
			i = ii;
			p = l.getNode(i);
		}

		public boolean hasNext() {
			return p != dummy;
		}

		public T next() {
			T x = p.x;
			last = p;
			p = p.next;
			i++;
			return x;
		}

		public int nextIndex() {
			return i;
		}

		public boolean hasPrevious() {
			return p.prev != dummy;
		}

		public T previous() {
			p = p.prev;
			last = p;
			i--;
			return p.x;
		}

		public int previousIndex() {
			return i - 1;
		}

		public void add(T x) {
			DLList.this.addBefore(p, x);
		}

		public void set(T x) {
			last.x = x;
		}

		public void remove() {
			if (p == last) {
				p = p.next;
			}
			DLList.this.remove(last);
		}

	}
}
