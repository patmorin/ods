package ods;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class BinarySearchTree<Node extends BSTNode<Node,T>, T extends Comparable<T>> extends
		BinaryTree<Node> implements SSet<T> {

	protected Comparator<T> c;
	
	/**
	 * The number of nodes (elements) currently in the treap
	 */
	protected int n;

	public BinarySearchTree(Node is, Comparator<T> c) {
		super(is);
		this.c = c; 
	}

	public BinarySearchTree(Node is) {
		this(is, new DefaultComparator<T>());
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
	 * Search for a value in the tree
	 * @return the last node on the search path for x
	 */
	protected Node findGENode(T x) {
		Node w = root, z = null;
		while (w != null) {
			int res = c.compare(x, w.x);
			if (res < 0) {
				z = w;
				w = w.left;
			} else if (res > 0) {
				w = w.right;
			} else {
				return w;
			}
		}
		return z;
	}
	
	public T findGE(T x) {
		if (x == null) { // find the minimum value
			Node w = root;
			if (w == null) return null;
			while (w.left != null)
				w = w.left;
			return w.x;
		}
		Node w = findGENode(x);
		return w == null ? null : w.x;
	}

	/**
	 * Search for a value in the tree
	 * @return the last node on the search path for x
	 */
	protected Node findLTNode(T x) {
		Node w = root, z = null;
		while (w != null) {
			int res = c.compare(x, w.x);
			if (res < 0) {
				w = w.left;
			} else if (res > 0) {
				z = w;
				w = w.right;
			} else {
				return w;
			}
		}
		return z;
	}

	public T findLT(T x) {
		if (x == null) { // find the maximum value
			Node w = root;
			if (w == null) return null;
			while (w.right != null)
				w = w.right;
			return w.x;
		}
		Node w = findLTNode(x);
		return w == null ? null : w.x;
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
		n++;
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
		n--;
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
	public boolean remove(T x) {
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
	

	// TODO: make iterators bidirectional?
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

	public Iterator<T> iterator() {
		Node u = root;
		if (u == null)
			return iterator(u);
		while (u.left != null)
			u = u.left;
		return iterator(u);
	}

	public Iterator<T> iterator(T x) {
		return iterator(findGENode(x));
	}
	
	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public void clear() {
		super.clear();
		n = 0;
	}

	public Comparator<? super T> comparator() {
		return c;
	}
}
