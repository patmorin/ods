"""An implementation of Treaps/Cartesian trees

This is an implementation of the data structure called a Treap by Aragon
and Seidel:

C. R. Aragon and R. Seidel. Randomized Search Trees. In Algorithmica, 
   Vol. 16, Number 4/5, pp. 464-497, 1996.

Pretty-much the same structure was discovered earlier by Vuillemin:

J. Vuillemin. A unifying look at data structures. 
   Communications of the ACM, 23(4), 229-239, 1980. 
"""
import random

from binarysearchtree import BinarySearchTree

class Treap(BinarySearchTree):
    class Node(BinarySearchTree.Node):
        def __init__(self, x):
            super(Treap.Node, self).__init__(x)
            self.p = random.random()
            
        def __str__(self):
            return "[%r,%f]" % (self.x, self.p)
            
    def __init__(self, iterable=[]):
        super(Treap, self).__init__(iterable)
    
    def _new_node(self, x):
        return Treap.Node(x)
        
    def add(self, x):
        u = self._new_node(x)
        if self.add_node(u):
            self.bubble_up(u)
            return True
        return False
            
    def bubble_up(self, u):
        while u != self.r and u.parent.p > u.p:
            if u.parent.right == u:
                self.rotate_left(u.parent)
            else:
                self.rotate_right(u.parent)

        if u.parent == self.nil:
            self.r = u
            
    def remove(self, x):
        u = self._find_last(x)
        if u is not None and u.x == x:
            self.trickle_down(u)
            self.splice(u)
            return True
        return False
        
    def trickle_down(self, u):
        while u.left is not None or u.right is not None:
            if u.left is None:
                self.rotate_left(u)
            elif u.right is None:
                self.rotate_right(u)
            elif u.left.p < u.right.p:
                self.rotate_right(u)
            else:
                self.rotate_left(u)
            if self.r == u:
                self.r = u.parent
        