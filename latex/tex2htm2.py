#!/usr/bin/python3
import os
import sys
import re
from collections import defaultdict


# Some global variables
environment_handlers = defaultdict(lambda: process_environment_default)
command_handlers = defaultdict(lambda: process_command_default)
unprocessed_commands = set()

# Math mode
MATH = 1

#
# Utilities
#
def abort(msg, status=-1):
    sys.stderr.write(msg + '\n')
    sys.exit(status)

#
# Preprocessing functions
#
def preprocess_hashes(subtex):
    """Prevents percents inside hashes from being treated as comments"""
    blocks = list()
    rx = re.compile('#([^#]*)#', re.M|re.S)
    lastidx = 0
    for m in rx.finditer(subtex):
        blocks.append(subtex[lastidx:m.start()])
        lastidx = m.end()
        blocks.append(re.sub(r'(^|[^\\])%', r'\1\%', m.group(0)))
    blocks.append(subtex[lastidx:])
    return "".join(blocks)

def strip_comments(tex):
    lines = tex.splitlines()
    nulled = set()
    for i in range(len(lines)):
        if len(lines[i]) > 0:
            lines[i] = re.sub(r'(^|[^\\])\%.*$', r'\1', lines[i])
            if len(lines[i]) == 0:
                nulled.add(i)
    lines = [lines[i] for i in range(len(lines)) if i not in nulled]
    return "\n".join(lines)



#
# Label and Refeference Handling
# TODO: Add numbers to environments
def process_labels_and_refs(tex):
    headings = ['chapter'] + ['sub'*i + 'section' for i in range(4)]
    reh = r'(' + '|'.join(headings) + r'){(.+?)}'
    environments = ['thm', 'lem', 'exc', 'figure', 'equation']
    ree = r'begin{(' + '|'.join(environments) + r')}'
    rel = r'(\w+)label{(.+?)}'
    bigone = r'\\({})|\\({})|\\({})'.format(reh, ree, rel)

    sec_ctr = [0]*(len(headings)+1)
    env_ctr = [0]*len(environments)
    html = []
    splitters = [0]
    lastlabel = None
    labelmap = dict()
    for m in re.finditer(bigone, tex):
        if m.group(2):
            # This is a sectioning command
            splitters.append(m.start())
            i = headings.index(m.group(2))
            if i == 0:
                env_ctr = [0]*len(env_ctr)
            sec_ctr[i:] = [sec_ctr[i]+1]+[0]*(len(headings)-i-1)
            for j in range(i+1, len(sec_ctr)): sec_ctr[j] = 0
            idd = m.group(2) + ":" + ".".join([str(x) for x in sec_ctr[:i+1]])
            lastlabel = idd
            html.append("<a id='{}'></a>".format(idd))
        elif m.group(5):
            # This is an environment
            splitters.append(m.start())
            i = environments.index(m.group(5))
            env_ctr[i] += 1
            idd = "{}:{}.{}".format(m.group(5), sec_ctr[0], env_ctr[i])
            lastlabel = idd
            html.append("<a id='{}'></a>".format(idd))
        elif m.group(7):
            # This is a labelling command
            label = "{}:{}".format(m.group(7), m.group(8))
            labelmap[label] = lastlabel
    splitters.append(len(tex))
    chunks = [tex[splitters[i]:splitters[i+1]] for i in range(len(splitters)-1)]
    zipped = [chunks[i] + html[i] for i in range(len(html))]
    zipped.append(chunks[-1])
    tex = "".join(zipped)
    tex = re.sub(r'^\s*\\(\w+)label{[^}]+}\s*?$\n', '', tex, 0, re.M|re.S)
    tex = re.sub(r'\\(\w+)label{(.+?)}', '', tex)
    return process_references(tex, labelmap)

