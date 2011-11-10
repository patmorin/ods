package ods;

/**
 * This subclass of ArrayStack overrides some of its functions to make
 * them faster. In particular, it uses System.arraycopy() rather than for
 * loops copy elements between arrays and to shift them within arrays.
 * @author morin
 *
 * @param <T>
 */
public class FastArrayStack<T> extends ArrayStack<T> {
	public FastArrayStack(Class<T> t0) {
		super(t0);
	}

	protected void resize() {
		T[] b = f.newArray(Math.max(2*n,1));
		System.arraycopy(a, 0, b, 0, n);
		a = b;
	}

	protected void resize(int nn) {
		T[] b = f.newArray(nn);
		System.arraycopy(a, 0, b, 0, n);
		a = b;
	}

	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		if (n + 1 > a.length) resize();
		System.arraycopy(a, i, a, i+1, n-i); 
		a[i] = x;
		n++;
	}
	
	public T remove(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		T x = a[i];
		System.arraycopy(a, i+1, a, i, n-i-1);
		n--; 
		if (a.length >= 3*n) resize();
		return x;
	}
}
