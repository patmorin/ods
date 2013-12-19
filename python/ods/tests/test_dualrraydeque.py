import random

from nose.tools import *
from ods.dualarraydeque import DualArrayDeque
from listtest import list_test

def test_as():
    list_test(DualArrayDeque())
    
