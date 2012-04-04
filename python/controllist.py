'''
A class that wraps a python list in the ods List interface
Created on 2012-04-03

@author: morin
'''

class ControlList(object):
    def __init__(self):
        self.a = []
        
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
    
    def __len__(self):
        return len(self.a)
        