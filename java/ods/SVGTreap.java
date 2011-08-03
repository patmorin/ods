package ods;

import java.io.PrintStream;

public class SVGTreap extends Treap<Integer> {
	
	protected static double pagewidth=215.9; // 8.5 in
	protected static double pageheight=279.4;  // 11 in
	protected static double width = 152.4; // 6 in
	protected static double height = 76.2; // 3 in
	protected static int maxp = 2000000000;
	
	protected double radius, thickness, xscale, yscale;

	protected static double min(double a, double b) {
		return a < b ? a : b;
	}	
	public SVGTreap(int n) {
		super();
		for (int i = 0; i < n; i++) {
			add(i);
		}
		radius = width / (3*n);
		thickness = radius / 3;
		xscale = width / n;
		yscale = 3*height / maxp;
	}
	
	public boolean add(Integer x) {
		Node<Integer> u = new Node<Integer>();
		u.x = x;
		u.p = rand.nextInt(maxp);
		double tmp = Math.log(u.p);
		u.p = (int)((tmp / 22.) * maxp);
		if (super.add(u)) {
			bubbleUp(u);
			return true;
		}
		return false;
	}
	
	protected void draw(PrintStream s) {
		System.out.println("<?xml version=\"1.0\"?>");
		System.out.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" ");
		System.out.println("              \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">");
		System.out.println("<svg xmlns=\"http://www.w3.org/2000/svg\">");
		System.out.println("  <g style=\"fill-opacity:1.0;\">");
		Node<Integer> u = firstNode();
		while (u.left != null) u = u.left;
		while (u != null) {
			if (u != r)
				edge(s, getX(u.parent), getY(u.parent), getX(u), getY(u));
			u = nextNode(u);
		}
		u = firstNode();
		while (u.left != null) u = u.left;
		while (u != null) {
			node(s, getX(u), getY(u));
			u = nextNode(u);
		}

		s.println("  </g>");
		s.println("</svg>");
		
	}

	protected void node(PrintStream s, double x, double y) {
		s.print("    ");
		s.println("<circle cx=\""
						+ x
						+ "mm\" cy=\""
						+ y
						+ "mm\" r=\""+ radius +"mm\" style=\"fill:red; stroke:red; stroke-width:0\" />");
	}
	
	protected void edge(PrintStream s, double x1, double y1, double x2,
			double y2) {
		s.print("    ");
		s.println("<line x1=\"" + x1 + "mm\" y1=\"" + y1 + "mm\" x2=\"" + x2
				+ "mm\" y2=\"" + y2
				+ "mm\" style=\"stroke: black; stroke-width:"+thickness+"1mm;\"  />");
	}
	
	protected void printIt(PrintStream s) {
		
	}
	
	protected double getX(Node<Integer> u) {
		return u.x * xscale + (pagewidth-width)/2;
	}
	
	protected double getY(Node<Integer> u) {
		return u.p * yscale + (pageheight-height)/2;
	}
	
	public static void main(String[] args) {
		int n = 100;
		if (args.length == 1) {
			n = Integer.parseInt(args[0]);
		}
		new SVGTreap(n).draw(System.out);
	}
}
