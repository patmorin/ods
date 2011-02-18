package ods;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class implements the IntervalSet interface for storing a set of
 * disjoint intervals
 * @author morin
 *
 * @param <K>
 */
public class DisjointIntervalSet<K extends Comparable<K>> implements IntervalSet<K> {
	SortedSet<Interval<K>> intervals;
	
	public DisjointIntervalSet() {
		intervals = new TreeSet<Interval<K>>();
	}
	
	public boolean add(Interval<K> i) {
		// TODO: add checking to see if i overlaps anything in the set
		//       -- if so, don't add it and return false
		return intervals.add(i);
	}

	public void clear() {
		intervals.clear();
	}

	public boolean contains(K x) {
		// TODO Add code for searching here.  See Interval.main() for an example
		SortedSet<Interval<K>> ts = 
			intervals.tailSet(new Interval<K>(x, x));
		if (ts.size() > 0) {
			Interval<K> u = ts.first();
			return u.contains(x);
		}
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DumbIntervalSet.runTests(new DisjointIntervalSet<Integer>());
	}

}
