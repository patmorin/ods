'''
Created on 2012-04-02

@author: morin
'''
class DLList(object):
	
	class Node(object):
		def __init__(self, x):
			self.x = x
			self.next = None
			self.prev = None

	def __init__(self):
		self.n = 0
		self.dummy = DLList.Node(None)
		self.dummy.prev = self.dummy
		self.dummy.next = self.dummy

	def get_node(self, i):
		if i < self.n/2:
			p = self.dummy.next	
			for _ in range(0, i):
				p = p.next
		else:
			p = self.dummy
			for _ in range(self.n, i, -1):
				p = p.prev
		return p

	def get(self, i):
		if i < 0 or i >= self.n: raise IndexError()
		return self.get_node(i).x

	def set(self, i, x): #@ReservedAssignment
		if i < 0 or i >= self.n: raise IndexError()
		u = self.get_node(i)
		y = u.x
		u.x = x
		return y

	def _remove_node(self, w):
		w.prev.next = w.next
		w.next.prev = w.prev
		self.n -= 1	

	def remove(self, i):
		if i < 0 or i >= self.n: raise IndexError()
		self._remove_node(self.get_node(i))

	def add_before(self, w, x):
		u = DLList.Node(x)
		u.prev = w.prev
		u.next = w
		u.next.prev = u
		u.prev.next = u
		self.n += 1
		return u;

	def add(self, i, x):
		if i < 0 or i > self.n:	raise IndexError()
		self.add_before(self.get_node(i), x)

	def size(self):
		return self.n

	def __len__(self):
		return self.size()

	def __str__(self):
		s = "["
		u = self.dummy.next
		while u != self.dummy:
			s += "%r" % u.x
			u = u.next
			if u != self.dummy:
				s += ","
		return s + "]"

