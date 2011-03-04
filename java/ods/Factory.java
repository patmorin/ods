package ods;

import java.lang.reflect.Array;

/**
 * An ugly little class for allocating objects and arrays of generic
 * type T.  This is needed to work around limitations of Java generics.
 */
public class Factory<T> {
	Class<T> t;
	
	/**
	 * Return the type associated with this factory
	 * @return
	 */
	public Class<T> type() {
		return t;
	}
	
	/**
	 * Constructor - creates a factory for creating objects and 
	 * arrays of type t(=T)
	 * @param t0
	 */
	public Factory(Class<T> t0) {
		t = t0;
	}
	
	/**
	 * Allocate a new array of objects of type T.
	 * @param n the size of the array to allocate
	 * @return the array allocated
	 */
	@SuppressWarnings({"unchecked"})
	protected T[] newArray(int n) {
		return (T[])Array.newInstance(t, n);
	}

	/**
	 * 
	 * @return
	 */
	public T newInstance() {
		T x;
		try {
			x = t.newInstance();
		} catch (Exception e) {
			x = null;
		}
		return x;
	}
}
