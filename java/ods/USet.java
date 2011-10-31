package ods;

import java.util.Iterator;

public interface USet<T> {
	public int size();
	public boolean add(T x);
	public T remove(T x);
	public T find(T x);
	public Iterator<T> iterator();
	public void clear();
}
