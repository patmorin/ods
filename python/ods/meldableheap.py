"""An implementation of Gambin and Malinowsky's randomized meldable heaps

A. Gambin and A. Malinowski. Randomized meldable priority queues. 
  Proceedings of the XXVth Seminar on Current Trends in Theory and Practice
  of Informatics (SOFSEM'98), pp. 344-349, 1998
"""

import random
from base import BaseSet
from binarytree import BinaryTree

def random_bit():
    return random.getrandbits(1) == 0

class MeldableHeap(BinaryTree, BaseSet):
    class Node(BinaryTree.Node):
        def __init__(self, x):
            super(MeldableHeap.Node, self).__init__()
            self.x = x
            
    def __init__(self, iterable=[]):
        super(MeldableHeap, self).__init__()
        self.n = 0
        
    def _new_node(self, x):
        return MeldableHeap.Node(x)
    
    def find_min(self):
        if n == 0: raise IndexError('find_min on empty queue')
        return self.r.x

    def add(self, x):
        u = self._new_node(x)
        self.r = self.merge(u, self.r)
        self.r.parent = self.nil
        self.n += 1
        return True
    
    def remove(self):
        if self.n == 0: raise IndexError('remove from empty heap')
        x = self.r.x
        self.r = self.merge(self.r.left, self.r.right)
        if self.r != self.nil: self.r.parent = self.nil
        self.n -= 1
        return x
    
    def merge(self, h1, h2):
        if h1 == self.nil: return h2
        if h2 == self.nil: return h1
        if h2.x < h1.x: (h1, h2) = (h2, h1)
        if random_bit():
            h1.left = self.merge(h1.left, h2)
            h1.left.parent = h1
        else:
            h1.right = self.merge(h1.right, h2)
            h1.right.parent = h1
        return h1
    
    def __iter__(self):
        u = self.first_node()
        while u != self.nil:
            yield u.x
            u = self.next_node(u)

