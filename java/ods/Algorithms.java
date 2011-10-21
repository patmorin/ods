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

	/**
	 * Sort an array a whose entries contain integers in {0,...,k-1}
	 * @param a the array to sort
	 * @param k 
	 * @return an array b that contains a sorted version of a
	 */
	public static int[] countingSort(int[] a, int k) {
		int c[] = new int[k];
		for (int i = 0; i < a.length; i++)
			c[a[i]]++;
		for (int i = 1; i < k; i++)
			c[i] += c[i-1];
		int b[] = new int[a.length];
		for (int i = a.length-1; i >= 0; i--)
			b[--c[a[i]]] = a[i];
		return b;
	}

	/**
	 * Sort an array a of non-negative integers
	 * @param a
	 * @param k
	 * @return
	 */
	protected static int d = 8;
	protected static int w = 32;
	public static int[] radixSort(int[] a) {
		int[] b = null;
		for (int p = 0; p < w/d; p++) {
			int c[] = new int[1<<d];
			// the next three for loops implement counting-sort
			b = new int[a.length];
			for (int i = 0; i < a.length; i++)
				c[(a[i] >> d*p)&((1<<d)-1)]++;
			for (int i = 1; i < 1<<d; i++)
				c[i] += c[i-1];
			for (int i = a.length-1; i >= 0; i--)
				b[--c[(a[i] >> d*p)&((1<<d)-1)]] = a[i];
			a = b;
		}
		return b;
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
		int a[] = new int[n];
		for (int i = 0; i < n; i++)
			a[i] = rand.nextInt(1<<30);
		int[] b = radixSort(a);
		for (int x : b) {
			System.out.print(x + ",");
		}
		System.out.println();
		Arrays.sort(a);
		for (int x : a) {
			System.out.print(x + ",");
		}
//		Integer[] a = new Integer[n];
//		for (int i = 0; i < n; i++)
//			a[i] = rand.nextInt(10*n);
//		Integer[] b = Arrays.copyOfRange(a, 0, n);
//		Integer[] c = Arrays.copyOfRange(b, 0, n);
//		heapSort(a);
//		mergeSort(b);
//		quickSort(c);
//		for (int i = 0; i < n; i++)
//			Utils.myassert(a[i].equals(b[i]) && b[i].equals(c[i]));
//		if (n <= 100) {
//			System.out.println(Arrays.asList(a));
//			System.out.println(Arrays.asList(b));
//			System.out.println(Arrays.asList(c));
//		}
	}
}
