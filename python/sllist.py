'''
Created on 2012-04-02

@author: morin
'''
class SLList(object):
    
    class Node(object):
        def __init__(self, x):
            self.x = x
            self.next = None

    def __init__(self):
        self.n = 0
        self.head = None
        self.tail = None

    def new_node(x):
        return SLList.Node(x)

    def size(self):
        return self.n

    def push(self,x):
        u = self.new_node(x)
        u.next = self.head
        self.head = u
        if self.n == 0:
            self.tail = u
        self.n += 1
        return x

    def add(self, x):
        u = self.new_node(x)
        if self.n == 0:
            self.head = u
        else:
            self.tail.next = u
        self.tail = u
        self.n += 1
        return True

    def pop(self):
        if self.n == 0: return None
        x = self.head.x
        self.head = self.head.next
        self.n -= 1
        if self.n == 0:
            self.tail = None
        return x

    def __str__(self):
        s = "["
        u = self.head
        while u != None:
            s += "%r" % u.x
            u = u.next
            if u != None:
                s += ","
        return s + "]"

    def __len__(self):
        return self.size()

#l = SLList()
#for x in range(10):
#    l.add(x)
#
#print(l)
#
#print "l.size() = %d" % l.size()
#print "len(l) = %d" % len(l)
#
#while l.size() > 0:
#    print l.pop()
#
