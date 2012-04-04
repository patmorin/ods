'''
Created on 2012-04-02

@author: morin
'''

        
class ArrayDeque(object):
    def __init__(self):
        self.a = [None]
        self.j = 0
        self.n = 0

    def get(self, i):
        if (i < 0 or i >= self.n): raise IndexError()
        return self.a[(i+self.j)%len(self.a)]

    def set(self, i, x): #@ReservedAssignment
        if (i < 0 or i >= self.n): raise IndexError()
        y = self.a[(i+self.j)%len(self.a)]
        self.a[(i+self.j)%len(self.a)] = x
        return y

    def add(self, i, x): 
        if (i < 0 or i > self.n): raise IndexError()
        if (self.n == len(self.a)): self._resize()
        if (i < self.n/2):
            self.j = (self.j-1) % len(self.a)
            for k in xrange(i):
                self.a[(self.j+k)%len(self.a)] = self.a[(self.j+k+1)%len(self.a)]
        else:
            for k in xrange(self.n, i, -1):
                self.a[(self.j+k)%len(self.a)] = self.a[(self.j+k-1)%len(self.a)]
        self.a[(self.j+i)%len(self.a)] = x
        self.n += 1

    def remove(self, i): 
        if (i < 0 or i >= self.n): raise IndexError()
        x = self.a[(self.j+i)%len(self.a)]
        if (i < self.n / 2):
            for k in xrange(i, 0, -1):
                self.a[(self.j+k)%len(self.a)] = self.a[(self.j+k-1)%len(self.a)]
            self.j = (self.j+1) % len(self.a)
        else:
            for k in xrange(i, self.n-1): 
                self.a[(self.j+k)%len(self.a)] = self.a[(self.j+k+1)%len(self.a)]
        self.n -= 1
        if (len(self.a) >= 3*self.n): self._resize();
        return x;
  
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
        
