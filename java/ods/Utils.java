package ods;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;

/**
 * A utility class that holds a bunch of useful static functions and methods
 * @author morin
 *
 */
public class Utils {

	/**
	 * A version of assert(.) that doesn't need -ea java flag
	 * 
	 * @param b
	 *            the boolean assertion value (must be true)
	 * @throws AssertionError
	 */
	protected static void myassert(boolean b) throws AssertionError {
		if (!b) {
			throw new AssertionError();
		}
	}

	/**
	 * Create a string representation of an iterable collection
	 * @param <T> the collection type
	 * @param c the collection
	 * @return
	 */
	public static <T> String collectionToString(Iterable<T> c) {
		StringBuilder b = new StringBuilder();
		b.append("[");
		Iterator<T> it = c.iterator(); 
		while (it.hasNext()) {
			b.append(it.next());
			if (it.hasNext()) b.append(",");
		}
		b.append("]");
		return b.toString();
	}

	/**
	 * Compute an integer square root
	 * 
	 * @param x
	 *            an integer
	 * @return the floor of the square root of x
	 */
	public static final int intSqrt(int x) {
		int z = (int)Math.sqrt(x);
		while (z*z > x) z--;
		while ((z+1)*(z+1) <= x) z++;
		return z;
	}

	/**
	 * Just used for testing and debugging - changes constantly
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(-1 >>> 1);
		Random r = new Random();
		int n = 25;
		Integer[] a = new Integer[n];
		for (int i = 0; i < n; i++) {
			a[i] = r.nextInt(2500);
		}
		for (Integer x : a)
			System.out.print("" + x + ",");
		System.out.println("");
		BinaryHeap.sort(a);
		for (Integer x : a)
			System.out.print("" + x + ",");
		System.out.println("");

	}

	/**
	 * An implementation of the merge sort algorithm
	 * @param <T>
	 * @param a
	 */
	public <T extends Comparable<T>> void mergeSort(T[] a) {
		if (a.length <= 1)
			return;
		T[] a1 = Arrays.copyOfRange(a, 0, a.length/2);
		T[] a2 = Arrays.copyOfRange(a, a.length/2, a.length);
		mergeSort(a1);
		mergeSort(a2);
		merge(a1, a2, a);
	}

	/**
	 * The merge routine used by merge sort
	 * @param <T>
	 * @param a
	 * @param b
	 * @param c
	 */
	public <T extends Comparable<T>> void merge(T[] a, T[] b, T[] c) {
		int i = 0, j = 0, k = 0;
		while (i < a.length || j < b.length) {
			if (j == b.length) {
				c[k++] = a[i++];
			} else if (i == a.length) {
				c[k++] = b[j++];
			} else if (a[i].compareTo(b[j]) < 0) {
				c[k++] = a[i++];
			} else {
				c[k++] = b[j++];
			}
		}
	}
	
	/**
	 * Finds the smallest value greater than or equal to x in a SortedSet
	 * 
	 * @param <T>
	 * @param ss
	 *            a SortedSet
	 * @param x
	 *            the value to search for
	 * @return the smallest value in ss that is at least x, or null if no such
	 *         value exists
	 */
	protected static <T> T findGE(SortedSet<T> ss, T x) {
		SortedSet<T> p = ss.tailSet(x);
		return p.isEmpty() ? null : p.first();
	}

	/**
	 * Finds the largest value that is smaller than x in a SortedSet
	 * 
	 * @param <T>
	 * @param ss
	 *            a SortedSet
	 * @param x
	 *            the value to search for
	 * @return the largest value in ss that is less than x, or null if no such
	 *         value exists
	 */
	protected static <T> T findLT(SortedSet<T> ss, T x) {
		SortedSet<T> p = ss.headSet(x);
		return p.isEmpty() ? null : p.last();
	}

	/**
	 * Check if two objects are equal - handles the case where both objects are
	 * null
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	protected static boolean equals(Object a, Object b) {
		return (a == null && b == null) || a.equals(b);
	}


}
