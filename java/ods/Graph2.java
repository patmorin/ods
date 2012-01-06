package ods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Graph2 implements Graph {
	int n;
	
	List<List<Integer>> adj;

	public Graph2(int n) {
		adj = new ArrayList<List<Integer>>(n);
		for (int i = 0; i < n; i++) 
			adj.set(i, new ArrayList<Integer>());
	}
	
	public void addEdge(int i, int j) {
		adj.get(i).add(j);
	}

	public boolean hasEdge(int i, int j) {
		return adj.get(i).contains(j);
	}

	public int inDegree(int i) {
		int deg = 0;
		for (int j = 0; j < n; j++)
			if (adj.get(j).contains(i)) deg++;
		return deg;
	}

	public List<Integer> inEdges(int i) {
		List<Integer> edges = new ArrayList<Integer>();
		for (int j = 0; j < n; j++)
			if (adj.get(j).contains(i))	edges.add(j);
		return edges;
	}

	public int nVertices() {
		return n;
	}

	public int outDegree(int i) {
		return adj.get(i).size();
	}

	public List<Integer> outEdges(int i) {
		return adj.get(i);
	}

	public void removeEdge(int i, int j) {
		Iterator<Integer> it = adj.get(i).iterator();
		while (it.hasNext()) {
			if (it.next() == j) {
				it.remove();
				return;
			}
		}	
	}
}
