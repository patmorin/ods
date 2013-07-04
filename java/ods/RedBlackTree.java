package ods;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class RedBlackTree<T> extends BinarySearchTree<RedBlackTree.Node<T>, T>
		implements SSet<T> {
	
	protected static class Node<T> extends BinarySearchTree.BSTNode<Node<T>,T> {
		byte colour;
	}
	static byte red = 0;
	static byte black = 1;
	
	public RedBlackTree(Comparator<T> c) {
		super(new Node<T>(), new Node<T>(), c);
		nil.colour = black;
	}

	public RedBlackTree() {
		this(new DefaultComparator<T>());
	}

	/**
	 * Make u lighter and its children darker
	 * @param u
	 */
	protected void pushBlack(Node<T> u) {
		u.colour--;
		u.left.colour++;
		u.right.colour++;
	}

	/**
	 * Make u darker and its children lighter
	 * @param u
	 */
	protected void pullBlack(Node<T> u) {
		u.colour++;
		u.left.colour--;
		u.right.colour--;
	}

	protected void flipLeft(Node<T> u) {
		swapColors(u, u.right);
		rotateLeft(u);
	}

	protected void flipRight(Node<T> u) {
		swapColors(u, u.left);
		rotateRight(u);
	}

	/**
	 * Swap the color of u and w
	 * @param u
	 * @param w
	 */
	protected void swapColors(Node<T> u, Node<T> w) {
		byte tmp = u.colour;
		u.colour = w.colour;
		w.colour = tmp;
	}

	public boolean add(T x) {
		Node<T> u = newNode(x);
		u.colour = red;
		boolean added = add(u);
		if (added)
			addFixup(u);
		return added;
	}
	
	/**
	 * Fixup the newly added node u. u is a red node. Each iteration ensures
	 * that (1) u is red, (2) the only red-red edge [if any] is between u and
	 * u.parent (3) the only right-leaning node [if any] is u.parent.
	 * 
	 * @param u
	 */
	protected void addFixup(Node<T> u) {
		while (u.colour == red) {
			if (u == r) { // u is the root - done
				u.colour = black;
				return;
			}
			Node<T> w = u.parent;
			if (w.left.colour == black) { // ensure left-leaning
				flipLeft(w);
				u = w;
				w = u.parent;
			}
			if (w.colour == black)
				return; // no red-red edge = done
			Node<T> g = w.parent; // grandparent of u
			if (g.right.colour == black) {
				flipRight(g);
				return;
			} else {
				pushBlack(g);
				u = g;
			}
		}
	}

	public boolean remove(T x) {
		Node<T> u = findLast(x);
		if (u == nil || c.compare(u.x, x) != 0)
			return false;
		Node<T> w = u.right;
		if (w == nil) {
			w = u;
			u = w.left;
		} else {
			while (w.left != nil)
				w = w.left;
			u.x = w.x;
			u = w.right;
		}
		splice(w);
		u.colour += w.colour;
		u.parent = w.parent;
		removeFixup(u);
		return true;
	}

	/**
	 * Fixup u after the removal of u's parent. u is a node whose color is
	 * 1(black) or 2(double-black). In the latter case we do work to get rid of
	 * the double-black node.
	 * 
	 * @param u
	 */
	protected void removeFixup(Node<T> u) {
		while (u.colour > black) {
			if (u == r) {
				u.colour = black;
			} else if (u.parent.left.colour == red) {
				u = removeFixupCase1(u);
			} else if (u == u.parent.left) {
				u = removeFixupCase2(u);
			} else { 
				u = removeFixupCase3(u);
			}
		}
		if (u != r) { // restore left-leaning property if needed
			Node<T> w = u.parent;
			if (w.right.colour == red && w.left.colour == black) {
				flipLeft(w);
			}
		}
	}

	/**
	 * This case gets applied when the tree looks like this
	 *               1
	 *              / \
	 *             0   2(u)
	 * @param u
	 * @return the next node to fix up
	 */
	protected Node<T> removeFixupCase1(Node<T> u) {
		flipRight(u.parent);
		return u;
	}

	/**
	 * This case gets applied when the tree looks like this
	 *              ?
	 *             / \
	 *         (u)2   1
	 * @param u
	 * @return the next node to fix up
	 */
	protected Node<T> removeFixupCase2(Node<T> u) {
		Node<T> w = u.parent;
		Node<T> v = w.right;
		pullBlack(w); // w.left
		flipLeft(w); // w is now red
		Node<T> q = w.right;
		if (q.colour == red) { // q-w is red-red
			rotateLeft(w);
			flipRight(v);
			pushBlack(q);
			if (v.right.colour == red)
				flipLeft(v);
			return q;
		} else {
			return v;
		}
	}

	/**
	 * This case gets applied when the tree looks like this
	 *              ?
	 *             / \
	 *            1   2(u)
	 * @param u
	 * @return the next node to fix up
	 */
	protected Node<T> removeFixupCase3(Node<T> u) {
		Node<T> w = u.parent;
		Node<T> v = w.left;
		pullBlack(w);       
		flipRight(w); // w is now red
		Node<T> q = w.left;
		if (q.colour == red) { // q-w is red-red
			rotateRight(w);
			flipLeft(v);
			pushBlack(q);
			return q;
		} else {
			if (v.left.colour == red) {
				pushBlack(v); // both v's children are red
				return v;
			} else { // ensure left-leaning
				flipLeft(v);
				return w;
			}
		}				
	}
	
	
	/**
	 * Debugging function that verifies the red-black tree properties
	 */
	protected void verify() {
		if (size(r) != n)
			throw new IllegalArgumentException("size is incorrect");
		verify(r);
	}
	
	/**
	 * Debugging function that verifies the red-black tree properties
	 * for the subtree rooted at u
	 * @param u
	 * @return the black height of the node u
	 */
	protected int verify(Node<T> u) {
		if (u == nil)
			return u.colour;
		if (u.colour < red || u.colour > black)
			throw new AssertionError("Invalid color: " + u.colour);
		if (u.colour == red)
			if (u.left.colour == red || u.right.colour == red)
				throw new AssertionError("red-red edge found");
		if (u.right.colour == red && u.left.colour != red)
			throw new AssertionError("non-left-leaning node found");
		int dl = verify(u.left);
		int dr = verify(u.right);
		if (dl != dr)
			throw new AssertionError("black-height property violated");
		return dl + u.colour;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RedBlackTree<Integer> s = new RedBlackTree<Integer>();
		// sorted sequence
		for (int i = 0; i < 100; i++) {
			s.add(i);
		}
		for (Integer x : s) {
			System.out.print(x + " ");
		}
		System.out.println();
		s.clear();
		
		// reverse sorted sequence
		for (int i = 99; i >= 0; i--) {
			s.add(i);
		}
		for (Integer x : s) {
			System.out.print(x + " ");
		}
		System.out.println();
		s.clear();
		
		// pseudorandom sequence
		for (int i = 1; i <= 101; i++) {
			s.add((73*i)%101);
		}
		for (Integer x : s) {
			System.out.print(x + " ");
		}
		System.out.println();
		// pseudorandom sequence
		for (int i = 1; i <= 50; i++) {
			s.remove((i*89)%101);
		}
		for (Integer x : s) {
			System.out.print(x + " ");
		}
		System.out.println();
		System.out.print("Comparing to TreeSet...");
		System.out.flush();
		SortedSet<Integer> ts = new TreeSet<Integer>();
		s.clear();
		Random rand = new Random();
		int n = 100000;
		for (int i = 0; i < n; i++) {
			Integer x = rand.nextInt();
			ts.add(x);
			s.add(x);
		}
		s.verify();
		Utils.myassert(ts.size() == s.size());
		Iterator<Integer> tsi = ts.iterator();
		Iterator<Integer> si = s.iterator();
		while (tsi.hasNext()) {
			Utils.myassert(tsi.next().equals(si.next()));
		}
		Utils.myassert(s.size() == ts.size());
		for (int i = 0; i < n/2; i++) {
			Integer x = rand.nextInt();
			Integer y = s.findGE(x);
			if (y != null) {
				s.remove(y);
				ts.remove(y);
			}
		}
		s.verify();
		Utils.myassert(ts.size() == s.size());
		tsi = ts.iterator();
		si = s.iterator();
		while (tsi.hasNext()) {
			Utils.myassert(tsi.next().equals(si.next()));
		}
		System.out.println("done");
	}
	

}
