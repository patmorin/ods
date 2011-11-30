/*
 * RootishArrayStack.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#include "RootishArrayStack.h"

namespace ods {
template<class T>  RootishArrayStack<T>::RootishArrayStack()
{
	n = 0;
}

template<class T>  RootishArrayStack<T>::~RootishArrayStack()
{
}

template<class T>  void RootishArrayStack<T>::add(int i, T x)
{
    int r = blocks.size();
    if (r*(r+1)/2 < n + 1) grow();
    n++;
    for (int j = n-1; j > i; j--)
            set(j, get(j-1));
    set(i, x);
}

template<class T>  T RootishArrayStack<T>::remove(int i)
{
    T x = get(i);
    for (int j = i; j < n-1; j++)
            set(j, get(j+1));
    n--;
    int r = blocks.size();
    if ((r-2)*(r-1)/2 >= n) shrink();
    return x;
}

template<class T>  void RootishArrayStack<T>::grow()
{
    blocks.add(blocks.size(), new T[blocks.size()+1]);
}

template<class T>  void RootishArrayStack<T>::shrink()
{
    int r = blocks.size();
    while (r > 0 && (r-2)*(r-1)/2 >= n) {
            delete blocks.remove(blocks.size()-1);
            r--;
    }
}

template<class T>
void RootishArrayStack<T>::clear() {
	while (blocks.size() > 0) {
		T* b = blocks.remove(blocks.size()-1);
		delete b;
	}
}

template RootishArrayStack<int>::RootishArrayStack();
template RootishArrayStack<int>::~RootishArrayStack();
template void RootishArrayStack<int>::add(int,int);
template int RootishArrayStack<int>::remove(int);
template void RootishArrayStack<int>::grow();
template void RootishArrayStack<int>::shrink();

} /* namespace ods */
