/*
 * AdjacencyMatrix.cpp
 *
 *  Created on: 2012-01-13
 *      Author: morin
 */

#include "AdjacencyMatrix.h"

namespace ods {

AdjacencyMatrix::AdjacencyMatrix(int n0) {
	n = n0;
	a = new bool*[n];
	for (int i = 0; i < n; i++)
		a[i] = new bool[n];
	for (int i = 0; i < n; i++)
		for (int j = 0; j < n; j++)
			a[i][j] = false;
}

AdjacencyMatrix::~AdjacencyMatrix() {
	for (int i = 0; i < n; i++)
		delete[] a[i];
	delete[] a;
}

} /* namespace ods */
