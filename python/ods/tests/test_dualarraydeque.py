import random

from nose.tools import *
from ods.dualarraydeque import DualArrayDeque
from listtest import list_test

def test_dad():
    list_test(DualArrayDeque())
    
