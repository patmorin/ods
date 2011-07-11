package ods;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class BinarySearchTree<Node extends BSTNode<Node,T>, T> extends
		BinaryTree<Node> implements SSet<T> {

	protected Comparator<T> c;
	
	/**
	 * The number of nodes (elements) currently in the treap
	 */
	protected int n;
	
	protected Node newNode(T x) {
		Node u = newNode();
		u.x = x;
		return u;
	}

	public BinarySearchTree(Node is, Comparator<T> c) {
		super(is);
		this.c = c; 
	}

	public BinarySearchTree(Node is) {
		this(is, new DefaultComparator<T>());
	}

	/**
	 * Create a new instance of this class
	 * @warning child must set sampleNode before anything that 
	 * might make calls to newNode()
	 */
	public BinarySearchTree() {
		this(null, new DefaultComparator<T>());
	}

	/**
	 * Search for a value in the tree
	 * @return the last node on the search path for x
	 */
	protected Node findLast(T x) {
		Node w = r, prev = null;
		while (w != null) {
			prev = w;
			int comp = c.compare(x, w.x);
			if (comp < 0) {
				w = w.left;
			} else if (comp > 0) {
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
		Node w = r, z = null;
		while (w != null) {
			int comp = c.compare(x, w.x);
			if (comp < 0) {
				z = w;
				w = w.left;
			} else if (comp > 0) {
				w = w.right;
			} else {
				return w;
			}
		}
		return z;
	}

	protected T findEQ(T x) {
		Node u = r;
		while (u != null) {
			int comp = c.compare(x, u.x);
			if (comp < 0) 
				u = u.left;
			else if (comp > 0)
				u = u.right;
			else
				return u.x;
		}
		return null;
	}

	protected T find(T x) {
		Node w = r, z = null;
		while (w != null) {
			int comp = c.compare(x, w.x);
			if (comp < 0) {
				z = w;
				w = w.left;
			} else if (comp > 0) {
				w = w.right;
			} else {
				return w.x;
			}
		}
		return z == null ? null : z.x;
	}

	public T findGE(T u) {
		if (u == null) { // find the minimum value
			Node w = r;
			if (w == null) return null;
			while (w.left != null)
				w = w.left;
			return w.x;
		}
		Node w = findGENode(u);
		return w == null ? null : w.x;
	}

	/**
	 * Search for a value in the tree
	 * @return the last node on the search path for x
	 */
	protected Node findLTNode(T x) {
		Node u = r, z = null;
		while (u != null) {
			int comp = c.compare(x, u.x);
			if (comp < 0) {
				u = u.left;
			} else if (comp > 0) {
				z = u;
				u = u.right;
			} else {
				return u;
			}
		}
		return z;
	}

	public T findLT(T x) {
		if (x == null) { // find the maximum value
			Node w = r;
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
			r = u;              // inserting into empty tree
		} else {
			int comp = c.compare(u.x, p.x);
			if (comp < 0) {
				p.left = u;
			} else if (comp > 0) {
				p.right = u;
			} else {
				return false;   // u.x is already in the tree
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
		Node p = findLast(x);
		return addChild(p, newNode(x));		
	}

	/**
	 * Add a new value
	 * @param x
	 * @return
	 */
	public boolean add(Node u) {
		Node p = findLast(u.x);
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
		if (u == r) {
			r = s;
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
		n--;
	}
	
	/**
	 * Remove the node u from the binary search tree
	 * @param u
	 */
	protected void remove(Node u) {
		if (u.left == null || u.right == null) {
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
	protected void rotateLeft(Node u) {
		Node w = u.right;
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
	protected void rotateRight(Node u) {
		Node w = u.left;
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
		Node u = findLast(x);
		if (u != null) {
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
		Node u = findLast((T)x);
		return u != null && c.compare(u.x, (T)x) == 0;
	}
	
	public boolean containsAll(Collection<?> c) {
		for (Object x : c)
			if (!contains(x))
				return false;
		return true;
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
				w = nextNode(w);
				return x;
			}
			public void remove() {
				BinarySearchTree.this.remove(prev);
			}
		}
		return new BTI(u);
	}

	public Iterator<T> iterator() {
		Node u = r;
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
