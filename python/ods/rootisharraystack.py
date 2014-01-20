"""
A list implementation with O(1+n-i) update time and O(sqrt(n)) wasted space

Implements the List interface using a collection of arrays of sizes 
1, 2, 3,..., and so on.  The main advantage of this representation is that
it uses only O(sqrt(n)) pointers and there are never more than O(sqrt(n))
unused array entries.

This is an implementation of the (simpler) data structure described by 
Brodnik et al:

Andrej Brodnik, Svante Carlsson, Erik D. Demaine, J. Ian Munro, 
Robert Sedgewick: Resizable Arrays in Optimal Time and Space. WADS 1999: 37-48
"""
from math import ceil, sqrt

from utils import new_array
from arraystack import ArrayStack
from base import BaseList

class RootishArrayStack(BaseList):
    def __init__(self, iterable=[]):
        self._initialize()
        self.add_all(iterable)
        
    def _initialize(self):
        self.n = 0
        self.blocks = ArrayStack()

    def _i2b(self, i):
        return int(ceil((-3.0 + sqrt(9 + 8*i)) / 2.0))
    
    def grow(self):
        self.blocks.append(new_array(self.blocks.size()+1))
    
    def shrink(self):
        r = self.blocks.size()
        while r > 0 and (r-2)*(r-1)/2 >= self.n:
            self.blocks.remove(self.blocks.size()-1)
            r -= 1
    
    def get(self, i):
        if i < 0 or i > self.n - 1: raise IndexError()
        b = self._i2b(i)
        j = i - b*(b+1)/2
        return self.blocks.get(b)[j]

    def set(self, i, x):
        if i < 0 or i > self.n - 1: raise IndexError()
        b = self._i2b(i)
        j = i - b*(b+1)/2
        y = self.blocks.get(b)[j]
        self.blocks.get(b)[j] = x
        return y
    
    def add(self, i, x):
        if i < 0 or i > self.n: raise IndexError()
        r = self.blocks.size()
        if r*(r+1)/2 < self.n + 1: self.grow()
        self.n += 1
        for j in range(self.n-1, i, -1):
            self.set(j, self.get(j-1))
        self.set(i, x)
    
    def remove(self, i):
        if i < 0 or i > self.n - 1: raise IndexError()
        x = self.get(i)
        for j in range(i, self.n-1):
            self.set(j, self.get(j+1))
        self.n -= 1
        r = self.blocks.size()
        if (r-2)*(r-1)/2 >= self.n: self.shrink()
        return x

    def clear(self):
        self.blocks.clear()
        self.n = 0


