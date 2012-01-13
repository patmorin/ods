/*
 * AdjacencyLists.cpp
 *
 *  Created on: 2012-01-13
 *      Author: morin
 */

#include "AdjacencyLists.h"

namespace ods {

AdjacencyLists::AdjacencyLists(int n0) {
	n = n0;
	adj = new List[n];
}

AdjacencyLists::~AdjacencyLists() {
	delete[] adj;
}

} /* namespace ods */
