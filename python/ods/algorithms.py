"""Implementations of some sorting and graph algorithms"""
import random
from utils import new_zero_array, new_array, _new_array, w
from binaryheap import BinaryHeap

def average(a):
    s = 0
    for i in range(len(a)):
        s += a[i]
    return s/len(a)

def left_shift_a(a):
    for i in range(len(a)-1):
        a[i] = a[i+1]
    a[len(a)-1] = None

def left_shift_b(a):
    a[0:len(a)-1] = a[1:len(a)]
    a[len(a)-1] = None

def zero(a):
    a[0:len(a)] = 0

def merge_sort(a):
    if len(a) <= 1:
        return a
    m = len(a)//2
    a0 = merge_sort(a[:m])
    a1 = merge_sort(a[m:])
    merge(a0, a1, a)
    return a

def merge(a0, a1, a):
    i0 = i1 = 0
    for i in range(len(a)):
        if i0 == len(a0):
            a[i] = a1[i1]
            i1 += 1
        elif i1 == len(a1):
            a[i] = a0[i0]
            i0 += 1
        elif a0[i0] <= a1[i1]:
            a[i] = a0[i0]
            i0 += 1
        else:
            a[i] = a1[i1]
            i1 += 1

_random_int = random.randrange

def quick_sort(a):
    _quick_sort(a, 0, len(a))

def _quick_sort(a, i, n):
    if n <= 1: return
    x = a[i + _random_int(n)]
    (p, j, q) = (i-1, i, i+n)
    while j < q:
        if a[j] < x:
            p += 1
            a[j], a[p] = a[p], a[j]
            j += 1
        elif a[j] > x:
            q -= 1
            a[j], a[q] = a[q], a[j]
        else:
           j += 1
    _quick_sort(a, i, p-i+1)
    _quick_sort(a, q, n-(q-i))

def heap_sort(a):
    h = BinaryHeap()
    h.a = a
    h.n = len(a)
    m = h.n//2
    for i in range(m-1, -1, -1):
        h.trickle_down(i)
    while h.n > 1:
        h.n -= 1
        h.a[h.n], h.a[0] = h.a[0], h.a[h.n]
        h.trickle_down(0)
    a.reverse()
   
def counting_sort(a, k):
    c = new_zero_array(k)
    for i in range(len(a)):
        c[a[i]] += 1 
    for i in range(1, k):
        c[i] += c[i-1]  
    b = new_array(len(a))
    for i in range(len(a)-1, -1, -1):
        c[a[i]] -= 1        
        b[c[a[i]]] = a[i]   
    return b
    
d = 8

def radix_sort(a):
    for p in range(w//d):
        c = new_zero_array(1<<d)
        b = new_array(len(a))
        for i in range(len(a)):
            bits = (a[i] >> d*p)&((1<<d)-1) 
            c[bits] += 1 
        for i in range(1, 1<<d):
            c[i] += c[i-1]
        for i in range(len(a)-1, -1, -1):
            bits = (a[i] >> d*p)&((1<<d)-1)
            c[bits] -=1
            b[c[bits]] = a[i] 
        a = b
    return b

def bfs(g, r):
    seen = new_boolean_array(n)
    q = SLList()
    q.add(r)
    seen[r] = True
    while q.size() > 0:
        i = q.remove()
        for j in g.out_edges(i):
            if seen[j] is False:
                q.add(j)
                seen[j] = True

white, grey, black = 0, 1, 2

def dfs(g, r):
    c = new_array(g.n)
    _dfs(g, r, c)
    
def _dfs(g, i, c):
    c[i] = grey
    for j in g.out_edges(i):
        if c[j] == white:
            c[j] = grey
            dfs(g, j, c)
    c[i] = black
    
def dfs2(g, r):
    c = new_array(g.n)
    s = SLList()
    s.push(r)
    while s.size() > 0:
        i = s.pop()
        if c[i] == white:
            c[i] = grey
            for j in g.out_edges(i):
                s.push(j)
    

    
