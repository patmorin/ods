# A one-off utility for getting an ipe colour stylesheet
from __future__ import division

import re

print("""<?xml version="1.0"?>
<!DOCTYPE ipestyle SYSTEM "ipe.dtd">
<ipestyle name="odscolors">
""")

for line in open('colorlist.html').read().splitlines():
    m =  re.match('<div style="background-color:#(\w+);">(.*)<\/div>', line)
    if m:
        htmlcode = m.group(1) 
        name = m.group(2).lower()
        rgb = [int(htmlcode[i:i+2], 16)/255 for i in [0, 2, 4]]
        fmt = '<color name="{}" value="{} {} {}"/>'
        print(fmt.format(name, rgb[0], rgb[1], rgb[2]))
print("</ipestyle>")
