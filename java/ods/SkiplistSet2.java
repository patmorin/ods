package ods;

import java.lang.reflect.Array;

public class SkiplistSet2<T> extends SkiplistSet<T> {
	Node[] stack;
	
	public SkiplistSet2() {
		super();
		stack = (Node[])Array.newInstance(Node.class, sentinel.next.length);
	}
	
	public boolean add(T x) {
		Node u = sentinel;
		int r = h;
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null && (comp = c.compare(u.next[r].x,x)) < 0)
				u = u.next[r];
			if (u.next[r] != null && comp == 0) return false;
			stack[r--] = u;
		}
		Node w = new Node(x, pickHeight());
		while (h < w.height())
			stack[++h] = sentinel;
		for (int i = 0; i < w.next.length; i++) {
			w.next[i] = stack[i].next[i];
			stack[i].next[i] = w;
		}
		n++;
		return true;
	}

}
