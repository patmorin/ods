
import random

def exercise_sort(sort, gen=random.random):
    """Run some tests on the sorting function, sort"""
    for n in [0, 1, 100, 1000, 10000]: 
        a = [gen() for _ in range(n)]
        b = a[:]
        # in-place sorts return None, other sorts return sorted array
        c = sort(b)
        if c is None: c = b
        assert(sorted(a) == list(c))
        
        