/*
 * DualArrayDeque.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#include "DualArrayDeque.h"
#include "utils.h"

namespace ods {

template<class T>
DualArrayDeque<T>::DualArrayDeque() {
}

template<class T>
DualArrayDeque<T>::~DualArrayDeque() {
}

template<class T>
int DualArrayDeque<T>::size() {
	return front.size() + back.size();
}

template<class T>
void DualArrayDeque<T>::add(int i, T x) {
	if (i < front.size()) {
		front.add(front.size() - i, x);
	} else {
		back.add(i - front.size(), x);
	}
	balance();
}

template<class T>
T DualArrayDeque<T>::remove(int i) {
    T x;
    if (i < front.size()) {
            x = front.remove(front.size()-i-1);
    } else {
            x = back.remove(i-front.size());
    }
    balance();
    return x;
}

template<class T>
void DualArrayDeque<T>::balance() {
	if (3*front.size() < back.size()
			|| 3*back.size() < front.size()) {
		int n = front.size() + back.size();
		int nf = n/2;
		array<T> af(max(2*nf, 1));
		for (int i = 0; i < nf; i++) {
			af[nf-i-1] = get(i);
		}
		int nb = n - nf;
		array<T> ab(max(2*nb, 1));
		for (int i = 0; i < nb; i++) {
			ab[i] = get(nf+i);
		}
		front.a = af;
		front.n = nf;
		back.a = ab;
		back.n = nb;
	}
}

template<class T>
void DualArrayDeque<T>::clear() {
	front.clear();
	back.clear();
}

template DualArrayDeque<int>::DualArrayDeque();
template DualArrayDeque<int>::~DualArrayDeque();
template int DualArrayDeque<int>::get(int);
template int DualArrayDeque<int>::set(int,int);
template void DualArrayDeque<int>::add(int,int);
template int DualArrayDeque<int>::remove(int);
template int DualArrayDeque<int>::size();



} /* namespace ods */
