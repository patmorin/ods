'''
Created on 2012-04-03

@author: morin
'''

from binarytree import BinaryTree

class BinarySearchTree(BinaryTree):

    class Node(BinaryTree.Node):
        def __init__(self, x):
            super(BinarySearchTree.Node, self).__init__()
            self.x = x
            
    def __init__(self):
        super(BinarySearchTree, self).__init__()
        self.n = 0
        
    def _find_last(self, x):
        w = self.r
        prev = self.nil
        while w != self.nil: 
            prev = w;
            if (x < w.x):
                w = w.left;
            elif (x > w.x):
                w = w.right;
            else:
                return w;
        return prev;
        
    def _add_child(self, p, u):
        if p == self.nil:
            self.r = u; # inserting into empty tree
        else:
            if u.x < p.x:
                p.left = u;
            elif u.x > p.x:
                p.right = u;
            else:
                return False; # u.x is already in the tree
            u.parent = p;
        self.n += 1
        return True;  
    
    def find(self, x):
        w = self.r
        z = self.nil
        while w != self.nil:
            if x < w.x:
                z = w
                w = w.left
            elif x > w.x:
                w = w.right
            else:
                return w.x
        if z == self.nil: return None 
        return z.x
        
    def add(self, x):
        p = self._find_last(x);
        return self._add_child(p, BinarySearchTree.Node(x))
    
    def splice(self, u):
        if u.left != self.nil:
            s = u.left
        else:
            s = u.right;
        if u == self.r:
            self.r = s;
            p = self.nil;
        else:
            p = u.parent;
            if p.left == u:
                p.left = s;
            else:
                p.right = s; 
        if s != self.nil: 
            s.parent = p;
        self.n -= 1;

    
    def _remove_node(self, u):
        if u.left == self.nil or u.right == self.nil:
            self.splice(u)
        else: 
            w = u.right;
            while w.left != self.nil: 
                    w = w.left
            u.x = w.x
            self.splice(w)

    def remove(self, x):
        u = self._find_last(x);
        if u != self.nil and x == u.x:
            self._remove_node(u);
            return True;
        return False;
            

    def __str__(self):
        s = '[';
        u = self.first_node()
        while u != self.nil:
            s += "%r," % u.x
            u = self.next_node(u)
        return s + "]"
