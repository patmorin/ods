package ods;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;

/**
 * TODO: Should implement Queue interface
 * @author morin
 *
 * @param <T>
 */
public class MergeableHeap<T extends Comparable<T>> extends
		BinaryTree<MHeapNode<T>> implements Queue<T> {
	
	protected Random rand;
	
	protected int n;
	
	public MergeableHeap() {
		super(new MHeapNode<T>());
		rand = new Random();
	}
	
	protected  MergeableHeap(MHeapNode<T> r) {
		super(new MHeapNode<T>());
		this.r = r;
	}
	
	public boolean add(T x) {
		MHeapNode<T> u = new MHeapNode<T>();
		u.x = x;
		r = merge(u, r);
		r.parent = null;
		n++;
		return true;
	}
	
	public T findMin() {
		return r.x;
	}
	
	public T remove() {
		T x = r.x;
		r = merge(r.left, r.right);
		if (r != null) r.parent = null;
		n--;
		return x;
	}
	
	protected void remove(MHeapNode<T> u) {
		if (u == r) {
			remove();
		} else {
			if (u == u.parent.left) {
				u.parent.left = null;
			} else {
				u.parent.right = null;
			}
			u.parent = null;
			r = merge(r, u.left);
			r = merge(r, u.right);
			r.parent = null;
			n--;
		}
	}
	
	public MHeapNode<T> merge(MHeapNode<T> h1, MHeapNode<T> h2) {
		if (h1 == null) return h2;
		if (h2 == null) return h1;
		if (h2.x.compareTo(h1.x) < 0) {  // ensure h1.x < h2.x
			MHeapNode<T> tmp = h1;
			h1 = h2;
			h2 = tmp;
		}
		if (rand.nextBoolean()) {
			h1.left = merge(h1.left, h2);
			if (h1.left != null) h1.left.parent = h1;
		} else {
			h1.right = merge(h1.right, h2);
			if (h1.right != null) h1.right.parent = h1;
		}
		return h1;
	}
	
	public static void main(String[] args) {
		MergeableHeap<Integer> h = new MergeableHeap<Integer>();
		Random r = new Random();
		int n = 20;
		for (int i = 0; i < n; i++) {
			h.add(r.nextInt(2500));
		}
		for (Integer x : h) {
			System.out.print(x + ",");
		}
		System.out.println();
		for (Integer x : h) {
			System.out.print(x + ",");
		}
		System.out.println();
		while (!h.isEmpty()) {
			System.out.print("" + h.remove() + ",");
		}
		System.out.println("");

		h.clear();
		n = 1000000;
		long start, stop;
		double elapsed;
		System.out.print("performing " + n + " adds...");
		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			h.add(r.nextInt());
		}
		stop = System.nanoTime();
		elapsed = 1e-9*(stop-start);
		System.out.println("(" + elapsed + "s [" 
				+ (int)(((double)n)/elapsed) + "ops/sec])");
		n *= 10;
		System.out.print("performing " + n + " add/removes...");
		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			if (r.nextBoolean()) {
				h.add(r.nextInt());
			} else {
				h.remove();
			}
		}
		stop = System.nanoTime();
		elapsed = 1e-9*(stop-start);
		System.out.println("(" + elapsed + "s [" 
				+ (int)(((double)n)/elapsed) + "ops/sec])");
	}

	public T element() {
		if (r == null) throw new NoSuchElementException();
		return r.x;
	}

	public boolean offer(T x) {
		return add(x);
	}

	public T peek() {
		return r == null ? null : r.x;
	}

	public T poll() {
		return r == null ? null : remove();
	}

	public boolean addAll(Collection<? extends T> c) {
		for (T x : c) add(x);
		return !c.isEmpty();
	}

	public boolean contains(Object x) {
		for (T y : this) {
			if (y.equals(x)) return true;
		}
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		for (Object x : c) {
			if (!contains(x)) return false;
		}
		return true;
	}

	public Iterator<T> iterator() {
		class MHI implements Iterator<T> {
			protected MHeapNode<T> w, prev;
			public MHI(MHeapNode<T> iw) {
				w = iw;
			}
			public boolean hasNext() {
				return w != null;
			}
			public T next() {
				T x = w.x;
				prev = w;
				w = nextNode(w);
				return x;
			}
			public void remove() {
				MergeableHeap.this.remove(prev);
			}
		}
		MHeapNode<T> w = r;
		while (w.left != null) w = w.left;
		return new MHI(w);
	}

	public boolean remove(Object x) {
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			if (it.next().equals(x)) {
				remove();
				return true;
			}
		}
		return false;
	}

	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		for (Object x : c) {
			modified = modified || remove(x);
		}
		return modified;
	}

	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			if (!c.contains(it.next())) {
				it.remove();
				modified = true;
			}
		}
		return modified;
	}

	public int size() {
		return n;
	}
	
	public void clear() {
		r = null;
		n = 0;
	}
	
	public Object[] toArray() {
		Object[] a = new Object[n];
		int i = 0;
		for (T x : this) {
			a[i++] = x;
		}
		return a;
	}

	@SuppressWarnings("unchecked")
	public <T2> T2[] toArray(T2[] a) {
		if (a.length < n) {
			a = (T2[])Array.newInstance(a.getClass().getComponentType(), n);
		}
		int i = 0;
		for (T x : this) {
			a[i++] = (T2)x;
		}
		return a;
	}
}
