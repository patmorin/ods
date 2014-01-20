
import random
import ods

def exercise_graph(Clz=ods.AdjacencyMatrix):
    n = 50
    g = Clz(n)
    s = set()

    # add a bunch of random edges
    for i in range(5*n):
        i, j = random.randrange(n), random.randrange(n)
        if (i,j) not in s:
            s.add((i,j))
            g.add_edge(i,j)
        
    for i in range(n):
        for j in range(n):
            assert(g.has_edge(i,j) == ((i,j) in s))

    # remove some of these edges
    rem = random.sample(s, n)
    for e in rem:
        s.remove(e)
        g.remove_edge(e[0], e[1])

    for i in range(n):
        for j in range(n):
            assert(g.has_edge(i,j) == ((i,j) in s))

    # check that in and out degrees are correctly computed    
    for i in range(n):
        od = len([e for e in s if e[0] == i])
        id = len([e for e in s if e[1] == i])
        assert(g.out_degree(i) == od)
        assert(g.in_degree(i) == id)
        
    