from ods.arrayqueue import ArrayQueue

def test_aq():
    m = 10000
    n = 500
    q = ArrayQueue()
    for i in range(m):
        q.add(i)
        if q.size() > n: 
            x = q.remove()
            assert x == i - n 