def process_references(tex, labelmap):
    map = dict([('chap', 'Chapter'),
                ('sec', 'Section'),
                ('thm', 'Theorem'),
                ('lem', 'Lemma'),
                ('fig', 'Figure'),
                ('eq', 'Equation'),
                ('exc', 'Exercise')])
    pattern = r'\\(\w+)ref{(.*?)}'
    m = re.search(pattern, tex)
    while m:
        kind = m.group(1).lower()
        print(kind)
        label = "{}:{}".format(kind, m.group(2))
        if label not in labelmap:
            print("Info: undefined label {}".format(label))
            idd = 'REFERR'
            num = '??'
        else:
            idd = labelmap[label]
            num = idd[idd.find(':')+1:]
        if kind in map:
            html = '<a href="#{}">{}&nbsp;{}</a>'.format(idd, map[kind], num)
        else:
            html = '<a href="#{}">{}&nbsp;{}</a>'.format(idd, kind, num)
        tex = tex[:m.start()]  + html + tex[m.end():]
        m = re.search(pattern, tex)
    return tex

#
# LaTeX commands
#





class environment(object):
    def __init__(self, name, content, start, end, args):
        self.name = name
        self.content = content
        self.start = start
        self.end = end
        self.args = args

    def __repr__(self):
        return "environment({},{},{},{},{})".format(repr(self.name),
                                                    repr(self.content),
                                                    repr(self.start),
                                                    repr(self.end),
                                                    repr(self.args))


def process_hash(b, env, mode):
    inner = r'\mathtt{{{}}}'.format(re.sub(r'(^|[^\\])&', r'\1\&', env.content))
    if mode & MATH:
        return [ inner ]
    else:
        return [ r'\(', inner, r'\)' ]

def process_passthroughs(b, env, mode):
    blocks = [r'\begin{{{}}}'.format(env.name)]
    blocks.extend(process_recursively(env.content, mode))
    blocks.append(r'\end{{{}}}'.format(env.name))
    return blocks

def process_display_math(b, env, mode):
    blocks = [r'\begin{{{}}}'.format(env.name)]
    blocks.extend(process_recursively(env.content, mode | MATH))
    blocks.append(r'\end{{{}}}'.format(env.name))
    return blocks

def process_inline_math(b, env, mode):
    blocks = [r'\(']
    blocks.extend(process_recursively(env.content, mode | MATH))
    blocks.append(r'\)')
    return blocks


def process_list(b, env, mode):
    newblocks = list()
    mapper = dict([('itemize', 'ul'), ('enumerate', 'ol'), ('list', 'ul')])
    tag = mapper[env.name]
    newblocks.append('<{} class="{}">'.format(tag, env.name))
    newblocks.extend(process_recursively(process_list_items(env.content), mode))
    newblocks.append('</li></{}>'.format(tag))
    return newblocks

def process_list_items(b):
    b = re.sub(r'\\item\s+', '\1', b, 1)
    b = re.sub(r'\\item\s+', '\2\1', b)
    b = re.sub(r'\s*' + '\1' + r'\s*', '<li>', b, 0, re.M|re.S)
    b = re.sub(r'\s*' + '\2' + r'\s*', '</li>', b, 0, re.M|re.S)
    return b

def process_tabular(tex, env, mode):
    inner = "".join(process_recursively(env.content, mode))
    rows = re.split(r'\\\\', inner)
    rows = [re.split(r'\&', r) for r in rows]
    table = '<table align="center">'
    for r in rows:
        table += '<tr>'
        for c in r:
            table += '<td>' + c + '</td>'
        table += '</tr>'
    table += '</table>'
    return table

def process_environment_default(tex, env, mode):
    newblocks = ['<div class="{}">'.format(env.name)]
    newblocks.extend(process_recursively(env.content, mode))
    newblocks.append('</div><!-- {} -->'.format(env.name))
    return newblocks




class command(object):
    def __init__(self, name, optargs, args, start, end):
        self.name = name
        self.optargs = optargs
        self.args = args
        self.start = start
        self.end = end

    def __repr__(self):
        return "command({},{},{},{},{})".format(repr(self.name), repr(self.optargs),
                repr(self.args), repr(self.start), repr(self.end))

def match_parens(tex, i, open, close):
    di = defaultdict(int, {open: 1, close: -1})
    if i == len(tex): return i
    try:
        d = di[tex[i]]
        j = i+d
        while d > 0:
            d += di[tex[j]]
            j+=1
        return j
    except IndexError:
        sys.stderr.write("Couldn't match parenthesis:\n")
        sys.stderr.write(tex[max(0,i-10):] + "\n")
        sys.exit(-1)

