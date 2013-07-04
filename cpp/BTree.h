/*
 * BTree.h
 * TODO: This thing needs a lot of work. It doesn't do any cleanup at all right now
 *  Created on: 2013-07-03
 *      Author: morin
 */

#ifndef BTREE_H_
#define BTREE_H_

#include "array.h"
#include "BlockStore.h"

namespace ods {

template<class T>
class BTree {
public:
	T null;
	int b; // the maximum number of children of a node (must be odd)
	int B; // b div 2
	int n; // number of elements stored in the tree
	int ri; // index of the root

	int findIt(array<T> &a, T x) {
		int lo = 0, hi = a.length;
		while (hi != lo) {
			int m = (hi+lo)/2;
			int cmp = a[m] == null ? -1 : compare(x, a[m]);
			if (cmp < 0)
				hi = m;      // look in first half
			else if (cmp > 0)
				lo = m+1;    // look in second half
			else
				return -m-1; // found it
		}
		return lo;
	}

	class Node;
	BlockStore<Node*> bs;
	class Node {
	public:
		BTree *t;
		int id;
		array<T> keys;
		array<int> children;
	public:
		Node(BTree *t) : keys(t->b), children(t->b+1) {
			this->t = t;
			std::fill_n(children+0, t->b+1, -1);
			id = t->bs.placeBlock(this);
		}

		bool isLeaf() {
			return children[0] < 0;
		}

		bool isFull() {
			return keys[keys.length-1] != t->null;
		}

		int size() {
			int lo = 0, h = keys.length;
		    while (h != lo) {
		    	int m = (h+lo)/2;
		        if (keys[m] == t->null)
		        	h = m;
		        else
		        	lo = m+1;
		    }
		    return lo;
        }

		bool add(T x, int ci) {
			int i = t->findIt(keys, x);
			if (i < 0) return false;
			if (i < keys.length-1) std::copy(keys + i, keys + t->b-1, keys + i+1);
			keys[i] = x;
			if (i < keys.length-1) std::copy(children + i+1, children + t->b, children + i+2);
			children[i+1] = ci;
			return true;
		}

		T remove(int i) {
            T y = keys[i];
            std::copy(keys + i+1, keys + t->b, keys + i);
            keys[keys.length-1] = t->null;
            return y;
		}

		Node* split() {
            Node *w = new Node(t);
            int j = keys.length/2;
            std::copy(keys + j, keys + t->b, w->keys + 0);
            std::fill(keys + j, keys + t->b, t->null);
            std::copy(children + j+1, children + t->b+1, w->children + 0);
            std::fill(children + j+1, children + t->b+1, -1);
            t->bs.writeBlock(id, this);
            return w;
		}

		virtual ~Node() {
		}
	};

	void clear() {
		// TODO: cleanup
		ri = (new Node(this))->id;
	}

	Node* addRecursive(T x, int ui) {
		Node *u = bs.readBlock(ui);
		int i = findIt(u->keys, x);
		if (i < 0) throw(-1);
		if (u->children[i] < 0) { // leaf node, just add it
			u->add(x, -1);
			bs.writeBlock(u->id, u);
		} else {
			Node* w = addRecursive(x, u->children[i]);
			if (w != NULL) {  // child was split, w is new child
				x = w->remove(0);
				bs.writeBlock(w->id, w);
				u->add(x, w->id);
				bs.writeBlock(u->id, u);
			}
		}
		return u->isFull() ? u->split() : NULL;
	}


	void merge(Node *u, int i, Node *v, Node *w) {
		assert(v->id == u->children[i]);
		assert(w->id == u->children[i+1]);
		int sv = v->size();
		int sw = w->size();
		// copy keys from w to v
		std::copy(w->keys + 0, w->keys + sw, v->keys + sv+1);
		std::copy(w->children + 0, w->children + sw+1, v->children + sv+1);
		// add key to v and remove it from u
		v->keys[sv] = u->keys[i];
		std::copy(u->keys + i+1, u->keys + b, u->keys + i);
		u->keys[b-1] = null;
		std::copy(u->children + i+2, u->children + b+1, u->children + i+1);
		u->children[b] = -1;
	}

	void shiftLR(Node *u, int i, Node *v, Node *w) {
		int sw = w->size();
		int sv = v->size();
		int shift = ((sw+sv)/2) - sw;  // num. keys to shift from v to w
		// make space for new keys in w
		std::copy(w->keys + 0, w->keys + sw, w->keys + shift);
		std::copy(w->children + 0, w->children + sw+1, w->children + shift);
		// move keys and children out of v and into w (and u)
		w->keys[shift-1] = u->keys[i];
		u->keys[i] = v->keys[sv-shift];
		std::copy(v->keys + sv-shift+1, v->keys + sv, w->keys + 0);
		std::fill(v->keys + sv-shift, v->keys + sv, null);
		std::copy(v->children + sv-shift+1, v->children + sv+1, w->children + 0);
		std::fill(v->children + sv-shift+1, v->children + sv+1, -1);
	}

