import random

from nose.tools import *
from ods.arraystack import ArrayStack
from ods.controllist import ControlList

def list_cmp(l1, l2):
    assert(len(l1) == len(l2))
    for i in range(len(l1)):
        assert(l1.get(i) == l2.get(i))

def test_as():
    l1 = ArrayStack()
    l2 = ControlList()
    n = 100
    for _ in range(n):
        x = random.random();
        i = random.randrange(0, len(l1)+1)
        l1.add(i, x)
        l2.add(i, x)
        list_cmp(l1, l2)
    for _ in range(5*n):
        op = random.randrange(0,3)
        if (op == 0):
            i = random.randrange(0, len(l1)+1)
            x = random.random();
            l1.add(i, x)
            l2.add(i, x)
        elif op == 1:
            i = random.randrange(0, len(l1))
            x = random.random();
            l1.set(i,x)
            l2.set(i,x)
        else:
            i = random.randrange(0, len(l1))
            l1.remove(i)
            l2.remove(i)
        list_cmp(l1, l2)

    
