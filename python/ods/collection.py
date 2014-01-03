
class Collection(object):
    def add_all(self, iterable):
        for x in iterable:
            self.add(x)
            
    def __len__(self):
        return self.size()   
