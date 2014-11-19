#!/usr/bin/python

import os
import re
import xml.etree.ElementTree as ET


def process_file(filename):
    """Process basename+'.xml' to create basename+'.html'"""

    basename = filename.rstrip('.xml')
    infile = basename + ".xml"
    outfile = basename + ".html"
    tree = ET.parse(infile)
    root = tree.getroot()

    assert(root.tag == 'coursepage')
    pagetitle = root.attrib['title'].strip()
    css = root.find('./css').text.strip()
    title = root.find('./title').text.strip()
    subtitle = root.find('./subtitle').text.strip()
    preface = root.find('./preface').text.strip()
    youtube_id = root.find('./video').attrib['ytid']
    epilogue = root.find('./epilogue').text.strip()
    exercises = [e.text for e in root.findall('.exercises/exercise')]
    exercises = "".join(["<div class='exercise'>" + e + "</div>" 
                            for e in exercises])
    js_head = root.find('./js-head').text.strip()
    js_tail = root.find('./js-tail').text.strip()

    subs = {'pagetitle': pagetitle,
            'css': css,
            'title': title,
            'subtitle': subtitle,
            'preface': preface,
            'ytid': youtube_id,
            'epilogue': epilogue,
            'js_head': js_head,
            'js_tail': js_tail,
            'exercises': exercises }

    of = open(outfile, 'w')
    with open('template.htm') as f:
        for line in f:
            match = re.search(r'#(\w+)#', line)
            while match:
                key = match.group(1)
                if key in subs:
                    replacement = subs[key]
                else:
                    replacement = '<!--??{}??-->'.format(key)
                line = re.sub(r'#{}#'.format(key), replacement, line)
                match = re.search(r'#(\w+)#', line)
            of.write(line)

if __name__ == "__main__":
    dir = '.'
    xmlfiles = [ f for f in os.listdir(dir) 
                    if os.path.isfile(os.path.join(dir, f)) 
                        and f.endswith('.xml') ]
    for f in xmlfiles:
        process_file(f)





