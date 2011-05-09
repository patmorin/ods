package ods;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;

/**
 * An implementation of a FIFO Queue as a singly-linked list.
 * This also includes the stack operations push and pop, which
 * operate on the head of the queue
 * @author morin
 *
 * @param <T> the class of objects stored in the queue
 */
public class SLList<T> extends AbstractQueue<T> {
	class Node {
		T x;
		Node next;
	}
	
	/**
	 * Front of the queue
	 */
	Node head;
	
	/**
	 * Tail of the queue
	 */
	Node tail;
	
	/**
	 * The number of elements in the queue
	 */
	int n;
	
	public Iterator<T> iterator() {
		class SLIterator implements Iterator<T> {
			protected Node p;

			public SLIterator() {
				p = head;
			}
			public boolean hasNext() {
				return p != null;
			}
			public T next() {
				T x = p.x;
				p = p.next;
				return x;
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		return new SLIterator();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return n;
	}

	public boolean add(T x) {
		Node u = new Node();
		u.x = x;
		if (n == 0) {
			head = u;
		} else {
			tail.next = u;
		}
		tail = u;
		n++;
		return true;
	}
	
	public boolean offer(T x) {
		return add(x);
	}

	@Override
	public T peek() {
		return head.x;
	}

	public T poll() {
		if (n == 0)
			return null;
		T x = head.x;
		head = head.next;
		if (--n == 0)
			tail = null;
		return x;
	}
	
	/**
	 * Stack push operation - push x onto the head of the list
	 * @param x the element to push onto the stack
	 * @return x
	 */
	public T push(T x) {
		Node u = new Node();
		u.x = x;
		u.next = head;
		head = u;
		if (n == 0)
			tail = u;
		n++;
		return x;
	}
	
	protected void deleteNext(Node u) {
		if (u.next == tail)
			tail = u;
		u.next = u.next.next;
	}
	
	protected void addAfter(Node u, Node v) {
		v = u.next.next;
		u.next = v;
		if (u == tail) 
			tail = v;
	}
	
	protected Node getNode(int i) {
		Node u = head;
		for (int j = 0; j < i; j++)
			u = u.next;
		return u;
	}

	/**
	 * Stack pop operation - pop off the head of the list
	 * @return the element popped off 
	 */
	public T remove() {
		if (n == 0)	return null;
		T x = head.x;
		head = head.next;
		if (--n == 0) tail = null;
		return x;
	}	
	
	public T pop() {
		if (n == 0)	return null;
		T x = head.x;
		head = head.next;
		if (--n == 0) tail = null;
		return x;
	}	


	public static void main(String[] args) {
		Queue<Integer> q = new SLList<Integer>();
		for (int i = 0; i < 100; i++) {
			q.add(i);
		}
		System.out.println(q);
		for (int i = 0; i < 50; i++) {
			q.remove();
		}
		System.out.println(q);
		for (int i = 100; i < 200; i++) {
			q.add(i);
		}
		System.out.println(q);
		for (int i = 0; i < 50; i++) {
			q.remove();
		}
		System.out.println(q);
		while (!q.isEmpty()) {
			q.remove();
		}
		System.out.println(q);

	}
}
