package ods;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * This class is a wrapper that allows any class that implements SSet<T> to
 * allow it to also implement SortedSet<T>.
 * 
 * @author morin
 * 
 * @param <T>
 */
public class SortedSSet<T> extends AbstractSet<T> implements SortedSet<T> {
	protected SSet<T> s;

	public SortedSSet(SSet<T> s) {
		this.s = s;
	}

	public Comparator<? super T> comparator() {
		return s.comparator();
	}

	public T first() {
		return s.findGE(null);
	}

	public T last() {
		return s.findLT(null);
	}

	public SortedSet<T> headSet(T b) {
		return new RangeSSet<T>(s, null, b);
	}

	public SortedSet<T> subSet(T a, T b) {
		return new RangeSSet<T>(s, a, b);
	}

	public SortedSet<T> tailSet(T a) {
		return new RangeSSet<T>(s, a, null);
	}

	public boolean add(T x) {
		return s.add(x);
	}

	public void clear() {
		s.clear();
	}

	@SuppressWarnings("unchecked")
	public boolean contains(Object o) {
		T y = s.findGE((T) o);
		return y != null && y.equals(o);
	}

	public Iterator<T> iterator() {
		return s.iterator();
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object x) {
		return s.remove((T) x);
	}

	public int size() {
		return s.size();
	}

	public boolean isEmpty() {
		return !s.iterator().hasNext();
	}

	public static void main(String[] args) {
		Runtime r = Runtime.getRuntime();
		int n = 500000;
		Collection<SortedSet<Integer>> css = new ArrayList<SortedSet<Integer>>();
		css.add(new SortedSSet<Integer>(new SkiplistSet<Integer>()));
		css.add(new SortedSSet<Integer>(new SkiplistSet2<Integer>()));
		// css.add(new SortedSSet<Integer>(new ScapegoatTree<Integer>()));
		while (1 < 2) {
			Testum.sortedSetSpeedTests(css, n);
			r.gc();
		}
	}
}
