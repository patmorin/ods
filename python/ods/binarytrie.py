"""An implementation of a binary trie for storing w bit integers

This structure is able to store elements, x, where int(x) is an unsigned
w bit integer.
"""

from utils import new_array, w, binfmt
from base import BaseSet


class BinaryTrie(BaseSet):
    class Node(object):
        def __init__(self):
            self.child  = new_array(2)
            self.jump = None
            self.parent = None
            self.x = None

        @property
        def left(self):
            return self.child[0]

        @left.setter
        def left(self, u):
            self.child[0] = u

        @property
        def right(self):
            return self.child[1]

        @right.setter
        def right(self, u):
            self.child[1] = u

        @property
        def prev(self):
            return self.child[0]

        @prev.setter
        def prev(self, u):
            self.child[0] = u

        @property
        def next(self):
            return self.child[1]

        @next.setter
        def next(self, u):
            self.child[1] = u
            
        def __str__(self):
            return "{" + str(self.x) + "}"
        
    def _new_node(self):
        return BinaryTrie.Node()
    
    def __init__(self):
        super(BinaryTrie, self).__init__()
        self._initialize()
        
    def _initialize(self):
        self.dummy = self._new_node()
        self.dummy.prev = self.dummy.next = self.dummy
        self.r = self._new_node()
        self.r.jump = self.dummy
        self.n = 0
        
    def clear(self):
        self._initialize()
        
    def add(self, x):
        ix = int(x)
        u = self.r
        # 1 - search for ix until falling out of the tree
        i = 0
        while i < w:
            c = (ix >> w-i-1) & 1
            if u.child[c] is None: break
            u = u.child[c]
            i += 1
        if i == w: return False  # already contains x - abort
        pred = [u.jump.prev, u.jump][c]
        u.jump = None  # u will soon have two children
        
        # 2 - add the path to ix
        while i < w:
            c = (ix >> w-i-1) & 1
            u.child[c] = self._new_node()
            u.child[c].parent = u
            u = u.child[c]
            i += 1
        u.x = x
            
        # 3 - add u to the linked list
        u.prev = pred
        u.next = pred.next
        u.prev.next = u
        u.next.prev = u
        
        # 4 - walk back up, updating jump pointers
        v = u.parent
        while v is not None:
            if (v.left is None  
                    and (v.jump is None or int(v.jump.x) > ix)) \
                or (v.right is None
                    and (v.jump is None or int(v.jump.x) < ix)):
                v.jump = u
            v = v.parent
        self.n += 1
        return True
    
    def find(self, x):
        ix = int(x)
        u = self.r
        i = 0
        while i < w:
            c = (ix >> w-i-1) & 1
            if u.child[c] is None: break
            u = u.child[c]
            i += 1
        if i == w: return u.x  # found it
        u = [u.jump, u.jump.next][c]
        if u is self.dummy: return None
        return u.x
        
    def remove(self, x):
        ix = int(x)
        u = self.r
        # 1 - find leaf, u, that contains x
        i = 0
        while i < w:
            c = (ix >> w-i-1) & 1
            if u.child[c] is None: return False
            u = u.child[c]
            i += 1
        
        # 2 - remove u from linked list
        u.prev.next = u.next
        u.next.prev = u.prev
        v = u
        
        # 3 - delete nodes on path to u
        for i in range(w-1, -1, -1):
            c = (ix >> w-i-1) & 1
            v = v.parent
            v.child[c] = None
            if v.child[1-c] is not None: break
            
        # 4 - update jump pointers
        pred = u.prev
        succ = u.next
        v.jump = [pred, succ][v.left is None]
        v = v.parent
        while v is not None:
            if v.jump is u:
                v.jump = [pred, succ][v.left is None]
            v = v.parent
            
        self.n -= 1
        return True
    
    def _check(self):
        u = self.dummy.next
        i = 0
        while u is not self.dummy:
            assert(u.next.prev is u)
            u = u.next
            i += 1
        assert(i == self.n)
        self._check_it()  

    def _check_it(self, u=None, d=0, prefix=0):
        """Consistency check of the subtree rooted at u (with depth d)"""
        if u is None: u = self.r
        if d == w:
            assert(u.x == prefix)
        else:
            assert(u is self.r or u.left is not None or u.right is not None)
            if u.left is None and u.right is not None:
                # TODO: check u.jump.x against prefix
                val = prefix << w-d-1
                assert(u.jump.x is not None)
                assert(u.jump.x >> w-d == prefix)
                assert(u.jump.x >> w-d-1 == (prefix << 1)|1)
            if u.right is None and u.left is not None:
                assert(u.jump.x is not None)
                assert(u.jump.x >> w-d == prefix)
                assert(u.jump.x >> w-d-1 == (prefix << 1))
            if u.left is not None and u.right is not None:
                assert(u.jump is None)
            if u.left is not None:
                self._check_it(u.left, d+1, prefix << 1)
            if u.right is not None:
                self._check_it(u.right, d+1, (prefix << 1) | 1)
                
    def __iter__(self):
        u = self.dummy.next
        while u != self.dummy:
            yield u.x
            u = u.next
            

