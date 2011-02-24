
sources = intro.tex arrays.tex linkedlists.tex
temps = $(sources:.tex=-tmp.tex)
java = $(wildcard ../java/ods/*.java)

ods.pdf : $(temps) ods.tex
	pdflatex ods
	pdflatex ods

%-tmp.tex : %.tex $(java) snarf.pl 
	sed -r 's/#([^#]+)#/\\verb#\1#/g;s/(\\begin|\\end)\{code\}/\1{verbatim}/' \
        $< | ./snarf.pl > $*-tmp.tex 
	
clean:
	rm -f *-tmp.tex *.log *.bbl ods.pdf

