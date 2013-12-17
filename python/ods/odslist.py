"""
An abstract superclass for all lists

Subclasses of this must implement get(i), set(i,x), add(i,x), 
remove(i) and size().  The main point of subclassing from this
is that the resulting data structure will support most of the
operations in Python's list implementation.
"""
import random

class ODSList(object):
    def append(self, x):
        self.add(self.size(), x)

    def add_all(self, iterable):
        for x in iterable:
            self.append(x)
        
    def clear(self):
        while self.size() > 0:
            self.remove(self.size()-1)

    def insert(i, x):
        self.add(i, x)
        
    def __repr__(self):
        return self.__class__.__name__ + "(" \
                + repr([x for x in self]) \
                + ")"
        
    def __str__(self):
        return str([x for x in self])

    def __len__(self):
        return self.size()

    def __iter__(self):
        for i in range(len(self)):
            yield(self.get(i))

    def __eq__(self, a):
        if len(a) != len(self): return False
        for i in range(len(a)):
            if (a[i] != self.get(i)): return False
        return True

    def __ne__(self, a):
        return not self == a
    
    def index(self, x):
        for i in range(len(self)):
            if self.get(i) == x:
                return i
        raise ValueError('%r is not in the list' % x)
    
    def remove_value(self, x):
        try:
            return self.remove(self.index(x))
        except ValueError:
            return False

    def __getitem__(self, key):
        return self.get(key)

    def __setitem__(self, key, value):
        return self.set(key, value)

    def test(self):
        a = list()
        self.clear()

        for k in range(5):
            for i in range(20):
                a.append(i)
                self.append(i)

            print "list = " + str(a)
            print "self = " + str(self)

            for i in range(20, 1000):
                j = random.randrange(len(a)+1)
                a.insert(j, i)
                self.add(j, i)
                assert self == a;
                
            while len(a) > 0:
                j = random.randrange(len(a))
                del a[j] 
                self.remove(j)
                assert self == a;


