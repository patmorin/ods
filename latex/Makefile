
sources = intro.tex arrays.tex linkedlists.tex skiplists.tex hashing.tex binarytrees.tex
temps = $(sources:.tex=-tmp.tex)
java = $(wildcard ../java/ods/*.java)

ods.pdf : $(temps) ods.tex figs ods.sty
	(cd figs ; make )
	pdflatex -interaction=nonstopmode ods
	pdflatex -interaction=nonstopmode ods

ods.dvi : $(temps) ods.tex figs ods.sty
	(cd figs ; make )
	latex ods
	latex ods

%-tmp.tex : %.tex $(java) snarf.pl Makefile
	cat $< | ./snarf.pl > $*-tmp.tex 
	
clean:
	rm -f $(temps) *.log *.bbl ods.pdf

