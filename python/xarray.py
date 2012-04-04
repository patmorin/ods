'''
Created on 2012-04-03

@author: morin
'''

class xarray(list):
    def __init__(self, m):
        self.a = [None]*m
        self.length = m
        