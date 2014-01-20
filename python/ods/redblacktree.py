"""An implementation of Guibas and Sedgewick's red-black trees

This is an implementation of left-leaning red-black trees.  The best
documentation for this implementation is available in Chapter 9 of
Open Data Structures.

Leonidas J. Guibas, Robert Sedgewick: A Dichromatic Framework for Balanced
   Trees. FOCS 1978: 8-21
"""
from binarysearchtree import BinarySearchTree

red = 0
black = 1

class RedBlackTree(BinarySearchTree):
    class Node(BinarySearchTree.Node):
        def __init__(self, x):
            super(RedBlackTree.Node, self).__init__(x)
            self.colour = black
        
    def _new_node(self, x):
        u = RedBlackTree.Node(x)
        u.left = u.right = u.parent = self.nil
        return u
        
    def __init__(self, iterable=[]):
        self.nil = RedBlackTree.Node(None)
        self.nil.right = self.nil.left = self.nil.parent = self.nil
        super(RedBlackTree, self).__init__([], self.nil)
        self.r = self.nil
        self.add_all(iterable)
        
    def push_black(self, u):
        u.colour -= 1
        u.left.colour += 1
        u.right.colour += 1
        
    def pull_black(self, u):
        u.colour += 1
        u.left.colour -= 1
        u.right.colour -= 1
        
    def flip_left(self, u):
        self.swap_colours(u, u.right)
        self.rotate_left(u)
        
    def flip_right(self, u):
        self.swap_colours(u, u.left)
        self.rotate_right(u)
        
    def swap_colours(self, u, w):
        (u.colour, w.colour) = (w.colour, u.colour)
        
    def add(self, x):
        u = self._new_node(x)
        u.colour = red
        if self.add_node(u):
            self.add_fixup(u)
            return True
        return False
        
    def add_fixup(self, u):
        while u.colour == red:
            if u == self.r:
                u.colour = black
            w = u.parent
            if w.left.colour == black:
                self.flip_left(w)
                u = w
                w = u.parent
            if w.colour == black:
                return   # red-red edge is eliminated - done
            g = w.parent
            if g.right.colour == black:
                self.flip_right(g)
                return
            self.push_black(g)
            u = g
            
    def remove(self, x):
        u = self._find_last(x)
        if u == self.nil or u.x != x:
            return False
        w = u.right
        if w == self.nil:
            w = u
            u = w.left
        else:
            while w.left != self.nil:
                w = w.left
            u.x = w.x
            u = w.right
        self.splice(w)
        u.colour += w.colour
        u.parent = w.parent
        self.remove_fixup(u)
        return True
    
    def remove_fixup(self, u):
        while u.colour > black:
            if u == self.r:
                u.colour = black
            elif u.parent.left.colour == red:
                u = self.remove_fixup_case1(u)
            elif u == u.parent.left:
                u = self.remove_fixup_case2(u)
            else:
                u = self.remove_fixup_case3(u)
        if u != self.r:   # restore left-leaning property, if needed
            w = u.parent
            if w.right.colour == red and w.left.colour == black:
                self.flip_left(w)
            
    def remove_fixup_case1(self, u):
        self.flip_right(u.parent)
        return u
    
    def remove_fixup_case2(self, u):
        w = u.parent
        v = w.right
        self.pull_black(w)
        self.flip_left(w)
        q = w.right
        if q.colour == red:
            self.rotate_left(w)
            self.flip_right(v)
            self.push_black(q)
            if v.right.colour == red:
                self.flip_left(v)
            return q
        else:
            return v
    
    def remove_fixup_case3(self, u):
        w = u.parent
        v = w.left
        self.pull_black(w)
        self.flip_right(w)  # w is now red
        q = w.left
        if q.colour == red:  # q-w is red-red
            self.rotate_right(w)
            self.flip_left(v)
            self.push_black(q)
            return q
        else:
            if v.left.colour == red:
                self.push_black(v)
                return v
            else:  # ensure left-leaning
                self.flip_left(v)
                return w
    
    