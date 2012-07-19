

public class Comp {
	static long binom(long a, long b) {
		long z = 1;
		while (a > b)
			z *= a--;
		while (b > 0)
			z /= b--;
		return z;
	}
	
	
	
	static long s(long h) {
		return (1l << (h+1l))-1l;
	}
	
	static long f(long h) {
		if (h == 0) return 1;
		long z = f(h-1);
		return z*z*binom(2 * s(h-1), s(h-1)); 
	}
	
	public static void main(String[] args) {
		System.out.println(f(3));
		System.out.println(binom(6,3));
	}
}
