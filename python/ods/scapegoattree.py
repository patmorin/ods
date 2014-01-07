import math

from binarysearchtree import BinarySearchTree

def log32(q):
    return int(math.log(q, 1.5))

class ScapegoatTree(BinarySearchTree):
    def __init__(self):
        super(ScapegoatTree, self).__init__()
        self._initialize()
        
    def _initialize(self):
        self.n = 0
        self.q = 0
        
    def remove(self, x):
        if super(ScapegoatTree, self).remove(x):
            if 2*n < q:
                self.rebuild(self.r)
                q = n
            return True
        return False
        
        
    def add_with_depth(self, x):
        "A binary tree add(x) method that returns the depth of the added node"""
        u = self._new_node(x)
        w = self.r
        if w == self.nil:
            self.r = u
            self.n += 1
            self.q += 1
            return (u, 0)
        done = False
        d = 0
        while not done:
            if x < w.x:
                if w.left == self.nil:
                    w.left = u
                    u.parent = w
                    done = True
                else:
                    w = w.left
            elif x > w.x:
                if w.right == self.nil:
                    w.right = u
                    u.parent = w
                    done = True
                else:
                    w = w.right
            else:
                return (None, -1)
            d += 1
        self.n += 1
        self.q += 1
        return (u, d)
    
    def add(self, x):
        (u, d) = self.add_with_depth(x)
        if d > log32(self.q):
            # depth exceeded, find scapegoat
            w = u.parent
            while 3*size(w) <= 2*size(w.parent):
                w = w.parent
            self.rebuild(w.parent)
        return d >= 0
        
    def rebuild(self, u):
        ns = size(u)
        p = u.parent
        a = new_array(ns)
        pack_into_array(u, a, 0)
        if p == self.nil:
            self.r = build_balanced(a, 0, ns)
            self.r.parent = nil
        elif p.right == u:
            p.right = build_balanced(a, 0, ns)
            p.right.parent = p
        else:
            p.left = build_balanced(a, 0, ns)
            p.left.parent = p
        
    def pack_into_array(self, u, a, i):
        if u == self.nil:
            return i
        i = pack_into_array(u.left, a, i)
        a[i] = u
        i += 1
        return pack_into_array(u.right, a, i)
        
    def build_balanced(self, a, i, ns):
        if ns == 0:
            return self.nil
        m = ns // 2
        a[i+m].left = build_balanced(a, i, m)
        if a[i+m].left != nil:
            a[i+m].left.parent = a[i+m]
        a[i+m].right = build_balanced(a, i+m+1, ns-m-1)
        if a[i+m].right != nil:
            a[i+m].right.parent = a[i+m]
        return a[i+m]
        
