import random

from nose.tools import *

import ods
from listtest import list_test

def test_as():
    list_test(ods.SkiplistList())
    
