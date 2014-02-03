"""A basic binary tree implementation"""

from arrayqueue import ArrayQueue

class BinaryTree(object):
    class Node(object):
        def __init__(self):
            self.left = self.right = self.parent = None

    def __init__(self):
        super(BinaryTree, self).__init__()
        self.nil = None
        self.r = None
        self.initialize()
        
    def initialize(self):
        self.r = None
        
    def depth(self, u):
        d = 0
        while (u != self.r):
            u = u.parent
            d += 1
        return d
    
    def size(self):
        return self._size(self.r)
    
    def _size(self, u):
        if u == self.nil: return 0
        return 1 + self._size(u.left) + self._size(u.right)
    
    def size2(self):
        u = self.r
        prv = self.nil
        n = 0
        while u != self.nil:
            if prv == u.parent:
                n += 1
                if u.left != self.nil: nxt = u.left
                elif u.right != self.nil: nxt = u.right
                else: nxt = u.parent
            elif prv == u.left:
                if u.right != self.nil: nxt = u.right
                else: nxt = u.parent
            else:
                nxt = u.parent
            prv = u
            u = nxt
        return n 
    
    def height(self):
        return self.height_r(self.r)
    
    def _height(self, u):
        if u == self.nil: return 0
        return 1 + max(self._height(u.left), self._height(u.right))
    
    def traverse(self, u):
        if u == self.nil: return
        self.traverse(u.left)
        self.traverse(u.right)
        
    def traverse2(self):
        u = self.r
        prv = self.nil
        while u != self.nil:
            if prv == u.parent:
                if u.left != self.nil: nxt = u.left
                elif u.right != self.nil: nxt = u.right
                else: nxt = u.parent
            elif prv == u.left:
                if u.right != self.nil: nxt = u.right
                else: nxt = u.parent
            else:
                nxt = u.parent
            prv = u
            u = nxt

    def bf_traverse(self):
        q = ArrayQueue()
        if self.r != self.nil: q.add(self.r)
        while q.size() > 0:
            u = q.remove()
            if u.left != self.nil: q.add(u.left)
            if u.right != self.nil: q.add(u.right)
            
    def first_node(self):
        """Find the first node in an in-order traversal"""
        w = self.r
        if w == self.nil: return self.nil
        while w.left != self.nil:
            w = w.left
        return w
    
    def next_node(self, w):
        """Find the node that follows w in an in-order traversal"""
        if w.right != self.nil:
            w = w.right
            while w.left != self.nil:
                w = w.left
        else:
            while w.parent != self.nil and w.parent.left != w:
                w = w.parent
            w = w.parent
        return w

        
