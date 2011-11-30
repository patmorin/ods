/*
 * utils.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef UTILS_H_
#define UTILS_H_

namespace ods {

template<class T> inline
T min(T a, T b) {
	return ((a)<(b) ? (a) : (b));
}

template<class T> inline
T max(T a, T b) {
	return ((a)>(b) ? (a) : (b));
}

template<class T> inline
int compare(T &x, T &y) {
	if (x < y) return -1;
	if (x > y) return 1;
	return 0;
}

} /* namespace ods */


#endif /* UTILS_H_ */
