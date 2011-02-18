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
	BinaryTree(Node isampleNode) {
		sampleNode = isampleNode;
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
		if (u == null) {
			return 0;
		}
		return 1 + size(u.left) + size(u.right);
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
			q.add(u.left);
			if (--n > 0) {
				u.right = t.newNode();
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
		double p = ((double)0.5 - ((double)1)/(n+n));
		while (!q.isEmpty()) {
			BinaryTreeNode<Node> u = q.remove();
			if (r.nextDouble() < p) {
				u.left = t.newNode();
				q.add(u.left);
				u.right = t.newNode();
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
		List<Node> l = new ArrayList<Node>();
		Node prev = null;
		l.add(root);
		while (!l.isEmpty()) {
			Node u = l.get(l.size());
			if (u.isLeaf() || prev == u.right) {
				l.remove(l.size());  // return to parent
			} else if (prev == u.left) { // returning from left child
				l.add(u.right);   // visit right child
			} else {              // arrived here from parent 
				l.add(u.left);    // visit left child
			}
			prev = u;
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
			BinaryTreeNode<Node> u = q.remove();
			if (r.nextDouble() < p) {
				u.left = t.newNode();
				q.add(u.left);
			} if (r.nextDouble() < p) {
				u.right = t.newNode();
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
	 * @param args
	 */
	public static void main(String[] args) {
		BinaryTree<SimpleBinaryTreeNode> t = new BinaryTree<SimpleBinaryTreeNode>(new SimpleBinaryTreeNode());
		completeBinaryTree(t, 16);
		System.out.println(t.size());
		System.out.println(t.toString());
	}

}
