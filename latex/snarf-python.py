#!/usr/bin/python
"""Touch up a LaTeX file

   Snarf up a LaTeX input file importing appropriate code where requested
   and adding markup to code snippets delimited by #'s"""
import sys
from sys import argv
import re


def matches(line, methods):
    """Math a line of python code to a method definition

    Determine if this line of python code is a method definition listed
    in methods"""
    m = re.match(r'\s{4}def\s+(\w+)(\(.*\))\s*:', line)
    if (not m): return False
    name = m.group(1)
    args = re.sub(r' |self,', '', m.group(2))
    sig = name + args
    return sig in methods
    
def touchup_code(line):
    """Touch up a line of python code to make it look more like pseudocode"""
    line = re.sub(r'self\.', '', line)
    line = re.sub(r'\bdef ', '', line)
    line = re.sub(r'\bif (.*):', r'if \1 then', line) 
    line = re.sub(r'\blen\s*\(\s*(\w+)\s*\)', r'length(\1)', line);
    line = re.sub(r'\brange\s*\(\s*([^)]+)\s*\)', r'0,...,\1-1', line)
    line = re.sub(r'\bself\b,?\s*', '', line)
    line = re.sub(r'_', '', line)
    return line

def print_code(clazz, methods):
    """Print out the methods in clazz that are listed in methods"""
    print r'\begin{Verbatim}[frame=single,label=\texttt{%s}]' % clazz 
    try:
        filename = "../python/" + clazz.lower() + ".py"
        code = open(filename).read().splitlines()
        printing = False;
        for line in code:
            if printing:
                printing = line == '' or line.startswith('     ')
            if not printing and matches(line, methods):
                printing = True
            if printing:
                print touchup_code(line)
    except IOError:
        print "Unable to open %s" % filename
    print r'\end{Verbatim}'

def code_subs(line):
    """Touch up code snippets in a line of LaTeX code"""
    m = re.match(r'.*#([^#]+)#', line)
    while m:
        code = m.group(1)
        if re.match(r'[A-Z]\w*', code):
            code = r'\\texttt{%s}' % code # just a class name
        else:
            code = re.sub(r'%', r'\%', code)
            code = r'\ensuremath{\mathtt{%s}}' % code
        line = re.sub(r'#([^#]+)#', code, line, 1)
        m = re.match(r'.*#([^#]+)#', line)
    return line

def snarf(infile):
    """Program entry point"""
    lines = infile.read().splitlines();
    for line in lines:
        if line.startswith(r'\cppimport') or line.startswith(r'\javaimport'):
            # do nothing
            print "%%%s" % line
        elif line.startswith('\\codeimport'):
            print "%%importing %s: " % line
            m = re.search(r'\{\w+/(\w+)(.*)\}', line)
            clazz = m.group(1)
            methods = m.group(2).lstrip('.').split('.')
            print_code(clazz, methods)
            # do some stuff
        else:
            # do some other stuff
            print code_subs(line)

def die(msg, code=-1):
    """Print the error message msg and exit"""
    sys.stderr.write("Fatal Error: " + msg + "\n")
    sys.exit(code)

if len(argv) == 1:
    infile = sys.stdin
elif len(argv) == 2:
    try:
        infile = open(argv[1])
    except IOError:
        die("Unable to open input file %s" % argv[1])
else:
    die("Usage: %s [<infile>]" % argv[0])
snarf(infile)



