package ods;

import java.util.Random;

public class SkiplistRope {
	class Node {
		SString s;
		Node[] next;
		int[] length;
		public Node(SString s, int h) {
			this.s = s;
			next = (Node[])new Node[h+1];
			length = new int[h+1];
		}
	}
	
	/**
	 * This node sits on the left side of the skiplist
	 */
	protected Node sentinel;
	
	/**
	 * The maximum height of any element
	 */
	int height;
	
	/**
	 * The number of SStrings stored in the skiplist
	 */
	int n;
	
	/**
	 * A source of random numbers
	 */
	Random r;
	
	public SkiplistRope() {
		n = 0;
		sentinel = new Node(null, 33);
		height = 0;
		r = new Random(0);
	}
	
	public char charAt(int i) {
		Node u = sentinel;
		int r = height - 1;
		int j = -1;   // the index of the first character of u's string
		while (r >= 0) {
			while (u.next[r] != null && j + u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			r--;
		}
		return u.s.charAt(i-j);
	}
}
