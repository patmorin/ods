"""
An abstract superclass for all array-based lists

Subclasses of this must implement get(i), set(i,x), add(i,x), remove(i)
and size()
"""

class ArrayBasedList(object):
    def __str__(self):
        return str([self.get(i) for i in range(self.size())])

    def __len__(self):
        return self.size()

    def append(self, x):
        self.add(self.size(), x)

    def clear(self):
        while self.size() > 0:
            self.remove(self.size()-1)

    def new_array(self, n):
        return [None]*n

    def test(self):
        a = list()
        self.clear()
        for i in range(20):
            a.append(i)
            self.append(i)

        print "list = " + str(a)
        print "self = " + str(self)
