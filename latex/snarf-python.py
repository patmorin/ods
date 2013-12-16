#!/usr/bin/python
"""Touch up a LaTeX file

   Snarf up a LaTeX input file importing appropriate code where requested
   and adding markup to code snippets delimited by #'s"""
import sys
import re


def matches(line, methods):
    """Match a line of Python code to a method definition

    Determine if this line of Python code is a method definition listed
    in methods"""
    m = re.match(r'\s{4}def\s+(\w+)(\(.*\))\s*:', line)
    if (not m): return False
    name = m.group(1)
    # remove leading and trailing underscores
    name = re.sub(r'^_*|_*$', '', name)
    args = m.group(2)
    # remove self from argument list
    args = re.sub(r'^\(self\s*,?\s*', '(', args)
    # remove spaces from argument list
    args = re.sub(r'\s*', '', args)
    sig = name + args
    sys.stderr.write("signature: %s %r\n" % (sig, methods))
    return sig in methods
    

def translate_code(line):
    """Translate a snippet of Python into LaTeX markup for pseudocode"""
    # hide input checking/exception handling in pseudocode
    if re.search(r'\braise\b', line): return ''

    # hide compiler directives
    line = re.sub(r'#@.*$', '', line)

    # don't touch comments
    comment = ''
    m = re.search(r'#.*$', line)
    if m:
	comment = m.group(0)
        line = re.sub(r'#.*$', '', line)

    # eliminate self and def
    line = re.sub(r'self\.', '', line)
    line = re.sub(r'\bdef\s*', '', line)
    line = re.sub(r'\bself\b,?\s*', '', line)

    # for <blah>: => for <blah> do
    line = re.sub(r'\b(for\b[^:]+):', r'\1 do', line)

    # if <blah>: => if <blah> then
    line = re.sub(r'\bif (.*):', r'if \1 then', line) 

    # len(<blah>) => length(<blah>)
    line = re.sub(r'\blen\s*\(\s*(\w+)\s*\)', r'length(\1)', line)
    line = re.sub(r'\b(\w+)\.length', r'length(\1)', line)

    # range(a,b,-1) => a,...,b+1
    line = re.sub(r'\brange\s*\(\s*(.*),\s*(.*),\s*-1\s*\)',
                  r'\1,\1-1,\1-2,\ldots,\2+1', line)

    # range(a, b-1) => a,...,b-2
    line = re.sub(r'\brange\s*\(\s*(.*),\s*(.*)\s*\-\s*1\s*\)', 
                  r'\1,\1+1,\1+2,\ldots,\2-2', line)

    # range(a, b) => a,...,b-1
    line = re.sub(r'\brange\s*\(\s*(.*),\s*(.*)\s*\)', r'\1,\1+1,\1+2,\ldots,\2-1', line)

    # range(a) => 0,...,a-1
    line = re.sub(r'\brange\s*\(\s*([^),]+)\s*\)', r'0,1,2,\ldots,\1-1', line)

    # a += b => a = a + b
    line = re.sub(r'\b(\w+)\b\s*\+=\s*(.*)\s*', r'\1 = \1 + \2', line)
    line = re.sub(r'\b(\w+)\b\s*-=\s*(.*)\s*', r'\1 = \1 - \2', line)

    # get rid of any remaining colons
    line = re.sub(r':', '', line)

    # int(ceil(blah)) => \lceil{blah}\rceil
    line = re.sub(r'int\(ceil\((.+)\)\)', \
                  r'\ensuremath{\left\lceil{\1}\\right\\rceil}', line)

    # int(sqrt(blah)) => \sqrt{blah}
    line = re.sub(r'\bsqrt\((.+)\)', r'\sqrt{\1}', line)

    # elif => else if
    line = re.sub(r'\belif\b', r'else if', line)

    # highlight keywords
    keywords = r'\b(if|or|and|then|else|in|for|do|return|raise)\b'
    line = re.sub(keywords, r'\\textbf{\1}', line)

    # recognize mathematical expression and ensuremath them
    # warning: doesn't handle nested parentheses or brackets
    basic = r'\b\w+\b'
    fncall = r'%s(\([^\)]*\))?' % basic
    indexed = r'%s(\[[^\]]+\])?' % fncall
    operand = indexed # r'%s(\.%s)*' % (indexed, indexed)
    op = r'(,\\ldots,|&|>=|\.|,|//|<=|==|!=|=|%|<|>|\+|-|/|\*)'
    expr0 = r'(%s(\s*%s\s*%s)*)' % (operand, op, operand)
    parenexpr = r'(%s|\(%s\))' % (expr0, expr0)
    expr = r'(^|[^{\\])(%s(\s*%s\s*%s)*)' % (parenexpr, op, parenexpr)
    line = re.sub(expr, r'\1\ensuremath{\2}', line)

    # turn Python math operators into LaTeX math operators
    line = re.sub(r'>=', r'\\ge', line) 
    line = re.sub(r'<=', r'\le', line)
    line = re.sub(r'%', r'\\bmod ', line) 
    line = re.sub(r'\*', r'\cdot ', line)
    line = re.sub(r'!=', r'\\ne', line)
    line = re.sub(r'==', r'\eq', line) 
    line = re.sub(r'//', r'\\bdiv ', line)
    line = re.sub(r'(^|[^\\])&', r'\1 and ', line)
    line = re.sub(r'([^=])=([^=])', r'\1\\gets \2', line)

    # lowercase True and False
    line = re.sub(r'True', r'true', line)
    line = re.sub(r'False', r'false', line)

    # None/null => \textbf{nil}
    line = re.sub(r'\b(None|null)\b', r'nil', line)

    # typeset function names in mathrm 
    line = re.sub(r'\b(\w+)\(', r'\\mathrm{\1}(', line)

    # typeset variables in mathit
    # this loop is a dirty hack to deal with expressions like 'i-front.size()'
    for i in range(3):   
        line = re.sub(r'(^|[^\\\w])([a-z_][a-z0-9_]*)([^{}\w]|}?$)', \
                r'\1\ensuremath{\mathit{\2}}\3', line)
    
    # typeset class names in mathrm
    line = re.sub(r'([A-Z]\w+)', r'\mathrm{\1}', line)

    # remove leading underscores
    line = re.sub(r'\b_+(\w+)', r'\1', line)

    # remove trailing underscores
    line = re.sub(r'([a-z])_+\b', r'\1', line)
 
    # escape underscores
    line = re.sub(r'_', r'\_', line)

    # add comment back and escape hashes
    if comment:
        line += comment
    line = re.sub(r'#', r'\#', line)
    return line


