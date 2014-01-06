'''
Created on 2012-04-04

@author: morin
'''

from ods.controlsset import ControlSSet
import random
        

def sset_test(t=ControlSSet()):
    t2 = ControlSSet()

    t.clear()
    n = 200
    print t
    print t2
    for i in range(n):
        x = random.randrange(0,5*n)
        t.add(x)
        t2.add(x)
        print t
        print t2
        assert(str(t) == str(t2))
        assert(len(t) == len(t2))

    print t
    print t2
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
