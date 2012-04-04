'''
Created on 2012-04-04

@author: morin
'''

from binarysearchtree import BinarySearchTree          
from controlsset import ControlSSet
import random
        
        
t = BinarySearchTree()
print t

t2 = ControlSSet()

n = 20
for i in xrange(n):
    x = random.randrange(0,5*n)
    t.add(x)
    t2.add(x)
st = str(t)
st2 = str(t2)
print "t  = " + st
print "t2 = " + st2
assert(st == st2)

for i in xrange(n):
    x = random.randrange(0,5*n)
    y = t.find(x)
    y2 = t2.find(x)
    assert(y == y2)


for i in xrange(n):
    x = random.randrange(0,5*n)
    t.remove(x)
    t2.remove(x)
st = str(t)
st2 = str(t2)
print "t  = " + st
print "t2 = " + st2
assert(st == st2)