def next_command(tex, pos):
    rx = re.compile(r'\\(\w+)')
    m = rx.search(tex, pos)
    if m:
        optargs = []
        j = m.end()
        k = match_parens(tex, j, '[', ']')
        while k > j:
            optargs.append(tex[j+1:k-1])
            j = k
            k = match_parens(tex, j, '[', ']')
        args = []
        k = match_parens(tex, j, '{', '}')
        while k > j:
            args.append(tex[j+1:k-1])
            j = k
            k = match_parens(tex, j, '{', '}')
        cmd = command(m.group(1), optargs, args, m.start(), j)
        return cmd
    return None


def process_command_default(tex, cmd, mode):
    """By default, we just pass commands through untouched"""
    unprocessed_commands.add(cmd.name)
    print("Unprocessed: {}".format(cmd.name))
    blocks = [r'\{}'.format(cmd.name)]
    for a in cmd.optargs:
        blocks.append(r'[{}]'.format(a))
    for a in cmd.args:
        blocks.append('{')
        blocks.extend(process_recursively(a, mode))
        blocks.append('}')
    return blocks

def process_command_worthless(tex, cmd, mode):
    print("Worthless command: {}".format(cmd.name))
    return ['']

def setup_command_handlers():
    handlers = {'chapter': process_chapter,
                'section': process_section,
                'subsection': process_subsection,
                'subsubsection': process_subsubsection,
                'paragraph': process_paragraph,
                'emph': process_emph,
                'caption': process_caption,
                'includegraphics': process_graphics,
                'codeimport': process_codeimport,
                'javaimport': process_codeimport,
                'cite': lambda tex, cmd, mode: ['[{}]'.format(cmd.args[0])],
                'ldots': process_dots_cmd
               }
    for c in handlers:
        command_handlers[c] = handlers[c]  # FIXME: Lazy
    handlers = None
    worthless = ['newlength', 'setlength', 'addtolength', 'vspace', 'index',
                 'cpponly', 'cppimport', 'pcodeonly', 'pcodeimport', 'qedhere',
                 'end', 'hline']
    for c in worthless:
        command_handlers[c] = process_command_worthless
    strip = ['javaonly', 'notpcode']
    for c in strip:
        command_handlers[c] = strip_command

def process_dots_cmd(tex, cmd, mode):
    if mode & MATH:
        return process_command_default(tex, cmd, mode)
    else:
        return [ '&hellip;' ]

def process_recursively(tex, mode):
    newblocks = list()
    lastidx = 0
    cmd = next_command(tex, lastidx)
    while cmd:
        newblocks.append(tex[lastidx:cmd.start])
        if cmd.name == 'begin':
            env = get_environment(tex, cmd)
            lastidx = env.end
            print("name='{}'".format(env.name))
            print("content='{}'".format(env.content))
            print("everything='{}'".format(tex[env.start:env.end]))
            newblocks.extend(environment_handlers[env.name](tex, env, mode))
        else:
            lastidx = cmd.end
            print("name='{}'".format(cmd.name))
            print("args='{}'".format(cmd.args))
            print("everything='{}'".format(tex[cmd.start:cmd.end]))
            newblocks.extend(command_handlers[cmd.name](tex, cmd, mode))
        cmd = next_command(tex, lastidx)
    newblocks.append(tex[lastidx:])
    return newblocks

def strip_command(text, cmd, mode):
    return process_recursively(cmd.args[0], mode)

def process_section(text, cmd, mode):
    blocks = ["<h1>"]
    blocks.extend(process_recursively(cmd.args[0], mode))
    blocks.append("</h1>")
    return blocks

def process_subsection(text, cmd, mode):
    blocks = ["<h2>"]
    blocks.extend(process_recursively(cmd.args[0], mode))
    blocks.append("</h2>")
    return blocks

def process_subsubsection(text, cmd, mode):
    blocks = ["<h2>"]
    blocks.extend(process_recursively(cmd.args[0], mode))
    blocks.append("</h2>")
    return blocks

