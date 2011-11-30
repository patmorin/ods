package ods;

import java.util.Arrays;

/**
 * Implement square roots without relying on Math.sqrt(x)
 * @author morin
 *
 */
public class FastSqrt {
	protected static final int[] sqrtab;
	protected static final int[] logtab;
	protected static final int halfint = 1 << 16;
	
	static {
		sqrtab = new int[halfint];
		logtab = new int[halfint];
		for (int d = 0; d < 16; d++) 
			Arrays.fill(logtab, 1<<d, 2<<d, d);
		int s = 1<<7;                        // sqrt(2^14)
		for (int i = 0; i < halfint; i++) {
			if ((s+1)*(s+1) <= i << 14) s++; // sqrt increases
			sqrtab[i] = s;
		}
	}
	
	/* Fake code for inclusion in the book
	static void inittabs() {
		sqrtab = new int[1<<(r/2)];
		logtab = new int[1<<(r/2)];
		for (int d = 0; d < r/2; d++) 
			Arrays.fill(logtab, 1<<d, 2<<d, d);
		int s = 1<<(r/4);                    // sqrt(2^(r/2))
		for (int i = 0; i < 1<<(r/2); i++) {
			if ((s+1)*(s+1) <= i << (r/2)) s++; // sqrt increases
			sqrtab[i] = s;
		}
	}
	*/

	
	/**
	 * Compute the floor of the base-2 logarithm of x 
	 * @param x
	 * @return the floor of the base-2 logarithm of x
	 */
	public static final int log(int x) {
		if (x >= halfint)
			return 16 + logtab[x>>>16];
		return logtab[x];
	}
	
	protected static final int r = 29;

	/**
	 * Compute the floor of the square root of x
	 * @param x
	 * @return the floor of the square root of x
	 */
	public static final int sqrt(int x) {
		int rp = log(x);
		int upgrade = ((r-rp)/2) * 2;
		int xp = x << upgrade;  // xp has r or r-1 bits
		int s = sqrtab[xp>>(r/2)] >> (upgrade/2);
		while ((s+1)*(s+1) <= x) s++;  // executes at most twice
		return s;
	}

	public static final int sqrt(int x, int r) {
		int s = sqrtab[x>>r/2];
		while ((s+1)*(s+1) <= x) s++; // executes at most twice
		return s;
	}
	
	public static void main(String[] args) {
		int n = 1 << 30;
//		System.out.print("Testing correctness of " + n + " inputs...");
//		for (int t = 1; t < n; t++) {
//			if (t % 10000000 == 0) System.out.print(".");
//			int x = t;
//			int logx = log(x);
//			if (((1 << logx) & x) == 0)
//				throw new RuntimeException("log(x) is incorrect [1]");
//			if ( (( (1 << (logx+1)) - 1 ) & x) != x)
//				throw new RuntimeException("log(x) is incorrect [2]");
//			double ms = Math.sqrt(x);
//			double fss = FastSqrt.sqrt(x);
//			// System.out.println(x + " " + logx + " " + ms + " " + fss + " " + Math.abs(ms-fss));
//			if (Math.abs(ms-fss) > 2.0)
//				throw new RuntimeException("sqrt(x) is incorrect [1]");
//		}
//		System.out.println("passed!");


		n = 1 << 27;
		long start, stop;
		double elapsed;
		

		System.out.print("Performing " + n + " Math.sqrts...");
		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			Math.sqrt(i);
		}
		stop = System.nanoTime();
		elapsed = (stop-start)*1e-9;
		System.out.println("done (" + elapsed + "s)");

		System.out.print("Performing " + n + " FastSqrt.sqrts roots...");
		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			FastSqrt.sqrt(i);
		}
		stop = System.nanoTime();
		elapsed = (stop-start)*1e-9;
		System.out.println("done (" + elapsed + "s)");

	}
}
