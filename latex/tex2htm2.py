#!/usr/bin/python3
import os
import sys
import re

from collections import defaultdict

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

class argument(object):
    def __init__(self, content, start, end):
        self.content = content
        self.start = start
        self.end = end

class environment(object):
    def __init__(self, name, content, start, end, cstart, cend, args=''):
        self.name = name
        self.content = content
        self.start = start
        self.end = end
        self.cstart = cstart
        self.cend = cend
        self.args = args

    def __repr__(self):
        return "environment({},{},{},{},{})".format(repr(self.name),
                                                    repr(self.content),
                                                    repr(self.start),
                                                    repr(self.end),
                                                    repr(self.args))

def get_args(tex, pos=0, endpos=-1):
    rx = re.compile(r'(\[([^\]]+?)\])?({[^}]+})?\]', re.M|re.S)
    ma = rx.match(tex, pos, endpos)  # FIXME: Not perfect
    if ma:
        return argument(ma.group(0), ma.start(), ma.end())
    return argument('', pos, pos)

def get_environment(tex, name, pos=0, endpos=-1):
    return get_environment_regex(tex, re.escape(name), pos, endpos)

def get_generic_environment(tex, pos=0, endpos=-1):
    return get_environment_regex(tex, r'[^}]+', pos, endpos)

def get_environment_regex(tex, regex, pos=0, endpos=-1):
    if endpos == -1: endpos = len(tex)
    rx = re.compile(r'\\begin{(' + regex + ')}')
    mb = rx.search(tex, pos, endpos)
    if mb:
        name = mb.group(1)
        args = get_args(tex, mb.end(), endpos)
        rxb = re.compile(r'\\begin{(' + re.escape(name) + ')}')
        rxe = re.compile(r'\\end{(' + re.escape(name) + ')}')
        ite = rxe.finditer(tex, mb.end(), endpos)
        d = 1
        i = args.end
        while d > 0:
            me = ite.__next__()
            d -= 1
            d += len(rxb.findall(tex, i, me.start()))
            i = me.end()
        return environment(name, tex[args.end:me.start()], mb.start(), me.end(),
                           args.end, me.start(), args.content)
    return None

def process_environments_recursively(tex):
    handlers = {'dollar': process_inline_math,
                'tabular': process_tabular }
    displaymaths = ['equation', 'equation*', 'align', 'align*', 'eqnarray*']
    for d in displaymaths:
        handlers[d] = process_display_math
    passthroughs = ['array', 'cases']
    for d in passthroughs:
        handlers[d] = process_passthroughs
    lists = ['itemize', 'enumerate', 'list']
    for ell in lists:
        handlers[ell] = process_list
    newblocks = list()
    lastidx = 0
    b = tex
    env = get_generic_environment(b)
    while env:
        newblocks.append(b[lastidx:env.start])
        lastidx = env.end
        if env.name in handlers:
            newblocks.extend(handlers[env.name](tex, env))
        else:
            newblocks.append('<div class="{}">'.format(env.name))
            newblocks.extend(process_environments_recursively(env.content))
            newblocks.append('</div><!-- {} -->'.format(env.name))
        env = get_generic_environment(b, lastidx)
    newblocks.append(b[lastidx:])
    return newblocks

def process_passthroughs(b, env):
    blocks = [r'\begin{{{}}}'.format(env.name)]
    blocks.extend(process_environments_recursively(env.content))
    blocks.append(r'\end{{{}}}'.format(env.name))
    return blocks

def process_display_math(b, env):
    blocks = process_passthroughs(b, env)
    return process_math_hashes("".join(blocks))

def process_inline_math(b, env):
    return '\(' + process_math_hashes(env.content) + '\)'

def preprocess_hashes(subtex):
    blocks = list()
    rx = re.compile('#([^#]*)#', re.M|re.S)
    lastidx = 0
    for m in rx.finditer(subtex):
        blocks.append(subtex[lastidx:m.start()])
        lastidx = m.end()
        blocks.append(re.sub(r'(^|[^\\])%', r'\1\%', m.group(0)))
    blocks.append(subtex[lastidx:])
    return "".join(blocks)

def process_math_hashes(subtex):
    blocks = list()
    rx = re.compile('\0([^\0]*)\0', re.M|re.S)
    lastidx = 0
    for m in rx.finditer(subtex):
        blocks.append(subtex[lastidx:m.start()])
        lastidx = m.end()
        inner = m.group(1)
        inner = re.sub(r'(^|[^\\])&', r'\1\&', inner)
        blocks.append(r'\mathtt{{{}}}'.format(inner))
    blocks.append(subtex[lastidx:])
    return "".join(blocks)