def process_paragraph(text, cmd, mode):
    blocks = ['<div class="paragraph_title">']
    blocks.extend(process_recursively(cmd.args[0], mode))
    blocks.append("</div><!-- paragraph_title -->")
    return blocks

def process_emph(text, cmd, mode):
    blocks = ["<em>"]
    blocks.extend(process_recursively(cmd.args[0], mode))
    blocks.append("</em>")
    return blocks

def process_chapter(text, cmd, mode):
    blocks = list()
    blocks.append('<div class="chapter">')
    blocks.extend(process_recursively(cmd.args[0], mode))
    blocks.append('</div><!-- chapter -->')
    return blocks

def process_caption(text, cmd, mode):
    blocks = ['<div class="caption">']
    blocks.extend(process_recursively(cmd.args[0], mode))
    blocks.append("</div><!-- caption -->")
    return blocks

def process_graphics(text, cmd, mode):
    return ['<img src="{}.svg"/>'.format(cmd.args[0], mode)]

def process_codeimport(text, cmd, mode):
    blocks = ['<div class="codeimport">']
    blocks.extend(process_recursively(cmd.args[0], mode))
    blocks.append("</div><!-- codeimport -->")
    return blocks

def setup_environment_handlers():
    environment_handlers['dollar'] = process_inline_math
    environment_handlers['tabular'] = process_tabular
    environment_handlers['hash'] = process_hash
    displaymaths = ['equation', 'equation*', 'align', 'align*', 'eqnarray*']
    for name in displaymaths:
        environment_handlers[name] = process_display_math
    passthroughs = ['array', 'cases']
    for name in passthroughs:
        environment_handlers[name] = process_passthroughs
    lists = ['itemize', 'enumerate', 'list']
    for name in lists:
        environment_handlers[name] = process_list

def get_environment(tex, begincmd):
    name = begincmd.args[0]
    d = 1
    pos = begincmd.end
    regex = r'\\(begin|end){{{}}}'.format(re.escape(name))
    rx = re.compile(regex)
    while d >= 0:
        m = rx.search(tex, pos)
        if not m:
            abort("Unmatched environment:". format(name))
        if m.group(1) == 'begin':
            d += 1
        else:
            d -= 1
    return environment(name, tex[begincmd.end:m.start()],

                       begincmd.start, m.end(), begincmd.args[1:])

def tex2htm(tex):
    # Some preprocessing
    tex = preprocess_hashes(tex)
    tex = strip_comments(tex)
    tex = re.sub(r'\\\[', r'\\begin{equation*}', tex)
    tex = re.sub(r'\\\]', r'\end{equation*}', tex)
    tex = re.sub(r'\$([^\$]*(\\\$)?)\$', r'\\begin{dollar}\1\\end{dollar}', tex,
                 0, re.M|re.S)
    tex = re.sub(r'\\myeqref', '\\eqref', tex)
    tex = re.sub(r'---', r'&mdash;', tex)
    tex = re.sub(r'--', r'&ndash;', tex)
    tex = re.sub('#([^#]*)#', r'\\begin{hash}\1\end{hash}', tex, 0, re.M|re.S)


    # replace hashes with nulls because html references contain hashes
    tex = re.sub(r'#', '\0', tex)
    tex = process_labels_and_refs(tex)

    blocks = process_recursively(tex, 0)
    #blocks = [process_nonmath_hashes(b) for b in blocks]

    return "".join(blocks)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        sys.stderr.write("Usage: {} <texfile>\n".format(sys.argv[0]))
        sys.exit(-1)
    filename = sys.argv[1]
    base, ext = os.path.splitext(filename)
    outfile = base + ".html"
    print("Reading from {} and writing to {}".format(filename, outfile))

    # Setup a few things
    setup_environment_handlers()
    setup_command_handlers()

    # Read and translate the input
    tex = open(filename).read()
    htm = tex2htm(tex)
    chapter = "None"

    # Write the output
    (head, tail) = re.split('CONTENT', open('head.htm').read())
    head = re.sub('TITLE', chapter, head)
    of = open(outfile, 'w')
    of.write(head)
    of.write(htm)
    of.write(tail)
    of.close()
