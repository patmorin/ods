package ods;

import java.lang.reflect.Array;

public class ScapegoatTree2<T extends Comparable<T>> extends ScapegoatTree<T> {
	public ScapegoatTree2() {
		super(new ScapegoatNode<T>());
	}
	
	@SuppressWarnings("unchecked")
	protected void rebuild(ScapegoatNode<T> u) {
		int ns = size(u);
		ScapegoatNode<T> p = u.parent;
		ScapegoatNode<T>[] a = (ScapegoatNode<T>[]) Array.newInstance(
				sampleNode.getClass(), ns);
		packIntoArray(u, a, 0);
		if (p == null) {
			root = buildBalanced(a, 0, ns);
			root.parent = null;
		} else if (p.right == u) {
			p.right = buildBalanced(a, 0, ns);
			p.right.parent = p;
		} else {
			p.left = buildBalanced(a, 0, ns);
			p.left.parent = p;
		}
	}

	/**
	 * A recursive helper that packs the subtree rooted at u into
	 * a[i],...,a[i+size(u)-1]
	 * 
	 * @param u
	 * @param a
	 * @param i
	 * @return size(u)
	 */
	protected int packIntoArray(ScapegoatNode<T> u, ScapegoatNode<T>[] a, int i) {
		if (u == null) {
			return i;
		}
		i = packIntoArray(u.left, a, i);
		a[i++] = u;
		return packIntoArray(u.right, a, i);
	}

	/**
	 * A recursive helper that builds a perfectly balanced subtree out of
	 * a[i],...,a[i+ns-1]
	 * 
	 * @param a
	 * @param i
	 * @param ns
	 * @return the rooted of the newly created subtree
	 */
	protected ScapegoatNode<T> buildBalanced(ScapegoatNode<T>[] a, int i, int ns) {
		if (ns == 0)
			return null;
		int m = ns / 2;
		a[i + m].left = buildBalanced(a, i, m);
		if (a[i + m].left != null)
			a[i + m].left.parent = a[i + m];
		a[i + m].right = buildBalanced(a, i + m + 1, ns - m - 1);
		if (a[i + m].right != null)
			a[i + m].right.parent = a[i + m];
		return a[i + m];
	}

//	public static void main(String[] args) {
//		ScapegoatTree<Integer> t 
//		   = new ScapegoatTree2<Integer>(new ScapegoatNode<Integer>());
//		int n = 100000;
//		correctnessTests(t, n);
//		t.clear();
//		performanceTests(t);
//	}

}
