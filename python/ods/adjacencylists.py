
from arraylist import ArrayList

length = len

class AdjacencyLists(object):
    def __init__(self, n):
        self.n = n
        self._initialize()
        
    def _initialize(self)
        self.adj = new_array(self.n)
        for i in range(n):
            self.adj[i] = ArrayList()
            
    def add_edge(self, i, j):
        adj[i].add(j)
        
    def remove_edge(i, j):
        for k in length(adj[i]):
            if self.adj[i].get(k) == j:
                self.adj[i].remove(k)
                return
                
    def has_edge(self, i, j):
        for k in self.adj[i]:
            if k == j:
                return True
        return False
        
    def out_edges(self, i):
        return self.adj[i]
        
    def in_edges(self, i):
        out = ArrayList()
        for j in range(self.n):
            if self.has_edge(j, i): out.add(j)
        return out
        
            
