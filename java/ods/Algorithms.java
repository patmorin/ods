package ods;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Algorithms {
	protected static Random rand = new Random();
	
	public static <T extends Comparable<T>> void mergeSort(T[] a) {
		mergeSort(a, new DefaultComparator<T>());
	}
	
	public static <T> void mergeSort(T[] a, Comparator<T> c) {
		if (a.length <= 1) return;
		T[] a0 = Arrays.copyOfRange(a, 0, a.length/2);
		T[] a1 = Arrays.copyOfRange(a, a.length/2, a.length);
		mergeSort(a0, c);
		mergeSort(a1, c);
		merge(a0, a1, a, c);
	}

	protected static <T> void merge(T[] a0, T[] a1, T[] a, Comparator<T> c) {
		int i0 = 0, i1 = 0;
		for (int i = 0; i < a.length; i++) {
			if (i0 == a0.length)
				a[i] = a1[i1++];
			else if (i1 == a1.length)
				a[i] = a0[i0++];
			else if (c.compare(a0[i0], a1[i1]) < 0)
				a[i] = a0[i0++];
			else 
				a[i] = a1[i1++];
		}
	}

	
	

	public static <T extends Comparable<T>> void quickSort(T[] a) {
		quickSort(a, new DefaultComparator<T>());
	}

	public static <T> void quickSort(T[] a, Comparator<T> c) {
		quickSort(a, 0, a.length, c);
	}

	
	public static <T extends Comparable<T>> void heapSort(T[] a) {
		BinaryHeap.sort(a, new DefaultComparator<T>());
	}

	public static <T> void heapSort(T[] a, Comparator<T> c) {
		BinaryHeap.sort(a, c);
	}

	
	protected final static <T> void  swap(T[] a, int i, int j) {
		T t = a[i];
		a[i] = a[j];
		a[j] = t;
	}
	
	/**
	 * Run quicksort on the subarray a[i],...,a[i+n-1]
	 * @param <T>
	 * @param a
	 * @param i
	 * @param n
	 * @param c
	 */
	public static <T> void quickSort(T[] a, int i, int n, Comparator<T> c) {
		if (n <= 1) return;
		T x = a[i + rand.nextInt(n)];
		int p = i-1, j = i, q = i+n;
		// a[i..p]<x,  a[p+1..q-1]??x, a[q..i+n-1]>x 
		while (j < q) {
			int comp = c.compare(a[j], x);
			if (comp < 0) {       // move to beginning of array
				swap(a, j++, ++p);
			} else if (comp > 0) {
				swap(a, j, --q);  // move to end of array
			} else {
				j++;              // keep in the middle
			}
		}
		// a[i..p]<x,  a[p+1..q-1]=x, a[q..i+n-1]>x 
		quickSort(a, i, p-i+1, c);
		quickSort(a, q, n-(q-i), c);
	}

	public static void main(String[] args) {
		int n = 100;
		Integer[] a = new Integer[n];
		for (int i = 0; i < n; i++)
			a[i] = rand.nextInt(10*n);
		Integer[] b = Arrays.copyOfRange(a, 0, n);
		Integer[] c = Arrays.copyOfRange(b, 0, n);
		heapSort(a);
		mergeSort(b);
		quickSort(c);
		for (int i = 0; i < n; i++)
			Utils.myassert(a[i].equals(b[i]) && b[i].equals(c[i]));
		if (n <= 100) {
			System.out.println(Arrays.asList(a));
			System.out.println(Arrays.asList(b));
			System.out.println(Arrays.asList(c));
		}
	}
}
