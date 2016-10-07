#!/bin/python3
import os
import sys
import re


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
    if endpos == -1: endpos = len(tex)
    rx = re.compile(r'\[([^\]]+?)\]', re.M|re.S)
    ma = rx.match(tex, pos, endpos)  # FIXME: Not perfect
    if ma:
        return argument(ma.group(1), ma.start(), ma.end())
    return argument('', pos, pos)

def get_environment(tex, name, pos=0, endpos=-1):
    return get_environment_regex(tex, re.escape(name), pos, endpos)

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

def process_lists(blocks):
    newblocks = list()
    mapper = dict([('itemize', 'ul'), ('enumerate', 'ol'), ('list', 'ul')])
    for b in blocks:
        regex = 'itemize|enumerate|list'
        lastidx = 0
        env = get_environment_regex(b, regex)
        while env:
            newblocks.append(b[lastidx:env.start])
            lastidx = env.end
            tag = mapper[env.name]
            newblocks.append('<{} class="{}">'.format(tag, env.name))
            newblocks.extend(process_lists([env.content]))
            newblocks.append('</li></{}>'.format(tag))
            env = get_environment_regex(b, regex, lastidx)
        newblocks.append(b[lastidx:])
    return newblocks

def process_list_items(blocks):
    newblocks = list()
    for b in blocks:
        b = re.sub(r'\\item\s+', '\0', b, 1)
        b = re.sub(r'\\item\s+', '\1\0', b)
        b = re.sub(r'\s*' + '\0' + r'\s*', '<li>', b, 0, re.M|re.S)
        b = re.sub(r'\s*' + '\1' + r'\s*', '</li>', b, 0, re.M|re.S)
        newblocks.append(b)
    return newblocks

def process_math_hashes(subtex):
    pattern = r'#(.*?)#'
    m = re.search(pattern, subtex, re.M|re.S)
    while m:
        inner = re.sub(r'\&', r'\&', m.group(1))
        inner = r'\mathtt{' + inner + '}'
        subtex = subtex[:m.start()] + inner + subtex[m.end():]
        m = re.search(pattern, subtex, re.M|re.S)
    return re.sub('[\0\1]', ' ', subtex)

def process_display_formulae(blocks):
    envs = ['equation', 'equation*', 'align', 'align*', 'eqnarray*']
    regex = "|".join([re.escape(e) for e in envs])
    newblocks = list()
    for b in blocks:
        b = re.sub(r'\\\[', r'\\begin{equation*}', b)
        b = re.sub(r'\\\]', r'\end{equation*}', b)
        lastidx = 0
        env = get_environment_regex(b, regex)
        while env:
            newblocks.append(b[lastidx:env.start])
            lastidx = env.end
            newblocks.append(process_math_hashes(b[env.start:env.end]))
            env = get_environment_regex(b, regex, lastidx)
        newblocks.append(b[lastidx:])
    return newblocks

def process_easy_environments(blocks):
    envs = ['figure', 'proof', 'center', 'lem', 'thm', 'exc']
    regex = "|".join([re.escape(e) for e in envs])
    newblocks = list()
    for b in blocks:
        lastidx = 0
        env = get_environment_regex(b, regex)
        while env:
            newblocks.append(b[lastidx:env.start])
            lastidx = env.end
            newblocks.append('<div class="{}">'.format(env.name))
            newblocks.extend(process_easy_environments([env.content]))
            newblocks.append('</div><!--{}-->'.format(env.name))
            env = get_environment_regex(b, regex, lastidx)
        newblocks.append(b[lastidx:])
    return newblocks

def process_inline_formulae(blocks):
    newblocks = list()
    rx = re.compile(r'\$(.*?)\$', re.M|re.S)
    lastidx = 0
    for b in blocks:
        for m in rx.finditer(b):
            newblocks.append(b[lastidx:m.start()])
            lastidx = m.end()
            newblocks.append(process_math_hashes(m.group(0)))
        newblocks.append(b[lastidx:])
    return newblocks

def tex2htm(tex):
    blocks = [tex]

    blocks = process_lists(blocks)
    blocks = process_list_items(blocks)

    blocks = process_display_formulae(blocks)
    blocks = process_inline_formulae(blocks)


    blocks = ["".join(blocks)]
    #blocks = process_easy_environments(blocks)

    html = "".join(blocks)
    return html


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
