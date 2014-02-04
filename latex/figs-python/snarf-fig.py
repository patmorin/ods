#!/usr/bin/python
import sys
import re


if __name__ == "__main__":
    lines = open(sys.argv[1]).readlines()
    for line in lines:
        line = re.sub(r'{\\color{var}', r'\mathit{', line)
        line = re.sub(r'\\mathtt', r'\mathrm', line)
        line = re.sub(r'\\texttt', r'\\textrm', line)
        line = re.sub(r'(\\mathrm{[a-z_]+)([A-Z]+)', 
                    lambda m: m.group(1) + '\_' + m.group(2).lower(), line)
        line = re.sub(r'(\\mathit{\w+)(\d})', r'\1_\2', line)	
        print line,
