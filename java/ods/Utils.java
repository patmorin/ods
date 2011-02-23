package ods;

import java.util.Arrays;
import java.util.Random;

public class Utils {

	protected static void myassert(boolean b) throws AssertionError {
		if (!b) {
			throw new AssertionError();
		}
	}
	
	public static int max(int a, int b) {
		return a > b ? a : b;
	}

	public static int min(int a, int b) {
		return a < b ? a : b;
	}

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

	public <T extends Comparable<T>> void mergeSort(T[] a) {
		if (a.length <= 1)
			return;
		T[] a1 = Arrays.copyOfRange(a, 0, a.length/2);
		T[] a2 = Arrays.copyOfRange(a, a.length/2, a.length);
		mergeSort(a1);
		mergeSort(a2);
		merge(a1, a2, a);
	}

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

}
