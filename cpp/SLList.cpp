/*
 * SLList.cpp
 *
 *  Created on: 2011-11-25
 *      Author: morin
 */

#include "SLList.h"

namespace ods {

template SLList<int>::SLList();
template SLList<int>::~SLList();
template int SLList<int>::push(int x);
template bool SLList<int>::add(int x);
template int SLList<int>::remove();
template int SLList<int>::pop();
template int SLList<int>::size();

} /* namespace ods */
