package ods;

/**
 * A node in a DLList
 * 
 * @author morin
 */
class DLLNode<T> {
	DLLNode<T> prev;
	T x;
	DLLNode<T> next;
	public DLLNode(DLLNode<T> prev, T x, DLLNode<T> next) {
		this.prev = prev;
		this.x = x;
		this.next = next; 
	}
	public DLLNode() {
	}
}