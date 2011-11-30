/*
 * DLList.cpp
 *
 *  Created on: 2011-11-24
 *      Author: morin
 */

#include "DLList.h"

namespace ods {

template<class T>
DLList<T>::DLList() {
	dummy.next = &dummy;
	dummy.prev = &dummy;
	n = 0;
}

template<class T>
DLList<T>::~DLList() {
	clear();
}

template<class T>
void DLList<T>::clear() {
	Node *u = dummy.next;
	while (u != &dummy) {
		Node *w = u->next;
		delete u;
		u = w;
	}
	n = 0;
}

template DLList<int>::DLList();
template DLList<int>::~DLList();

} /* namespace ods */
