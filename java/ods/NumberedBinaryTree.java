package ods;

import java.util.LinkedList;
import java.util.Queue;

public class NumberedBinaryTree extends BinaryTree<NumberedBinaryTreeNode> {
	
	public NumberedBinaryTree() {
		super(new NumberedBinaryTreeNode());
	}
	
	/**
	 * Compute a preorder numbering of the nodes in this tree
	 * @return
	 */
	public int preorderNumber() {
		return preorderNumber(root, 0);
	}

	/**
	 * Recursive helper for preorderNumber()
	 * @param u
	 * @param c
	 * @return
	 */
	protected int preorderNumber(NumberedBinaryTreeNode u, int c) {
		if (u == null)
			return c;
		u.preNum = c++;
		c = preorderNumber(u.left, c);
		c = preorderNumber(u.right, c);
		return c;
	}

	/**
	 * Compute a preorder numbering of the nodes in this tree
	 * @return
	 */
	public int inorderNumber() {
		return inorderNumber(root, 0);
	}

	/**
	 * Recursive helper for preorderNumber()
	 * @param u
	 * @param c
	 * @return
	 */
	protected int inorderNumber(NumberedBinaryTreeNode u, int c) {
		if (u == null)
			return c;
		c = inorderNumber(u.left, c);
		u.inNum = c++;
		c = inorderNumber(u.right, c);
		return c;
	}
	
	/**
	 * Compute a preorder numbering of the nodes in this tree
	 * @return
	 */
	public int postorderNumber() {
		return postorderNumber(root, 0);
	}

	/**
	 * Recursive helper for preorderNumber()
	 * @param u
	 * @param c
	 * @return
	 */
	protected  int postorderNumber(NumberedBinaryTreeNode u, int c) {
		if (u == null)
			return c;
		c = postorderNumber(u.left, c);
		c = postorderNumber(u.right, c);
		u.postNum = c++;
		return c;
	}
	
	protected int bfsNumber() {
		int c = 0;
		Queue<NumberedBinaryTreeNode> q = new LinkedList<NumberedBinaryTreeNode>();
		q.add(root);
		while (!q.isEmpty()) {
			NumberedBinaryTreeNode u = q.remove();
			if (u != null) {
				u.bfsNum = c++;
				q.add(u.left);
				q.add(u.right);
			}
		}
		return c;
	}


	public static void main(String args[]) {
		NumberedBinaryTree t = new NumberedBinaryTree();
		completeBinaryTree(t, 12);
		t.preorderNumber();
		t.inorderNumber();
		t.postorderNumber();
		t.bfsNumber();
		System.out.println(t);
	}

}
