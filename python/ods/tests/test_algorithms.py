import random

from sorttest import exercise_sort
from ods import merge_sort, quick_sort, counting_sort, radix_sort, heap_sort

def test_sorts():
    exercise_sort(merge_sort)
    exercise_sort(quick_sort)
    exercise_sort(heap_sort)
    k = 100
    exercise_sort(lambda a: counting_sort(a, k), lambda : random.randrange(k))
    r = 1000000
    exercise_sort(lambda a: radix_sort(a), lambda : random.randrange(r))
