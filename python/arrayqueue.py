'''
Created on 2012-04-03

@author: morin
'''

class ArrayQueue(object):
    '''
    classdocs
    '''
'''
Created on 2012-04-02

@author: morin
'''

        
class ArrayDeque(object):
    def __init__(self):
        self.a = [None]
        self.j = 0
        self.n = 0

    def add(self, x): 
        if (self.n == len(self.a)): self._resize()
        self.a[(self.j+self.n) % len(self.a)] = x
        self.n += 1

    def remove(self): 
        if (self.n == 0): raise IndexError()
        x = self.a[self.j]
        self.j = (self.j+1) % len(self.a)
        self.n -= 1
        if len(self.a) > 3*self.n: self._resize()
        return x
  
    def _resize(self):
        b = [None] * max(1, 2*self.n)
        for k in xrange(self.n):
            b[k] = self.a[(self.j+k)%len(self.a)]
        self.a = b
        self.j = 0
        
    def size(self):
        return self.n
    
    def __len__(self):
        return self.size()
    
    def __str__(self):
        s = "["
        for i in xrange(self.n):
            s += self.get(i)
            if i < self.n-1: s += ","
        s += "]"
