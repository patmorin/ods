package ods;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class BinaryTrie<Node extends BinaryTrie.Nodez<Node,T>, T> extends
	BinaryTree<Node> implements SSet<T> {
	
	public static class Nodez<Node extends Nodez<Node, T>, T> 
	extends	BinaryTree.BTNode<Node> {
		T x;
		Node jump;
	}

	protected static int w = 32;
	Node dummy;
	Integerizer<T> it;
	
	public BinaryTrie(Node sampleNode, Integerizer<T> it) {
		super(sampleNode);
		r = newNode();
		dummy = newNode();
		dummy.right = dummy.left = dummy;
		this.it = it;
	}
	
	protected class Location {
		int i;
		Node u;
	}
	
	
	/**
	 * Fix all the jump pointers from u up to the root
	 * @param u
	 * @param ix
	 */
	protected void fixJumps(Node u, int ix) {
		Node w = u;
		while (w != null) {
			if (w.jump == nil
			|| (w.left == nil && it.intValue(w.jump.x) > ix)
			|| (w.right == nil && it.intValue(w.jump.x) < ix)) {
				w.jump = u;
			}	
			w = w.parent;
		}
	}

	public boolean add(T x) {
		int i, ix = it.intValue(x);
		Node u = r;
		for (i = 0; i < w; i++) {
			if (((ix >>> w-i-1) & 1) == 0) {
				if (u.left == nil) break;
				u = u.left;
			} else {
				if (u.right == nil) break;
				u = u.right;
			}
		}
		if (i == w) return false;
		// now it's time to start adding nodes
		Node a = u.jump;
		for (; i < w; i++) {
			if (((ix >>> w-i-1) & 1) == 0) {
				u.left = newNode();
				u.left.parent = u;
				u = u.left;
			} else {
				u.right = newNode();
				u.right.parent = u;
				u = u.right;
			}			
		}
		u.x = x;
		// now insert new node into linked list
		if (a == nil) 
			a = dummy;
		else if (it.intValue(a.x) > ix)
			a = a.left;
		u.right = a.right;
		u.left = a.right.left;
		u.left.right = u;
		u.right.left = u;
		
		// finally, fix jump pointers
		fixJumps(u, ix);
		return true;
	}

	public T find(T x) {
		int i = 0, ix = it.intValue(x);
		Node u = r;
		// following code is duplicated in add(x)
		for (i = 0; i < w; i++) {  
			if (((ix >>> w-i-1) & 1) == 0) {
				if (u.left == nil) break;
				u = u.left;
			} else {
				if (u.right == nil) break;
				u = u.right;
			}
		}
		// end dup
		Node a = u.jump;
		if (a == nil || a == dummy) 
			a = dummy;
		else if (it.intValue(a.x) > ix)
			a = a.left;
		return (a.right == dummy) ? null : a.right.x;
	}
	
	public boolean remove(T x) {
		int i = 0, ix = it.intValue(x);
		Node u = r;
		for (i = 0; i < w; i++) {  
			if (((ix >>> w-i-1) & 1) == 0) {
				if (u.left == nil) return false;
				u = u.left;
			} else {
				if (u.right == nil) return false;
				u = u.right;
			}
		}
		// remove u from linked list
		Node b = u;
		u.left.right = u.right;
		u.right.left = u.left;
		u.right = nil;
		// remove path to u
		while (u != r && u.left == nil && u.right == nil) {
			if (u == u.parent.left)
				u.parent.left = nil;
			else // u == u.parent.right 
				u.parent.right = nil;
			u = u.parent;
		}
		// fix jump pointers
		while (u != null) {
			if (u.jump == b) {   // Note - this decision could be moved outside of loop
				if (u.right == nil) {
					u.jump = b.left;
				} else {
					u.jump = b.right;
				}
			}
			u = u.parent;
		}
		return true;
	}


	/**
	 * Part of SSet interface, but not really relevant here
	 * TODO: We can still implement this
	 */
	public Comparator<? super T> comparator() {
		return null;
	}

	@Override
	public T findGE(T x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findLT(T x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> iterator(T x) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			protected Node p = dummy.right;
			
			public boolean hasNext() {
				return p != dummy;
			}
	
			public T next() {
				T x = p.x;
				p = p.right;
				return x;
			}
			
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	protected void checkList() {
		Node u = dummy.right;
		do {
			Utils.myassert(u.left.right == u);
			Utils.myassert(u.right.left == u);
			u = u.right;
		} while (u != dummy);
	}
	
	public static void main(String[] args) {
		class N<T> extends Nodez<N<T>, T> {};
		N<Integer> n = new N<Integer>();
		class I implements Integerizer<Integer> { public int intValue(Integer i) { return i; } };
		Integerizer<Integer> it = new I(); 
		BinaryTrie<N<Integer>,Integer> t = new BinaryTrie<N<Integer>,Integer>(n, it);
		Random rand = new Random();
		for (int i = 0; i < 20; i++) {
			int x = rand.nextInt(2000);
			System.out.print(x + ",");
			t.add(x);
		}
		System.out.println();
		for (Integer x : t)
			System.out.print(x + ",");
		System.out.println();
		for (int i = 0; i < 20; i++) {
			int x = rand.nextInt(2000);
			System.out.print(x + "=>" + t.find(x) + ",");
		}
		System.out.println();
		System.out.print("Removing: ");
		for (int i = 0; i < 10; i++) {
			Integer x = t.find(rand.nextInt(2000));
			if (x != null) {
				System.out.print(x + ",");
				t.remove(x);
			}
			t.checkList();
		}
		System.out.println();
		for (Integer x : t) {
			System.out.print(x + ",");
		}
	}
	
	
}
