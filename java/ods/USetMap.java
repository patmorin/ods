package ods;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class USetMap<K,V> extends AbstractMap<K,V> {
	/**
	 * FIXME: Doesn't have the same equals() and hashCode()
	 *  that the API demands
	 * @author morin
	 */
	protected class Entry implements Map.Entry<K, V> {
		K k;
		V v;
		public Entry(K k, V v) {
			this.k = k;
			this.v = v;
		}
		public boolean equals(Entry x) {
			return x.k.equals(k);
		}
		public int hashCode() {
			return k.hashCode();
		}
		@Override
		public K getKey() {
			return k;
		}
		@Override
		public V getValue() {
			return v;
		}
		@Override
		public V setValue(V value) {
			return v = value;
		}
	}

	protected USet<Map.Entry<K, V>> s;
	
	public USetMap(USet<Map.Entry<K, V>> s) {
		this.s = s;
	}
	
	public V put(K k, V v) {
		Entry p = new Entry(k, v);
		Map.Entry<K, V> q = s.remove(p);
		s.add(p);
		return q == null ? null : q.getValue();
	}
	
	@SuppressWarnings("unchecked")
	public V remove(Object k) {
		Map.Entry<K,V> p = s.remove(new Entry((K)k, null));
		return p == null ? null : p.getValue();
	}
	
	@SuppressWarnings("unchecked")
	public boolean containsKey(Object k) {
		return s.find(new Entry((K)k, null)) != null;
	}
	
	public int size() {
		return s.size();
	}
	
	public Set<Map.Entry<K, V>> entrySet() {
		return new USetSet<Map.Entry<K, V>>(s);
	}
}
