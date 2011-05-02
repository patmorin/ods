package ods;

public class TreapNode<T extends Comparable<T>> extends BSTNode<TreapNode<T>, T> {
	/**
	 * This node's priority
	 */
	int prio;
}
