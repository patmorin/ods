'''
An array-based list implementation with O(1+n-i) amortized update time.

Stores the list in an array, a, so that the i'th list item is stored
at a[(j+i)%len(a)].

Uses a doubling strategy for resizing a when it becomes full or too empty.
'''
from utils import new_array

from base import BaseList

class FastArrayStack(BaseList):
    def __init__(self, iterable=[]):
        self._initialize()
        self.add_all(iterable)
        
    def _initialize(self):
        self.a = new_array(1)
        self.n = 0

    def get(self, i):
        if i < 0 or i >= self.n: raise IndexError()
        return self.a[i]

    def set(self, i, x):
        if i < 0 or i >= self.n: raise IndexError()
        y = self.a[i]
        self.a[i] = x
        return y

    def add(self, i, x): 
        if i < 0 or i > self.n: raise IndexError()
        if self.n == len(self.a): self._resize()
        self.a[i+1:self.n+1] = self.a[i:self.n] 
        self.a[i] = x
        self.n += 1

    def remove(self, i): 
        if i < 0 or i >= self.n: raise IndexError()
        x = self.a[i]
        self.a[i:self.n-1] = self.a[i+1:self.n]
        self.n -= 1
        if len(self.a) >= 3*self.n: self._resize()
        return x
  
    def _resize(self):
        b = new_array(max(1, 2*self.n))
        b[0:self.n] = self.a[0:self.n]
        self.a = b
    


