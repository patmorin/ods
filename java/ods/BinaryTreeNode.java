package ods;

/**
 * This class represents a node in a binary tree. This class is abstract and
 * should be subclassed by a concrete class. See, for example the class
 * SimpleBinaryTreeNode.
 * 
 * @author morin
 * 
 * @param <N>
 *            the class of the children of this node
 */
public class BinaryTreeNode<N extends BinaryTreeNode<N>> {
	/**
	 * left child
	 */
	protected N left;

	/**
	 * right child
	 */
	protected N right;

	/**
	 * Parent node
	 */
	protected N parent;
	
	public boolean isLeaf() {
		return left == null && right == null;
	}
}
