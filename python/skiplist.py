
class Node(object):
    """A node in a skip list"""
    def __init__(self, x, h):
        self.x = x
        self.next = new_array(h+1)

    def height(self):
        return len(self.next) - 1
        
class SkiplistSSet(object):
    def __init__(self):
        self.h = 0
        self.n = 0
        self.sentinel = Node(32)
        self.stack = new_array(sentinel.height()+1)
    
    def find_pred_node(x):
        u = self.sentinel
        r = h
        while r >= 0:
            while u.next[r] != None and u.next[r] < x:
                u = u.next[r]    # go right in line r
            r -= 1  # go down into list r-1
        return u

    def find(self, x):
        u = find_pred_node(x)
        if u.next[0] == None: return None
        return u.next[0].x
        
    def add(self, x):
        pass
        
    def remove(self, x):
        removed = False
        u = sentinel
        r = h
        while r >= 0:
            while u.next[r] != null and u.next[r] <= x:
                u = u.next[r]
            if u.next[r] != null and u.next[r] == x:
                removed = True
                u.next[r] = u.next[r].next[r]
                if u == sentinel and u.next[r] == null:
                    h -= 1 # height has decreased
                r -= 1
        if removed: n -= 1
        return removed    
        
    
