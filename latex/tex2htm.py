#!/usr/bin/python3
import re
import markdown


def get_arg(s, i):
    """Match the brace starting at position i-1 with its corresponding brace"""
    if s[i] == '[':
        while s[i] != ']': i += 1
        i += 1
    depth = 0
    for j in range(i, len(s)):
        if s[j] == '{': depth += 1
        if s[j] == '}': depth -= 1
        if depth == 0: return i+1, j
    return i+1, len(s)

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

def split_paragraphs(tex):
    """Identify paragraph breaks"""
    lines = [line.strip() for line in tex.splitlines()]
    out=''
    for i in range(len(lines)-1):
        if lines[i] == '' and lines[i+1] != '':
            lines[i] += "<p>"
    return "\n".join(lines)

def preprocess_hashes(tex):
    """Preprocess hashes

    Right now all we do is protect % inside hashes
    """
    pattern = r'#(.*?)#'
    m = re.search(pattern, tex, re.M|re.S)
    while m:
        replacement = '\0' + re.sub('%', r'\%', m.group(1)) + '\1'
        tex = tex.replace(m.group(0), replacement)
        m = re.search(pattern, tex, re.M|re.S)
    return re.sub('[\0\1]', '#', tex)

def process_hashes(tex):
    """Process the hashes

    Call this after hashes within math environments have already been dealt with
    """
    pattern = r'#(.*?)#'
    m = re.search(pattern, tex, re.M|re.S)
    while m:
        code = ":::java\n" + m.group(1)
        code = markdown.markdown(code, ['codehilite'])
        replacement = '\0' + code + '\1'
        code = "$\mathtt{" + m.group(1) + "}$"
        tex = tex.replace(m.group(0), code)
        m = re.search(pattern, tex, re.M|re.S)
    return re.sub('[\0\1]', ' ', tex)

def process_inline_formulae(tex):
    pattern = r'\$(.*?)\$'
    m = re.search(pattern, tex, re.M|re.S)
    while m:
        inner = re.sub(r'#(.*?)#', r'\\mathtt{\1}', m.group(1))
        replacement = '\0' + inner + '\1'
        tex = tex.replace(m.group(0), replacement)
        m = re.search(pattern, tex, re.M|re.S)
    tex = re.sub('\0', r'\(', tex)
    return re.sub('\1', r'\)', tex)

def process_quotes(tex):
    return re.sub(r"``(.*?)''", r'<q>\1</q>', tex, 0, re.M|re.S)

def process_display_formulae2(tex, begin, end):
    pattern = re.escape(begin) + r'(.*?)' + re.escape(end)
    m = re.search(pattern, tex, re.M|re.S)
    while m:
        inner = re.sub(r'#(.*?)#', r'\\mathtt{\1}', m.group(1))
        replacement = '\0' + inner + '\1'
        tex = tex.replace(m.group(0), replacement)
        m = re.search(pattern, tex, re.M|re.S)
    tex = re.sub('\0', begin, tex)
    return re.sub('\1', end, tex)

def process_display_formulae(tex):
    tex = process_display_formulae2(tex, r'\[', r'\]')
    tex = process_display_formulae2(tex, r'\begin{align*}', r'\end{align*}')
    tex = process_display_formulae2(tex, r'\begin{eqnarray*}', r'\end{eqnarray*}')
    return tex

def strip_comments(tex):
    lines = tex.splitlines()
    for i in range(len(lines)):
        lines[i] = re.sub(r'(^|[^\\])\%.*$', r'\1', lines[i])
    return "\n".join(lines)

def process_tabulars(tex):
    pattern = r'\\begin{tabular}{.*?}(.*?)\\end{tabular}'
    m = re.search(pattern, tex, re.M|re.S)
    while m:
        i = m.start()
        j = m.end()
        print(m.group(1))
        rows = re.split(r'\\\\', m.group(1))
        rows = [re.split(r'\&', r) for r in rows]
        table = '<table align="center">'
        for r in rows:
            table += '<tr>'
            for c in r:
                table += '<td>' + c + '</td>'
            table += '</tr>'
        table += '</table>'

        print(rows)
        tex = tex[:i] + table + tex[j:]
        m = re.match(pattern, tex, re.MULTILINE | re.DOTALL)
    return tex

def process_figures(tex):
    pattern = r'\\begin{figure}(.*?)\\end{figure}'
    replacement = r'<div class="figure">\1</div>'
    return re.sub(pattern, replacement, tex, 0, re.MULTILINE | re.DOTALL)

def process_command(tex, cmd, htmlhead, htmltail):
    pattern = '\\' + cmd
    i = tex.find(pattern)
    while i >= 0:
        j = i+len(pattern)
        j, k = get_arg(tex, j)
        arg = tex[j:k]
        replacement = htmlhead + arg + htmltail
        tex = tex[:i] + replacement + tex[k+1:]
        i = tex.find(pattern)
    return tex

