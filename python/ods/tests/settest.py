
import random

def set_cmp(s1, s2):
    """Compare s1 and s2 several different ways"""
    assert(len(s1) == len(s2))
    assert(sorted(s1) == sorted(s2))
    for x in s1:
        assert(x in s2)
    for x in s2:
        assert(x in s1)
        
def exercise_set(s):
    s1 = s
    s2 = set()
    
    n = 100
    for j in range(5):
        if j == 2:
            s1.clear()
            s2.clear()
        for _ in range(n):
            x = random.randrange(2*n)
            s1.add(x)
            s2.add(x)
            set_cmp(s1, s2)
        
        for i in range(2*n):
            assert((i in s1) == (i in s2))
                           
        for i in range(n):
            x = random.randrange(2*n)
            if x in s2:
                s1.remove(x)
                s2.remove(x)
            set_cmp(s1, s2)
            
        for i in range(2*n):
            assert((i in s1) == (i in s2))
        

