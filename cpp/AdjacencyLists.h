/*
 * AdjacencyLists.h
 *
 *  Created on: 2012-01-13
 *      Author: morin
 */

#ifndef ADJACENCYLISTS_H_
#define ADJACENCYLISTS_H_

#include "ArrayStack.h"

namespace ods {

class AdjacencyLists {
protected:
	class List : public ArrayStack<int> {
	public:
		List() : ArrayStack<int>() {};
		virtual ~List() {};
		bool contains(int j) {
			for (int i = 0; i < n; i++)
				if (a[i] == j) return true;
			return false;
		}
	};
	int n;
	List *adj;
public:
	AdjacencyLists(int n0);
	virtual ~AdjacencyLists();
	void addEdge(int i, int j) {
		adj[i].add(j);
	}

	bool hasEdge(int i, int j) {
		return adj[i].contains(j);
	}

	template<class LisT>
	void inEdges(int i, LisT &edges) {
		for (int j = 0; j < n; j++)
			if (adj[j].contains(i))	edges.add(j);
	}

	int nVertices() {
		return n;
	}

	template<class LisT>
	void outEdges(int i, LisT &edges) {
		for (int k = 0; k < adj[i].size(); k++)
			edges.add(adj[i].get(k));
	}

	void removeEdge(int i, int j) {
		for (int k = 0; k < adj[i].size(); k++) {
			if (adj[i].get(k) == j) {
				adj[i].remove(k);
				return;
			}
		}
	}

};

} /* namespace ods */
#endif /* ADJACENCYLISTS_H_ */