def process_captions(tex):
    cmd = 'caption'
    htmlhead = '<div class="caption">'
    htmltail = '</div>'
    return process_command(tex, cmd, htmlhead, htmltail)

def process_emphasis(tex):
    cmd = 'emph'
    htmlhead = '<em>'
    htmltail = '</em>'
    return process_command(tex, cmd, htmlhead, htmltail)

def process_headings(tex):
    chapter = "None"  # FIXME
    cmd = 'chapter'
    htmlhead = '<div class="chapter">'
    htmltail = '</div>'
    tex = process_command(tex, cmd, htmlhead, htmltail)
    for i in range(4):
        cmd = ('sub'*i) + 'section'
        htmlhead = '<h{}>'.format(i+1)
        htmltail = '</h{}>'.format(i+1)
        tex = process_command(tex, cmd, htmlhead, htmltail)
    return chapter, tex

def process_centers(tex):
    pattern = r'\\begin{center}(.*?)\\end{center}'
    replacement = r'<div class="center">\1</div>'
    return re.sub(pattern, replacement, tex, 0, re.M|re.S)

def process_proofs(tex):
    pattern = r'\\begin{proof}(.*?)\\end{proof}'
    replacement = r'<div class="proof">\1</div>'
    return re.sub(pattern, replacement, tex, 0, re.M|re.S)

def process_theorem_like(tex, env, name):
    pattern = r'\\begin{{{}}}(.*?)\\end{{{}}}'.format(env, env)
    replacement = r'<div class="{}">\1</div>'.format(env)
    return re.sub(pattern, replacement, tex, 0, re.M|re.S)

def process_dontcares(tex):
    dontcare=r'(hline|vspace|hspace|index|hspace|setlength|newlength|addtolength)'
    pattern = r'\\' + dontcare + r'({.+?})*'
    return re.sub(pattern, '', tex, 0, re.M|re.S)

def process_lists(tex):
    tex = re.sub(r'\\begin{itemize}', '<ul>', tex)
    tex = re.sub(r'\\end{itemize}', '\1</ul>', tex)
    tex = re.sub(r'\\begin{enumerate}', '<ol>', tex)
    tex = re.sub(r'\\end{enumerate}', '\1</ol>', tex)
    tex = re.sub(r'\\item', '\0', tex)
    tex = re.sub('\0([^\0\1]*)', r'<li>\1</li>', tex, 0, re.M|re.S)
    return tex

def process_references(tex):
    tex = re.sub(r'\\(\w+)label{(.*?)}', '', tex)
    return re.sub(r'\\(\w+)ref{(.*?)}', '**REF**', tex)

def process_imports(tex):
    pattern = r'\\(cpp|java|code|pcode)import{([^}]+)}'
    replacement = r'<div class="import">\\\1import{\2}</div>'
    return re.sub(pattern, replacement, tex)

def process_onlies(tex):
    langs=r'(cpp|java|pcode)'
    pattern = r'\\' + langs + 'only{.*?}' + r'({.+?})*'
    return re.sub(pattern, '', tex, 0, re.M|re.S)

def process_graphics(tex):
    pattern = r'\\includegraphics(\[.*?\]){(.*?)}'
    replacement = r'<img scale="120%" src="\2.svg"/>'
    return re.sub(pattern, replacement, tex)


def tex2html(tex):
    # The ordering here is important
    tex = preprocess_hashes(tex)
    tex = strip_comments(tex)
    tex = process_dontcares(tex)
    tex = process_onlies(tex)  # danger --- this needs to be better
    chapter, tex = process_headings(tex)
    tex = process_inline_formulae(tex)
    tex = process_display_formulae(tex)
    tex = process_hashes(tex)
    tex = process_quotes(tex)

    tex = process_tabulars(tex)
    tex = process_figures(tex)
    tex = process_captions(tex)
    tex = process_centers(tex)
    tex = process_theorem_like(tex, 'thm', 'Theorem')
    tex = process_theorem_like(tex, 'lem', 'Lemma')
    tex = process_proofs(tex)
    tex = process_emphasis(tex)
    tex = process_lists(tex)
    tex = process_imports(tex)

    tex = process_graphics(tex)
    tex = process_references(tex)
    tex = split_paragraphs(tex)
    return chapter, tex



if __name__ == "__main__":
    # Read and translate the input
    tex = open('arrays-test.tex').read()
    chapter, htm = tex2html(tex)

    # Write the output
    (head, tail) = re.split('CONTENT', open('head.htm').read())
    head = re.sub('TITLE', chapter, head)

    of = open('arrays.html', 'w')
    of.write(head)
    of.write(htm)
    of.write(tail)
    of.close()
