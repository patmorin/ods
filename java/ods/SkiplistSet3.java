package ods;

public class SkiplistSet3<T> extends SkiplistSSet<T> {
	protected Node<T> findPredNode(T x) {
		Node<T> u = sentinel;
		int r = h;
		while (r >= 0) {
			while (u.next[r] != null && c.compare(u.next[r].x,x) < 0)
				u = u.next[r];   // go right in list r
			do {
				r--;               // go down into list r-1
			} while (r >= 0 && u.next[r] == u.next[r+1]);
		}
		return u;
	}
}
