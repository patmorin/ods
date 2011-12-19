/*
 * BinarySearchTree.cpp
 *
 *  Created on: 2011-11-28
 *      Author: morin
 */

#include "BinarySearchTree.h"
#include "utils.h"
namespace ods {

/**
 * Todo: Do this for other classes and/or move this up into BinarySearchTree<Node,T>
 */
template<>
BinarySearchTree1<int>::BinarySearchTree1() : BinarySearchTree<BSTNode1<int>, int>(INT_MIN) {
}

template<>
BinarySearchTree1<long>::BinarySearchTree1() : BinarySearchTree<BSTNode1<long>, long>(LONG_MIN) {
}

template<>
BinarySearchTree1<long long>::BinarySearchTree1() : BinarySearchTree<BSTNode1<long long>, long long>(LLONG_MIN) {
}

template<>
BinarySearchTree1<double>::BinarySearchTree1() : BinarySearchTree<BSTNode1<double>, double>(NAN) {
}

template<>
BinarySearchTree1<float>::BinarySearchTree1() : BinarySearchTree<BSTNode1<float>, float>(NAN) {
}

template class BinarySearchTree1<int>;
template class BinarySearchTree1<double>;
// template class BinarySearchTree1<dodo>;

} /* namespace ods */
