import random


def exercise_heap(h=None):
    if h is None: return
    a0 = []
    h
    n = 1000
    for _ in range(n):
        x = random.randrange(5*n)
        a0.append(x)
        h.add(x)
        assert(len(h) == len(a0))
    a1 = [h.remove() for _ in range(n)]
    assert(sorted(a0) == a1)
    assert(len(h) == 0)


