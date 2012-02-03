package ods;

public class ScapegoatTree2<T> extends ScapegoatTree<T> {

	protected void rebuild(Node<T> u) {
		int n = size(u);
		Node<T> p = u.parent;
		// FIXME: check if p is root
		if (p == nil) {
			r = rebuildTree(n,u);
		} else if (p.left == u) {
			u = rebuildTree(n, u);
			p.left = u;
		} else {
			u = rebuildTree(n, u);
			p.right = u;
		}
		u.parent = p;
	}
	
	protected Node<T> flatten(Node<T> x, Node<T> y) {
		if (x == nil) return y;
		x.right = flatten(x.right, y);
		return flatten(x.left, x);
	}
	
	protected Node<T> buildTree(int n, Node<T> x) {
		if (n == 0) {
			x.left = nil;
			return x;
		}
		int k = n/2;
		Node<T> r = buildTree(k, x);
		Node<T> s = buildTree(n-k-1, r.right);
		r.right = s.left;
		if (s.left != nil) s.left.parent = r;
		s.left = r;
		r.parent = s;
		return s;
	}
	
	protected Node<T> rebuildTree(int n, Node<T> scapegoat) {
		Node<T> w = newNode();
		Node<T> z = flatten(scapegoat, w);
		buildTree(n, z);
		return w.left;
	}
}
