import random

from ods import ControlSSet

        
def exercise_sset(t1):
    random.seed(0)

    t2 = ControlSSet()
    n = 200
    for k in range(5):
        for i in range(n):
            x = random.randrange(0,5*n)
            t1.add(x)
            t2.add(x)
            assert(str(t1) == str(t2))
            assert(len(t1) == len(t2))
    
        assert(t2 == t1)
        assert(len(t1) == len(t2))
        assert(str(t1) == str(t2))
    
        for i in range(n):
            x = random.randrange(0,5*n)
            y = t1.find(x)
            y2 = t2.find(x)
            assert(y == y2)
    
        assert(len(t1) == len(t2))
        assert(str(t1) == str(t2))
    
        for i in range(n):
            x = random.randrange(0,5*n)
            b1 = t1.remove(x)
            b2 = t2.remove(x)
            assert(b1 == b2)
            
        assert(len(t1) == len(t2))
        assert(str(t1) == str(t2))
    
        for i in range(n):
            x = random.randrange(0,5*n)
            y = t1.find(x)
            y2 = t2.find(x)
            assert(y == y2)
    
        assert(len(t1) == len(t2))
        assert(str(t1) == str(t2))
