package ods;

/**
 * This interface is for a data structure that stores a set of 
 * intervals.  These intervals have endpoints of type k and one
 * can query if the set contains any interval that contains a 
 * particular value
 * @author morin
 *
 * @param <K>
 * @param <V>
 */
public interface IntervalSet<K extends Comparable<K>> {
	/**
	 * Add an interval to the set
	 * @param i the interval i
	 * @return true if i was successfully added to the set and false otherwise
	 */
	public boolean add(Interval<K> i);
	
	/**
	 * Determine if some interval in the set contains x
	 * @param x the value to check
	 * @return true iff x is contained in some interval of our set
	 */
	public boolean contains(K x);
	
	/**
	 * Remove all the elements of this interval set
	 */
	public void clear();
}
