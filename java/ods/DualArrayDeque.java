package ods;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements the List<T> interface using two Lists 
 * so that insertion/deletions at the front or back of the list
 * are fast.  This can be used as an efficient Deque.
 * @author morin
 *
 * @param <T> the type of data stored in this List
 */
public class DualArrayDeque<T> extends AbstractList<T> {
	/**
	 * The class of objects stored in this structure
	 */
	protected Factory<T> f;
	
	/**
	 * the front "half" of the deque 
	 */
	protected List<T> front;
	
	/** 
	 * the back "half" of the deque
	 */
	protected List<T> back;
	
	/**
	 * Create a new empty List data structure.
	 * 
	 * Subclasses should override this if they want to use
	 * different structures for front and back. 
	 * @return
	 */
	protected List<T> newStack() {
		return new ArrayList<T>();
	}
	
	/**
	 * Constructor
	 * @param t0 the class of the objects stored in this list
	 */
	public DualArrayDeque(Class<T> t) {
		f = new Factory<T>(t);
		front = newStack();
		back = newStack();
	}
	
	public T get(int i) {
		if (i < front.size()) {
			return front.get(front.size()-i-1);
		} else {
			return back.get(i-front.size());
		}
	}
	
	public T set(int i, T x) {
		if (i < front.size()) {
			return front.set(front.size()-i-1, x);
			
		} else {
			return back.set(i-front.size(), x);
		}
	}
	
	public void add(int i, T x) {
		if (i < front.size()) { 
			front.add(front.size()-i, x);
		} else {
			back.add(i-front.size(), x);
		}
		balance();
	}

	/**
	 * Rebalance the elements between front and back
	 * if necessary
	 */
	protected void balance() {
		int n = size();
		if (3*front.size() < back.size()) {
			int s = n/2 - front.size();
			List<T> l1 = newStack();
			List<T> l2 = newStack();
			l1.addAll(back.subList(0,s));
			Collections.reverse(l1);
			l1.addAll(front);
			l2.addAll(back.subList(s, back.size()));
			front = l1;
			back = l2;
		} else if (3*back.size() < front.size()) {
			int s = front.size() - n/2;
			List<T> l1 = newStack();
			List<T> l2 = newStack();
			l1.addAll(front.subList(s, front.size()));
			l2.addAll(front.subList(0, s));
			Collections.reverse(l2);
			l2.addAll(back);
			front = l1;
			back = l2;
		}
	}
	
	public T remove(int i) {
		T x;
		if (i < front.size()) {
			x = front.remove(front.size()-i-1);
		} else {
			x = back.remove(i-front.size());
		}
		balance();
		return x;
	}

	public int size() {
		return front.size() + back.size();		
	}
	
	public void clear() {
		front.clear();
		back.clear();
	}
	
	public static void main(String args[]) {
		List<Integer> l = new DualArrayDeque<Integer>(Integer.class);
		for (int i = 0; i < 20; i++) {
			l.add(new Integer(i));
		}
		System.out.println(l);
		for (int i = -1; i > -20; i--) {
			l.add(0,new Integer(i));
		}
		System.out.println(l);
		
		Integer n = 1000;
		Integer x = 20;
		List<Integer> q = new DualArrayDeque<Integer>(Integer.class);
		for (int i = 0; i < n; i++) {
			q.add(i);
		}
		while (true) {
			q.add(x);
			q.remove(0);
		}
	}
}
