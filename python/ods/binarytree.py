'''
Created on 2012-04-03

@author: morin
'''

from arrayqueue import ArrayQueue

class BinaryTree(object):
    class Node(object):
        def __init__(self):
            self.left = self.right = self.parent = None

    def __init__(self):
        self.r = None
        self.nil = None
        
    def depth(self, u):
        d = 0
        while (u != self.r):
            u = u.parent
            d += 1
        return d
    
    def size(self):
        return self.size_r(self.r)
    
    def size_r(self, u):
        if u == self.nil: return 0
        return 1 + self.size_r(u.left) + self.size_r(u.right)
    
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
    
    def height_r(self, u):
        if u == self.nil: return 0
        return 1 + max(self.height_r(u.left), self.height_r(u.right))
    
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
        if (self.r != self.nil): q.add(self.r)
        while (q.size() > 0):
            u = q.remove()
            if u.left != self.nil: q.add(u.left)
            if u.right != self.nil: q.add(u.right)
            
    '''
    Find the first node in an in-order traversal
    '''
    def first_node(self):
        w = self.r
        if w == self.nil: return self.nil
        while w.left != self.nil:
            w = w.left
        return w
    
    '''
    Find the node that follows w in an in-order traversal
    '''
    def next_node(self, w):
        if w.right != self.nil:
            w = w.right;
            while w.left != self.nil:
                w = w.left;
        else:
            while w.parent != self.nil and w.parent.left != w:
                w = w.parent;
            w = w.parent;
        return w;

        