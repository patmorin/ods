
from utils import new_array
from base import BaseList
from arraydeque import ArrayDeque

class SEList(BaseList):
    class BDeque(ArrayDeque):
        """A bounded-size deque"""
        def __init__(self, b):
            super(SEList.BDeque, self).__init__()
            self.a = new_array(b+1)
        
        def _resize(self):
            pass
        
    class Node(object):
        def __init__(self, b):
            self.d = SEList.BDeque(b)
            self.prev = None
            self.next = None
            
    def __init__(self, b):
        super(SEList, self).__init__()
        self.b = b
        self._initialize()
        
    def _new_node(self):
        return SEList.Node(self.b)
    
    def _initialize(self):
        self.dummy = self._new_node()
        self.dummy.next = self.dummy.prev = self.dummy
        self.n = 0
        
    def _add_before(self, w):
        """Create a new node and add it before w"""
        u = self._new_node()
        u.prev = w.prev
        u.next = w
        u.next.prev = u
        u.prev.next = u
        return u
    
    def _remove_node(self, w):
        w.prev.next = w.next
        w.next.prev = w.prev
        
    def _get_location(self, i):
        if i < self.n//2:
            u = self.dummy.next
            while i >= u.d.size():
                i -= u.d.size()
                u = u.next
            return u,i
        else:
            u = self.dummy
            idx = self.n
            while i < idx:
                u = u.prev
                idx -= u.d.size()
        return u, i-idx
    
    def get(self, i):
        u, j = self._get_location(i)
        return u.d.get(j)

    def set(self, i, x):
        u, j = self._get_location(i)
        return u.d.set(j, x)

    def _spread(self, u):
        w = u
        for j in range(self.b):
            w = w.next
        w = self._add_before(w)
        while w is not u:
            while w.d.size() < self.b:
                w.d.add_first(w.prev.d.remove_last())
            w = w.prev
            
    def _gather(self, u):
        w = u
        for j in range(self.b-1):
            while w.d.size() < self.b:
                w.d.add_last(w.next.d.remove_first())
            w = w.next
        self._remove_node(w)
        
        
    def add(self, i, x):
        if i < 0 or i > self.n: raise IndexError()
        if i == self.n:
            self.append(x)
            return
        u, j = self._get_location(i)
        r = 0
        w = u
        while r < self.b and w is not self.dummy and w.d.size() == self.b+1:
            w = w.next
            r += 1
        if r == self.b:  # b blocks, each with b+1 elements
            self._spread(u)
            w = u
        if w == self.dummy: # ran off the end - add new node
            w = self._add_before(w)
        while w is not u: # work backwards, shifting elements as we go
             w.d.add_first(w.prev.d.remove_last())
             w = w.prev
        w.d.add(j, x)
        self.n += 1

    def append(self, x):
        last = self.dummy.prev
        if last is self.dummy or last.d.size() == self.b+1:
            last = self._add_before(self.dummy)
        last.d.append(x)
        self.n += 1
        
    def remove(self, i):
        if i < 0 or i > self.n-1: raise IndexError()
        u, j = self._get_location(i)
        y = u.d.get(j)
        w = u
        r = 0
        while r < self.b and w is not self.dummy and w.d.size() == self.b-1:
            w = w.next
            r += 1
        if r == self.b: # b blocks, each with b-1 elements
            self._gather(u)
        u.d.remove(j)
        while u.d.size() < self.b-1 and u.next is not self.dummy:
            u.d.add_last(u.next.d.remove_first())
            u = u.next
        if u.d.size() == 0: self._remove_node(u)
        self.n -= 1

    def clear(self):
        self._initialize()
        
    def __iter__(self):
        u = self.dummy.next
        while u is not self.dummy:
            for x in u.d:
                yield x
            u = u.next

if __name__ == "__main__":
    ell = SEList(6)
    print ell
    ell.append(20)
    ell.append(21)
    print ell  
