
from nose.tools import *
from ods.skiplistsset import SkiplistSSet
from ssettest import sset_test

def test_treap():
    sset_test(t=SkiplistSSet())
