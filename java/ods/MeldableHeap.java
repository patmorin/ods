package ods;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
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
public class MeldableHeap<T> extends
		BinaryTree<MeldableHeap.Node<T>> implements Queue<T> {
	
	protected Random rand;
	
	protected int n;
	
	Comparator<T> c;
	
	protected static class Node<T> extends BinaryTree.BTNode<Node<T>> {
		T x;
	}
	
	public MeldableHeap() {
		this(new DefaultComparator<T>());
	}

	public MeldableHeap(Comparator<T> c0) {
		super(new Node<T>());
		c = c0;
		rand = new Random();
		sampleNode = newNode();
	}

	public boolean add(T x) {
		Node<T> u = newNode();
		u.x = x;
		r = merge(u, r);
		r.parent = nil;
		n++;
		return true;
	}
	
	public T findMin() {
		return r.x;
	}
	
	public T remove() {
		T x = r.x;
		r = merge(r.left, r.right);
		if (r != nil) r.parent = nil;
		n--;
		return x;
	}
	
	protected void remove(Node<T> u) {
		if (u == r) {
			remove();
		} else {
			if (u == u.parent.left) {
				u.parent.left = nil;
			} else {
				u.parent.right = nil;
			}
			u.parent = nil;
			r = merge(r, u.left);
			r = merge(r, u.right);
			r.parent = nil;
			n--;
		}
	}
	
	public Node<T> merge(Node<T> h1, Node<T> h2) {
		if (h1 == nil) return h2;
		if (h2 == nil) return h1;
		if (c.compare(h2.x, h1.x) < 0) return merge(h2, h1);
		// now we know h1.x <= h2.x
		if (rand.nextBoolean()) {
			h1.left = merge(h1.left, h2);
			h1.left.parent = h1;
		} else {
			h1.right = merge(h1.right, h2);
			h1.right.parent = h1;
		}
		return h1;
	}
	
	public T element() {
		if (r == nil) throw new NoSuchElementException();
		return r.x;
	}

	public boolean offer(T x) {
		return add(x);
	}

	public T peek() {
		return r == nil ? null : r.x;
	}

	public T poll() {
		return r == nil ? null : remove();
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
			protected Node<T> w, prev;
			public MHI(Node<T> iw) {
				w = iw;
			}
			public boolean hasNext() {
				return w != nil;
			}
			public T next() {
				T x = w.x;
				prev = w;
				w = nextNode(w);
				return x;
			}
			public void remove() {
				MeldableHeap.this.remove(prev);
			}
		}
		Node<T> w = r;
		while (w.left != nil) w = w.left;
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
		r = nil;
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

	public static void main(String[] args) {
		System.out.println("\n==== BinaryHeap ====");
		speedTests(new BinaryHeap<Integer>(Integer.class));
		System.out.println("==== MeldableHeap ====");
		speedTests(new MeldableHeap<Integer>());
	}

	protected static void speedTests(Queue<Integer> q) {
		Random r = new Random();
		q.clear();
		int n = 1000000;
		long start, stop;
		double elapsed;
		System.out.print("performing " + n + " adds...");
		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			q.add(r.nextInt());
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
				q.add(r.nextInt());
			} else {
				q.remove();
			}
		}
		stop = System.nanoTime();
		elapsed = 1e-9*(stop-start);
		System.out.println("(" + elapsed + "s [" 
				+ (int)(((double)n)/elapsed) + "ops/sec])");
		
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
