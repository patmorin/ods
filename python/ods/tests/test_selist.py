import random
from nose.tools import *

import ods
from listtest import exercise_list

def test_sel():
    exercise_list(ods.SEList(5))
    exercise_list(ods.SEList(10))
    exercise_list(ods.SEList(42))
    
