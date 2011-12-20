/*
 * FastSqrt.h
 *
 *  Created on: 2011-11-25
 *      Author: morin
 */

#ifndef FASTSQRT_H_
#define FASTSQRT_H_

namespace ods {

static int *sqrtab;
static int *logtab;

class FastSqrt {
protected:
	static const int halfint = 1<<16;
	static const int r = 29;

	/**
	 * Initialize the logarithm and square root tables
	 */
	static void inittabs(int dummy) {
		sqrtab = new int[halfint];
		logtab = new int[halfint];
		for (int d = 0; d < 16; d++)
			for (int k = 0; k < 1<<d; k++)
				logtab[(1<<d)+k] = d;
		int s = 1<<7;                        // sqrt(2^14)
		for (int i = 0; i < halfint; i++) {
			if ((s+1)*(s+1) <= i << 14) s++; // sqrt increases
			sqrtab[i] = s;
		}
	}
	/*  Fake code that appears only in the book
	void inittabs() {
		sqrtab = new int[1<<(r/2)];
		logtab = new int[1<<(r/2)];
		for (int d = 0; d < r/2; d++)
			for (int k = 0; k < 1<<d; k++)
				logtab[1<<d+k] = d;
		int s = 1<<(r/4);                    // sqrt(2^(r/2))
		for (int i = 0; i < 1<<(r/2); i++) {
			if ((s+1)*(s+1) <= i << (r/2)) s++; // sqrt increases
			sqrtab[i] = s;
		}
	}
	*/
public:
	static void init() { inittabs(0); };
	static int log(int x) {
		if (x >= halfint)
			return 16 + logtab[x>>16];
		return logtab[x];
	}

	static int sqrt(int x) {
		int rp = log(x);
		int upgrade = ((r-rp)/2) * 2;
		int xp = x << upgrade;  // xp has r or r-1 bits
		int s = sqrtab[xp>>(r/2)] >> (upgrade/2);
		while ((s+1)*(s+1) <= x) s++;  // executes at most twice
		return s;
	}

	/* Fake code used only in the book
	int sqrt(int x, int r) {
		int s = sqrtab[x>>r/2];
		while ((s+1)*(s+1) <= x) s++; // executes at most twice
		return s;
	}
	*/

};

} /* namespace ods */
#endif /* FASTSQRT_H_ */
