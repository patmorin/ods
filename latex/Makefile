
sources = intro.tex arrays.tex linkedlists.tex skiplists.tex hashing.tex binarytrees.tex rbs.tex scapegoat.tex redblack.tex heaps.tex 
temps = $(sources:.tex=-tmp.tex)
java = $(wildcard ../java/ods/*.java)

LFLAGS=-interaction=nonstopmode -halt-on-error

ods.pdf : $(temps) ods.tex figs ods.sty ods.bbl figs
	(cd figs ; make )
	pdflatex $(LFLAGS) ods
	pdflatex $(LFLAGS) ods

ods.bbl : ods.bib
	pdflatex $(LFLAGS) ods
	bibtex ods

%-tmp.tex : %.tex $(java) snarf.pl Makefile
	cat $< | ./snarf.pl > $*-tmp.tex 

install : ods.pdf
	scp ods.pdf morin@cg.scs.carleton.ca:public_html/ods/
	
clean:
	rm -f $(temps) *.log *.bbl ods.pdf
	(cd figs; make clean)

