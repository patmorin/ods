import random

from ods import ControlSSet

        

def sset_test(t=ControlSSet()):
    t2 = ControlSSet()

    t.clear()
    n = 200
    for i in range(n):
        x = random.randrange(0,5*n)
        t.add(x)
        t2.add(x)
        assert(str(t) == str(t2))
        assert(len(t) == len(t2))

    assert(t2 == t)
    assert(len(t) == len(t2))
    assert(str(t) == str(t2))

    for i in range(n):
        x = random.randrange(0,5*n)
        y = t.find(x)
        y2 = t2.find(x)
        assert(y == y2)

    assert(len(t) == len(t2))
    assert(str(t) == str(t2))

    for i in range(n):
        x = random.randrange(0,5*n)
        b = t.remove(x)
        b2 = t2.remove(x)
        assert(b == b2)
        
    assert(len(t) == len(t2))
    assert(str(t) == str(t2))
