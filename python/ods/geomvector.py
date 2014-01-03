"""This code doesn't even compile"""

class GeomVector(object):
    def hash_code(self):
        p = (1<<32)-5    # this is a prime number
        z = 0x64b6055a  # 32 bits from random.org
        z2 = 0x5067d19d  # random odd 32 bit number
        s = 0
        zi = 1
        for i in range(len(x)):
            # reduce to 31 bits
            \ensuremath{xi = ((x[i].hash_code() * z2)%(1<<32)) >> 1} 
            s = (s + zi * xi) % p
            zi = (zi * z) % p      
        \ensuremath{s = (s + zi * (p-1)) % p}
        return s%(1<<32)
