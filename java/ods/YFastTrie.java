package ods;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class YFastTrie<T> implements SSet<T> {
	protected static class Pair<T> {
		STreap<T> t;
		T x;
		public Pair(T x) { this.x = x; }
		public Pair(T x, STreap<T> t) { this.x = x; this.t = t; }
	}
	protected static class STreap<T> extends Treap<T> {
		public STreap<T> split(T x) {
			Node<T> u = findLast(x);
			Node<T> s = newNode();
			if (u.right == null) {
				u.right = s;
			} else {
				u = u.right;
				while (u.left != null)
					u = u.left;
				u.left = s;
			}
			s.parent = u;
			s.p = Integer.MIN_VALUE;
			bubbleUp(s);
			this.r = s.right;
			if (this.r != null) this.r.parent = nil;
			STreap<T> ret = new STreap<T>();
			ret.r = s.left;
			if (ret.r != null) ret.r.parent = nil;
			return ret;
		}
		public void absorb(STreap<T> t) {
			Node<T> s = newNode();
			s.left = this.r;
			this.r.parent = s;
			s.right = t.r;
			t.r.parent = s;
			this.r = s;
			t.r = null;
			trickleDown(s);
			splice(s);
		}
		public int size() {
			throw new UnsupportedOperationException();
		}
		public String toString() {
			return Utils.collectionToString(this);
		}
	}
	protected static class N<T> extends XFastTrie.Nöde<N<T>,Pair<T>> {};
	protected XFastTrie<N<T>,Pair<T>> xft;
	STreap<T> overflow;
	protected Integerizer<T> it;
	Random rand;
	int n;
	
	public YFastTrie(Integerizer<T> itx) {
		this.it = itx;
		Integerizer<Pair<T>> it2 = new Integerizer<Pair<T>>() {
			public int intValue(Pair<T> p) {
				return it.intValue(p.x);
			}
		};
		xft = new XFastTrie<N<T>,Pair<T>>(new N<T>(), it2);
		rand = new Random();
		overflow = new STreap<T>();
		n = 0;
	}
	
	@SuppressWarnings("static-access")
	public boolean add(T x) {
		Pair<T> p = xft.find(new Pair<T>(x));
		STreap<T> t = (p == null) ? overflow : p.t;
		if (t.add(x)) {
			n++;
			if (rand.nextInt(xft.w) == 1) {
				STreap<T> t2 = t.split(x);
				xft.add(new Pair<T>(x, t2));
			}
			return true;
		}
		return false;
	}

	public void clear() {
		xft.clear();
		n = 0;
	}

	public Comparator<? super T> comparator() {
		return new Comparator<T>() {
			public int compare(T a, T b) {
				return it.intValue(a) - it.intValue(b);
			}
		};
	}

	public T find(T x) {
		Pair<T> p = xft.find(new Pair<T>(x));
		Treap<T> t = (p == null) ? overflow : p.t;
		return t.find(x);
	}

	public T findGE(T x) {
		Pair<T> p = xft.find(new Pair<T>(x));
		Treap<T> t = (p == null) ? overflow : p.t;
		return t.find(x);
	}

	@Override
	public T findLT(T x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> iterator(T x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(T x) {
		Pair<T> p = xft.find(new Pair<T>(x));
		STreap<T> t = (p == null) ? overflow : p.t;
		boolean ret = t.remove(x);
		if (ret) n--;
		if (p != null && it.intValue(p.x) == it.intValue(x)) {
			// remove from x fast trie
		}
		return ret;
	}

	@Override
	public int size() {
		return n;
	}

	protected class MIterator implements Iterator<T> {
		boolean over;
		Iterator<Pair<T>> it1;
		Iterator<T> it2;
		public MIterator() {
			it1 = xft.iterator();
			if (it1.hasNext()) {
				it2 = it1.next().t.iterator();
			} else {
				it2 = overflow.iterator();
			}
		}
		public boolean hasNext() {
			return it2 != null && it2.hasNext();
		}
		public T next() {
			T x = it2.next();
			while (it2 != null && !it2.hasNext()) {
				if (it1.hasNext()) {
					it2 = it1.next().t.iterator();
				} else if (!over) {
					over = true;
					it2 = overflow.iterator();
				} else {
					it2 = null;
				}
			}
			return x;
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new MIterator();
	}

	public String toString() {
		return Utils.collectionToString(this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		class I implements Integerizer<Integer> { 
			public int intValue(Integer i) { return i; }
		}
		int n = 50;
		YFastTrie<Integer> t = new YFastTrie<Integer>(new I());
		Random rand = new Random(0);
		System.out.println("Adding: ");
		for (int i = 0; i < n; i++) {
			int x = rand.nextInt(100*n);
			System.out.print(x + ((i < n - 1) ? "," : ""));
			t.add(x);
			// t.checkIt();
		}
		System.out.println();
		for (Pair<Integer> p : t.xft) {
			System.out.print(p.x + ",");
		}
		System.out.println();
		for (Pair<Integer> p : t.xft) {
			System.out.print(p.t + ",");
		}
		System.out.println(t.overflow);

		System.out.println(t);
		System.out.print("Searching: ");
		for (int i = 0; i < n; i++) {
			int x = rand.nextInt(100*n);
			System.out.print(x + "=>" + t.find(x) + ",");
		}
		System.out.println();
		System.out.println(t);
		System.out.print("Removing: ");
		for (int i = 0; i < n/2; i++) {
			Integer x = t.find(rand.nextInt(100*n));
			if (x != null) {
				System.out.print(x + ((i < n/2-1) ? "," : ""));
				System.out.flush();
				t.remove(x);
			}
			// t.checkIt();
		}
		System.out.println();
		System.out.println("Size = " + t.size());
		System.out.println(t);
		System.out.print("Searching: ");
		for (int i = 0; i < n; i++) {
			int x = rand.nextInt(100*n);
			System.out.print(x + "=>" + t.find(x) + ",");
		}
		System.out.println();
		System.out.println("done");
	}


}
