"""A skiplist implementation of a list

"""
import random
import numpy
from utils import new_array
from odslist import ODSList

class Node(object):
    """A node in a skip list"""
    def __init__(self, x, h):
        self.x = x
        self.next = new_array(h+1)
        self.length = numpy.zeros(h+1, dtype=int)

    def height(self):
        return len(self.next) - 1
   
        
class SkiplistList(ODSList):
    def __init__(self, iterable=[]):
        self.h = 0
        self.n = 0
        self.sentinel = Node(None, 32)
        self.stack = new_array(self.sentinel.height()+1)
        for x in iterable:
            self.append(x)
    
    def find_pred(self, i):
        u = self.sentinel
        r = self.h
        j = -1
        while r >= 0:
            while u.next[r] != None and j + u.length[r] < i:
                j += u.length[r]
                u = u.next[r]  # go right in list r
            r -= 1  # go down into list r-1
        return u

    def get(self, i):
        if i < 0 or i > self.n-1: raise IndexError()
        return self.find_pred(i).next[0].x

    def set(self, i, x):
        if i < 0 or i > self.n-1: raise IndexError()
        u = self.find_pred(i).next[0]
        y = u.x
        u.x = x
        return y
        
    def add_node(self, i, w):
        u = self.sentinel
        k = w.height()
        r = self.h
        j = -1
        while r >= 0:
            while u.next[r] != None and j+u.length[r] < i:
                j += u.length[r]
                u = u.next[r]
            u.length[r] += 1
            if r <= k:
                w.next[r] = u.next[r]
                u.next[r] = w
                w.length[r] = u.length[r] - (i-j)
                u.length[r] = i - j
            r -= 1
        self.n += 1
        return u
        
    def add(self, i, x):
        if i < 0 or i > self.n: raise IndexError()
        w = Node(x, self.pick_height())
        if w.height > self.h:
            self.h = w.height()
        self.add_node(i, w)
        
    def append(self, x):
        self.add(len(self), x)
                
    def remove(self, i, x):
        if i < 0 or i > self.n-1: raise IndexError()
        u = self.sentinel
        r = self.h
        j = -1
        while r >= 0:
            while u.next[r] != None and j + u.length[r] < i:
                j += u.length[r]
                u = u.next[r]
            u.length[r] -= 1
            if j + u.length[r] + 1 == i and u.next[r] != None:
                x = u.next[r].x
                u.length[r] += u.next[r].length[r]
                u.next[r] = u.next[r].next[r]
                if u == self.sentinel and u.next[r] == None:
                    self.h -= 1
            r -= 1
        self.n -= 1
        return x    

    def __iter__(self):
        u = self.sentinel.next[0]
        while u != None:
            yield u.x
            u = u.next[0]
                      
    def size(self):
        return self.n
        
    def pick_height(self):
        z = random.getrandbits(32)
        k = 0
        while z & 1:
            k += 1
            z = z // 2
        return k
        
        
sl = SkiplistList()
sl.add(0, "a")
print sl
sl.add(1, "b")
print sl
sl.add(0, "c")
print sl

