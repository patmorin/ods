package ods;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * An implementation of the Queue<T> interface using an array
 * 
 * All operations takes constant amortized time.
 * @author morin
 *
 * @param <T>
 */
public class ArrayQueue<T> extends AbstractQueue<T> {
	/**
	 * The class of elements stored in this queue
	 */
	protected Factory<T> f;
	
	/**
	 * Array used to store elements
	 */
	protected T[] a;
	
	/**
	 * Index of next element to de-queue
	 */
	protected int j;
	
	/**
	 * Number of elements in the queue
	 */
	protected int n;
	
	/**
	 * Grow the internal array
	 */
	protected void resize() {
		T[] b = f.newArray(Math.max(1,n*2));
		for (int k = 0; k < n; k++) 
			b[k] = a[(j+k) % a.length];
		a = b;
		j = 0;
	}
	
	/**
	 * Constructor
	 */
	public ArrayQueue(Class<T> t) {
		f = new Factory<T>(t);
		a = f.newArray(1);
		j = 0;
		n = 0;
	}
	
	/**
	 * Return an iterator for the elements of the queue. 
	 * This iterator does not support the remove operation
	 */
	public Iterator<T> iterator() {
		class QueueIterator implements Iterator<T> {
			int k;
			
			public QueueIterator() {
				k = 0;
			}

			public boolean hasNext() {
				return (k < n);
			}
			
			public T next() {
				if (k > n) throw new NoSuchElementException();
				T x = a[(j+k) % a.length];
				k++;
				return x;
			}
			
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		return new QueueIterator();
	}

	public int size() {
		return n;
	}

	public boolean offer(T x) {
		return add(x);
	}

	public boolean add(T x) {
		if (n + 1 > a.length) resize();
		a[(j+n) % a.length] = x;
		n++;
		return true;
	}
	
	public T peek() {
		T x = null;
		if (n > 0) {
			x = a[j];
		}
		return x;
	}

	public T remove() { 
		if (n == 0) throw new NoSuchElementException();
		T x = a[j];
		j = (j + 1) % a.length;
		n--;
		if (a.length >= 3*n) resize();
		return x;
	}

	public T poll() {
		return n == 0 ? null : remove();
	}
	
	public static void main(String args[]) {
		int m = 10000, n = 50;
		Queue<Integer> q = new ArrayQueue<Integer>(Integer.class);
		for (int i = 0; i < m; i++) {
			q.add(new Integer(i));
			if (q.size() > n) {
				Integer x = q.remove();
				assert(x == i - n);
			}
		}
		Iterator<Integer> i = q.iterator();
		while (i.hasNext()) {
			System.out.println(i.next());
		}
	}
}
