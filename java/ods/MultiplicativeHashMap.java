package ods;

import java.util.AbstractMap;
import java.util.Set;

class Pair<V> {
	public Object key;
	public V value;
	public Pair(Object ikey, V ivalue) {
		key = ikey;
		value = ivalue;
	}
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		return ((o instanceof Pair) && key.equals(((Pair<V>)o).key)); 
	}
	public int hashCode() {
		return key.hashCode();
	}
}

public class MultiplicativeHashMap<K,V> extends AbstractMap<K,V> {
	/**
	 * The underlying hash table
	 */
	HashTable<Pair<V>> tab;

	public V put(K key, V value) {
		Pair<V> p = new Pair<V>(key, value);
		Pair<V> r = tab.removeOne(p);
		tab.add(p);
		return (r == null) ? null : r.value;
	}
	
	public V get(Object key) {
		Pair<V> p = new Pair<V>(key, null);
		Pair<V> r = tab.find(p);
		return (r == null) ? null : r.value;
	}
	
	public V remove(Object key) {
		Pair<V> p = new Pair<V>(key, null);
		Pair<V> r = tab.removeOne(p);
		return (r == null) ? null : r.value;		
	}
	
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return null;
	}
	
}
