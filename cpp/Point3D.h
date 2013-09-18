
#include "utils.h"

namespace ods {

class Point3D {
protected:
	int x0, x1, x2;

public:	
	unsigned hashCode() {
		// random number from random.org
		long long z[] = {0x2058cc50L, 0xcb19137eL, 0x2cb6b6fdL};
		long zz = 0xbea0107e5067d19dL;

		long h0 = ods::hashCode(x0);
		long h1 = ods::hashCode(x1);
		long h2 = ods::hashCode(x2);
		return (int)(((z[0]*h0 + z[1]*h1 + z[2]*h2)*zz) >> 32);
	}
};

} // namespace ods

