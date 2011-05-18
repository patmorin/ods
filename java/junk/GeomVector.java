package junk;

import sun.security.util.BigInt;

public class GeomVector {
	
	Double[] x;
	
	public boolean equals(Object o) {
		if (!(o instanceof GeomVector)) return false;
		GeomVector gvo = (GeomVector)o;
		if (gvo.x.length != x.length) return false;
		for (int i = 0; i < x.length; i++)
			if (gvo.x[i] != x[i]) return false;
		return true;
	}
	
	public int hashCode() {
		long p = (1L<<32)+15;  // prime: 2^32 + 15
		long z = 0x64b6055aL;  // 30 bits from random.org
		long s = 0;
		long zi = 1;
		for (int i = 0; i < x.length; i++) {
			long xi = x[i].hashCode() & ((1L<<32)-1); // unsigned int to long
			s = (s + zi * xi) % p;
			zi = (zi * z) % p;	
		}
		s = (s + zi * (p-1)) % p;
		return (int)(p & ((1<<32L)-1));
	}
	
	public static void main(String[] args) {
		System.out.printf("%d\n", 4294967311L * 0x64b6055aL + ((1L<<32)-1));
		System.out.printf("%d\n", (1L<<63)-1);		
	}

}
