import random

from nose.tools import *
from ods.arraydeque import ArrayDeque
from listtest import list_test

def test_as():
    list_test(ArrayDeque())
    
