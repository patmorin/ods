package ods;

import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class RedBlackTree<T> extends BinarySearchTree<RedBlackTree.Node<T>, T>
		implements SSet<T> {

	Node<T> nil2;
	
	protected static class Node<T> extends BSTNode<Node<T>,T> {
		int color;
		public String toString() {
			String[] colors = {"red", "black"};
			return "" + x + " (" + colors[color] + ")";
		}
	}
	static byte red = 0;
	static byte black = 1;
	
	public RedBlackTree() {
		r = sampleNode = nil = new Node<T>();
		nil2 = new Node<T>();
		nil.color = black;
		n = 0;
	}
	
	public boolean add(T x) {
		Node<T> u = newNode(x);
		u.color = red;
		boolean added = add(u);
		if (added)
			addFixup(u);
		// verify();
		return added;
	}
	
	protected void verify() {
		if (size(r) != n)
			throw new IllegalArgumentException("size is incorrect");
		verify(r);
	}
	
	protected int verify(Node<T> u) {
		if (u == nil) return u.color;
		if (u.color < red || u.color > black)
			throw new AssertionError("Invalid color: " + u.color);
		if (u.color == red) 
			if (u.left.color == red || u.right.color == red)
				throw new AssertionError("red-red edge found");
		if (u.right.color == red && u.left.color != red) 
			throw new AssertionError("non-left-leaning node found");
		int dl = verify(u.left);
		int dr = verify(u.right);
		if (dl != dr)
			throw new AssertionError("black-height property violated");
		return dl + u.color;
	}


	/**
	 * Fixup the newly added node u.  u is a red leaf.  Each iteration
	 * ensures that (1) u is red, (2) the only red-red edge [if any] 
	 * is between u and u.parent (3) the only non-left-leaning node
	 * [if any] is u.parent.
	 * @param u
	 */
	protected void addFixup(Node<T> u) {
		while (1<2) {
			if (u == r) {                  // u is the root - done
				u.color = black;
				return;
			}
			Node<T> w = u.parent;
			if (w.left.color == black) {   // ensure left-leaning
				flipLeft(w);
				u = w;
				w = u.parent;
			}
			if (w.color == black) return;  // no red-red edge = done
			Node<T> g = w.parent;          // grandparent - exists because parent is red
			if (g.right.color == black) {        // a red-black problem, not a 2-4 problem
				flipRight(g);
				return;
			} else {
				pushBlack(g);
				u = g;
			}
		}
	}
	
	protected void pushBlack(Node<T> u) {
		u.color--;
		u.left.color++;
		u.right.color++;
	}

	protected void pullBlack(Node<T> u) {
		u.color++;
		u.left.color--;
		u.right.color--;
		Utils.myassert(u.left.color >= red);
		Utils.myassert(u.right.color >= red);
	}

	protected void pushColor(Node<T> u, int d) {
		u.color -= d;
		Utils.myassert(u.color >= red);
		u.left.color += d;
		u.right.color += d;
	}

	protected void swapColors(Node<T> u, Node<T> w) {
		int tmp = u.color;
		u.color = w.color;
		w.color = tmp;
	}
	

	@Override
	public boolean remove(T x) {
		Node<T> u = findLast(x);
		if (u == nil || c.compare(u.x, x) != 0) return false;
		Node<T> w = u.right;
		if (w == nil) {
			w = u;
			u = w.left;   // w has no right child
		} else {
			while (w.left != nil)
				w = w.left;
			u.x = w.x;
			u = w.right;  // w has no right child (by left-leaning property)
		} 
		splice(w);
		u.color += w.color;
		u.parent = w.parent;   // we are setting nil's parent
		if (u.color > black) {
			removeFixup(u);
			Utils.myassert(nil.color == black);
		} else if (u != r && u.parent.right.color == red && u.parent.left.color == black) {
			flipLeft(u.parent);  // restore left-leaning
		}
		// verify();
		return true;
	}

	protected void checkLean(Node<T> u) {
		checkLean(u, 3);
	}
	
	protected void checkLean(Node<T> u, int d) {
		if (d <= 0 || u == nil) return;
		if (u.right.color == red && u.left.color != red)
			throw new AssertionError("Node is not left-leaning");
		checkLean(u.left, d-1);
		checkLean(u.right, d-1);
	}
	
	/**
	 * Fixup node u after a removal of u's parent. u is a double-black node
	 * @param u
	 */
	protected void removeFixup(Node<T> u) {
		while (u.color > black) {
			if (u == r) { 
				u.color = black;
				return;
			}
			Node<T> w = u.parent;
			if (w.left.color == red) {
				flipRight(w);
				u = w.right;
			} else if (u == w.left) {
				Node<T> v = w.right;
				pullBlack(w);       
				flipLeft(w);            // w is now red
				Node<T> q = w.right;
				if (q.color == red) {   // q-w is red-red
					rotateLeft(w);
					flipRight(v);
					pushBlack(q);
					if (v.right.color == red) 
						flipLeft(v);
					checkLean(q);
					u = q;
				} else {
					u = v;
					checkLean(u);
				}
			} else {  // u == u.parent.right
				Node<T> v = w.left;
				pushColor(w, -1);       
				flipRight(w);            // w is now red
				Node<T> q = w.left;
				if (q.color == red) {    // q-w is red-red
					rotateRight(w);
					flipLeft(v);
					pushBlack(q);
					checkLean(q);
					u = q;
				} else {                
					if (v.left.color == red) {
						pushBlack(v);   // both v's children are red
						checkLean(v);
						u = v;
					} else {            // ensure left-leaning
						flipLeft(v);
						u = w;
						checkLean(u);
					}
				}				
			}
		}
		if (u != r) {
			Node<T> w = u.parent;
			if (w.right.color == red && w.left.color == black) {
				flipLeft(w);
			}
		}
	}
	
	protected void flipLeft(Node<T> w) {
		rotateLeft(w);
		swapColors(w, w.parent);
	}

	protected void flipRight(Node<T> w) {
		rotateRight(w);
		swapColors(w, w.parent);
	}

	protected void restoreLean(Node<T> w) {
		if (w != nil && w.left.color == black && w.left.color == red) {
			rotateLeft(w);
			swapColors(w, w.parent);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SSet<Integer> s = new RedBlackTree<Integer>();
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
		int n = 1000000;
		for (int i = 0; i < n; i++) {
			Integer x = rand.nextInt();
			ts.add(x);
			s.add(x);
		}
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
		Utils.myassert(ts.size() == s.size());
		tsi = ts.iterator();
		si = s.iterator();
		while (tsi.hasNext()) {
			Utils.myassert(tsi.next().equals(si.next()));
		}
		System.out.println("done");
	}
	

}
