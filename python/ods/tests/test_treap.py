
from nose.tools import *
from ods.treap import Treap
from ssettest import sset_test

def test_treap():
    sset_test(Treap())