	void shiftRL(Node *u, int i, Node *v, Node *w) {
		assert(w->id == u->children[i] && v->id == u->children[i+1]);
		int sw = w->size();
		int sv = v->size();
		int shift = ((sw+sv)/2) - sw;  // num. keys to shift from v to w
		// shift keys and children from v to w
		w->keys[sw] = u->keys[i];
		std::copy(v->keys + 0, v->keys + shift-1, w->keys + sw+1);
		std::copy(v->children + 0, v->children + shift, w->children + sw+1);
		u->keys[i] = v->keys[shift-1];
		// delete keys and children from v
		std::copy(v->keys + shift, v->keys + b, v->keys + 0);
		std::fill(v->keys + sv-shift, v->keys + b, null);
		std::copy(v->children + shift, v->children + b+1, v->children + 0);
		std::fill(v->children + sv-shift+1, v->children + b+1, -1);
	}

	void checkUnderflowZero(Node *u, int i) {
		Node *w = bs.readBlock(u->children[i]);
		if (w->size() < B-1) {  // underflow at w
			Node *v = bs.readBlock(u->children[i+1]);
			if (v->size() > B) { // w can borrow from v
				shiftRL(u, i, v, w);
			} else { // w will absorb w
				merge(u, i, w, v);
				u->children[i] = w->id;
			}
		}
	}

	void checkUnderflowNonZero(Node *u, int i) {
		Node *w = bs.readBlock(u->children[i]);
		if (w->size() < B-1) {  // underflow at w
			Node *v = bs.readBlock(u->children[i-1]);
			if (v->size() > B) {  // w can borrow from v
				shiftLR(u, i-1, v, w);
			} else { // v will absorb w
				merge(u, i-1, v, w);
			}
		}
	}

	void checkUnderflow(Node* u, int i) {
		if (u->children[i] < 0) return;
		if (i == 0)
			checkUnderflowZero(u, i); // use u's right sibling
		else
			checkUnderflowNonZero(u, i);
	}

	T removeSmallest(int ui) {
		Node* u = bs.readBlock(ui);
		if (u->isLeaf())
			return u->remove(0);
		T y = removeSmallest(u->children[0]);
		checkUnderflow(u, 0);
		return y;
	}

	bool removeRecursive(T x, int ui) {
		if (ui < 0) return false;  // didn't find it
		Node* u = bs.readBlock(ui);
		int i = findIt(u->keys, x);
		if (i < 0) { // found it
			i = -(i+1);
			if (u->isLeaf()) {
				u->remove(i);
			} else {
				u->keys[i] = removeSmallest(u->children[i+1]);
				checkUnderflow(u, i+1);
			}
			return true;
		} else if (removeRecursive(x, u->children[i])) {
			checkUnderflow(u, i);
			return true;
		}
		return false;
	}

public:
	BTree(int b) : bs() {
		null = (T)NULL;
		b += (b+1)%2;
		this->b = b;
		B = b/2;
		ri = (new Node(this))->id;
	}

	// FIXME: This has problems if we try to add a duplicate value
	bool add(T x) {
        Node *w;
        try {
        	w = addRecursive(x, ri);
        } catch (int e) {
        	return false; // adding duplicate value
        }
        if (w != NULL) {   // root was split, make new root
			Node *newroot = new Node(this);
			x = w->remove(0);
			bs.writeBlock(w->id, w);
			newroot->children[0] = ri;
			newroot->keys[0] = x;
			newroot->children[1] = w->id;
			ri = newroot->id;
			bs.writeBlock(ri, newroot);
        }
        n++;
        return true;
	}

	bool remove(T x) {
		if (removeRecursive(x, ri)) {
			n--;
			Node *r = bs.readBlock(ri);
			if (r->size() == 0 && n > 0) // root has only one child
				ri = r->children[0];
			return true;
		}
		return false;
	}

	virtual ~BTree() {
		// FIXME: Do this
	} ;


	T find(T x) {
		T z = null;
		int ui = ri;
		while (ui >= 0) {
			Node *u = bs.readBlock(ui);
			int i = findIt(u->keys, x);
			if (i < 0) return u->keys[-(i+1)]; // found it
			if (u->keys[i] != null)
				z = u->keys[i];
			ui = u->children[i];
		}
		return z;
	}

	int size() {
		return n;
	}

};

} /* namespace ods */
#endif /* BTREE_H_ */
