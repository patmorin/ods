import random

from sorttest import sort_test
from ods import merge_sort, quick_sort, counting_sort, radix_sort, heap_sort

def test_sorts():
    sort_test(merge_sort)
    sort_test(quick_sort)
    sort_test(heap_sort)
    k = 100
    sort_test(lambda a: counting_sort(a, k), lambda : random.randrange(k))
    r = 1000000
    sort_test(lambda a: radix_sort(a), lambda : random.randrange(r))
