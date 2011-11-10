package ods;

import java.util.AbstractList;
import java.util.Collection;

/**
 * This is a copy of the JCF class ArrayList.  It implements the List
 * interface as a single array a.  Elements are stored at positions
 * a[0],...,a[size()-1].  Doubling/halving is used to resize the array
 * a when necessary. 
 * @author morin
 *
 * @param <T> the type of objects stored in the List
 */
public class ArrayStack<T> extends AbstractList<T> {
	/**
	 * keeps track of the class of objects we store
	 */
	Factory<T> f;
	
	/**
	 * The array used to store elements
	 */
	T[] a;
	
	/**
	 * The number of elements stored
	 */
	int n;
	
	/**
	 * Resize the internal array
	 */
	protected void resize() {
		T[] b = f.newArray(Math.max(n*2,1));
		for (int i = 0; i < n; i++) {
			b[i] = a[i];
		}
		a = b;
	}

	/**
	 * Resize the internal array
	 */
	protected void resize(int nn) {
		T[] b = f.newArray(nn);
		for (int i = 0; i < n; i++) {
			b[i] = a[i];
		}
		a = b;
	}

	/**
	 * Constructor
	 * @param t0 the type of objects that are stored in this list
	 */
	public ArrayStack(Class<T> t) {
		f = new Factory<T>(t);
		a = f.newArray(1);
		n = 0;
	}

	public T get(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		return a[i];
	}
	
	public int size() {
		return n;
	}
	
	public T set(int i, T x) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		T y = a[i];
		a[i] = x;
		return y;
	}
	
	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		if (n + 1 > a.length) resize();
		for (int j = n; j > i; j--) 
			a[j] = a[j-1];
		a[i] = x;
		n++;
	}
	
	public T remove(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		T x = a[i];
		for (int j = i; j < n-1; j++) 
			a[j] = a[j+1];
		n--;
		if (a.length >= 3*n) resize();
		return x;
	}

	// The following methods are not strictly necessary. The parent
	// class, AbstractList, has default implementations of them, but
	// our implementations are more efficient - especially addAll
	
	/**
	 * A small optimization for a frequently used method
	 */
	public boolean add(T x) {
		if (n + 1 > a.length) resize();
		a[n++] = x;
		return true;
	}
	
	/**
	 * We override addAll because AbstractList implements it by 
	 * repeated calls to add(i,x), which can take time 
	 * O(size()*c.size()).  This happens, for example, when i = 0.
	 * This version takes time O(size() + c.size()).
	 */
	public boolean addAll(int i, Collection<? extends T> c) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		int k = c.size();
		if (n + k > a.length) resize(2*(n+k));
		for (int j = n+k-1; j >= i+k; j--)
			a[j] = a[j-k];
		for (T x : c)
			a[i++] = x;
		n += k;		
		return true;
	}
	
	/**
	 * We override this method because AbstractList implements by
	 * repeated calls to remove(size()), which takes O(size()) time.
	 * This implementation runs in O(1) time.
	 */
	public void clear() {
		n = 0;
		resize();
	}
}
