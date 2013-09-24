'''
Created on 2012-04-02

@author: morin
'''

from utils import new_array

class ArrayStack(object):
    def __init__(self):
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
        for j in range(self.n, i, -1):
            self.a[j] = self.a[j-1]
        self.a[i] = x
        self.n += 1

    def remove(self, i): 
        if i < 0 or i >= self.n: raise IndexError()
        x = self.a[i]
        for j in range(i, self.n-1): 
            self.a[j] = self.a[j+1]
        self.n -= 1
        if len(self.a) >= 3*self.n: self._resize()
        return x
  
    def _resize(self):
        b = new_array(max(1, 2*self.n))
        for i in range(self.n):
            b[i] = self.a[i]
        self.a = b
        
    def size(self):
        return self.n
    
    def __len__(self):
        return self.size()

    def __str__(self):
        return str([self.a[i] for i in range(self.n)])

        
