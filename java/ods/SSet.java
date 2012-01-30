package ods;

import java.util.Comparator;
import java.util.Iterator;

/**
 * The SSet<T> interface is a simple interface that allows a class to implement
 * all the functionality of the (more complicated) SortedSet<T> interface. Any
 * class that implements SSet<T> can be wrapped in a SortedSSet<T> to obtain an
 * implementation of SortedSet<T>
 * 
 * @author morin
 * 
 * @param <T>
 * @see SortedSSet<T>
 */
public interface SSet<T> extends Iterable<T> {
	/**
	 * @return the comparator used by this SSet
	 */
	public Comparator<? super T> comparator();

	/**
	 * @return the number of elements in this SSet
	 */
	public int size();

	/**
	 * Find the smallest element in the SSet that is greater than or equal to x.
	 * 
	 * @param x
	 * @return the smallest element in the SSet that is greater than or equal to
	 *         x or null if no such element exists
	 */
	public T find(T x);

	/**
	 * Find the smallest element in the SSet that is greater than or equal to x.
	 * If x is null, return the smallest element in the SSet
	 * 
	 * @param x
	 * @return the smallest element in the SSet that is greater than or equal to
	 *         x or null if no such element exists. If x is null then the
	 *         smallest element in the SSet
	 */
	public T findGE(T x);

	/**
	 * Find the largest element in the SSet that is greater than to x. If x is
	 * null, return the largest element in the SSet
	 * 
	 * @param x
	 * @return the largest element in the SSet that is less than x. If x is null
	 *         then the smallest element in the SSet
	 */
	public T findLT(T x);

	/**
	 * Add the element x to the SSet
	 * 
	 * @param x
	 * @return true if the element was added or false if x was already in the
	 *         set
	 */
	public boolean add(T x);

	/**
	 * Remove the element x from the SSet
	 * 
	 * @param x
	 * @return true if x was removed and false if x was not removed (because x
	 *         was not present)
	 */
	public boolean remove(T x);

	/**
	 * Clear the SSet, removing all elements from the set
	 */
	public void clear();

	/**
	 * Return an iterator that iterates over the elements in sorted order,
	 * starting at the first element that is greater than or equal to x.
	 */
	public Iterator<T> iterator(T x);
}
