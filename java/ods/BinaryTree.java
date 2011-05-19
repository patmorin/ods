package ods;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
	Node root;

	/**
	 * Create a new instance of this class
	 * @param isampleNode
	 */
	BinaryTree(Node sampleNode) {
		this.sampleNode = sampleNode;
	}
	
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
		while (u != root) {
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
		return size(root);
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
		return height(root);
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
		return root == null;
	}
	
	/**
	 * Make this tree into the empty tree
	 */
	public void clear() {
		root = null;
	}
	
	/**
	 * Return a representation of this tree as a string
	 */
	public String toString() {
		return toString(root, "  ");
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
		t.root = t.newNode();
		q.add(t.root);
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
		t.root = t.newNode();
		q.add(t.root);
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
		Node prev = null;
		Node u = root;
		while (prev != root || u != null) {
			if (u == null) {               // external node - return to prev
				u = prev;
				prev = null;
			} else if (prev == u.right) {  // done here - return to parent 
				prev = u;
				u = u.parent;
			} else if (prev == u.left) {   // done left - visit right
				prev = u;
				u = u.right;
			} else if (prev == u.parent) { // first visit - visit left
				prev = u;
				u = u.left;
			} 
		}
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
		t.root = t.newNode();
		q.add(t.root);
		double p = ((double)0.5 - ((double)1)/(n+n));
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
		t.root = copyTo(root, t);
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
	 * @param u
	 * @return
	 */
	protected Node nextNode(Node u) {
		if (u.right != null) {
			u = u.right;
			while (u.left != null)
				u = u.left;
		} else {
			Node prev;
			do {
				prev = u;
				u = u.parent;
			} while (u != null && u.left != prev);
		}
		return u;
	}

	/**
	 * Find the node that precedes u in an in-order traversal
	 * @param u
	 * @return
	 */
	protected Node prevNode(Node u) {
		if (u.left != null) {
			u = u.left;
			while (u.right != null)
				u = u.right;
		} else {
			Node prev;
			do {
				prev = u;
				u = u.parent;
			} while (u != null && u.right != prev);
		}
		return u;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BinaryTree<SimpleBinaryTreeNode> t = new BinaryTree<SimpleBinaryTreeNode>(new SimpleBinaryTreeNode());
		completeBinaryTree(t, 16);
		System.out.println(t.size());
		// System.out.println(t.toString());
		t.traverse(t.root);
		System.out.println();
		t.traverse2();
		System.out.println();
		t.traverse3();
		System.out.println();
	}

}
