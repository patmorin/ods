#!/usr/bin/python3
""" Create a picture of a skiplist and search path """
import os
import sys
import random

yoffset = -112
boxx, boxy = -32, 112+yoffset
boxes = """
<path layer="alpha" matrix="1 0 0 1 {x} {y}" stroke="black">
128 208 m
128 192 l
144 192 l
144 208 l
h
</path>
<path matrix="1 0 0 1 {x} {y}" stroke="black">
144 208 m
144 192 l
160 192 l
160 208 l
h
</path>"""

box = """
<path matrix="1 0 0 1 {x} {y}" stroke="black">
144 208 m
144 192 l
160 192 l
160 208 l
h
</path>"""

hbox = """
<path matrix="1 0 0 1 {x} {y}" stroke="rose gold" pen="ultrafat">
144 208 m
144 192 l
160 192 l
160 208 l
h
</path>"""

ballx, bally = -31.728, 112.454+yoffset
ball = """
<path matrix="1 0 0 1 {x} {y}" stroke="black" fill="black">
1.6704 0 0 1.6704 151.728 199.546 e
</path>
"""

valuex, valuey = -31.288, 108.473+yoffset
value = """
<text matrix="1 0 0 1 {x} {y}" transformations="translations" pos="135.288 200.261" stroke="black" type="label" width="7.153" height="6.976" depth="1.96" halign="center" valign="baseline">{symbol}</text>
"""

arrowx, arrowy = -32, 112+yoffset
arrow="""
<path matrix="1 0 0 1 {x} {y}" stroke="black" arrow="pointed/small">
152 200 m
{x1} 200 l
</path>
"""

harrow="""
<path matrix="1 0 0 1 {x} {y}" stroke="rose gold" pen="ultrafat" arrow="pointed/small">
152 200 m
{x1} 200 l
</path>
"""

symbols = [ "Female", "Male", "Hermaphrodite", "Neutral", "FemaleFemale",
            "MaleMale", "FemaleMale"]
random.shuffle(symbols)

def get_xy(i, j):
    return 3*16*i , 16*j

def lower_node(i, j):
    x, y = get_xy(i, j)
    if i == 0:
        symbol = r'\SquarePipe'
    else:
        symbol = str(i)
    return boxes.format(x=x+boxx, y=y+boxy) \
            + ball.format(x=x+ballx, y=y+bally) \
            + value.format(x=x+valuex, y=y+valuey, symbol=symbol)

def upper_node(i, j):
    x, y = get_xy(i, j)
    return box.format(x=x+boxx, y=y+boxy) \
            + ball.format(x=x+ballx, y=y+bally)

def node(i, j):
    if j == 0: return lower_node(i, j)
    return upper_node(i, j)

def path_node(i, j):
    x, y = get_xy(i, j)
    return hbox.format(x=x+boxx, y=y+boxy)

def pointer(i, j, k):
    x, y = get_xy(i, j)
    d = 3*(k - i) - 1 - (j == 0)
    x1 = 160 + d*16
    return arrow.format(x=x+arrowx, y=y+arrowy, x1=x1)

def hpointer(i, j, k):
    x, y = get_xy(i, j)
    d = 3*(k - i) - 1 - (j == 0)
    x1 = 160 + d*16
    return harrow.format(x=x+arrowx, y=y+arrowy, x1=x1)

def draw_it(n, outfilename):
    filename = "skiplist-big.ipex"
    print("Reading from {}".format(filename))
    head, tail = open(filename).read().split("BARGLE")
    print("Writing to {}".format(outfilename))
    fp = open(outfilename, "w")
    fp.write(head)
    datax = range(1, n+1)
    j = 0
    u = int(3*n/4)
    while datax:
        data = datax
        datax = list()
        state = 0
        for i in reversed(range(len(data))):
            if state == 1:
                fp.write(hpointer(data[i], j, data[i+1]))
            if state == 0 and data[i] < u:
                state = 1 # current node is on search path
            if state == 1:
                fp.write(path_node(data[i], j))  # hilight current node
            fp.write(node(data[i], j))
            if i+1 < len(data):
                fp.write(pointer(data[i], j, data[i+1]))
            if random.randrange(2):
                datax.append(data[i])
                if state == 1: state = 2 # search path goes up from here
        if state == 1:
            fp.write(hpointer(0, j, data[0]))
        if state < 2:
            fp.write(path_node(0, j))
        fp.write(node(0, j))
        fp.write(pointer(0, j, data[0]))
        j += 1
        datax = datax[::-1]
    fp.write(tail)
    fp.close()
    print("Done")

    print("Converting to PDF")
    os.system("ipetoipe -pdf {}".format(outfilename))

if __name__ == "__main__":
    for i in range(39):
        draw_it(10+i*5, "skiplist-big-{}.ipe".format(i+1))

#    print("Opening in system viewer")
#    os.system("xdg-open {}".format(filename.replace(".ipe", ".pdf")))
