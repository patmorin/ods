import numpy

w = 32

def new_array(n, dtype=numpy.object):
    return numpy.empty(n, dtype)

def _new_array(n):
    return [None]*n
    
def new_zero_array(n):
    return numpy.zeros(n)
    
def new_boolean_matrix(n, m):
    return numpy.zeros([n, m], numpy.bool_)
    
def new_boolean_array(n):
    return numpy.zeros(n, numpy.bool_)

def new_int_array(n, init=0):
    a = numpy.empty(n, numpy.int32)
    a.fill(init)
    return a

def binfmt(n):
    return "{0:012b}".format(n)
