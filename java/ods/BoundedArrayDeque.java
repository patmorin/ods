package ods;

/**
 * This implements an ArrayDeque in which the size of the backing array
 * a is specified at the time of creation and it can neither grow nor shrink
 * @author morin
 *
 * @param <T>
 */
public class BoundedArrayDeque<T> extends ArrayDeque<T> {
	
	/**
	 * Create a new instance that can store a maximum of nMax elements
	 * @param t
	 * @param nMax
	 */
	public BoundedArrayDeque(Class<T> t, int nMax) {
		super(t);
		a = f.newArray(nMax);
	}

	protected void grow() {
		throw new IndexOutOfBoundsException("try to add to a full " 
				+ this.getClass());
	}
	
	protected void shrink() {
	}
}
