"""For testing the functionality of list implementations"""
import random

from nose.tools import *
from ods import ControlList

def list_cmp(l1, l2):
    assert(l1 == l2)
    assert(list(l1) == list(l2))
    assert(len(l1) == len(l2))
    for i in range(len(l1)):
        assert(l1.get(i) == l2.get(i))

def exercise_list(ell=None):
    l1 = [ell, ControlList()][ell is None]
    l2 = ControlList()
    random.seed(5)
    n = 100
    for i in range(5):
        if i == 2: 
            l1.clear()
            l2.clear()
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

