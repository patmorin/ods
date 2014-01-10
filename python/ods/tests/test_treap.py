
from nose.tools import *

import ods
from ssettest import sset_test

def test_treap():
    sset_test(t=ods.Treap())
