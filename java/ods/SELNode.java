package ods;

/**
 * A node in a DLList
 * 
 * @author morin
 */
class SELNode<T> {
	SELNode<T> prev;
	T[] a;
	int s;
	SELNode<T> next;
	public SELNode(SELNode<T> prev, T[] a, SELNode<T> next) {
		this.prev = prev;
		this.a = a;
		this.next = next;
	}
	public SELNode(T[] a) {
		this.a = a;
	}
}