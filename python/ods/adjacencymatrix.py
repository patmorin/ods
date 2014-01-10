

class AdjacencyMatrix(object):
    def __init__(self, n):
        self.initialize()
                
    def initialize(self, n):
        self.n = n
        self.a = new_boolean_matrix(n, n)    

    def add_edge(self, i, j):
        self.a[i][j] = True
        
    def remove_edge(self, i, j):
        self.a[i][j] = False
        
    def has_edge(self, i, j):
        return self.a[i][j]
        
    def out_edges(self, i):
        return [j for j in range(n) if a[i][j]]
        
    def in_edges(self, i):
        return [j for j in range(n) if a[j][i]]
        
    def in_degree(self, i):
        deg = 0
        for j in range(n):
            if a[j][i]:
                count += 1
                
    def out_degree(self, i):
        deg = 0
        for j in range(n):
            if a[i][j]:
                count += 1

