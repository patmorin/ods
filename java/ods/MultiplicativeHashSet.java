package ods;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

public class MultiplicativeHashSet<T> extends AbstractSet<T> {
	/**
	 * The underlying hash table
	 */
	HashTable<T> tab;

	public MultiplicativeHashSet() {
		tab = new HashTable<T>();
	}

	protected MultiplicativeHashSet(HashTable<T> tab) {
		this.tab = tab;
	}

	/**
	 * Return true iff this hash table contains an object y such
	 * that x.equals(y)
	 */
	public boolean contains(Object x) {
		return tab.find(x) != null;
	}
	
	public boolean add(T x) {
		if (contains(x)) {
			return false;
		} else {
			return tab.add(x);
		}
	}

	public boolean remove(Object x) {
		return tab.remove(x);
	}

	public Iterator<T> iterator() {
		return tab.iterator();
	}

	public int size() {
		return tab.size();
	}
	
	public static void main(String[] args) {
		int n = 100002;
		Set<Integer> t = new MultiplicativeHashSet<Integer>();
		for (int i = 0; i < n; i++) {
			t.add(i*2);
		}
		for (int i = 0; i < 2*n; i++) {
			if (i % 2 == 0) {
				assert(t.contains(i));
			} else {
				assert(!t.contains(i));
			}
		}
		for (int i = 0; i < n/2; i++) {
			t.remove(i*4);
		}
		for (int i = 0; i < 2*n; i++) {
			if (i % 4 == 2) {
				assert(t.contains(i));
			} else {
				assert(!t.contains(i));
			}
		}
		boolean[] checked = new boolean[2*n];
		for (Integer x : t) {
			assert(checked[x] == false);
			checked[x] = true;
		}
		for (int i = 0; i < 2*n; i++) {
			if (i % 4 == 2) {
				assert(checked[i] == true);
			} else {
				assert(checked[i] == false);
			}
		}

	}

}
