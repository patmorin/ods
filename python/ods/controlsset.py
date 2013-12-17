'''
Created on 2012-04-03

@author: morin
'''

import bisect

class ControlSSet(object):
    def __init__(self, iterable=[]):
        self.a = []
        self.add_all(iterable)
    
    def add(self, x):
        i = bisect.bisect_left(self.a, x)
        if i == len(self.a) or self.a[i] != x:
            bisect.insort_right(self.a, x)
            return True
        return False
    
    def remove(self, x):
        i = bisect.bisect_left(self.a, x)
        if i != len(self.a) and self.a[i] == x:
            self.a.pop(i)
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
        s = "["
        for x in self.a:
            s += str(x) + ","
        return s + "]"