package ods;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class YFastTrie<T> implements SSet<T> {
	protected static class Pair<T> {
		int x;
		STreap<T> t;
		public Pair(int x) { this.x = x; }
		public Pair(int x, STreap<T> t) { this.x = x; this.t = t; }
	}
	protected static class STreap<T> extends Treap<T> {
		/**
		 * Split this treap into a new treap containing all elements less
		 * than or equal to x and leave all elements greater than x in the 
		 * current treap -- assumes x is contained in the current treap
		 * @param x
		 * @return
		 */
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
		/**
		 * Absorb the elements of treap t, which should all be smaller than
		 * all the elements in this
		 * @param t
		 * @return
		 */
		public void absorb(STreap<T> t) {
			Node<T> s = newNode();
			s.right = this.r;
			if (this.r != null) this.r.parent = s;
			s.left = t.r;
			if (t.r != null) t.r.parent = s;
			this.r = s;
			t.r = null;
			trickleDown(s);
			splice(s);
		}
		/**
		 * Override this to throw an exception, since the value of n is not
		 * maintained on these treaps
		 */
		public int size() {
			throw new UnsupportedOperationException();
		}
		public String toString() {
			return Utils.collectionToString(this);
		}
	}
	protected static class Node<T> extends XFastTrie.Nöde<Node<T>,Pair<T>> {};
	protected static final int w = XFastTrie.w;
	protected XFastTrie<Node<T>,Pair<T>> xft;
	protected Integerizer<T> it;
	Random rand;
	int n;
	
	public YFastTrie(Integerizer<T> itx) {
		this.it = itx;
		Integerizer<Pair<T>> it2 = new Integerizer<Pair<T>>() {
			public int intValue(Pair<T> p) {
				return p.x;
			}
		};
		xft = new XFastTrie<Node<T>,Pair<T>>(new Node<T>(), it2);
		xft.add(new Pair<T>(0xffffffff, new STreap<T>()));
		rand = new Random(0);   // FIXME - for debugging only
		n = 0;
	}
	
	public boolean add(T x) {
		int ix = it.intValue(x);
		STreap<T> t = xft.find(new Pair<T>(ix)).t;
		if (t.add(x)) {
			n++;
			if (rand.nextInt(w) == 0) {
				STreap<T> t1 = t.split(x);
				xft.add(new Pair<T>(ix, t1));
			}
			return true;
		} 
		return false;
	}

	public void clear() {
		xft.clear();
		xft.add(new Pair<T>(0xffffffff, new STreap<T>()));
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
		return xft.find(new Pair<T>(it.intValue(x))).t.find(x);
	}

	public T findGE(T x) {
		return xft.find(new Pair<T>(it.intValue(x))).t.find(x);
	}

	public T findLT(T x) {
		int ix = it.intValue(x);
		Node<T> u = xft.findNode(ix);
		T y = u.x.t.findLT(x);
		if (y == null && u.child[0] != xft.dummy)
			y = u.child[0].x.t.findLT(x);
		return y;
	}

	public Iterator<T> iterator(T x) {
		Iterator<Pair<T>> it1 = xft.iterator(new Pair<T>(it.intValue(x)));
		Pair<T> p = it1.next();
		return new MIterator(it1, p.t.iterator(x));
	}

	public boolean remove(T x) {
		int ix = it.intValue(x);
		Node<T> u = xft.findNode(ix);
		boolean ret = u.x.t.remove(x);
		if (ret) n--;
		if (u.x.x == ix && ix != 0xffffffff) {
			STreap<T> t2 = u.child[1].x.t;
			t2.absorb(u.x.t);
			xft.remove(u.x);
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
		public MIterator(Iterator<Pair<T>> it1, Iterator<T> it2) {
			this.it1 = it1;
			this.it2 = it2;
		}
		public MIterator() {
			it1 = xft.iterator();
			if (it1.hasNext()) {
				it2 = it1.next().t.iterator();
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
