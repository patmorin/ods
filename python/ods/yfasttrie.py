"""An implementation of Willard's Y-Fast tries

This structure is able to store w-bit integers with O(log w) time searches,
additions, and removals.

D. E. Willard. Log-logarithmic worst-case range queries are possible in 
  space Theta(n). Information Processing Letters, 17, 81-84. 1984.
"""
import random

from base import BaseSet
from treap import Treap
from xfasttrie import XFastTrie
from utils import w

class STreap(Treap):
    """A Treap that implements the split/absorb functionality"""
    
    def split(self, x):
        """Remove all values <= x and return a STreap containing these values"""
        u = self._find_last(x)
        s = self._new_node(None)
        if u.right is self.nil:
            u.right = s
        else:
            u = u.right
            while u.left is not self.nil:
                u = u.left
            u.left = s
        s.parent = u
        s.p = -1
        self.bubble_up(s)
        self.r = s.right
        if self.r is not self.nil: self.r.parent = self.nil
        ret = STreap()
        ret.r = s.left
        if ret.r is not ret.nil: ret.r.parent = ret.nil
        return ret
    
    def absorb(self, t):
        """Absorb the treap t (which must only contain smaller values)"""
        s = self._new_node(None)
        s.right = self.r
        if self.r is not self.nil: self.r.parent = s
        s.left = t.r
        if t.r is not t.nil: t.r.parent = s
        self.r = s
        t.r = t.nil
        self.trickle_down(s)
        self.splice(s)
    
    def size(self):
        """Raise an error because our implementation is only half-assed"""
        raise AttributeError(self.__class__.__name__ \
                             + "does not correctly maintain size()")

class Pair(tuple):
    def __new__(cls, x, y=None):
        return super(Pair, cls).__new__(cls, (x, y))
    
    @property
    def t(self):
        return self[1]
    
    @property
    def x(self):
        return self[0]
    
    def __int__(self):
        return int(self[0])
    
class YFastTrie(BaseSet):
    def __init__(self):
        super(YFastTrie, self).__init__()
        self._initialize()
        
    def _initialize(self):
        self.xft = XFastTrie()
        self.xft.add(Pair((1<<w)-1, STreap()))
        self.n = 0
    
    def add(self, x):
        ix = int(x)
        t = self.xft.find(Pair(ix))[1]
        if t.add(x):
            self.n += 1
            if random.randrange(w) == 0:
                t1 = t.split(x)
                self.xft.add(Pair(ix, t1))
            return True
        return False
    
    def find(self, x):
        return self.xft.find(Pair(int(x)))[1].find(x)
    
    def remove(self, x):
        ix = int(x)
        u = self.xft.find_node(ix)
        ret = u.x[1].remove(x)
        if ret: self.n -= 1
        if u.x[0] == ix and ix != (1<<w) - 1:
            t2 = u.next.x[1]
            t2.absorb(u.x[1])
            self.xft.remove(u.x)
        return ret
        
    def __iter__(self):
        # self.xft is a bunch of pairs
        for p in self.xft:
            # the one'th element of each pair is an STreap
            for x in p[1]:
                yield x
    
    def clear(self):
        self._initialize()
        
    