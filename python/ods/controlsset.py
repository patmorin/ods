'''
Created on 2012-04-03

@author: morin
'''

import bisect

from collection import Collection

class ControlSSet(Collection):
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
    
    def __str__(self):
        return "[" + ','.join([str(x) for x in self.a]) + "]"
        
    def __iter__(self):
        return a.__iter__()
        
        
