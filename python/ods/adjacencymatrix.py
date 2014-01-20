"""An implementation of the adjacency matrix representation of a graph"""
from utils import new_boolean_matrix

class AdjacencyMatrix(object):
    def __init__(self, n):
        self.n = n
        self._initialize()
                
    def _initialize(self):
        self.a = new_boolean_matrix(self.n, self.n)    

    def add_edge(self, i, j):
        self.a[i][j] = True
        
    def remove_edge(self, i, j):
        self.a[i][j] = False
        
    def has_edge(self, i, j):
        return self.a[i][j]

    def out_edges(self, i):
        out = list()
        for j in range(self.n):
            if self.a[i][j]: out.append(j)
        return out
        
    def in_edges(self, i):
        out = list()
        for j in range(self.n):
            if self.a[j][i]: out.append(j)
        return out
        
    def in_degree(self, i):
        deg = 0
        for j in range(self.n):
            if self.a[j][i]:
                deg += 1
        return deg
        
    def out_degree(self, i):
        deg = 0
        for j in range(self.n):
            if self.a[i][j]:
                deg += 1
        return deg

