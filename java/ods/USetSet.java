package ods;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
	
	@SuppressWarnings("unchecked")
	public boolean remove(Object x) {
		return s.remove((T)x) != null;
	}
	
	public Iterator<T> iterator() {
		return s.iterator();
	}

	public int size() {
		return s.size();
	}
	
	public void clear() {
		s.clear();
	}
	
	public static void main(String[] args) {
		int n = 1000000;
		Collection<Set<Integer>> cs = new ArrayList<Set<Integer>>();
		cs.add(new HashSet<Integer>());
		cs.add(new USetSet<Integer>(new LinearHashTable<Integer>(-1)));
		cs.add(new USetSet<Integer>(new ChainedHashTable<Integer>()));
		Testum.setSpeedTests(cs, n);
	}

}
