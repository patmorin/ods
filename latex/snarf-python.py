#!/usr/bin/python
"""Touch up a LaTeX file

   Snarf up a LaTeX input file importing appropriate code where requested
   and adding markup to code snippets delimited by #'s"""
import sys
import re


# Ack! Global!
html = False

def matches(line, methods):
    """Match a line of Python code to a method definition

    Determine if this line of Python code is a method definition listed
    in methods"""
    m = re.match(r'(\s{4})?def\s+(\w+)(\(.*\))\s*:', line)
    if (not m): return False
    name = m.group(2)
    # remove leading and trailing underscores
    name = re.sub(r'^_*|_*$', '', name)
    args = m.group(3)
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

    # don't touch comments or, rather, save them for later
    comment = ''
    m = re.search(r'#.*$', line)
    if m:
        comment = m.group(0)
        line = re.sub(r'#.*$', '', line)

    # eliminate self and def
    line = re.sub(r'self\.', '', line)
    line = re.sub(r'\bdef\s*', '', line)
    line = re.sub(r'\bself\b,?\s*', '', line)

    # super(Class,self) => super
    line = re.sub(r'super\([^)]*\)', 'super', line)

    # for <blah>: => for <blah> do
    # while <blah>: => while <blah> do
    # Note: The space after 'do' is important here (see [1])
    line = re.sub(r'\b((for|while)\b[^:]+):', r'\1 do ', line)

    # if <blah>: => if <blah> then
    # Note: The space after 'then' is important here (see [1])
    line = re.sub(r'\bif (.*):', r'if \1 then ', line) 

    # else: <blah> => else <blah>
    # Note: The space after 'then' is important here (see [1])
    line = re.sub(r'\belse:', r'else ', line) 

    # len(<blah>) => length(<blah>)
    line = re.sub(r'\blen\s*\(\s*(\w+)\s*\)', r'length(\1)', line)

    # array.length (from Java) => length(array)
    line = re.sub(r'\b(a|t|c)\.length', r'length(\1)', line)

    # range(a,b,-1) => a,...,b+1
    line = re.sub(r'\brange\s*\(\s*(.*),\s*(.*),\s*-1\s*\)',
                  r'\1,\1-1,\1-2,\ldots,\2+1', line)

    # range(a, b-1) => a,...,b-2
    #line = re.sub(r'\brange\s*\(\s*(.*),\s*(.*)\s*\-\s*1\s*\)', 
    #              r'\1,\1+1,\1+2,\ldots,\2-2', line)

    # range(a, b) => a,...,b-1
    line = re.sub(r'\brange\s*\(\s*(.*),\s*(.*)\s*\)', 
                  r'\1,\1+1,\1+2,\ldots,\2-1', line)

    # range(a) => 0,...,a-1
    #re.sub(r'\brange\s*\(\s*([^),]+(\([^)]*\)[^)]*)?)\s*\)', 
    line = re.sub(r'\brange\s*\((.*)\)', r'0,1,2,\ldots,\1-1', line)

    # a[x:y] => a[x,x+1,\ldots,y-1]
    line = re.sub(r'\[([^\]]+):([^\]]+)\]', r'[\ensuremath{\1,\1+1,\ldots,\2-1}]', line)
    
    # some dumb arithmetic
    line = re.sub(r'\+\s*1\s*-\s*1', '', line)
    line = re.sub(r'-\s*1\s*-\s*1', '-2', line)
    line = re.sub(r'0\s*\+\s*1', '1', line)
    

    # -1-1 => -2 and similar arithmetic
    line = re.sub(r'-\s*1\s*-\s*1', r'-2', line)
    line = re.sub(r'-\s*1\s*-\s*2', r'-3', line)
    line = re.sub(r'-\s*1\s*\+\s*1', r'0', line)
    line = re.sub(r'1\s*\+\s*1', r'2', line)
    line = re.sub(r'1\s*\+\s*2', r'3', line)

    # <blah>.hashCode() => hash_code(<blah>)
    # line = re.sub(r'(\w+)\.hashCode()', r'hash_code(\1)', line)

    # a += b => a = a + b --- handles +=, -=, *=, and /=
    line = re.sub(r'([^\s]+)\s*([+*-/])=\s*(.*)$', r'\1 = \1 \2 \3', line) 

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
    keywords = r'\b(if|or|and|then|else|in|for|do|return|raise|while|break)\b'
    line = re.sub(keywords, r'\\textbf{\1}', line)

    # a is b => a == b (our code doesn't care about equality vs. identity
    line = re.sub(r'\bis\s+not\b', r'!=', line)
    line = re.sub(r'\bis\b', '==', line) 

    # recognize mathematical expression and \ensuremath them
    # warning: doesn't handle nested parentheses or brackets
    basic = r'\b\w+\b'
    fncall = r'%s(\([^\)]*\))?' % basic
    indexed = r'%s(\[[^\]]+\])?' % fncall
    operand = r'(-?%s)' % indexed 
    op = r'(,\\ldots,|&|>=|\.|,|//|<=|==|!=|=|%|<<|>>|<|>|\+|-|/|\*|\^)'
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
    line = re.sub(r'(^|[^\\])&', r'\1 \\wedge ', line)
    line = re.sub(r'//', r'\\bdiv ', line)
    line = re.sub(r'([^=])=([^=])', r'\1\\gets \2', line)

    # these are hacks and should eventually be fixed
    line = re.sub(r'\>\>', r'\ensuremath{\\gg}', line) 
    line = re.sub(r'\<\<', r'\ensuremath{\\ll}', line) 
    line = re.sub(r'\^', r'\ensuremath{\oplus}', line)

    # del is a python keyword, but we have a variable called del in Ch. 5
    line = re.sub(r'\bdl\b', r'\mathit{del}', line)

    # 1 << <blah> => 2^{<blah>}
    line = re.sub(r'\(?\s*1\s*<<\s*(\w+)\s*\)?', r'2^{\1}', line)

    # lowercase True and False
    line = re.sub(r'True', r'true', line)
    line = re.sub(r'False', r'false', line)

    # Camelcase variable names to underscores
    line = re.sub(r'\b([a-z_][a-z0-9_]*)([A-Z])', r'\1_\2', line)
    line = re.sub(r'_[A-Z]_', lambda s: s.group(0).lower(), line)

    # None/null => \textbf{nil}
    line = re.sub(r'\b(None|null)\b', r'nil', line)

    # typeset function names in mathrm 
    line = re.sub(r'\b(\w+)\(', r'\\mathrm{\1}(', line)

    # typeset variables in mathit
    # this loop is a dirty hack to deal with expressions like 'i-front.size()'
    for i in range(3):   
        # This RE is delicate. It interacts with [1]
        line = re.sub(r'(^|[^\\\w])([a-z_][a-z0-9_]*)([^{}\w]|}?$)', \
                r'\1\ensuremath{\mathit{\2}}\3', line)
                
    # don't be afraid to use l as a variable name
    line = re.sub(r'\bl\b', r'\ell', line)
    
    # typeset class names in mathrm
    line = re.sub(r'([A-Z]\w+)', r'\mathrm{\1}', line)

    # remove leading underscores
    line = re.sub(r'\b_+(\w+)', r'\1', line)

    # remove trailing underscores
    line = re.sub(r'([a-z])_+\b', r'\1', line)
 
    # escape underscores
    line = re.sub(r'_', r'\_', line)

    # This backstop makes sure we ensuremath any assignment 
    line = re.sub(r'(\s*)([^#]*\\gets[^\w][^#]*)', r'\1\ensuremath{\2}', line) 

    # add comment back and escape hashes
    if comment:
        if not line.endswith(' '):
            line += r'\ '
        line += r'{\color{comment}' + comment + '}'
    line = re.sub(r'#', r'\#', line)
    return line


