package ods;

import java.util.Random;

public class MergeableHeap<T extends Comparable<T>> extends
		BinaryTree<MHeapNode<T>> {
	
	Random rand;
	
	public MergeableHeap() {
		super(new MHeapNode<T>());
		rand = new Random();
	}
	
	protected  MergeableHeap(MHeapNode<T> r) {
		super(new MHeapNode<T>());
		this.r = r;
	}
	
	public boolean add(T x) {
		MHeapNode<T> p = new MHeapNode<T>();
		p.x = x;
		r = merge(p, r);
		return true;
	}
	
	public T findMin() {
		return r.x;
	}
	
	public T remove() {
		T x = r.x;
		r = merge(r.left, r.right);
		return x;
	}
	
	public MHeapNode<T> merge(MHeapNode<T> h1, MHeapNode<T> h2) {
		if (h1 == null) return h2;
		if (h2 == null) return h1;
		if (h2.x.compareTo(h1.x) < 0) {
			MHeapNode<T> tmp = h1;
			h1 = h2;
			h2 = tmp;
		}
		if (rand.nextBoolean()) {
			h1.left = merge(h1.left, h2);
		} else {
			h1.right = merge(h1.right, h2);
		}
		return h1;
	}
	
	public static void main(String[] args) {
		MergeableHeap<Integer> h = new MergeableHeap<Integer>();
		Random r = new Random();
		int n = 20;
		for (int i = 0; i < n; i++) {
			h.add(r.nextInt(2500));
		}
		while (!h.isEmpty()) {
			System.out.print("" + h.remove() + ",");
		}
		System.out.println("");

		h.clear();
		n = 100000;
		long start, stop;
		double elapsed;
		System.out.print("performing " + n + " adds...");
		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			h.add(r.nextInt());
		}
		stop = System.nanoTime();
		elapsed = 1e-9*(stop-start);
		System.out.println("(" + elapsed + "s [" 
				+ (int)(((double)n)/elapsed) + "ops/sec])");
		n *= 10;
		System.out.print("performing " + n + " add/removes...");
		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			if (r.nextBoolean()) {
				h.add(r.nextInt());
			} else {
				h.remove();
			}
		}
		stop = System.nanoTime();
		elapsed = 1e-9*(stop-start);
		System.out.println("(" + elapsed + "s [" 
				+ (int)(((double)n)/elapsed) + "ops/sec])");
	}
}
