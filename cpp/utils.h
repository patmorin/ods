/*
 * utils.h
 *
 *  Created on: 2011-11-23
 *      Author: morin
 */

#ifndef UTILS_H_
#define UTILS_H_

template<class T> inline
T min(T a, T b) {
	return ((a)<(b) ? (a) : (b));
}

template<class T> inline
T max(T a, T b) {
	return ((a)>(b) ? (a) : (b));
}



#endif /* UTILS_H_ */
