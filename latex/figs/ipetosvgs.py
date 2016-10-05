#!/usr/bin/python
""" ipetosvgs - Convert all views in an ipe file to their own SVG images

    This opens an ipe file, figures out how many views it contains, and
    converts each view into an SVG.  When convering a file name myfile.ipe,
    this generates these svg files:
    myfile.svg (view 1)
    myfile-1.svg (view 1)
    myfile-2.svg (view 2)
    ...
    myfile-N.svg (view N)
"""
import os
import sys

def execute_or_die(cmd):
    print("Exec: {}".format(cmd))
    retval = os.system(cmd)
    if retval != 0:
        sys.err.write("Error executing: {}\n".format(cmd))
        sys.exit(retval)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        sys.err.write("Usage: {} <ipefile>\n".format(sys.argv[0]))
        sys.exit(-1)
    filename = sys.argv[1]
    views = open(filename).read().count("<view")
    print("{} has {} views".format(filename, views))
    base, ext = os.path.splitext(filename)
    outfile = base + ".svg"
    cmd = "iperender -svg {} {}".format(filename, outfile)
    execute_or_die(cmd)
    for i in range(1, views+1):
        outfile = "{}-{}.svg".format(base, i)
        cmd = "iperender -svg -view {} {} {}".format(i, filename, outfile)
        execute_or_die(cmd)
