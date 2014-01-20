import random

from nose.tools import *

import ods
from listtest import exercise_list

def test_dad():
    exercise_list(ods.DualArrayDeque())
    
