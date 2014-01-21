"""An implementation of Willard's X-Fast tries

This structure is able to store w-bit integers with O(log w) time searches
and O(w) time addition/removal

D. E. Willard. Log-logarithmic worst-case range queries are possible in 
  space Theta(n). Information Processing Letters, 17, 81-84. 1984.
"""
from base import BaseSet
from binarytrie import BinaryTrie
from linearhashtable import LinearHashTable
from utils import new_array, w


class XFastTrie(BinaryTrie):
    class Node(BinaryTrie.Node):
        def __init__(self):
            super(XFastTrie.Node, self).__init__()
            self.prefix = 0
            
        def __eq__(self, other):
            return self.prefix == other.prefix
        
        def __hash__(self):
            return hash(self.prefix)
        
    def _new_node(self):
        return XFastTrie.Node()
        
    def __init__(self):
        super(XFastTrie, self).__init__()
        self.nil = self._new_node()
        self.t = new_array(w+1)
        for i in range(w+1):
            self.t[i] = LinearHashTable()
        self.t[0].add(self.r)
    
    def add(self, x):
        if super(XFastTrie, self).add(x):
            ix = int(x)
            u = self.r.child[(ix >> w-1) & 1]
            for i in range(1, w+1):
                u.prefix = ix >> w-i
                self.t[i].add(u)
                if i < w:
                    c = (ix >> w-i-1) & 1
                u = u.child[c]
            return True
        return False
    
    def find(self, x):
        ix = int(x)
        l, h = 0, w+1
        u = self.r
        q = self._new_node()
        while h-l > 1:
            i = (l+h)/2
            q.prefix = ix >> w-i
            v = self.t[i].find(q)
            if v is None:
                h = i
            else:
                u = v
                l = i
        if l == w: return u.x
        c = ix >> (w-l-1) & 1
        pred = [u.jump.prev, u.jump][c]
        if pred.next is None: return None
        return pred.next.x

    # TODO: Too much duplication with find(x)
    def find_node(self, x):
        ix = int(x)
        l, h = 0, w+1
        u = self.r
        q = self._new_node()
        while h-l > 1:
            i = (l+h)/2
            q.prefix = ix >> w-i
            v = self.t[i].find(q)
            if v is None:
                h = i
            else:
                u = v
                l = i
        if l == w: return u
        c = ix >> (w-l-1) & 1
        pred = [u.jump.prev, u.jump][c]
        if pred.next is None: return None
        return pred.next
    
    def remove(self, x):
        # 1 - fine leaf, u, containing x
        ix = int(x)
        i = 0
        u = self.r
        while i < w:
            c = ix >> (w-i-1) & 1
            u = u.child[c]
            if u is None: return False
            i += 1
        
        # 2 - remove u from linked list
        pred = u.prev
        succ = u.next
        pred.next = succ
        succ.prev = pred
        u.next = u.prev = None
        v = u
        
        # 3 - delete nodes on path to u
        while v is not self.r and v.left is None and v.right is None:
            if v is v.parent.left:
                v.parent.left = None
            else:
                v.parent.right = None
            self.t[i].remove(v)
            i -= 1
            v = v.parent
        
        # 4 - update jump pointers
        v.jump = [pred, succ][v.left is None]
        v = v.parent
        while v is not None:
            if v.jump is u:
                v.jump = [pred, succ][v.left is None]
            v = v.parent
        self.n -= 1
        return True
            
                
        
      