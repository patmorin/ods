/*
 * DualArrayDeque.cpp
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#include "DualArrayDeque.h"
#include "utils.h"

namespace ods {


template DualArrayDeque<int>::DualArrayDeque();
template DualArrayDeque<int>::~DualArrayDeque();
template int DualArrayDeque<int>::get(int);
template int DualArrayDeque<int>::set(int,int);
template void DualArrayDeque<int>::add(int,int);
template int DualArrayDeque<int>::remove(int);
template int DualArrayDeque<int>::size();



} /* namespace ods */