def touchup_code_line(line):
    """Add LaTeX markup for a line in a code listing"""
    if line == '': return line

    # 4 spaces => 1em
    line = re.sub(r' {4}', r'\hspace*{1em} ', line)

    # add \\ at the end of each line
    line = re.sub(r'$', r'\\\\', line)
    return line
    

def print_code(clazz, methods):
    """Print out the methods in clazz that are listed in methods"""

    # translate camel-case method names to Python style
    methods = [re.sub(r'([a-z])([A-Z])', r'\1_\2', s).lower() for s in methods]
    sys.stderr.write(str(methods) + '\n')

    if not html:
        print r'\begin{oframed}'
    print r'\begin{flushleft}'
    printed = False
    try:
        filename = "../python/ods/" + clazz.lower() + ".py"
        code = open(filename).read().splitlines()
        printing = False
        for line in code:
            if printing:
                printing = line == '' or line.startswith(indent)
            if not printing and matches(line, methods):
                indent = re.match(r'\s*', line).group(0) + '    '
                if printed: print '\\ \\\\' # to add space between methods
                printing = True
            if printing and len(line.strip()) > 0:
                printed = True
                if len(indent) == 4:
                    line = '    ' + line
                print touchup_code_line(translate_code(line))
    except IOError:
        print "Unable to open %s" % filename
    if not printed: 
        print r'NO OUTPUT (looking for \verb+' + str(methods) + "+)"
    print r'\end{flushleft}'
    if not html:
        print r'\end{oframed}'


def code_subs(line):
    """Touch up code snippets in a line of LaTeX code"""
    pattern = r'^(.*)#([^#]+)#(.*)$'
    m = re.search(pattern, line)
    while m:
        code = m.group(2)
        if re.match(r'^[A-Z]\w*$', code):
            pass
        else:
            code = r'\ensuremath{' + translate_code(code) + r'}'
        line = m.group(1) + code + m.group(3)
        m = re.search(pattern, line)
    return line


def snarf(infile):
    """Program entry point"""
    lines = infile.read().splitlines();
    for line in lines:
        if line.startswith(r'\cppimport') \
                or line.startswith(r'\javaimport'):
            print "%%%s" % line
        elif line.startswith('\\codeimport') \
                or line.startswith(r'\pcodeimport'):
            print "%%importing %s: " % line
            m = re.search(r'\{\w+/(\w+)(.*)\}', line)
            clazz = m.group(1)
            methods = m.group(2).lstrip('.').split('.')
            if [m for m in methods if re.match(r'^\w+$', m)]:
                sys.stderr.write('looking for initialize() in %s because of %s\n' \
                                % (clazz, m)) 
                methods.append('initialize()')
            sys.stderr.write('looking for %r in %s\n' \
                              % (methods, clazz))
            print_code(clazz, methods)
        else:
            print code_subs(line)


def die(msg, code=-1):
    """Print the error message msg and exit"""
    sys.stderr.write("Fatal Error: " + msg + "\n")
    sys.exit(code)


if __name__ == "__main__":
    argv = sys.argv[:]
    if "-html" in argv:
        html = True
        html = False # fixit
        argv = [s for s in argv if s != "-html"]
    if len(argv) == 1:
        infile = sys.stdin
    elif len(argv) == 2:
        try:
            infile = open(argv[1])
        except IOError:
            die("Unable to open input file %s" % argv[1])
    else:
        die("Usage: %s [-html] [<infile>]" % argv[0])
    snarf(infile)


