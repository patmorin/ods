"""An array-based implementation of a queue that uses modular arithmetic"""
from utils import new_array

from base import BaseSet

class ArrayQueue(BaseSet):
    def __init__(self, iterable=[]):
        self._initialize()
        self.add_all(iterable)
        
    def _initialize(self):
        self.a = new_array(1)
        self.j = 0
        self.n = 0
    
    def _resize(self):
        b = new_array(max(1, 2*self.n))
        for k in range(self.n):
            b[k] = self.a[(self.j+k) % len(self.a)]
        self.a = b
        self.j = 0
    
    def add(self, x):
        if self.n + 1 > len(self.a): self._resize()
        self.a[(self.j+self.n) % len(self.a)] = x
        self.n += 1
        return True

    def remove(self):
        if self.n == 0: raise IndexError()
        x = self.a[self.j]
        self.j = (self.j + 1) % len(self.a)
        self.n -= 1
        if len(self.a) >= 3*self.n: self._resize()
        return x
