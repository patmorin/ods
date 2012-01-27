/*
 * YFastTrie.h
 *
 *  Created on: 2012-01-27
 *      Author: morin
 */

#ifndef YFASTTRIE_H_
#define YFASTTRIE_H_

#include "Treap.h"
#include "XFastTrie.h"

namespace ods {


/**
 * Warning YFastTrie depends on XFastTrie, so you can't use it to store
 * the values UINT_MAX-1 or UINT_MAX-2
 */
template<class T>
class YPair {
public:
	unsigned ix;
	Treap1<T> *t;
	YPair(unsigned ix0, Treap1<T> *t0) { ix = ix0; t = t0; }
	YPair(unsigned ix0) { ix = ix0; }
	YPair() { }
};


template<class T>
unsigned intValue(const YPair<T> &p) {
	return p.ix;
}


template<class T>
class YFastTrie {
	static const int w = 32; // FIXME portability
	XFastTrie1<YPair<T> > xft;
	int n;
public:
	YFastTrie();
	virtual ~YFastTrie();
	T find(T x);
	bool add(T x);
	bool remove(T x);
	int size() { return n; }
	void clear() {} ;
};

template<class T>
YFastTrie<T>::YFastTrie() : xft() {
	xft.add(YPair<T>(UINT_MAX, new Treap1<T>()));
	n = 0;
}

template<class T>
YFastTrie<T>::~YFastTrie() {
/*  FIXME: Need to iterate over the YFastTrie elements
	XFastTrieNode1<YPair<T> > *u = xft.dummy.next;
	while (u != &xft.dummy) {
		delete u->x.t;
		u = u->next;
	}
*/
	xft.clear();
	n = 0;
}

template<class T>
T YFastTrie<T>::find(T x) {
	return xft.find(YPair<T>(intValue(x))).t->find(x);
}

template<class T>
bool YFastTrie<T>::add(T x) {
	unsigned ix = intValue(x);
	Treap1<T> *t = xft.find(YPair<T>(ix)).t;
	if (t->add(x)) {
		n++;
		if (rand() % w == 0) {
			Treap1<T> *t1 = (Treap1<T>*)t->split(x);
			xft.add(YPair<T>(ix, t1));
		}
		return true;
	}
	return false;
	return true;
}

template<class T>
bool YFastTrie<T>::remove(T x) {
	unsigned ix = intValue(x);
	XFastTrieNode1<YPair<T> > *u = xft.findNode(ix);
	bool ret = u->x.t->remove(x);
	if (ret) n--;
	if (u->x.ix == ix && ix != UINT_MAX) {
		Treap1<T> *t2 = u->child[1]->x.t;
		t2->absorb(*u->x.t);
		xft.remove(u->x);
	}
	return ret;
}

} /* namespace ods */
#endif /* YFASTTRIE_H_ */
