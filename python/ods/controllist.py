"""A class that wraps a python list in the ods List interface"""
from base import BaseList

class ControlList(BaseList):
    def __init__(self, iterable=[]):
        self.a = []
        self.add_all(iterable)
        
    def get(self, i):
        return self.a[i]
    
    def set(self, i, x): #@ReservedAssignment
        y = self.a[i]
        self.a[i] = x
        return y
        
    def add(self, i, x):
        self.a.insert(i, x)
    
    def remove(self, i):
        self.a.pop(i)
    
    def size(self):
        return len(self.a)
