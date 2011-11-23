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
	// TODO Auto-generated constructor stub

}

template<class T>
DualArrayDeque<T>::~DualArrayDeque() {
	// TODO Auto-generated destructor stub
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
		int lengthf = max(1, 2*nf);
		T *af = new T[lengthf];
		for (int i = 0; i < nf; i++) {
			af[nf-i-1] = get(i);
		}
		int nb = n - nf;
		int lengthb = max(1, 2*nb);
		T *ab = new T[lengthb];
		for (int i = 0; i < nb; i++) {
			ab[i] = get(nf+i);
		}
		delete front.a;
		front.a = af;
		front.n = nf;
		front.length = lengthf;
		delete back.a;
		back.a = ab;
		back.n = nb;
		back.length = lengthb;
	}
}

template DualArrayDeque<int>::DualArrayDeque();
template DualArrayDeque<int>::~DualArrayDeque();
template int DualArrayDeque<int>::get(int);
template int DualArrayDeque<int>::set(int,int);
template void DualArrayDeque<int>::add(int,int);
template int DualArrayDeque<int>::remove(int);
template int DualArrayDeque<int>::size();



} /* namespace ods */
