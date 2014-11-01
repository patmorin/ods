"""Take a random walk on the word graph.

This takes a random walk on the graph whose vertices are words and for which
an edge exists between two words if their edit distance is 1.
"""
from __future__ import division
import random

def adjacent(w1, w2):
    """Test if w1 and w2 have edit distance exactly one"""

    # Equal length words means hamming distance must be 1
    if len(w1) == len(w2):
        h = sum ([ w1[i] != w2[i] for i in range(len(w1)) ])
        if h == 1: return True

    # Ensure w1 is the longer word.
    if len(w2) > len(w1):
        (w1, w2) = (w2, w1)

    # Length can differ by at most one.
    if len(w1) - len(w2) > 1:
        return False

    # Try all possible deletions.
    for i in range(len(w2)):
        if w1[:i] + w1[i+1:] == w2: 
            return True
    
    # Nothing works.
    return False
    
def difference(w1, w2):
    """Return the operation that converts w1 into w2

    This returns a string representing the operation add, remove, set,
    that converts w1 into w2. The strings w1 and w2 must have edit
    distance exactly 1.
    """
    if len(w1) == len(w2):
        diffs = [i for i in range(len(w1)) if w1[i] != w2[i]]
        i = diffs[0]
        return "set({},'{}')".format(i, w2[i])
    if len(w1) < len(w2):
        diffs = [i for i in range(len(w1)) if w1[i] != w2[i]]
        i = diffs[0]
        return "add({},'{}')".format(i, w2[i])
    diffs = [i for i in range(len(w2)) if w1[i] != w2[i]]
    i = diffs[0]
    return "remove({})".format(i)
        

    
if __name__ == "__main__":
    """Take a random walk on the word graph"""
    words = open('/usr/share/dict/words').read().splitlines()
    words = [w for w in words if w.lower() == w]
    words = [w for w in words if "'" not in  w]
    seen = set()
    next = 'a'
    while next:
        print next
        seen.add(next)
        w1 = next
        next = ''
        i = 0
        for w2 in words:
            if w2 not in seen and adjacent(w1, w2):
                i += 1
                if random.random() < 1/i:
                    next = w2
        if next:
            print difference(w1, next)

