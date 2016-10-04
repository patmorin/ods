#!/usr/bin/python3
import re
import markdown

def strip_tex_comments(tex):
    """Strip comments from a latex file"""
    return re.sub(r'([^\\])%.*$', r'\1', tex)

def merge_lines(tex):
    """Merge lines in a latex file"""
    lines = [line.strip() for line in tex.splitlines()]
    out=''
    for i in range(len(lines)-1):
        if lines[i] == '' or lines[i+1] == '':
            sep = '\n'
        else:
            sep = ' '
        out += lines[i] + sep
    return out


def tex2md(tex):
    md = tex
    # Lists
    md = re.sub(r'\\begin{itemize}', '', md)
    md = re.sub(r'\\end{itemize}', '', md)
    md = re.sub(r'\\begin{enumerate}', '', md)
    md = re.sub(r'\\end{enumerate}', '', md)

    md = merge_lines(strip_tex_comments(md))

    md = merge_lines(md)
    # Strip \addtolength, \setlength, etc..
    md = re.sub(r'\\\w+length({[^}]+})+', '', md)

    # More commands we don't care about
    dontcare=r'(vspace|hspace|index)'
    regex = r'\\' + dontcare + r'({[^}]+})+'
    md = re.sub(regex, '', md)

    # List items
    md = re.sub(r'\\item', '*', md)

    # Emphasis (italics)
    md = re.sub(r'\\emph{([^}]+)}', '*\1*', md)

    # Formulae
    # For now, prevent interactions between formula and code
    # TODO: Improve this
    regex = r'\$([^\$]+)\$'
    m = re.search(regex, md)
    while m:
        txt = m.group(1)
        txt2 = re.sub('#', '', txt)
        txt2 = '~~~' + txt2 + '~~~'
        md = md.replace(m.group(0), txt2)
        m = re.search(regex, md)
    md = re.sub(r'~~~', r'$', md)

    md = re.sub(r'\\begin{proof}', '*Proof:*', md)
    me = re.sub(r'\\end{proof}', 'QED', md)

    # Code snippets
    md = re.sub(r'#([^#]+)#', r'`\1`', md)

    # Chapter and section headings
    md = re.sub(r'\\chapter{([^}]+)}', r'# \1', md)
    md = re.sub(r'\\section{([^}]+)}', r'## \1', md)
    md = re.sub(r'\\subsection{([^}]+)}', r'### \1', md)
    md = re.sub(r'\\subsubsection{([^}]+)}', r'#### \1', md)

    # TODO: Handle labels and references -- for now, just kill them
    md = re.sub(r'\\(\w+)label{(\w+)}', '', md)
    md = re.sub(r'\\(\w+)ref{(\w+)}', '**REF**', md)

    # TODO: Handle code imports
    # regex = r'\\codeimport{ods/(\w+)\.}

    return md



if __name__ == "__main__":
    tex = open('arrays.tex').read()
    md = tex2md(tex)

    of = open('arrays.md', 'w')
    of.write(md)
    of.close()

    htm = markdown.markdown(md, ['codehilite'])
    (head, tail) = re.split('CONTENT', open('head.htm').read())
    of = open('arrays.html', 'w')
    of.write(head)
    of.write(htm)
    of.write(tail)
    of.close()
