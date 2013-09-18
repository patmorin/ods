package ods;

public class XFastTrie<Node extends XFastTrie.Nöde<Node,T>, T> 
	extends BinaryTrie<Node, T> {

	/**
	 * The hash tables used to store prefixes
	 */
	USet<Node>[] t;
	
	protected static class Nöde<N extends Nöde<N, T>, T> 
		extends	BinaryTrie.Nöde<N, T> {
		int prefix;
		@SuppressWarnings("unchecked")
		public boolean equals(Object u) {
			return (u instanceof Nöde<?, ?>)
					&& this.prefix == ((Nöde<N,T>)u).prefix;
		}
		public int hashCode() {
			return prefix;
		}
	}

	@SuppressWarnings("unchecked")
	public XFastTrie(Node sampleNode, Integerizer<T> it)  {
		super(sampleNode, it);
		t = (USet<Node>[])new USet[w+1];
		Node nil = (Node)new XFastTrie.Nöde<Node,T>();
		nil.prefix = Integer.MIN_VALUE;
		for (int i = 0; i <= w; i++) {
			t[i] = new LinearHashTable<Node>(nil);
		}
		t[0].add(r);
	}

	@SuppressWarnings("unchecked")
	public XFastTrie(Integerizer<T> it)  {
		this((Node)new XFastTrie.Nöde<Node,T>(), it);
	}
	
	public boolean add(T x) {
		if (super.add(x)) {
			int i, c = 0, ix = it.intValue(x);
			Node u = r.child[(ix>>>w-1) & 1];
			for (i = 1; i <= w; i++) {
				u.prefix = ix >>> w-i;
				t[i].add(u);
				c = (ix >>> w-i-1) & 1;
				u = u.child[c];
			}
			return true;
		}
		return false;
	}
	
	public boolean remove(T x) {
		// 1 - find leaf, u, containing x
		int i = 0, c, ix = it.intValue(x);
		Node u = r;
		for (i = 0; i < w; i++) {
			c = (ix >>> w-i-1) & 1;
			if (u.child[c] == null) return false;
			u = u.child[c];
		}
		// 2 - remove u from linked list
		Node pred = u.child[prev];   // predecessor
		Node succ = u.child[next];   // successor
		pred.child[next] = succ;
		succ.child[prev] = pred;
		u.child[next] = u.child[prev] = null;
		Node w = u;
		// 3 - delete nodes on path to u
		while (w != r && w.child[left] == null && w.child[right] == null) {
			if (w == w.parent.child[left])
				w.parent.child[left] = null;
			else // u == u.parent.child[right] 
				w.parent.child[right] = null;
			t[i--].remove(w);
			w = w.parent;
		}
		// 4 - update jump pointers
		w.jump = (w.child[left] == null) ? succ : pred;
		w = w.parent;
		while (w != null) {
			if (w.jump == u)
				w.jump = (w.child[left] == null) ? succ : pred;
			w = w.parent;
		}
		n--;
		return true;
	}
	
	protected Node findNode(int ix) {
		// find lowest node that is an ancestor of ix
		int l = 0, h = w+1;
		Node v, u = r, q = newNode();
		while (h-l > 1) {
			int i = (l+h)/2;
			q.prefix = ix >>> w-i;
			if ((v = t[i].find(q)) == null) {
				h = i;
			} else {
				u = v;
				l = i;
			}
		}
		if (l == w) return u;
		Node pred = (((ix >>> w-l-1) & 1) == 1) ? u.jump : u.jump.child[0];
		return (pred.child[next] == dummy) ? null : pred.child[next];
	}
	
	public void clear() {
		super.clear();
		for (USet<Node> s : t) 
			s.clear();
	}
	
	public T find(T x) {
		int l = 0, h = w+1, ix = it.intValue(x);
		Node v, u = r, q = newNode();
		while (h-l > 1) {
			int i = (l+h)/2;
			q.prefix = ix >>> w-i;
			if ((v = t[i].find(q)) == null) {
				h = i;
			} else {
				u = v;
				l = i;
			}
		}
		if (l == w) return u.x;
		Node pred = (((ix >>> w-l-1) & 1) == 1) 
			     ? u.jump : u.jump.child[0];
		return (pred.child[next] == dummy) 
		             ? null : pred.child[next].x;
	}
	
	public static void main(String[] args) {
		class N extends XFastTrie.Nöde<N, Integer> {};
		class I implements Integerizer<Integer> { 
			public int intValue(Integer i) { return i; }
		}
		XFastTrie<N, Integer> t = new XFastTrie<N, Integer>(new I()); 
		BinaryTrie.easyTests(t, 20);
	}
}
