package ods;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

/**
 * This represents a view of the part of a SortedSSet in the interval [a,b).
 * This implementation adds only a constant additive overhead to methods except
 * size(), which runs in linear time.
 * 
 * @author morin
 * 
 * @param <T>
 */
public class RangeSSet<T> extends SortedSSet<T> {
	T a;
	T b;

	public RangeSSet(SSet<T> s, T a, T b) {
		super(s);
		this.a = a;
		this.b = b;
	}

	public Iterator<T> iterator() {
		class IT implements Iterator<T> {
			protected Iterator<T> it;
			T next, prev;

			public IT(Iterator<T> it) {
				this.it = it;
				if (it.hasNext()) {
					next = it.next();
				}
			}

			public boolean hasNext() {
				return next != null
						&& (b == null || s.comparator().compare(next, b) < 0);
			}

			public T next() {
				if (!hasNext())
					throw new NoSuchElementException();
				prev = next;
				next = it.next();
				return prev;
			}

			public void remove() {
				s.remove(prev);
			}
		}
		return new IT(s.iterator(a));
	}

	@SuppressWarnings("unchecked")
	public boolean contains(Object o) {
		T x = (T) o;
		Comparator<? super T> c = s.comparator();
		if (a != null && c.compare(x, a) < 0)
			return false;
		if (b != null && c.compare(x, b) >= 0)
			return false;
		return super.contains(x);
	}

	public T first() {
		return s.findGE(a);
	}

	public int size() {
		Iterator<T> it = iterator();
		int n = 0;
		while (it.hasNext()) {
			it.next();
			n++;
		}
		return n;
	}

	public T last() {
		return s.findLT(b);
	}

	protected final T max(T x, T y) {
		if (x == null)
			return y;
		if (y == null)
			return x;
		if (s.comparator().compare(x, y) > 0)
			return x;
		return y;
	}

	protected final T min(T x, T y) {
		if (x == null)
			return y;
		if (y == null)
			return x;
		if (s.comparator().compare(x, y) < 0)
			return x;
		return y;
	}

	protected final void rangeCheck(T x) {
		Comparator<? super T> c = s.comparator();
		if ((a != null && c.compare(x, a) < 0)
				|| (b != null && c.compare(x, b) >= 0))
			throw new IllegalArgumentException();
	}

	public boolean add(T x) {
		rangeCheck(x);
		return super.add(x);
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object x) {
		rangeCheck((T) x);
		return super.remove(x);
	}

	public SortedSet<T> subSet(T a, T b) {
		return new RangeSSet<T>(s, max(a, this.a), min(b, this.b));
	}

	public SortedSet<T> headSet(T b) {
		return subSet(a, b);
	}

	public SortedSet<T> tailSet(T a) {
		return subSet(a, b);
	}
}
