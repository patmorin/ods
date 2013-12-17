
import random
from chainedhashtable import ChainedHashTable
from linearhashtable import LinearHashTable


def set_cmp(s1, s2):
    """Compare s1 and s2 several different ways"""
    assert(len(s1) == len(s2))
    assert(sorted(s1) == sorted(s2))
    for x in s1:
        assert(x in s2)
    for x in s2:
        assert(x in s1)
        
def set_tests(s1, s2):
    s1.clear()
    s2.clear()
    
    n = 100
    a = range(0, 2*n, 2)
    random.shuffle(a)
    for i in a:
        s1.add(i)
        s2.add(i)
        set_cmp(s1, s2)
    
    for i in range(2*n):
        assert((i in s1) == (i in s2))
                       
    for i in range(n//2):
        if a[i] in s1: s1.remove(a[i])
        if a[i] in s2: s2.remove(a[i])
        set_cmp(s1, s2)
        
    for i in range(2*n):
        assert((i in s1) == (i in s2))
        
if __name__ == "__main__":
    set_tests(ChainedHashTable(), set())
    set_tests(LinearHashTable(), set())

