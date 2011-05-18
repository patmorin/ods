package ods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.AbstractCollection;

/**
 * This class implements hashing with chaining using multiplicative hashing
 * @author morin
 *
 * @param <T>
 */
public class HashTable<T> extends AbstractCollection<T> {
	/**
	 * The hash table
	 */
	List<T>[] t;

	/**
	 * The "dimension" of the table (table.length = 2^d)
	 */
	int d;

	/**
	 * The number of elements in the hash table
	 */
	int n;
		
	/**
	 * The multiplier
	 */
	int z;

	/**
	 * The number of bits in an int
	 */
	protected static final int w = 32;
	
	/**
	 * Create a new empty hash table
	 */
	public HashTable() {
		d = 0;
		t = allocTable(1<<d);
		Random r = new Random();
		z = r.nextInt() | 1;     // is a random odd integer
	}
	
	/**
	 * Allocate and initialize a new empty table
	 * @param s
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	protected List<T>[] allocTable(int s) {
		List<T>[] tab = new ArrayList[s];
		for (int i = 0; i < s; i++) {
			tab[i] = new ArrayList<T>();
		}
		return tab;
	}
	
	/**
	 * Resize the table so that it has size 2^d 
	 */
	protected void resize(int d) {
		this.d = d;
		List<T>[] oldTable = t;
		t = allocTable(1<<d);
		for (int i = 0; i < oldTable.length; i++) {
			for (T x : oldTable[i]) {
				add(x);
			}
		}
	}
	
	/**
	 * Double the size of the table
	 */
	protected void grow() {
		resize(d+1);
	}
	
	/**
	 * Halve the size of the table
	 */
	protected void shrink() {
		resize(d-1);
	}

	/**
	 * Return the number of elements stored in this hash table
	 */
	public int size() {
		return n;
	}

	/**
	 * Compute the table location for object x
	 * @param x
	 * @return ((x.hashCode() * z) mod 2^w) div 2^(w-d)
	 */
	protected final int hash(Object x) {
		return (z * x.hashCode()) >>> (w-d);
	}
	
	/**
	 * Add the element x to the hashtable if it is not
	 * already present
	 */
	public boolean add(T x) {
		if (n+1 > t.length)
			grow();
		t[hash(x)].add(x);
		n++;
		return true;
	}
	
	/**
	 * Remove the element x from the hashtable if it exists
	 * @param x
	 * @return
	 */
	public int removeAll(Object x) {
		int r = 0;
		Iterator<T> it = t[hash(x)].iterator();
		while (it.hasNext()) {
			T y = it.next();
			if (y.equals(x)) {
				it.remove();
				n--;
				r++;
			}
		}
		return r;
	}

	public T removeOne(Object x) {
		Iterator<T> it = t[hash(x)].iterator();
		while (it.hasNext()) {
			T y = it.next();
			if (y.equals(x)) {
				it.remove();
				n--;
				return y;
			}
		}
		return null;
	}

	public boolean remove(Object x) {
		return removeOne(x) != null;
	}

	/**
	 * Get the copy of x stored in this table.
	 * @param x - the item to get 
	 * @return - the element y stored in this table such that x.equals(y)
	 * is true, or null if no such element y exists
	 */
	public T find(Object x) {
		for (T y : t[hash(x)])
			if (y.equals(x))
				return y;
		return null;
	}
	
	/**
	 * Return a list of all the elements y in this hash table such that
	 * x.equals(y) is true
	 * @param x the value to search for
	 * @return a list of all elements y such that x.equals(y) is true
	 */
	public List<T> findAll(Object x) {
		List<T> l = new LinkedList<T>();
		int i = (x.hashCode() * z) >>> (w-d);
		for (T y : t[i]) {
			if (y.equals(x)) {
				l.add(y);
			}
		}
		return l;
	}

	public Iterator<T> iterator() {
		class IT implements Iterator<T> {
			int i, j;
			int ilast, jlast;
			IT() {
				i = 0;
				j = 0;
				while (i < t.length && t[i].isEmpty())
					i++;
			}
			protected void jumpToNext() {
				while (i < t.length && j + 1 > t[i].size()) {
					j = 0;
					i++;
				}
			}
			public boolean hasNext() {
				return i < t.length;
			}
			public T next() {
				ilast = i;
				jlast = j; 
				T x =  t[i].get(j);
				j++;
				jumpToNext();
				return x;
			}
			public void remove() {
				HashTable.this.remove(t[ilast].get(jlast));
			}		
		}
		return new IT();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 100000;
		HashTable<Integer> t = new HashTable<Integer>();
		for (int i = 0; i < n; i++) {
			t.add(i*2);
		}
		for (int i = 0; i < 2*n; i++) {
			Integer x = t.find(i);
			if (i % 2 == 0) {
				assert(x.intValue() == i);
			} else {
				assert(x == null);
			}
		}
	}

}
