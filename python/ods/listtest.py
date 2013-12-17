from controllist import ControlList
from dllist import DLList
from arraystack import ArrayStack
from arraydeque import ArrayDeque
from sllist import SLList
from dllist import DLList
from skiplistlist import SkiplistList


def list_test(l, n):
    # append 
    for i in range(n):
        l.add(len(l), i)
    assert len(l) == n
    for i in range(len(l)):
        assert l.get(i) == i

    # prepend
    for i in range(n):
        l.add(0, -i-1)
    assert len(l) == 2*n
    for i in range(len(l)):
        assert l.get(i) == -n+i
        
    # add in middle
    for i in range(n):
        l.add(n, i+2*n)
    assert len(l) == 3*n
    for i in range(n):
        assert l.get(i) == -n+i
    for i in range(n, 2*n):
        assert l.get(i) == 4*n - i - 1
    for i in range(2*n, n):
        assert l.get(i) == i - 2*n

    # remove from middle
    for i in range(n):
        l.remove(len(l)//2)
    assert len(l) == 2*n
    for i in range(len(l)):
        assert l.get(i) == -n+i
    
    # remove from end
    for i in range(n):
        l.remove(len(l)-1)
    assert len(l) == n
    for i in range(len(l)):
        assert l.get(i) == -n+i

    # remove from beginning
    for i in range(n):
        l.remove(0)
    assert len(l) == 0


# This code is for testing list implementations
import random

def list_cmp(l1, l2):
    assert(len(l1) == len(l2))
    for i in range(len(l1)):
        assert(l1.get(i) == l2.get(i))
    
def list_cmp_test(l1, l2, n):
    for _ in range(n):
        x = random.random();
        i = random.randrange(0, len(l1)+1)
        l1.add(i, x)
        l2.add(i, x)
        list_cmp(l1, l2)
    for _ in range(5*n):
        op = random.randrange(0,3)
        if (op == 0):
            i = random.randrange(0, len(l1)+1)
            x = random.random();
            l1.add(i, x)
            l2.add(i, x)
        elif op == 1:
            i = random.randrange(0, len(l1))
            x = random.random();
            l1.set(i,x)
            l2.set(i,x)
        else:
            i = random.randrange(0, len(l1))
            l1.remove(i)
            l2.remove(i)
        list_cmp(l1, l2)
    


n = 100


print "Performing basic list tests on ControlList...",
list_test(ControlList(), n)
print "done"

print "Performing basic list tests on DLList...",
list_test(DLList(), n)
print "done"

print "Performing basic list tests on ArrayStack...",
list_test(ArrayStack(), n)
print "done"

print "Performing basic list tests on ArrayDeque...",
list_test(ArrayDeque(), n)
print "done"

n = 100

print "Performing comparative list tests DLList versus ControlList...",
list_cmp_test(DLList(), ControlList(), 100)
print "done"

print "Performing comparative list tests ArrayStack versus ControlList...",
list_cmp_test(ArrayStack(), ControlList(), 100)
print "done"

print "Performing comparative list tests ArrayDeque versus ControlList...",
list_cmp_test(ArrayDeque(), ControlList(), 100)
print "done"

print "Performing comparative list tests SLList versus ControlList...",
list_cmp_test(SLList(), ControlList(), 100)
print "done"

print "Performing comparative list tests DLList versus ControlList...",
list_cmp_test(DLList(), ControlList(), 100)
print "done"

print "Performing comparative list tests SkiplistList versus ControlList...",
list_cmp_test(SkiplistList(), ControlList(), 100)
print "done"