def touchup_code_line(line):
    """Add LaTeX markup for a line in a code listing"""
    if line == '': return line

    # 4 spaces => 1em
    line = re.sub(r' {4}', r'\hspace*{1em} ', line)  # specific to lines

    # add \\ at the end of each line
    line = re.sub(r'$', r'\\\\', line)
    return line
    

def print_code(clazz, methods):
    """Print out the methods in clazz that are listed in methods"""

    # translate camel-case method names to Python style
    methods = [re.sub(r'([a-z])([A-Z])', r'\1_\2', s).lower() for s in methods]
    sys.stderr.write(str(methods) + '\n')

    # stupid special cases. Mostly caused by using method overloading in Java
    if clazz == 'SLList' and 'remove()' in methods:
	methods.append('_remove()')
    if clazz == 'DLList' and 'remove(w)' in methods:
        methods.append('remove_node(w)')
    if clazz == 'SkiplistList' and 'add(i,w)' in methods:
        sys.stderr.write("BRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFFFFFF\n")
	methods.append('add_node(i,w)')

    print r'\begin{framed}\begin{flushleft}'
    printed = False
    try:
        filename = "../python/" + clazz.lower() + ".py"
        code = open(filename).read().splitlines()
        printing = False
        for line in code:
            if printing:
                printing = line == '' or line.startswith('     ')
            if not printing and matches(line, methods):
                if printed: print '\\ \\\\' # to add space between methods
                printing = True
            if printing and len(line.strip()) > 0:
                printed = True
                print touchup_code_line(translate_code(line))
    except IOError:
        print "Unable to open %s" % filename
    if not printed: 
        print r'NO OUTPUT (looking for \verb+' + str(methods) + "+)"
    print r'\end{flushleft}\end{framed}'


def code_subs(line):
    """Touch up code snippets in a line of LaTeX code"""
    pattern = r'^(.*)#([^#]+)#(.*)$'
    m = re.search(pattern, line)
    while m:
        code = translate_code(m.group(2))
        if re.search(r'[A-Z]\w*', code):
            pass # just a class name - leave it as is
        else:
            code = r'\ensuremath{' + translate_code(code) + r'}'
        line = m.group(1) + code + m.group(3)
        m = re.search(pattern, line)
    return line


def snarf(infile):
    """Program entry point"""
    lines = infile.read().splitlines();
    for line in lines:
        if line.startswith(r'\cppimport') or line.startswith(r'\javaimport'):
            print "%%%s" % line
        elif line.startswith('\\codeimport'):
            print "%%importing %s: " % line
            m = re.search(r'\{\w+/(\w+)(.*)\}', line)
            clazz = m.group(1)
            methods = m.group(2).lstrip('.').split('.')
            if [m for m in methods if re.match(r'^\w+$', m)]:
                sys.stderr.write('looking for init() in %s because of %s\n' \
                                % (clazz, m)) 
                methods.append('init()')
            sys.stderr.write('looking for %r in %s\n' \
                              % (methods, clazz))
            print_code(clazz, methods)
        else:
            print code_subs(line)


def die(msg, code=-1):
    """Print the error message msg and exit"""
    sys.stderr.write("Fatal Error: " + msg + "\n")
    sys.exit(code)


def main():
    argv = sys.argv
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


main()
