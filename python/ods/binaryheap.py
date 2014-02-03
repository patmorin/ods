"""An implemnetation of a binary heap using an array"""
from utils import new_array
from base import BaseSet

def left(i):
    return 2*i + 1

def right(i):
    return 2*(i+1)

def parent(i):
    return (i-1)//2

class BinaryHeap(BaseSet):
    def __init__(self, iterable=[], a=None):
        if a: 
            self._make_heap(a)
        else:
            self._initialize()
            self.add_all(iterable)

    def _make_heap(self, a):
        self.a = a
        self.n = len(a)
        for i in range(self.n//2-1, -1, -1):
            self.trickle_down(i)
            
    def _initialize(self):
        self.a = new_array(1)
        self.n = 0

    clear = _initialize

    def resize(self):
        b = new_array(max(2*self.n, 1))
        for i in range(self.n):
            b[i] = self.a[i]
        self.a = b    

    def add(self, x):
        if len(self.a) < self.n+1:
            self.resize()
        self.a[self.n] = x
        self.n += 1
        self.bubble_up(self.n-1)
        return True

    def bubble_up(self, i):
        p = parent(i)
        while i > 0 and self.a[i] < self.a[p]:
            self.a[i], self.a[p] = self.a[p], self.a[i]
            i = p
            p = parent(i)

    def remove(self):
        x = self.a[0]
        self.a[0] = self.a[self.n-1]
        self.n -= 1
        self.trickle_down(0)
        if 3*self.n < len(self.a):
            self.resize()
        return x

    def trickle_down(self, i):
        while i >= 0:
            j = -1
            r = right(i)
            if r < self.n and self.a[r] < self.a[i]:
                l = left(i)
                if self.a[l] < self.a[r]:
                    j = l
                else:
                    j = r
            else:
                l = left(i)
                if l < self.n and self.a[l] < self.a[i]:
                    j = l
            if j >= 0:
                self.a[j], self.a[i] =     self.a[i], self.a[j]
            i = j



    def find_min(self):
        if n == 0: raise IndexError()
        return a[0]

    def __iter__(self):
        for i in range(self.n):
            yield self.a[i]


