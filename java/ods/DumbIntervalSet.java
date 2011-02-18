package ods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a dumb implementation of an IntervalMap.
 * @author morin
 *
 * @param <T>
 * @param <V>
 */
public class DumbIntervalSet<T extends Comparable<T>> implements IntervalSet<T> {
	/**
	 * Maps intervals onto values
	 */
	protected List<Interval<T>> intervals;

	public DumbIntervalSet() {
		intervals = new ArrayList<Interval<T>>();
	}
	
	public boolean add(Interval<T> i) {
		return intervals.add(i);
	}

	public boolean contains(T k) {
		Iterator<Interval<T>> it = intervals.iterator();
		while (it.hasNext()) {
			Interval<T> i = it.next();
			if (i.contains(k)) {
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		intervals.clear();
	}

	public static void runTests(IntervalSet<Integer> is) {
		int ns[] = {10, 10000, 100000};
		for (int n : ns) {
			long start, stop;
			double elapsed;
			List<Interval<Integer>> l = new LinkedList<Interval<Integer>>();
			for (int i = 0; i < n; i++) {
				l.add(new Interval<Integer>(5*i, 5*i+3));
			}
			if (n < 100) {
				System.out.println(l);
				Collections.shuffle(l);
			}

			is.clear();
			if (n >= 100) System.out.print("Adding " + n + " elements...");
			start = System.nanoTime();
			for (Interval<Integer> i : l) {
				is.add(i);
			}
			stop = System.nanoTime();
			elapsed = 1e-9*(stop-start);
			if (n >= 100) System.out.println("(" + elapsed + "s [" 
					+ (int)(((double)n)/elapsed) + "ops/sec])");

			if (n >= 100) System.out.print("Searching " + 5*n + " values...");
			start = System.nanoTime();			
			for (int i = 0; i < 5*n; i++) {
				if (is.contains(i)) {
					Utils.myassert(i % 5 < 3);
					if (n < 100) {
						System.out.print("" + i + " ");
					}
				} else {
					Utils.myassert(i % 5 >= 3);					
				}
			}
			if (n < 100) System.out.println("");
			stop = System.nanoTime();
			elapsed = 1e-9*(stop-start);
			if (n >= 100) System.out.println("(" + elapsed + "s [" 
					+ (int)(((double)n)/elapsed) + "ops/sec])");

		}		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		runTests(new DumbIntervalSet<Integer>());
	}
}
