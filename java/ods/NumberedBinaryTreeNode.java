package ods;

public class NumberedBinaryTreeNode extends
		BinaryTreeNode<NumberedBinaryTreeNode> {
	public int preNum, inNum, postNum, bfsNum;
	
	public String toString() {
		return "" + preNum + "," + inNum + "," + postNum + "," + bfsNum;
	}
}
