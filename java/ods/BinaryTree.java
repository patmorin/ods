package ods;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class BinaryTree<Node extends BinaryTreeNode<Node>> {
	/**
	 * Used to make a mini-factory
	 */
	protected Node sampleNode;
	
	/**
	 * The root of this tree
	 */
	Node r;

	/**
	 * Create a new instance of this class
	 * @param isampleNode
	 */
	protected BinaryTree(Node sampleNode) {
		this.sampleNode = sampleNode;
	}
	
	/**
	 * Create a new instance of this class
	 * @warning child must set sampleNode before anything that 
	 * might make calls to newNode()
	 */
	protected BinaryTree() { }
	
	/**
	 * Allocate a new node for use in this tree
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	protected Node newNode() {
		try {
			return (Node)sampleNode.getClass().newInstance();	
		} catch (Exception e) {
			return null;
		}
	}
	
	public int depth(Node u) {
		int d = 0;
		while (u != r) {
			u = u.parent;
			d++;
		}
		return d;
	}
	
	/**
	 * Compute the size (number of nodes) of this tree
	 * @return the number of nodes in this tree
	 */
	public int size() {
		return size(r);
	}
	
	/**
	 * @return the size of the subtree rooted at u
	 */
	protected int size(Node u) {
		if (u == null)
			return 0;
		return 1 + size(u.left) + size(u.right);
	}
	
	public int height() {
		return height(r);
	}
	
	/**
	 * @return the size of the subtree rooted at u
	 */
	protected int height(Node u) {
		if (u == null)
			return -1;
		return 1 + Utils.max(height(u.left), height(u.right));
	}

	
	/**
	 * @return
	 */
	public boolean isEmpty() {
		return r == null;
	}
	
	/**
	 * Make this tree into the empty tree
	 */
	public void clear() {
		r = null;
	}
	
	/**
	 * Return a representation of this tree as a string
	 */
	public String toString() {
		return toString(r, "  ");
	}
	
	/**
	 * Return a string representation of the subtree rooted at u
	 * @param u the root of the subtree
	 * @param indent the amount to indent the string by
	 * @return the string representation
	 */
	protected String toString(Node u, String prefix) {
		String s = prefix.substring(0, prefix.length()-2);
		if (u == null)
			return s + "null";
		s += "+-+" + u.toString() + "\n";
		s += toString(u.left, prefix + "| ") + "\n";
		s += toString(u.right, prefix + "  ");
		return s;
	}

	/**
	 * Create a complete binary tree with n nodes
	 * @param <Node>
	 * @param t the tree to create
	 * @param n the number of nodes in the tree
	 */
	static <Node extends BinaryTreeNode<Node>> void completeBinaryTree(BinaryTree<Node> t, int n) {
		Queue<Node> q = new LinkedList<Node>();
		t.clear();
		if (n == 0)
			return;		
		t.r = t.newNode();
		q.add(t.r);
		while (--n > 0) {
			Node u = q.remove();
			u.left = t.newNode();
			u.left.parent = u;
			q.add(u.left);
			if (--n > 0) {
				u.right = t.newNode();
				u.right.parent = u;
				q.add(u.right);
			}
		}
	}
	
	/**
	 * Create a new full binary tree whose expected number of nodes is n
	 * @param <Node>
	 * @param t
	 * @param n
	 */
	static <Node extends BinaryTreeNode<Node>> void galtonWatsonFullTree(BinaryTree<Node> t, int n) {
		Random r = new Random();
		Queue<Node> q = new LinkedList<Node>();
		t.clear();
		t.r = t.newNode();
		q.add(t.r);
		double p = (0.5 - (1.0)/(n+n));
		while (!q.isEmpty()) {
			Node u = q.remove();
			if (r.nextDouble() < p) {
				u.left = t.newNode();
				u.left.parent = u;
				q.add(u.left);
				u.right = t.newNode();
				u.right.parent = u;
				q.add(u.right);
			}
		}
	}
	
	public void traverse(Node u) {
		if (u == null) return;
		traverse(u.left);
		traverse(u.right);
	}

	public void traverse2() {
		Node u = r, prev = null, next;
		while (u != null) {
			if (prev == u.parent) {
				if (u.left != null) next = u.left;
				else if (u.right != null) next = u.right;
				else next = u.parent;
			} else if (prev == u.left) {
				if (u.right != null) next = u.right;
				else next = u.parent;
			} else {
				next = u.parent;
			}
			prev = u;
			u = next;
		}
	}

	
	public int size2() {
		Node u = r, prev = null, next;
		int n = 0;
		while (u != null) {
			if (prev == u.parent) {
				n++;
				if (u.left != null) next = u.left;
				else if (u.right != null) next = u.right;
				else next = u.parent;
			} else if (prev == u.left) {
				if (u.right != null) next = u.right;
				else next = u.parent;
			} else {
				next = u.parent;
			}
			prev = u;
			u = next;
		}
		return n;
	}
	

	
	/**
	 * Create a new full binary tree whose expected number of nodes is n
	 * @param <Node>
	 * @param t
	 * @param n
	 */
	static <Node extends BinaryTreeNode<Node>> void galtonWatsonTree(BinaryTree<Node> t, int n) {
		Random r = new Random();
		Queue<Node> q = new LinkedList<Node>();
		t.clear();
		t.r = t.newNode();
		q.add(t.r);
		double p = 0.5 - 1.0/(n+n);
		while (!q.isEmpty()) {
			Node u = q.remove();
			if (r.nextDouble() < p) {
				u.left = t.newNode();
				u.left.parent = u;
				q.add(u.left);
			} if (r.nextDouble() < p) {
				u.right = t.newNode();
				u.right.parent = u;
				q.add(u.right);
			}
		}
	}

	/**
	 * Copy the structure of this into the tree t
	 * @param <N2>
	 * @param t
	 */
	public <N2 extends BinaryTreeNode<N2>> void	copyTo(BinaryTree<N2> t) {
		t.clear();
		t.r = copyTo(r, t);
	}
	
	public void bfTraverse() {
		Queue<Node> q = new LinkedList<Node>();
		q.add(r);
		while (!q.isEmpty()) {
			Node u = q.remove();
			if (u.left != null) q.add(u.left);
			if (u.right != null) q.add(u.left);
		}
	}

	/**
	 * A recursive helper for the copyTo method
	 * @param <N2>
	 * @param u
	 * @param t
	 * @return
	 */
	protected <N2 extends BinaryTreeNode<N2>> N2 copyTo(Node u, BinaryTree<N2> t) {
		if (u == null) 
			return null;
		N2 w = t.newNode();
		w.left = copyTo(u.left, t);
		w.right = copyTo(u.right, t);
		return w;
	}

	/**
	 * Find the node that follows u in an in-order traversal
	 * @param w
	 * @return
	 */
	protected Node nextNode(Node w) {
		if (w.right != null) {
			w = w.right;
			while (w.left != null)
				w = w.left;
		} else {
			while (w.parent != null && w.parent.left != w)
				w = w.parent;
			w = w.parent;
		}
		return w;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BinaryTree<SimpleBinaryTreeNode> t = new BinaryTree<SimpleBinaryTreeNode>(new SimpleBinaryTreeNode());
		completeBinaryTree(t, 3999);
		System.out.println("size = (" + t.size() + "," + t.size2() + ")");
		// System.out.println("height = (" + t.height() + "," + t.height2() + ")");
		galtonWatsonTree(t, 4000);
		System.out.println("size = (" + t.size() + "," + t.size2() + ")");
		// System.out.println(t.toString());
	}

}
