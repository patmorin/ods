package ods;

import java.util.AbstractList;

/**
 * An implementation of the List interface that allows for fast modifications
 * at both the head and tail.
 * 
 * The implementation is as a circular array.  The List item of rank i is stored
 * at a[(j+i)%a.length].  Insertions and removals at position i take 
 * O(1+min{i, size()-i}) amortized time.
 * @author morin
 *
 * @param <T> the type of objects stored in this list
 */
public class ArrayDeque<T> extends AbstractList<T> {
	/**
	 * The class of elements stored in this queue
	 */
	Factory<T> f;
	
	/**
	 * Array used to store elements
	 */
	T[] a;
	
	/**
	 * Index of next element to de-queue
	 */
	int j;
	
	/**
	 * Number of elements in the queue
	 */
	int n;
	
	
	/**
	 * Grow the internal array
	 */
	protected void grow() {
		T[] b = f.newArray(a.length * 2);
		for (int k = 0; k < n; k++) 
			b[k] = a[(j+k) % a.length];
		a = b;
		j = 0;
	}
	
	/**
	 * Shrink the internal array if too much space is being wasted
	 */
	protected void shrink() {
		if (n > 0 && n < a.length / 4) {
			T[] b = f.newArray(n * 2);
			for (int k = 0; k < n; k++)
				b[k] = a[(j+k) % a.length];
			a = b;
			j = 0;			
		}
	}

	/**
	 * Constructor
	 */
	public ArrayDeque(Class<T> t) {
		f = new Factory<T>(t);
		a = f.newArray(1);
		j = 0;
		n = 0;
	}
	
	public int size() {
		return n;
	}
	
	public T get(int i) {
		if (i < 0 || i > n-1)
			throw new IndexOutOfBoundsException();
		return a[(j+i)%a.length];
	}
	
	public T set(int i, T x) {
		if (i < 0 || i > n-1)
			throw new IndexOutOfBoundsException();
		T y = a[(j+i)%a.length];
		a[(j+i)%a.length] = x;
		return y;
	}
	
	public void add(int i, T x) {
		if (i < 0 || i > n)
			throw new IndexOutOfBoundsException();
		if (n+1 > a.length)
			grow();
		if (i < n/2) {
			// shift elements 0,...,i-1 left in a
			j = (j == 0) ? a.length - 1 : j - 1;
			for (int k = 0; k <= i-1; k++)
				a[(j+k)%a.length] = a[(j+k+1)%a.length];
		} else {
			// shift elements i,...,n-1 right in a
			for (int k = n; k > i; k--)
				a[(j+k)%a.length] = a[(j+k-1)%a.length];
		}
		a[(j+i)%a.length] = x;
		n++;
	}
	
	public T remove(int i) {
		if (i < 0 || i > n - 1)
			throw new IndexOutOfBoundsException();
		T x = a[(j+i)%a.length];
		if (i < n/2) {
			for (int k = i; k > 0; k--)
				a[(j+k)%a.length] = a[(j+k-1)%a.length];
			j = (j + 1) % a.length;
		} else {
			for (int k = i; k < n-1; k++)
				a[(j+k)%a.length] = a[(j+k+1)%a.length];
		}
		n--;
		shrink();
		return x;
	}
	
	public void clear() {
		a = f.newArray(1);
		n = 0;
		j = 0;
	}
}
