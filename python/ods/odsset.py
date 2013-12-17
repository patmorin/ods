


class ODSSet(object):
	def __in__(self, x):
		return self.find(x) != None
	
	def __len__(self):
		return self.size()
	
	def add_all(self, iterable):
		for x in iterable:
			self.add(x)