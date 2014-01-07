import random

from nose.tools import *
from ods.skiplistlist import SkiplistList
from listtest import list_test

def test_as():
    list_test(SkiplistList())
    
