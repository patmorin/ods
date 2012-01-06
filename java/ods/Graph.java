package ods;

import java.util.List;


/**
 * This interface represents a directed graph whose vertices are
 * indexed by 0,...,nVertices()-1
 * @author morin
 *
 */
public interface Graph {
	public int nVertices();
	public void addEdge(int i, int j);
	public void removeEdge(int i, int j);
	public boolean hasEdge(int i, int j);
	public List<Integer> outEdges(int i);
	public List<Integer> inEdges(int i);
	public int outDegree(int i);
	public int inDegree(int i);
}
