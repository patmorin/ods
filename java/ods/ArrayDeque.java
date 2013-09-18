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
 * TODO: Implement addAll() and removeAll() efficiently
 */
public class ArrayDeque<T> extends AbstractList<T> {
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
		T[] b = f.newArray(Math.max(2*n,1));
		for (int k = 0; k < n; k++) 
			b[k] = a[(j+k) % a.length];
		a = b;
		j = 0;
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
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		return a[(j+i)%a.length];
	}
	
	public T set(int i, T x) {
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		T y = a[(j+i)%a.length];
		a[(j+i)%a.length] = x;
		return y;
	}
	
	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		if (n+1 > a.length) resize();
		if (i < n/2) { // shift a[0],..,a[i-1] left one position
			j = (j == 0) ? a.length - 1 : j - 1; //(j-1)mod a.length
			for (int k = 0; k <= i-1; k++)
				a[(j+k)%a.length] = a[(j+k+1)%a.length];
		} else { // shift a[i],..,a[n-1] right one position
			for (int k = n; k > i; k--)
				a[(j+k)%a.length] = a[(j+k-1)%a.length];
		}
		a[(j+i)%a.length] = x;
		n++;
	}
	
	public T remove(int i) {
		if (i < 0 || i > n - 1)	throw new IndexOutOfBoundsException();
		T x = a[(j+i)%a.length];
		if (i < n/2) {  // shift a[0],..,[i-1] right one position
			for (int k = i; k > 0; k--)
				a[(j+k)%a.length] = a[(j+k-1)%a.length];
			j = (j + 1) % a.length;
		} else { // shift a[i+1],..,a[n-1] left one position
			for (int k = i; k < n-1; k++)
				a[(j+k)%a.length] = a[(j+k+1)%a.length];
		}
		n--;
		if (3*n < a.length) resize();
		return x;
	}
	
	public void clear() {
		n = 0;
		resize();
	}
}