def process_nonmath_hashes(subtex):
    blocks = list()
    rx = re.compile('\0([^\0]*)\0', re.M|re.S)
    lastidx = 0
    for m in rx.finditer(subtex):
        blocks.append(subtex[lastidx:m.start()])
        lastidx = m.end()
        inner = m.group(1)
        inner = re.sub(r'(^|[^\\])&', r'\1\&', inner)
        blocks.append(r'\(\mathtt{{{}}}\)'.format(inner))
    blocks.append(subtex[lastidx:])
    return "".join(blocks)

def process_list(b, env):
    newblocks = list()
    mapper = dict([('itemize', 'ul'), ('enumerate', 'ol'), ('list', 'ul')])
    tag = mapper[env.name]
    newblocks.append('<{} class="{}">'.format(tag, env.name))
    newblocks.extend(process_environments_recursively(process_list_items(env.content)))
    newblocks.append('</li></{}>'.format(tag))
    return newblocks

def process_list_items(b):
    b = re.sub(r'\\item\s+', '\1', b, 1)
    b = re.sub(r'\\item\s+', '\2\1', b)
    b = re.sub(r'\s*' + '\1' + r'\s*', '<li>', b, 0, re.M|re.S)
    b = re.sub(r'\s*' + '\2' + r'\s*', '</li>', b, 0, re.M|re.S)
    return b

def process_tabular(tex, env):
    inner = "".join(process_environments_recursively(env.content))
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


class command(object):
    def __init__(self, name, optargs, args, start, end):
        self.name = name
        self.optargs = optargs
        self.args = args
        self.start = start
        self.end = end

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

def commands(tex):
    rx = re.compile(r'\\(\w+)')
    for m in rx.finditer(tex):
        if m.group(0) != r'\begin':
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
            yield cmd


def process_commands_recursively(tex):
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
                'cite': lambda tex, cmd: ['[{}]'.format(cmd.args[0])]
               }
    worthless = ['newlength', 'setlength', 'addtolength', 'vspace', 'index',
                 'cpponly', 'cppimport', 'pcodeonly', 'pcodeimport', 'qedhere']
    for c in worthless:
        handlers[c] = lambda tex, cmd : ['']
    strip = ['javaonly', 'notpcode']
    for c in strip:
        handlers[c] = strip_command
    newblocks = list()
    lastidx = 0
    for cmd in commands(tex):
        if cmd.name in handlers:
            newblocks.append(tex[lastidx:cmd.start])
            lastidx = cmd.end
            newblocks.extend(handlers[cmd.name](tex, cmd))
    newblocks.append(tex[lastidx:])
    return newblocks

def strip_command(text, cmd):
    return process_commands_recursively(cmd.args[0])

def process_section(text, cmd):
    blocks = ["<h1>"]
    blocks.extend(process_commands_recursively(cmd.args[0]))
    blocks.append("</h1>")
    return blocks

def process_subsection(text, cmd):
    blocks = ["<h2>"]
    blocks.extend(process_commands_recursively(cmd.args[0]))
    blocks.append("</h2>")
    return blocks

def process_subsubsection(text, cmd):
    blocks = ["<h2>"]
    blocks.extend(process_commands_recursively(cmd.args[0]))
    blocks.append("</h2>")
    return blocks

def process_paragraph(text, cmd):
    blocks = ['<div class="paragraph_title">']
    blocks.extend(process_commands_recursively(cmd.args[0]))
    blocks.append("</div><!-- paragraph_title -->")
    return blocks

def process_emph(text, cmd):
    blocks = ["<em>"]
    blocks.extend(process_commands_recursively(cmd.args[0]))
    blocks.append("</em>")
    return blocks

def process_chapter(text, cmd):
    blocks = list()
    blocks.append('<div class="chapter">')
    blocks.extend(process_commands_recursively(cmd.args[0]))
    blocks.append('</div><!-- chapter -->')
    return blocks

def process_caption(text, cmd):
    blocks = ['<div class="caption">']
    blocks.extend(process_commands_recursively(cmd.args[0]))
    blocks.append("</div><!-- caption -->")
    return blocks

def process_graphics(text, cmd):
    return ['<img src="{}.svg"/>'.format(cmd.args[0])]

def process_codeimport(text, cmd):
    blocks = ['<div class="codeimport">']
    blocks.extend(process_commands_recursively(cmd.args[0]))
    blocks.append("</div><!-- codeimport -->")
    return blocks

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

    # replace hashes with nulls because html references contain hashes
    tex = re.sub(r'#', '\0', tex)
    tex = process_labels_and_refs(tex)

    blocks = process_environments_recursively(tex)
    blocks = [process_nonmath_hashes(b) for b in blocks]

    blocks = process_commands_recursively("".join(blocks))
    return "".join(blocks)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        sys.stderr.write("Usage: {} <texfile>\n".format(sys.argv[0]))
        sys.exit(-1)
    filename = sys.argv[1]
    base, ext = os.path.splitext(filename)
    outfile = base + ".html"
    print("Reading from {} and writing to {}".format(filename, outfile))

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
