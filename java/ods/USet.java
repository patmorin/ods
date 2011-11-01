package ods;

public interface USet<T> extends Iterable<T> {
	public int size();
	public boolean add(T x);
	public T remove(T x);
	public T find(T x);
	public void clear();
}
