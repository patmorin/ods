"""An implementation of B-Trees"""

from utils import new_array, new_int_array
from base import BaseSet

class BlockStore(object):
    """This class simulates a block storage device like a file"""
    def __init__(self):
        self.blocks = list()
        self.free = list()
        
    def place_block(self, block):
        if len(self.free) > 0:
            i = self.free.pop()
            self.blocks[i] = block
        else:
            i = len(self.blocks)
            self.blocks.append(block)
        return i
    
    def free_block(self, i):
        self.blocks[i] = None
        self.free.append(i)
        
    def read_block(self, i):
        return self.blocks[i]
    
    def write_block(self, i, block):
        self.blocks[i] = block


def find_it(a, x):
    lo, hi = 0, len(a)
    while hi != lo:
        m = (hi+lo)//2
        if a[m] is None or x < a[m]:
            hi = m # look in first half
        elif x > a[m]:
            lo = m+1 # look in second half
        else:
            return -m-1 # found it
    return lo

        
class DuplicateValueError(LookupError):
    pass


class BTree(BaseSet):
    class Node(object):
        def __init__(self, btree):
            self.btree = btree
            self.keys = new_array(self.btree.b)
            self.children = new_int_array(self.btree.b+1, -1)
            self.id = self.btree.bs.place_block(self)
            
        def is_leaf(self):
            return self.children[0] < 0
        
        def is_full(self):
            return self.keys[-1] is not None
        
        def size(self):
            lo, hi = 0, len(self.keys)
            while hi != lo:
                m = (hi+lo) // 2
                if self.keys[m] is None:
                    hi = m
                else:
                    lo = m+1
            return lo
        
        def add(self, x, ci):
            i = find_it(self.keys, x)
            if i < 0: return False
            self.keys[i+1:len(self.keys)] = self.keys[i:len(self.keys)-1]
            self.children[i+2:len(self.children)] \
                = self.children[i+1:len(self.children)-1]
            self.children[i+1] = ci
            self.keys[i] = x
            
        def remove(self, i):
            y = self.keys[i]
            assert(y is not None)
            self.keys[i:len(self.keys)-1] = self.keys[i+1:len(self.keys)]
            self.keys[len(self.keys)-1] = None
            return y
            
        def split(self):
            w = BTree.Node(self.btree)
            j = len(self.keys) // 2
            w.keys[0:len(self.keys)-j] = self.keys[j:len(self.keys)]
            self.keys[j:len(self.keys)] = None
            w.children[0:len(self.children)-j-1] \
                = self.children[j+1:len(self.children)]
            self.children[j+1:len(self.children)] = -1
            self.btree.bs.write_block(self.id, self)
            return w
            
        def __str__(self):
            pairs = zip(self.children, self.keys)
            return "".join(["%s<%s>" % x for x in pairs]) \
                + "," + str(self.children[len(self.keys)]) 
                 
        
    def __init__(self, b):
        self._initialize(b)
        
    def _new_node(self):
        return BTree.Node(self)
        
    def _initialize(self, b):
        self.b = b | 1
        self.B = self.b // 2
        self.bs = BlockStore()
        self.ri = self.new_node().id
        self.n = 0
        
    def add(self, x):
        w = None
        try:
            w = self.add_recursive(x, self.ri)
        except DuplicateValueError:
            return False
        if w is not None:
            newroot = BTree.Node(self)
            x = w.remove(0)
            self.bs.write_block(w.id, w)
            newroot.children[0] = self.ri
            newroot.keys[0] = x
            newroot.children[1] = w.id
            self.ri = newroot.id
            self.bs.write_block(self.ri, newroot)
        self.n += 1
        return True
        
    def add_recursive(self, x, ui):
        u = self.bs.read_block(ui)
        i = find_it(u.keys, x)
        if i < 0: raise DuplicateValueError()
        if u.children[i] < 0:
            u.add(x, -1)
            self.bs.write_block(u.id, u)
        else:
            w = self.add_recursive(x, u.children[i])
            if w is not None:
                x = w.remove(0)
                self.bs.write_block(w.id, w)
                u.add(x, w.id)
                self.bs.write_block(u.id, u)
        if u.is_full(): return u.split()
        return None
    
    def find(self, x):
        z = None
        ui = self.ri
        while ui >= 0:
            u = self.bs.read_block(ui)
            i = find_it(u.keys, x)
            if i < 0:
                return u.keys[-(i+1)] # found it
            if u.keys[i] is not None:
                z = u.keys[i]
            ui = u.children[i]
        return z
    
    def remove(self, x):
        if self.remove_recursive(x, self.ri):
            self.n -= 1
            r = self.bs.read_block(self.ri)
            if r.size() == 0 and self.n > 0:
                ri = r.children[0]  # root has only one child
            return True
        return False
    
    def remove_recursive(self, x, ui):
        if ui < 0: return False  # didn't find it
        u = self.bs.read_block(ui)
        i = find_it(u.keys, x)
        if i < 0:  # found it
            i = -(i+1)
            if u.is_leaf():
                u.remove(i)
            else:
                u.keys[i] = self.remove_smallest(u.children[i+1])
                self.check_underflow(u, i+1)
            return True
        elif self.remove_recursive(x, u.children[i]):
            self.check_underflow(u, i)
            return True
        return False
        
    def remove_smallest(self, ui):
        u = self.bs.read_block(ui)
        if u.is_leaf():
            return u.remove(0)
        y = self.remove_smallest(u.children[0])
        self.check_underflow(u, 0)
        return y
    
    def check_underflow(self, u, i):
        if u.children[i] < 0: return
        if i == 0:
            self.check_underflow_zero(u, i)
        else:
            self.check_underflow_nonzero(u, i)
            
    def merge(self, u, i, v, w):
        assert(v.id == u.children[i])
        assert(w.id == u.children[i+1])
        sv = v.size()
        sw = w.size()
        # copy keys from w to v
        v.keys[sv+1:sv+1+sw] = w.keys[0:sw]
        v.children[sv+1:sv+sw+2] = w.children[0:sw+1]
        # add key to v and remove it from u
        v.keys[sv] = u.keys[i]
        u.keys[i:self.b-1] = u.keys[i+1:self.b]
        u.keys[self.b-1] = None
        u.children[i+1:self.b] = u.children[i+2:self.b+1]
        u.children[self.b] = -1
        
    def check_underflow_nonzero(self, u, i):
        w = self.bs.read_block(u.children[i])
        if w.size() < self.B-1:
            v = self.bs.read_block(u.children[i-1])
            if v.size() > self.B:  # w can borrow from v
                self.shift_lr(u, i-1, v, w)
            else: # v will absorb w
                self.merge(u, i-1, v, w)
    
    def shift_lr(self, u, i, v, w):
        sw = w.size()
        sv = v.size()
        shift = (sw+sv)//2 - sw  # num. keys to shift from v to w
        
        # make space for new keys in w
        w.keys[shift:shift+sw] = w.keys[0:sw]
        w.children[shift:shift+sw+1] = w.children[0:sw+1]
        
        # move keys out of v and into w (and u)
        w.keys[shift-1] = u.keys[i]
        u.keys[i] = v.keys[sv-shift]
        w.keys[0:shift-1] = v.keys[sv-shift+1:sv]
        v.keys[sv-shift:sv] = None
        w.children[0:shift] = v.children[sv-shift+1:sv+1]
        v.children[sv-shift+1:sv+1] = -1

    def check_underflow_zero(self, u, i):
        w = self.bs.read_block(u.children[i])
        if w.size() < self.B-1: # underflow at w
            v = self.bs.read_block(u.children[i+1]) # v right of w
            if v.size() > self.B:
                self.shift_rl(u, i, v, w)
            else:
                self.merge(u, i, w, v)
                u.children[i] = w.id
            
    def shift_rl(self, u, i, v, w):
        sw = w.size()
        sv = v.size()
        shift = (sw+sv)//2 - sw # num. keys to shift from v to w
        
        # shift keys from v to w
        w.keys[sw] = u.keys[i]
        w.keys[sw+1:sw+shift] = v.keys[0:shift-1]
        w.children[sw+1:sw+shift+1] = v.children[0:shift]
        u.keys[i] = v.keys[shift-1]
        
        # delete keys and children from v
        v.keys[0:self.b-shift] = v.keys[shift:self.b]
        v.keys[sv-shift:self.b] = None
        v.children[0:self.b-shift+1] = v.children[shift:self.b+1]
        v.children[sv-shift+1:self.b+1] = -1
        
    def __iter__(self):
        return self._traverse(self.ri)
                
    def _traverse(self, ui):
        """Helper function for the iterator"""
        if ui >= 0:
            u = self.bs.read_block(ui)
            i = 0
            while i < len(u.keys) and u.keys[i] is not None:
                for x in self._traverse(u.children[i]): yield x
                yield u.keys[i]
                i += 1
            for x in self._traverse(u.children[i]): yield x

    def _internal_repr(self):
        self._ir(self.ri)
        print

    def _ir(self, ui):    
        if ui >= 0:
            u = self.bs.read_block(ui)
            i = 0
            print '[',
            while i < len(u.keys) and u.keys[i] is not None:
                self._ir(u.children[i])
                print u.keys[i],
                i += 1
            self._ir(u.children[i])
            print ']',
        
if __name__ == "__main__":
    import random
    
    random.seed(0)
    b = 11
    bt = BTree(b)
    
    # testing a node
    n = BTree.Node(bt)
    print n
    for i in range(b):
        n.add(random.randrange(10*b), i)
        print n
    w = n.split()
    print "n = ", n
    print "w = ", w
    w.remove(0)
    print "n = ", n
    print "w = ", w
   
    n = 100
    for i in range(n):
        bt.add(random.randrange(100*n))
        print bt
        
