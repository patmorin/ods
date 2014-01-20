"""An implementation of the adjacency list representation of a graph"""
from arraystack import ArrayStack
from utils import new_array

length = len

class AdjacencyLists(object):
    def __init__(self, n):
        self.n = n
        self._initialize()
        
    def _initialize(self):
        self.adj = new_array(self.n)
        for i in range(self.n):
            self.adj[i] = ArrayStack()
            
    def add_edge(self, i, j):
        self.adj[i].append(j)
        
    def remove_edge(self, i, j):
        for k in range(length(self.adj[i])):
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

    def out_degree(self, i):
        return len(self.adj[i])
        
    def in_edges(self, i):
        out = ArrayStack()
        for j in range(self.n):
            if self.has_edge(j, i): out.append(j)
        return out
        
    def in_degree(self, i):
        deg = 0
        for j in range(self.n):
            if self.has_edge(j, i): deg += 1
        return deg
            
