/*
 * AdjacencyMatrix.h
 *
 *  Created on: 2012-01-13
 *      Author: morin
 */

#ifndef ADJACENCYMATRIX_H_
#define ADJACENCYMATRIX_H_

namespace ods {

class AdjacencyMatrix {
protected:
	int n;
	bool **a;
public:
	AdjacencyMatrix(int n);
	virtual ~AdjacencyMatrix();
	void addEdge(int i, int j) {
		a[i][j] = true;
	}

	void removeEdge(int i, int j) {
		a[i][j] = false;
	}

	bool hasEdge(int i, int j) {
		return a[i][j];
	}

	template<class List>
	void outEdges(int i, List &edges) {
		for (int j = 0; j < n; j++)
			if (a[i][j]) edges.add(j);
	}

	template<class List>
	void inEdges(int i, List &edges) {
		for (int j = 0; j < n; j++)
			if (a[j][i]) edges.add(j);
	}

	int nVertices() {
		return n;
	}
};

} /* namespace ods */
#endif /* ADJACENCYMATRIX_H_ */
