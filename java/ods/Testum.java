package ods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;

/**
 * A utility class with some static methods for testing List implementations
 * @author morin
 */
public class Testum {

	public static void runTests(Collection<List<Integer>> cl) {
		int n = 800;
		
		// addition/removal at end and reading
		for (List<Integer> l : cl) {
			l.clear();
			for (int i = 0; i < n; i++)
				l.add(new Integer(i));
			Utils.myassert(l.size() == n);
			for (int i = 0; i < n; i++)
				Utils.myassert (i == l.get(i));
			for (int i = 0; i < n; i++) {
				l.remove(l.size()-1);
				Utils.myassert(l.size()== n-i-1);
				for (int j = 0; j < l.size(); j++)
					Utils.myassert (j == l.get(j));
			}
			
			// get/set
			for (int i = 0; i < n; i++)
				l.add(new Integer(i));
			for (int i = 0; i < n; i++)
				l.set(i, i*3231);
			for (int i = 0; i < n; i++)
				Utils.myassert(i*3231 == l.get(i).intValue());

			// addition at front
			l.clear();
			for (int i = 0; i < n; i++)
				l.add(0, i);
			Utils.myassert(l.size() == n);
			for (int i = 0; i < n; i++)
				Utils.myassert (n-i-1 == l.get(i));
			for (int i = 0; i < n; i++) {
				l.remove(0);
				Utils.myassert(l.size() == n-i-1);
				for (int j = 0; j < l.size(); j++)
					Utils.myassert (n-i-2-j == l.get(j));
			}
			
			// removal from middle
			for (int i = 0; i < n; i++)
				l.add(new Integer(i));
			for (int i = 0; i < n/2; i++) 
				l.remove(n/4);
			for (int i = 0; i < n/4; i++)
				Utils.myassert(i == l.get(i).intValue());
			for (int i = n/4; i < n/2; i++) 
				Utils.myassert(i+n/2 == l.get(i).intValue());
			l.clear();
		}

	}
	
