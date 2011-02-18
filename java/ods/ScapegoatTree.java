package ods;


public class ScapegoatTree<T extends Comparable<T>> 
		extends BinarySearchTree<ScapegoatNode<T>,T> {
	/**
	 * The number of elements (and nodes) in the tree
	 */
	int n;
	
	/**
	 * An overestimate of the number of n
	 */
	int q;
	
	public ScapegoatTree(ScapegoatNode<T> is) {
		super(is);
	}
	
	public int size() {
		return n;
	}
	
	public boolean remove(Object x) {
		if (super.remove(x)) {
			n--;
			if (2*n < q) {
				rebuild(root);
				q = n;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Compute the ceiling of log_{3/2}(q)
	 * @param q
	 * @return the ceiling of log_{3/2}(q)
	 */
	protected static final int log32(int q) {
		final double log23 = 2.4663034623764317;
		return (int)Math.ceil(log23*Math.log(q));
	}
	
	/**
	 * Rebuild the subtree rooted at u so that it is perfectly
	 * balanced
	 * @param u
	 */
	// @SuppressWarnings("unchecked")
	protected void rebuild(ScapegoatNode<T> u) {
		// System.out.println(u);
		// if you need to allocate an array of nodes, use code like this:
		// ScapegoatNode<T>[] a = (ScapegoatNode<T>[]) Array.newInstance(
		//		sampleNode.getClass(), ns);

	}

	
	public boolean add(T x) {
		// first do basic insertion, while keeping
		// track of depth
		ScapegoatNode<T> u = newNode();
		u.x = x;
		int d = 0;
		ScapegoatNode<T> w = root;
		if (w == null) {
			root = u;
			n++; q++;
			return true;
		}
		boolean done = false;
		while (!done) {
			int res = x.compareTo(w.x);
			if (res < 0) {
				if (w.left == null) {
					w.left = u;
					u.parent = w;
					done = true;
				} else {
					w = w.left;
				}
			} else if (res > 0) {
				if (w.right == null) {
					w.right = u;
					u.parent = w;
					done = true;
				}
				w = w.right;
			} else {
				return false;
			}
			d++;
		}
		n++; q++;
		if (d > log32(q)) {
			// depth exceeded, find scapegoat
			while (3*size(w) <= 2*size(w.parent))
				w = w.parent;
			rebuild(w.parent);
		}
		return true;
	}

	public static void main(String[] args) {
		ScapegoatTree<Integer> t 
		   = new ScapegoatTree<Integer>(new ScapegoatNode<Integer>());
		int n = 100000;
		correctnessTests(t, n);
		t.clear();
		performanceTests(t);
	}

}
