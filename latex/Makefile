
sources = arrays.tex
temps = $(sources:.tex=-tmp.tex)

ods.pdf : $(temps) ods.tex
	pdflatex ods

%-tmp.tex : %.tex
	sed -r 's/#([^#]+)#/\\verb#\1#/g;s/(\\begin|\\end)\{code\}/\1{verbatim}/' \
        $< | ./snarf.pl > $*-tmp.tex 
	
clean:
	rm -f *-tmp.tex *.log *.bbl ods.pdf

