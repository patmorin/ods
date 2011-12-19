/*
 * RootishArrayStack.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#include "RootishArrayStack.h"

namespace ods {

template RootishArrayStack<int>::RootishArrayStack();
template RootishArrayStack<int>::~RootishArrayStack();
template void RootishArrayStack<int>::add(int,int);
template int RootishArrayStack<int>::remove(int);
template void RootishArrayStack<int>::grow();
template void RootishArrayStack<int>::shrink();

} /* namespace ods */
