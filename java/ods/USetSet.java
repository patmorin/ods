package ods;

import java.util.AbstractSet;
import java.util.Iterator;

public class USetSet<T> extends AbstractSet<T> {
	USet<T> s;
	
	public USetSet(USet<T> s) {
		this.s = s;
	}
	
	@SuppressWarnings("unchecked")
	public boolean contains(Object x) {
		return s.find((T)x) != null;
	}

	public boolean add(T x) {
		return s.add(x);
	}
	
	public T remove(T x) {
		return s.remove(x);
	}
	
	public Iterator<T> iterator() {
		return s.iterator();
	}

	public int size() {
		return s.size();
	}
}
