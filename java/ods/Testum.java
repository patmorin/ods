package ods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A utility class with some static methods for testing List implementations
 * @author morin
 */
public class Testum {

	public static void graphCmp(Graph g1, Graph g2) {
		int n = g1.nVertices();
		Utils.myassert(n == g2.nVertices());
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Utils.myassert(g1.hasEdge(i,j) == g2.hasEdge(i,j));
			}
		}
		for (int i = 0; i < n; i++) {
			List<Integer> l1 = g1.outEdges(i);
			List<Integer> l2 = g2.outEdges(i);
			for (Integer x: l1) Utils.myassert(l2.contains(x));
			for (Integer x: l2) Utils.myassert(l1.contains(x));
		}		
		for (int i = 0; i < n; i++) {
			List<Integer> l1 = g1.inEdges(i);
			List<Integer> l2 = g2.inEdges(i);
			for (Integer x: l1) Utils.myassert(l2.contains(x));
			for (Integer x: l2) Utils.myassert(l1.contains(x));
		}		
	}
	
	
	public static void graphTests(Graph g1, Graph g2) {
		int n = g1.nVertices();
		Random rand = new Random();
		for (int k = 0; k < 50*n*n; k++) {
			int i = rand.nextInt(n);
			int j = rand.nextInt(n);
			if (i != j) {
				if (g1.hasEdge(i,j)) {
					g1.removeEdge(i,j);
					g2.removeEdge(i,j);
				} else {
					g1.addEdge(i,j);
					g2.addEdge(i,j);					
				}
			}
			graphCmp(g1, g2);
		}
	}
	
	
	protected static String s(Object c) {
		String s = c.getClass().getName();
		if (c instanceof SortedSSet<?>) {
			s += " - " + ((SortedSSet<?>)c).s.getClass().getName();
		}
		if (c instanceof USetSet<?>) {
			s += " - " + ((USetSet<?>)c).s.getClass().getName();
		}
		return s;
	}
	
	public static void listSanityTests(Collection<List<Integer>> cl) {
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
	
	public static void sortedSetSanityTests(SortedSet<Integer> ss, int n) {
		SortedSet<Integer> ts = new TreeSet<Integer>();
		ss.clear();
		Random r = new Random();
		for (int i = 0; i < n; i++) {
			Integer x = r.nextInt(2*n);
			if (ts.add(x) != ss.add(x))
				throw new RuntimeException("add(x) returned wrong value");
			if (!compareSortedSets(ts,ss))
				throw new RuntimeException("sorted sets differ!");
		}
		for (int i = 0; i < n; i++) {
			Integer x = r.nextInt(2*n);
			if (ts.remove(x) != ss.remove(x))
				throw new RuntimeException("remove(x) returned wrong value");
			if (!compareSortedSets(ts,ss))
				throw new RuntimeException("sorted sets differ!");
		}
		ss.clear();
		ts.clear();
		compareSortedSets(ts,ss);
	}
	
	
	
	public static void setSpeedTests(Collection<Set<Integer>> cs, int n) {
		long start, stop;

		for (Set<Integer> s : cs) {
			s.clear();
			Utils.myassert(s.size() == 0);
		}
		Random r = new Random();		
		for (Set<Integer> s : cs) {
			System.out.print("random insertions (" + s(s) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++)
				s.add(r.nextInt(n*5));
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
			Utils.myassert(s.size() >= n/2);
		}
		System.out.println();

		for (Set<Integer> s : cs) {
			System.out.print("random contains (" + s(s) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++)
				s.contains(r.nextInt(n*5));
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();
		
		for (Set<Integer> s : cs) {
			System.out.print("sequential removals (" + s(s) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < 5*n; i++)
				s.remove(i);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

	}
	
	public static void sortedSetSpeedTests(Collection<SortedSet<Integer>> css, int n) {
		long start, stop;

		for (SortedSet<Integer> ss : css) {
			ss.clear();
			Utils.myassert(ss.size() == 0);
		}
		
		for (SortedSet<Integer> ss : css) {
			System.out.print("random insertions (" + s(ss) + ")...");
			start = System.nanoTime();
			Random r = new Random();
			for (int i = 0; i < n; i++)
				ss.add(r.nextInt(2*n));
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
			Utils.myassert(ss.size() >= n/2);
		}
		System.out.println();
		
		for (SortedSet<Integer> ss : css) {
			System.out.print("random contains (" + s(ss) + ")...");
			start = System.nanoTime();
			Random r = new Random();
			for (int i = 0; i < 2*n; i++)
				ss.contains(r.nextInt(2*n));
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("random removals (" + s(ss) + ")...");
			start = System.nanoTime();
			Random r = new Random();
			for (int i = 0; i < 2*n; i++)
				ss.remove(r.nextInt(2*n));
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("random headSets (" + s(ss) + ")...");
			start = System.nanoTime();
			Random r = new Random();
			for (int i = 0; i < n; i++) {
				ss.headSet(r.nextInt(2*n));
				Iterator<Integer> it = ss.iterator();
				if (it.hasNext()) it.next();
			}
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("iteration (" + s(ss) + ")...");
			start = System.nanoTime();
			for (Integer i : ss) { i = i + 1; }
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("clear (" + s(ss) + ")...");
			start = System.nanoTime();
			ss.clear();
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("sequential insertions (" + s(ss) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++) {
				Utils.myassert(ss.size() == i);
				ss.add(i*2);
			}
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
			Utils.myassert(ss.size() == n);
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("sequential contains (" + s(ss) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < 2*n; i++)
				ss.contains(i);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (SortedSet<Integer> ss : css) {
			System.out.print("sequential headSets (" + s(ss) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < 2*n; i++)
				ss.headSet(i);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();
		
		for (SortedSet<Integer> ss : css) {
			System.out.print("iteration over subSets (" + s(ss) + ")...");
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
	
	
	/**
	 * Run a sequence of performance tests on a List implementations
	 * @param cl - a collection of lists
	 * @param n - the "size" of the tests
	 */
	public static void listSpeedTests(Collection<List<Integer>> cl, int n) {
		long start, stop;

		int sn = (int)Math.sqrt(n);
		Integer x = new Integer(0);
		for (List<Integer> l : cl) {
			System.out.print("insert at back (" + s(l) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++)
				l.add(x);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("front gets (" + s(l) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++)
				l.get(0);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("back gets (" + s(l) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < n; i++)
				l.get(l.size()-1);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("random gets (" + s(l) + ")...");
			start = System.nanoTime();
			Random r = new Random(1);
			for (int i = 0; i < sn; i++)
				l.get(r.nextInt(n));
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("remove at back (" + s(l) + ")...");
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
			System.out.print("stack operations (" + s(l) + ")...");
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
			System.out.print("forward queue operations (" + s(l) + ")...");
			start = System.nanoTime();
			Random r = new Random(2);
			for (int i = 0; i < sn; i++) {
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
			System.out.print("backward queue operations (" + s(l) + ")...");
			start = System.nanoTime();
			Random r = new Random(2);
			for (int i = 0; i < sn; i++) {
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
			System.out.print("deque operations (" + s(l) + ")...");
			start = System.nanoTime();
			Random r = new Random(3);
			for (int i = 0; i < sn; i++) {
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
			System.out.print("add in middle (" + s(l) + ")...");
			start = System.nanoTime();
			for (int i = 0; i < sn; i++)
				l.add(l.size()/2+i, x);
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();

		for (List<Integer> l : cl) {
			System.out.print("clear (" + s(l) + ")...");
			start = System.nanoTime();
			l.clear();
			stop = System.nanoTime();
			System.out.println(" " + (1e-9 * (stop - start)) + " seconds");
		}
		System.out.println();
	}
	
	public static void main(String args[]) {
		int n = 1000000;
		
		Collection<List<Integer>> cl = new ArrayList<List<Integer>>();
		cl.add(new LinkedList<Integer>());
		cl.add(new DLList<Integer>());
		cl.add(new SEList<Integer>(20, Integer.class));
		cl.add(new SEList<Integer>(40, Integer.class));
		cl.add(new SEList<Integer>(60, Integer.class));
		cl.add(new SEList<Integer>(80, Integer.class));
		cl.add(new SEList<Integer>(100, Integer.class));
//		cl.add(new FastArrayStack<Integer>(Integer.class));
//		cl.add(new ArrayDeque<Integer>(Integer.class));
//		cl.add(new DualArrayDeque<Integer>(Integer.class));
//		cl.add(new RootishArrayStack<Integer>(Integer.class));
//		cl.add(new java.util.LinkedList<Integer>());
//		cl.add(new DLList<Integer>());
//		cl.add(new SkiplistList<Integer>());

		System.out.print("Running sanity tests...");
		listSanityTests(cl);
		System.out.println("done.");
		listSpeedTests(cl, n);
	}
}
