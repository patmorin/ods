package ods;

/**
 * This class represents a string that can be efficiently split and for which substrings
 * can be efficiently extracted
 * @author morin
 *
 */
public class SString {
	int i;        // index of first character  
	int m;        // length
	byte[] data;  // data 
	
	public SString(String s) {
		data = s.getBytes();
		i = 0;
		m = s.length();
	}

	protected SString(byte[] data, int i, int m) {
		this.data = data;
		this.i = i;
		this.m = m;
	}

	/**
	 * Truncate this SString so that its length is j and return a new SString
	 * consisting the trailing m-j characters of the this SString
	 * @param j
	 * @return
	 */
	public SString split(int j) {
		if (j < 0 || j >= m) throw new IndexOutOfBoundsException();
		SString s = new SString(data, i+j, m-j);
		this.m = j;
		return s;
	}

	/**
	 * Return a new string consisting of characters j,...,m-1.
	 * @param j
	 * @return
	 */
	public SString suffix(int j) {
		if (j < 0 || j >= m) throw new IndexOutOfBoundsException();
		return new SString(data, i+j, m-j);
	}

	/**
	 * Return a new string consisting of characters 0,...,j-1
	 * @param j
	 * @return
	 */
	public SString prefix(int j) {
		if (j < 0 || j >= m) throw new IndexOutOfBoundsException();
		return new SString(data, 0, j);
	}

	/**
	 * Return a new string consisting of characters j,...,j+n-1
	 * @param j
	 * @param n
	 * @return
	 */
	public SString subString(int j, int n) {
		if (j < 0 || j >= m) throw new IndexOutOfBoundsException();
		if (n < 0 || j + n > m) throw new IndexOutOfBoundsException();
		return new SString(data, i+j, n);
	}
	
	/**
	 * The length of this string
	 * @return
	 */
	public int length() {
		return m;
	}
	
	/**
	 * Return the character at index j
	 * @param j
	 * @return
	 */
	public char charAt(int j) {
		return (char)data[i+j];
	}
}
