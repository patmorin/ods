package ods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * This class implements hashing with chaining using multiplicative hashing
 * @author morin
 *
 * @param <T>
 */
public class ChainedHashTable<T> implements USet<T> {
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
	public ChainedHashTable() {
		d = 1;
		t = allocTable(1<<d);
		Random r = new Random();
		z = r.nextInt() | 1;     // is a random odd integer
	}
	
	public void clear() {
		d = 1;
		t = allocTable(1<<d);
		n = 0;
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
	protected void resize() {
		d = 1;
		while (1<<d <= n) d++;
        n = 0;
		List<T>[] oldTable = t;
		t = allocTable(1<<d);
		for (int i = 0; i < oldTable.length; i++) {
			for (T x : oldTable[i]) {
				add(x);
			}
		}
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
		if (find(x) != null) return false;
		if (n+1 > t.length) resize();
		t[hash(x)].add(x);
		n++;
		return true;
	}
	
	public T remove(T x) {
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
				ChainedHashTable.this.remove(t[ilast].get(jlast));
			}		
		}
		return new IT();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 100000;
		ChainedHashTable<Integer> t = new ChainedHashTable<Integer>();
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
