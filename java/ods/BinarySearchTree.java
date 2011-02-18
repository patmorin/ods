package ods;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class BinarySearchTree<Node extends BSTNode<Node,T>,T extends Comparable<T>> extends
		BinaryTree<Node> implements Collection<T> {

	public BinarySearchTree(Node is) {
		super(is);
	}
	
	/**
	 * Search for a value in the tree
	 * @return the last node on the search path for x
	 */
	protected Node findNode(T x) {
		Node w = root, prev = null;
		while (w != null) {
			prev = w;
			int res = x.compareTo(w.x);
			if (res < 0) {
				w = w.left;
			} else if (res > 0) {
				w = w.right;
			} else {
				return w;
			}
		}
		return prev;
	}
	
	/**
	 * Add the node u as a child of node p -- ASSUMES p has no child
	 * where u should be added
	 * @param p
	 * @param u
	 * @return true if the child was added, false otherwise
	 */
	protected boolean addChild(Node p, Node u) {
		if (p == null) {
			root = u;     // inserting into empty tree
		} else {
			int res = u.x.compareTo(p.x);
			if (res < 0) {
				p.left = u;
			} else if (res > 0) {
				p.right = u;
			} else {
				return false;
			}
			u.parent = p;
		}
		return true;		
	}

	/**
	 * Add a new value
	 * @param x
	 * @return
	 */
	public boolean add(T x) {
		Node u = newNode();
		u.x = x;
		return add(u);
	}
	
	protected boolean add(Node u) {
		Node p = findNode(u.x);
		return addChild(p, u);		
	}

	/**
	 * Remove the node u --- ASSUMING u has at most one child
	 * @param u
	 */
	protected void splice(Node u) {
		Node s, p;
		if (u.left != null) {
			s = u.left;
		} else {
			s = u.right;
		}
		if (u == root) {
			root = s;
			p = null;
		} else {
			p = u.parent;
			if (p.left == u) {
				p.left = s;
			} else {
				p.right = s; 
			}
		}
		if (s != null) {
			s.parent = p;
		}
	}
	
	/**
	 * Remove the node u from the binary search tree
	 * @param u
	 */
	protected void remove(Node u) {
		if (u.right == null) {
			splice(u);
		} else {
			Node w = u.right;
			while (w.left != null) 
				w = w.left;
			u.x = w.x;
			splice(w);
		}
	}
	
	/**
	 * Do a left rotation at u
	 * @param u
	 */
	protected void leftRotate(TreapNode<T> u) {
		TreapNode<T> w = u.right;
		w.parent = u.parent;
		if (w.parent != null) {
			if (w.parent.left == u) {
				w.parent.left = w;
			} else {
				w.parent.right = w;
			}
		}
		u.right = w.left;
		if (u.right != null) {
			u.right.parent = u;
		}
		u.parent = w;
		w.left = u;
	}	
	
	/**
	 * Do a right rotation at u
	 * @param u
	 */
	protected void rightRotate(TreapNode<T> u) {
		TreapNode<T> w = u.left;
		w.parent = u.parent;
		if (w.parent != null) {
			if (w.parent.left == u) {
				w.parent.left = w;
			} else {
				w.parent.right = w;
			}
		}
		u.left = w.right;
		if (u.left != null) {
			u.left.parent = u;
		}
		u.parent = w;
		w.right = u;
	}

	/**
	 * Remove a node
	 * @param x
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	public boolean remove(Object ox) {
		T x = (T)ox;
		Node u = findNode(x);
		if (u != null && u.x.compareTo(x) == 0) {
			remove(u);
			return true;
		}
		return false;
	}
	
	public String toString() {
		String s = "[";
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			s += it.next().toString() + (it.hasNext() ? "," : "");
		}
		s += "]";
		return s;
	}
	
	@SuppressWarnings({"unchecked"})
	public boolean contains(Object x) {
		Node u = findNode((T)x);
		return u != null && u.x.compareTo((T)x) == 0;
	}
	
	public boolean containsAll(Collection<?> c) {
		for (Object x : c)
			if (!contains(x))
				return false;
		return true;
	}
	
	public boolean isEmpty() {
		return root == null;
	}
	
	public Iterator<T> iterator() {
		Node u = root;
		if (u == null)
			return iterator(u);
		while (u.left != null)
			u = u.left;
		return iterator(u);
	}
	
	public Iterator<T> iterator(Node u) {
		class BTI implements Iterator<T> {
			protected Node w, prev;
			public BTI(Node iw) {
				w = iw;
			}
			public boolean hasNext() {
				return w != null;
			}
			public T next() {
				T x = w.x;
				prev = w;
				if (w.right != null) {
					w = w.right;
					while (w.left != null)
						w = w.left;
				} else {
					while (w.parent != null && w.parent.left != w)
						w = w.parent;
					w = w.parent;
				}
				return x;
			}
			public void remove() {
				BinarySearchTree.this.remove(prev);
			}
		}
		return new BTI(u);
	}
	
	public boolean retainAll(Collection<?> c) {
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			if (!c.contains(it.next())) {
				it.remove();
			}
		}
		return true;
	}
	
	public boolean removeAll(Collection<?> c) {
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			if (c.contains(it.next())) {
				it.remove();
			}
		}
		return true;
	}

	@SuppressWarnings({"unchecked"})
	public T[] toArray() {
		Object[] a = new Object[size()];
		return toArray((T[])a);
	}

	@SuppressWarnings({"unchecked"})
	public <U> U[] toArray(U[] a) {
		Iterator<T> it = iterator();
		int i = 0;
		while (it.hasNext()) {
			a[i++] = (U)it.next();
		}
		return a;
	}
	
	public boolean addAll(Collection<? extends T> c) {
		for (T x : c)
			add(x);
		return true;
	}
	
	protected static <T> boolean compareSortedSets(Collection<T> a, Collection<T> b) {
		if (a.size() != b.size()) 
			return false;
		for (T x : a) {
			if (!b.contains(x)) return false;
		}
		for (T x : b) {
			if (!a.contains(x)) return false;
		}
		Iterator<T> ita = a.iterator();
		Iterator<T> itb = b.iterator();
		while (ita.hasNext()) {
			if (!ita.next().equals(itb.next()))
				return false;
		}
		return true;
	}
	
	protected static <Node extends BSTNode<Node,Integer>> 
		void correctnessTests(BinarySearchTree<Node,Integer> t, int n) {
		SortedSet<Integer> ss = new TreeSet<Integer>();
		Random r = new Random(0);
		for (int i = 0; i < n; i++) {
			Integer j = r.nextInt();
			t.add(j);
			ss.add(j);
		}
		Utils.myassert(compareSortedSets(t, ss));
		r = new Random(0);
		for (int i = 0; i < n/3; i++) {
			Integer j = r.nextInt(); 
			t.remove(j);
			ss.remove(j);
			j = r.nextInt(); 
			t.remove(j);
			ss.remove(j);
			r.nextInt(2500);
		}
		Utils.myassert(compareSortedSets(t, ss));
	}
	
	protected static <Node extends BSTNode<Node,Integer>> 
		void performanceTests(BinarySearchTree<Node,Integer> t) {
		int ns[] = { 100, 1000, 10000, 1000000 };
		for (int n : ns) {
			Random r = new Random(0);
			t.clear();
			long stop, start;
			double elapsed;
			start = System.nanoTime();
			System.out.print(t.getClass() + ": performing " + n + " random insertions...");
			for (int i = 0; i < n; i++) {
				t.add(r.nextInt());
			}
			stop = System.nanoTime();
			elapsed = 1e-9*(stop-start);
			System.out.println(" (" + elapsed + "s [" 
					+ (int)(((double)n)/elapsed) + "ops/sec])");
			r = new Random(0);
			start = System.nanoTime();
			System.out.print(t.getClass() + ": performing " + n + " random searches...");
			for (int i = 0; i < n; i++) {
				t.contains(r.nextInt());
			}
			stop = System.nanoTime();
			elapsed = 1e-9*(stop-start);
			System.out.println(" (" + elapsed + "s [" 
					+ (int)(((double)n)/elapsed) + "ops/sec])");
			r = new Random(0);
			start = System.nanoTime();
			System.out.print(t.getClass() + ": performing " + n + " random deletions...");
			for (int i = 0; i < n; i++) {
				t.remove(r.nextInt());
			}
			stop = System.nanoTime();
			elapsed = 1e-9*(stop-start);
			System.out.println(" (" + elapsed + "s [" 
					+ (int)(((double)n)/elapsed) + "ops/sec])");
			t.clear();

			start = System.nanoTime();
			System.out.print(t.getClass() + ": performing " + n + " sequential insertions...");
			for (int i = 0; i < n; i++) {
				t.add(i);
			}
			stop = System.nanoTime();
			elapsed = 1e-9*(stop-start);
			System.out.println(" (" + elapsed + "s [" 
					+ (int)(((double)n)/elapsed) + "ops/sec])");
			r = new Random(0);
			start = System.nanoTime();
			System.out.print(t.getClass() + ": performing " + n + " sequential searches...");
			for (int i = 0; i < n; i++) {
				t.contains(i);
			}
			stop = System.nanoTime();
			elapsed = 1e-9*(stop-start);
			System.out.println(" (" + elapsed + "s [" 
					+ (int)(((double)n)/elapsed) + "ops/sec])");
			r = new Random(0);
			start = System.nanoTime();
			System.out.print(t.getClass() + ": performing " + n + " sequential deletions...");
			for (int i = 0; i < n; i++) {
				t.remove(i);
			}
			stop = System.nanoTime();
			elapsed = 1e-9*(stop-start);
			System.out.println(" (" + elapsed + "s [" 
					+ (int)(((double)n)/elapsed) + "ops/sec])");

		}
	}
	
	public static void main(String[] args) {
		BinarySearchTree<SimpleBSTNode<Integer>,Integer> t
		   = new BinarySearchTree<SimpleBSTNode<Integer>,Integer>(new SimpleBSTNode<Integer>());
		int n = 100000;
		correctnessTests(t, n);
	}
}
