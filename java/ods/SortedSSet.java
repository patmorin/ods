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
		int n = 500000;
		Collection<SortedSet<Integer>> css = new ArrayList<SortedSet<Integer>>();
		// css.add(new SortedSSet<Integer>(new ScapegoatTree<Integer>()));
		// css.add(new SortedSSet<Integer>(new ScapegoatTree2<Integer>()));
		css.add(new SortedSSet<Integer>(new Treap<Integer>()));
		css.add(new java.util.TreeSet<Integer>());
//		css.add(new SortedSSet<Integer>(new Treap<Integer>()));
		css.add(new SortedSSet<Integer>(new RedBlackTree<Integer>()));
		css.add(new SortedSSet<Integer>(new BTree<Integer>(10,Integer.class)));
		css.add(new SortedSSet<Integer>(new BTree<Integer>(50,Integer.class)));
		css.add(new SortedSSet<Integer>(new BTree<Integer>(100,Integer.class)));
//		css.add(new SortedSSet<Integer>(new ScapegoatTree<Integer>()));
//		css.add(new SortedSSet<Integer>(new SkiplistSSet<Integer>()));
		{ 
			class N<T> extends XFastTrie.Nöde<N<T>,T> {};
			class I implements Integerizer<Integer> { 
				public int intValue(Integer i) { return i; }
			}
			class BT<T> extends BinaryTrie<N<T>,T> { 
				public BT(Integerizer<T> it) { super(new N<T>(), it); }
			};
			class XFT<T> extends XFastTrie<N<T>,T> { 
				public XFT(Integerizer<T> it) { super(new N<T>(), it); }
			};
			css.add(new SortedSSet<Integer>(new BT<Integer>(new I())));
			css.add(new SortedSSet<Integer>(new XFT<Integer>(new I())));
			css.add(new SortedSSet<Integer>(new YFastTrie<Integer>(new I())));
		}
//		css.add(new java.util.TreeSet<Integer>());
		for (SortedSet<Integer> ss : css) {
			System.out.println("Testing sanity of " + Testum.s(ss));
			Testum.sortedSetSanityTests(ss, 100);
		}
		Testum.sortedSetSpeedTests(css, n);
	}
}
