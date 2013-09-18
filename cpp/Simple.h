/** Fake file used for providing a code snippet */

namespace ods {

template<class T>
class Simple {
	int *a;
	int n;
	void snippet();
}

template<class T>
void  Simple<T>::snippet() {
	for (int i = 0; i < n; i++) 
		a[i] = i;
}

} /* namespace ods */
