'''
Created on 2012-04-04

@author: morin
'''

from ods.controlsset import ControlSSet
import random
        

def sset_test(t=ControlSSet()):
    t2 = ControlSSet()

    n = 200
    for i in range(n):
        x = random.randrange(0,5*n)
        t.add(x)
        t2.add(x)
    print len(t)
    print len(t2)
    st = str(t)
    st2 = str(t2)
    print "t  = " + st
    print "t2 = " + st2
    assert(st == st2)

    for i in range(n):
        x = random.randrange(0,5*n)
        y = t.find(x)
        y2 = t2.find(x)
        assert(y == y2)

    for i in range(n):
        x = random.randrange(0,5*n)
        b = t.remove(x)
        b2 = t2.remove(x)
        assert(b == b2)
        
    st = str(t)
    st2 = str(t2)
    assert(st == st2)
