package ods;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class implements the IntervalSet interface for storing a set of
 * intervals, which may or may not be disjoint.
 * 
 * @author morin
 *
 * @param <K>
 */
public class OverlappingIntervalSet<K extends Comparable<K>> implements IntervalSet<K> {
	SortedSet<Interval<K>> intervals;
	
	public OverlappingIntervalSet() {
		intervals = new TreeSet<Interval<K>>();
	}
	
	public boolean add(Interval<K> i) {
		// TODO: Add code here
		return false;
	}

	public void clear() {
		intervals.clear();
	}

	public boolean contains(K x) {
		// TODO Add code for searching here.  See Interval.main() for an example
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// run some tests for disjoint intervals
		DumbIntervalSet.runTests(new OverlappingIntervalSet<Integer>());
		
		// TODO: Add some of your own tests here to test overlapping intervals
		//       Try borrowing some code from DumbIntervalSet.runTests()
	}

}
