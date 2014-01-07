

from nose.tools import *
from ods.scapegoattree import ScapegoatTree
from ssettest import sset_test

def test_bst():
    sset_test(ScapegoatTree())
