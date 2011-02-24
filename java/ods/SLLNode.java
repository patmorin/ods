/**
 * 
 */
package ods;

class SLLNode<T> {
	T x;
	SLLNode<T> next;
	
	public SLLNode(T x, SLLNode<T> next) {
		this.x = x;
		this.next = next;
	}
}