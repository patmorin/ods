package ods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * A utility class with some static methods for testing List implementations
 * @author morin
 */
public class ListTests {

	public static void runTests(Collection<List<Integer>> cl) {
		int n = 800;
		
		// addition/removal at end and reading
		for (List<Integer> l : cl) {
			l.clear();
			for (int i = 0; i < n; i++)
				l.add(new Integer(i));
			assert(l.size() == n);
			for (int i = 0; i < n; i++)
				assert (i == l.get(i).intValue());
			for (int i = 0; i < n; i++) {
				l.remove(l.size()-1);
				assert(l.size()== n-i-1);
				for (int j = 0; j < l.size(); j++)
					assert (j == l.get(j).intValue());
			}
			
			// get/set
			for (int i = 0; i < n; i++)
				l.add(new Integer(i));
			for (int i = 0; i < n; i++)
				l.set(i, i*3231);
			for (int i = 0; i < n; i++)
				assert(i*3231 == l.get(i).intValue());

			// addition at front
			l.clear();
			for (int i = 0; i < n; i++)
				l.add(0, new Integer(i));
			assert(l.size() == n);
			for (int i = 0; i < n; i++)
				assert (n-i-1 == l.get(i).intValue());
			for (int i = 0; i < n; i++) {
				l.remove(0);
				assert(l.size() == n-i-1);
				for (int j = 0; j < l.size(); j++)
					assert (n-i-2-j == l.get(j).intValue());
			}
			
			// removal from middle
			for (int i = 0; i < n; i++)
				l.add(new Integer(i));
			for (int i = 0; i < n/2; i++) 
				l.remove(n/4);
			for (int i = 0; i < n/4; i++)
				assert(i == l.get(i).intValue());
			for (int i = n/4; i < n/2; i++) 
				assert(i+n/2 == l.get(i).intValue());
			l.clear();
		}

	}
	
	/**
	 * Run a sequence of performance tests on a List implementations
	 * @param cl - a collection of lists
	 * @param n - the "size" of the tests
	 */
	public static void runSpeedTests(Collection<List<Integer>> cl, int n) {
		long start, stop;

		Integer x = new Integer(0);
		for (List<Integer> l : cl) {
			System.out.print("insert at back (" + l.getClass() + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++)
				l.add(x);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("front gets (" + l.getClass() + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++)
				l.get(0);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("back gets (" + l.getClass() + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++)
				l.get(l.size()-1);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("random gets (" + l.getClass() + ")...");
			start = System.nanoTime();
			Random r = new Random(1);
			for (int i = 0; i < n; i++)
				l.get(r.nextInt(n));
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("remove at back (" + l.getClass() + ")...");
			start = System.nanoTime();
			while(!l.isEmpty()) 
				l.remove(l.size()-1);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			for (int i = 0; i < n; i++)
				l.add(x);
			System.out.print("stack operations (" + l.getClass() + ")...");
			start = System.nanoTime();
			Random r = new Random(0);
			for (int i = 0; i < n; i++) {
				if (r.nextBoolean()) {
					l.add(x);
				} else {
					l.remove(l.size()-1);
				}
			}
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("forward queue operations (" + l.getClass() + ")...");
			start = System.nanoTime();
			Random r = new Random(2);
			for (int i = 0; i < n/40; i++) {
				if (r.nextBoolean()) {
					l.add(x);
				} else {
					l.remove(0);
				}
			}
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("backward queue operations (" + l.getClass() + ")...");
			start = System.nanoTime();
			Random r = new Random(2);
			for (int i = 0; i < n/40; i++) {
				if (r.nextBoolean()) {
					l.add(0,x);
				} else {
					l.remove(l.size()-1);
				}
			}
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();
		
		for (List<Integer> l : cl) {
			System.out.print("deque operations (" + l.getClass() + ")...");
			start = System.nanoTime();
			Random r = new Random(3);
			for (int i = 0; i < n/40; i++) {
				switch(r.nextInt(4)) {
				case 0:
					l.add(0,x);
					break;
				case 1:
					l.add(x);
					break;
				case 2:
					l.remove(0);
					break;
				case 3:
					l.remove(l.size()-1);
					break;
				}
			}
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("add in middle (" + l.getClass() + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n/20; i++)
				l.add(l.size()/2+i, x);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();
		
		for (List<Integer> l : cl) {
			Integer[] a = new Integer[n/20];
			Arrays.fill(a, x);
			System.out.print("addAll in middle (" + l.getClass() + ")...");
			start = System.nanoTime();
			l.addAll(l.size()/2, Arrays.asList(a));
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();


		for (List<Integer> l : cl) {
			System.out.print("clear (" + l.getClass() + ")...");
			start = System.nanoTime();
			l.clear();
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();
	}
	
	public static void main(String args[]) {
		int n = 200000;
		
		Collection<List<Integer>> cl = new ArrayList<List<Integer>>();
		cl.add(new ArrayList<Integer>());
		cl.add(new ArrayStack<Integer>(Integer.class));
//		cl.add(new FastArrayStack<Integer>(Integer.class));
//		cl.add(new DualArrayDeque<Integer>(Integer.class));
//		// cl.add(new FastArrayDeque<Integer>(Integer.class));
//		cl.add(new ArrayDeque<Integer>(Integer.class));
		cl.add(new RootishArrayStack<Integer>(Integer.class));

		System.out.print("Running sanity tests...");
		runTests(cl);
		System.out.println("done.");
		runSpeedTests(cl, n);
	}
}
