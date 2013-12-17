import numpy

def new_array(n, dtype=numpy.object):
    a = numpy.empty(n, dtype)
    a.fill(None)   # is this necessary?
    return a


