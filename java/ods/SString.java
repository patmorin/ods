package ods;

/**
 * This class represents a string that can be efficiently split
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
	
	public int length() {
		return m;
	}
	
	public char charAt(int j) {
		return (char)data[i+j];
	}
}
