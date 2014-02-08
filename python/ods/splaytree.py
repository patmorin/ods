"""An implementation of splay trees.

This is an implementation of Sleator and Tarjan's splay trees:

  D.D. Sleator and R.E. Tarjan. Self-adjusting binary search trees. 
  Journal of the ACM, 32(1985), 652-686.
"""
import random

from binarysearchtree import BinarySearchTree

class SplayTree(BinarySearchTree):
    def __init__(self, iterable=[]):
        super(SplayTree, self).__init__(iterable)
    
    def splay(self, u):
        while u.parent is not self.nil:
            if u.parent.parent is self.nil: # u is a child of the root
                if u is u.parent.left: # zig
                    self.rotate_right(u.parent)  
                else: # zag
                    self.rotate_left(u.parent) 
            elif u is u.parent.left:
                if u.parent is u.parent.parent.left: # zig-zig
                    self.rotate_right(u.parent.parent)
                    self.rotate_right(u.parent)
                else: # zig-zag
                    self.rotate_right(u.parent)
                    self.rotate_left(u.parent)
            else:
                if u.parent is u.parent.parent.right: # zag-zag
                    self.rotate_left(u.parent.parent)
                    self.rotate_left(u.parent)
                else: # zag-zig
                    self.rotate_left(u.parent)
                    self.rotate_right(u.parent)
                    
    def find(self, x):
        w = self.r
        prev = self.nil
        z = self.nil
        while w is not self.nil: 
            prev = w
            if (x < w.x):
                z = w
                w = w.left
            elif (x > w.x):
                w = w.right
            else:
                self.splay(w)
                return w.x
        if prev is not self.nil: self.splay(prev)
        if z == self.nil: return None
        return z.x

    def add(self, x):
        p = self._find_last(x)
        u = self._new_node(x)
        if self._add_child(p, u):
            self.splay(u)
            return True
        return False
        
    def meld(self, r1, r2):
        if r2 is self.nil: return r1
        r2.parent = self.nil
        while r2.left is not self.nil:
            r2 = r2.left
        self.splay(r2)       
        r2.left = r1
        if r1 is not self.nil: r1.parent = r2
        return r2

    def remove(self, x):
        u = self._find_last(x)
        if u is not self.nil and x == u.x:
            w = self.meld(u.left, u.right)
            if u is self.r:
                self.r = w
            elif u is u.parent.left:
                u.parent.left = w
            else:
                u.parent.right = w
            if w is not self.nil:
                w.parent = u.parent
                self.splay(w)
            return True
        return False
       
        


if __name__ == "__main__":
    import random
    n = 100
    t = SplayTree()
    print "adding..."
    for i in range(n):
        t.add(random.randrange(2*n))
    print t
    
    print "finding..."
    for i in range(n):
        t.find(random.randrange(2*n))
    print t
        
    print "removing..."
    for i in range(n):
        t.remove(random.randrange(2*n))
    print t
    
    
    