	public static void sortedSetSpeedTests(Collection<SortedSet<Integer>> css, int n) {
		long start, stop;

		for (SortedSet<Integer> ss : css) {
			ss.clear();
			Utils.myassert(ss.size() == 0);
		}
		
		for (SortedSet<Integer> ss : css) {
			System.out.print("random insertions (" + ss.getClass() + ")...");
			start = System.nanoTime();
			Random r = new Random();
			for (int i = 0; i < n; i++)
				ss.add(r.nextInt());
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
			Utils.myassert(ss.size() >= n/2);
		}
		System.out.println();
		
		for (SortedSet<Integer> ss : css) {
			System.out.print("random contains (" + ss.getClass() + ")...");
			start = System.nanoTime();
			Random r = new Random();
			for (int i = 0; i < 10*n; i++)
				ss.contains(r.nextInt());
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("random headSets (" + ss.getClass() + ")...");
			start = System.nanoTime();
			Random r = new Random();
			for (int i = 0; i < n; i++)
				ss.headSet(r.nextInt());
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("iteration (" + ss.getClass() + ")...");
			start = System.nanoTime();
			for (Integer i : ss) { i = i + 1; }
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("clear (" + ss.getClass() + ")...");
			start = System.nanoTime();
			ss.clear();
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("sequential insertions (" + ss.getClass() + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++)
				ss.add(i*2);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
			Utils.myassert(ss.size() == n);
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("sequential contains (" + ss.getClass() + ")...");
			start = System.nanoTime();
			for (int i = 0; i < 2*n; i++)
				ss.contains(i);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("sequential headSets (" + ss.getClass() + ")...");
			start = System.nanoTime();
			for (int i = 0; i < 2*n; i++)
				ss.headSet(i);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();
		
		for (SortedSet<Integer> ss : css) {
			System.out.print("iteration over subSets (" + ss.getClass() + ")...");
			start = System.nanoTime();
			for (Integer i : ss.subSet(n/2, 3*n/4)) { i = i + 1; }
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

	}
	
	protected static <T> boolean compareSortedSets(Collection<T> a, Collection<T> b) {
		if (a.size() != b.size()) 
			return false;
		for (T x : a) {
			if (!b.contains(x)) return false;
		}
		for (T x : b) {
			if (!a.contains(x)) return false;
		}
		Iterator<T> ita = a.iterator();
		Iterator<T> itb = b.iterator();
		while (ita.hasNext()) {
			if (!ita.next().equals(itb.next()))
				return false;
		}
		return true;
	}
	
//	protected static <Node extends BinaryTreeNode<Node>> 
//		void correctnessTests(BinarySearchTree<Node,Integer> t, int n) {
//		SortedSet<Integer> ss = new TreeSet<Integer>();
//		Random r = new Random(0);
//		for (int i = 0; i < n; i++) {
//			Integer j = r.nextInt();
//			t.add(j);
//			ss.add(j);
//		}
//		// Utils.myassert(compareSortedSets(t, ss));
//		r = new Random(0);
//		for (int i = 0; i < n/3; i++) {
//			Integer j = r.nextInt(); 
//			t.remove(j);
//			ss.remove(j);
//			j = r.nextInt(); 
//			t.remove(j);
//			ss.remove(j);
//			r.nextInt(2500);
//		}
//		// Utils.myassert(compareSortedSets(t, ss));
//	}
	
//	protected static <Node extends BinaryTreeNode<Node>> 
//		void performanceTests(BinarySearchTree<Node,Integer> t) {
//		int ns[] = { 100, 1000, 10000, 1000000 };
//		for (int n : ns) {
//			Random r = new Random(0);
//			t.clear();
//			long stop, start;
//			double elapsed;
//			start = System.nanoTime();
//			System.out.print(t.getClass() + ": performing " + n + " random insertions...");
//			for (int i = 0; i < n; i++) {
//				t.add(r.nextInt());
//			}
//			stop = System.nanoTime();
//			elapsed = 1e-9*(stop-start);
//			System.out.println(" (" + elapsed + "s [" 
//					+ (int)(((double)n)/elapsed) + "ops/sec])");
//			r = new Random(0);
//			start = System.nanoTime();
//			System.out.print(t.getClass() + ": performing " + n + " random searches...");
//			for (int i = 0; i < n; i++) {
//				t.contains(r.nextInt());
//			}
//			stop = System.nanoTime();
//			elapsed = 1e-9*(stop-start);
//			System.out.println(" (" + elapsed + "s [" 
//					+ (int)(((double)n)/elapsed) + "ops/sec])");
//			r = new Random(0);
//			start = System.nanoTime();
//			System.out.print(t.getClass() + ": performing " + n + " random deletions...");
//			for (int i = 0; i < n; i++) {
//				t.remove(r.nextInt());
//			}
//			stop = System.nanoTime();
//			elapsed = 1e-9*(stop-start);
//			System.out.println(" (" + elapsed + "s [" 
//					+ (int)(((double)n)/elapsed) + "ops/sec])");
//			t.clear();
//
//			start = System.nanoTime();
//			System.out.print(t.getClass() + ": performing " + n + " sequential insertions...");
//			for (int i = 0; i < n; i++) {
//				t.add(i);
//			}
//			stop = System.nanoTime();
//			elapsed = 1e-9*(stop-start);
//			System.out.println(" (" + elapsed + "s [" 
//					+ (int)(((double)n)/elapsed) + "ops/sec])");
//			r = new Random(0);
//			start = System.nanoTime();
//			System.out.print(t.getClass() + ": performing " + n + " sequential searches...");
//			for (int i = 0; i < n; i++) {
//				t.contains(i);
//			}
//			stop = System.nanoTime();
//			elapsed = 1e-9*(stop-start);
//			System.out.println(" (" + elapsed + "s [" 
//					+ (int)(((double)n)/elapsed) + "ops/sec])");
//			r = new Random(0);
//			start = System.nanoTime();
//			System.out.print(t.getClass() + ": performing " + n + " sequential deletions...");
//			for (int i = 0; i < n; i++) {
//				t.remove(i);
//			}
//			stop = System.nanoTime();
//			elapsed = 1e-9*(stop-start);
//			System.out.println(" (" + elapsed + "s [" 
//					+ (int)(((double)n)/elapsed) + "ops/sec])");
//
//		}
//	}

	
	/**
	 * Run a sequence of performance tests on a List implementations
	 * @param cl - a collection of lists
	 * @param n - the "size" of the tests
	 */
	public static void listSpeedTests(Collection<List<Integer>> cl, int n) {
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
			int m = (int)Math.sqrt(n);
			for (int i = 0; i < m; i++)
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
			for (int i = 0; i < n; i++) {
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
			for (int i = 0; i < n; i++) {
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
			for (int i = 0; i < n; i++) {
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
		int n = 2000000;
		
		Collection<List<Integer>> cl = new ArrayList<List<Integer>>();
//		cl.add(new ArrayList<Integer>());
//		cl.add(new ArrayStack<Integer>(Integer.class));
//		cl.add(new java.util.LinkedList<Integer>());
		cl.add(new DLList<Integer>());
		cl.add(new DLList<Integer>());
		cl.add(new SkiplistList<Integer>());
		cl.add(new java.util.LinkedList<Integer>());
		cl.add(new ArrayDeque<Integer>(Integer.class));
		cl.add(new DualArrayDeque<Integer>(Integer.class));
//		cl.add(new FastArrayStack<Integer>(Integer.class));
//		cl.add(new DualArrayDeque<Integer>(Integer.class));
//		// cl.add(new FastArrayDeque<Integer>(Integer.class));
//		cl.add(new ArrayDeque<Integer>(Integer.class));
//		cl.add(new RootishArrayStack<Integer>(Integer.class));

		System.out.print("Running sanity tests...");
		runTests(cl);
		System.out.println("done.");
		listSpeedTests(cl, n);
	}
}
