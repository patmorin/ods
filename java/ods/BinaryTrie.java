package ods;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class BinaryTrie<Node extends BinaryTrie.Nöde<Node,T>, T> implements SSet<T> {
	
	public static class Nöde<Node extends Nöde<Node, T>, T>  {
		T x;
		Node parent;
		Node[] child;
		Node jump;
		@SuppressWarnings("unchecked")
		Nöde() {
			child = (Node[])Array.newInstance(getClass(), 2);
		}
		public String toString() {
			return "{" + String.valueOf(x) + "}";
		}
	}
	
	
	protected static final int prev = 0;
	protected static final int next = 1;
	protected static final int left = 0;
	protected static final int right = 1;

	
	protected static int w = 32;
	
	/**
	 * The root node
	 */
	protected Node r;
	
	/**
	 * The dummy node in the doubly-linked list
	 */
	protected Node dummy;
	
	/**
	 * For converting elements of type T into integers
	 */
	protected Integerizer<T> it;

	/**
	 * The number of elements stored in the trie
	 */
	int n;

	/**
	 * To make a node factory
	 */
	protected Node sampleNode;
	
	/**
	 * Allocate a new node
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	protected Node newNode() {
		try {
			Node u = (Node)sampleNode.getClass().newInstance();
			u.parent = u.child[0] = u.child[1] = null;
			return u;
		} catch (Exception e) {
			return null;
		}
	}

	public BinaryTrie(Node sampleNode, Integerizer<T> it) {
		this.sampleNode = sampleNode;
		dummy = newNode();
		dummy.child[prev] = dummy.child[next] = dummy;
		r = newNode();
		r.jump = dummy;
		this.it = it;
		n = 0;
	}
	
	public String toString() {
		return Utils.collectionToString(this);
	}
	
	public boolean add(T x) {
		int i, c = 0, ix = it.intValue(x);
		Node u = r;
		// 1 - search for ix until falling out of the trie
		for (i = 0; i < w; i++) {
			c = (ix >>> w-i-1) & 1;
			if (u.child[c] == null) break;
			u = u.child[c];
		}		
		if (i == w) return false; // already contains x - abort
		Node pred = (c == right) ? u.jump : u.jump.child[0];
		u.jump = null;  // u will have two children shortly
		// 2 - add path to ix
		for (; i < w; i++) {
			c = (ix >>> w-i-1) & 1;
			u.child[c] = newNode();
			u.child[c].parent = u;
			u = u.child[c];
		}
		u.x = x;
		// 3 - add u to linked list
		u.child[prev] = pred;
		u.child[next] = pred.child[next];
		u.child[prev].child[next] = u;
		u.child[next].child[prev] = u;
		// 4 - walk back up, updating jump pointers
		Node v = u.parent;
		while (v != null) {
			if ((v.child[left] == null 
					&& (v.jump == null || it.intValue(v.jump.x) > ix))
			|| (v.child[right] == null 
					&& (v.jump == null || it.intValue(v.jump.x) < ix)))
				v.jump = u;
			v = v.parent;
		}
		n++;
		return true;
	}
	
	protected void checkIt() {
		checkList();
		checkIt(r, 0);
	}
	
	
	protected void checkIt(Node u, int d) {
		if (d == w) {
			Utils.myassert(u.x != null);
		} else {
			Utils.myassert(u == r || u.child[left] != null || u.child[right] != null);
			if ((u.child[left] == null && u.child[right] != null)
				|| (u.child[right] == null && u.child[left] != null)) {
				Utils.myassert(u.jump.x != null);
			}
			if (u.child[left] != null && u.child[right] != null)
				Utils.myassert(u.jump == null);
			if (u.child[left] != null) 
				checkIt(u.child[left], d+1);
			if (u.child[right] != null)
				checkIt(u.child[right], d+1);
		}
	}

	protected void checkList() {
		Node u = dummy.child[right];
		do {
			if (u.child[right] != dummy) 
				Utils.myassert(it.intValue(u.x) < it.intValue(u.child[right].x));
			Utils.myassert(u.child[left].child[right] == u);
			Utils.myassert(u.child[right].child[left] == u);
			u = u.child[right];
		} while (u != dummy);
	}

	public T find(T x) {
		int i, c = 0, ix = it.intValue(x);
		Node u = r;
		for (i = 0; i < w; i++) {
			c = (ix >>> w-i-1) & 1;
			if (u.child[c] == null) break;
			u = u.child[c];
		}
		if (i == w) return u.x;  // found it
		u = (c == 0) ? u.jump : u.jump.child[next]; 
		return u == dummy ? null : u.x;
	}

	public boolean remove(T x) {
		// 1 - find leaf, u, containing x
		int i, c, ix = it.intValue(x);
		Node u = r;
		for (i = 0; i < w; i++) {
			c = (ix >>> w-i-1) & 1;
			if (u.child[c] == null) return false;
			u = u.child[c];
		}
		// 2 - remove u from linked list
		u.child[prev].child[next] = u.child[next];
		u.child[next].child[prev] = u.child[prev];
		Node v = u;
		// 3 - delete nodes on path to u
		for (i = w-1; i >= 0; i--) {
			c = (ix >>> w-i-1) & 1;
			v = v.parent;
			v.child[c] = null;
			if (v.child[1-c] != null) break;
		}
		// 4 - update jump pointers
		c = (ix >>> w-i-1) & 1;
		v.jump = u.child[1-c];
		v = v.parent;
		i--;
		for (; i >= 0; i--) {
			c = (ix >>> w-i-1) & 1;
			if (v.jump == u) 
				v.jump = u.child[1-c];
			v = v.parent;
		}
		n--;
		return true;
	}


	/**
	 * Part of SSet interface, but not really relevant here
	 * TODO: We can still implement this
	 */
	public Comparator<? super T> comparator() {
		return new Comparator<T>() {
			public int compare(T a, T b) {
				return it.intValue(a) - it.intValue(b);
			}
		};
	}

	public T findGE(T x) {
		return find(x);
	}

	/**
	 * Find the node in the doubly-linked list that comes before
	 * the node that contains (the successor of) x
	 * @param x
	 * @return The node before the node that contains x in the linked list
	 */
	protected Node findPredNode(T x) {
		int i, c = 0, ix = it.intValue(x);
		Node u = r;
		for (i = 0; i < w; i++) {
			c = (ix >>> w-i-1) & 1;
			if (u.child[c] == null) break;
			u = u.child[c];
		}
		Node pred;
		if (i == w) pred = u.child[prev];
		else pred = (c == 1) ? u.jump : u.jump.child[0]; 
		return pred;
	}
	
	public T findLT(T x) {
		Node pred = findPredNode(x);
		return (pred == dummy) ? null : pred.child[next].x;
	}

	/**
	 * This is just a simple linked-list iterator
	 * @author morin
	 *
	 */
	protected class TrieIterator implements Iterator<T> {
		protected Node p;
		
		public TrieIterator(Node p) {
			this.p = p;
		}
		
		public boolean hasNext() {
			return p != dummy;
		}

		public T next() {
			T x = p.x;
			p = p.child[1];
			return x;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public Iterator<T> iterator(T x) {
		Node pred = findPredNode(x);
		return new TrieIterator(pred.child[next]);
	}

	public Iterator<T> iterator() {
		return new TrieIterator(dummy.child[next]);
	}
	
	public int size() {
		return n;
	}
	
	public void clear() {
		n = 0;
		r.child[0] = r.child[1] = null;
		r.jump = dummy;
		dummy.child[0] = dummy.child[1] = dummy;
	}
	
	public static <N extends BinaryTrie.Nöde<N,Integer> > 
	void easyTests(BinaryTrie<N,Integer> t, int n) {
		System.out.println(t.getClass());
		Random rand = new Random(0);
		System.out.println("Adding: ");
		for (int i = 0; i < n; i++) {
			int x = rand.nextInt(100*n);
			System.out.print(x + ((i < n - 1) ? "," : ""));
			t.add(x);
			t.checkIt();
		}
		System.out.println();
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
			t.checkIt();
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
	
	public static void main(String[] args) {
		int n = 20;
		class N<T> extends Nöde<N<T>, T> {};
		N<Integer> node = new N<Integer>();
		class I implements Integerizer<Integer> { 
			public int intValue(Integer i) { return i; } 
		};
		Integerizer<Integer> it = new I(); 
		BinaryTrie<N<Integer>,Integer> t 
			= new BinaryTrie<N<Integer>,Integer>(node, it);
		easyTests(t, n);
	}
}
