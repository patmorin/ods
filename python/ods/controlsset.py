"""A class that implements a stupid SSet (for testing purposes)"""

import bisect

from base import BaseSet

class ControlSSet(BaseSet):
    def __init__(self, iterable=[]):
        self.a = []
        self.add_all(iterable)
        
    def add_all(self, iterable):
        for x in iterable:
            self.add(x)
    
    def add(self, x):
        i = bisect.bisect_left(self.a, x)
        if i == len(self.a) or self.a[i] != x:
            bisect.insort_right(self.a, x)
            return True
        return False
    
    def remove(self, x):
        i = bisect.bisect_left(self.a, x)
        if i != len(self.a) and self.a[i] == x:
            del self.a[i]
            return True
        return False
    
    def find(self, x):
        i = bisect.bisect_left(self.a, x)
        if i != len(self.a): 
            return self.a[i]
        return None
    
    def size(self):
        return len(self.a)
    
    def clear(self):
        self.a = list()
    
    def __iter__(self):
        return self.a.__iter__()
        
        
