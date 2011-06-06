package ods;

/**
 * This class represents a node in a binary tree. This class is abstract and
 * should be subclassed by a concrete class. See, for example the class
 * SimpleBinaryTreeNode.
 * 
 * @author morin
 * 
 * @param <Node>
 *            the class of the children of this node
 */
public class BinaryTreeNode<Node extends BinaryTreeNode<Node>> {
	/**
	 * left child
	 */
	protected Node left;

	/**
	 * right child
	 */
	protected Node right;

	/**
	 * Parent node
	 */
	protected Node parent;	
}
