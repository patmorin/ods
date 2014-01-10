import numpy

def new_array(n, dtype=numpy.object):
    return numpy.empty(n, dtype)

def _new_array(n):
    return [None]*n
    
def new_zero_array(n):
    return numpy.zeros(n)
    
def new_boolean_matrix(n, m):
    return numpy.zeros([n, n], bool)
    

