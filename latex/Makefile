
sources = intro.tex arrays.tex linkedlists.tex skiplists.tex hashing.tex binarytrees.tex rbs.tex scapegoat.tex redblack.tex heaps.tex 
temps = $(sources:.tex=-tmp.tex)
gstemps = $(sources:.tex=-tmp-gs.tex)
java = $(wildcard ../java/ods/*.java)

LFLAGS=-interaction=nonstopmode -halt-on-error

ods.pdf : $(temps) ods.tex figs images ods.sty ods.bbl
	(cd figs ; make )
	(cd images ; make )
	pdflatex $(LFLAGS) ods
	pdflatex $(LFLAGS) ods


ods.bbl : ods.bib
	pdflatex $(LFLAGS) ods
	bibtex ods

%-tmp.tex : %.tex $(java) snarf.pl Makefile
	cat $< | ./snarf.pl > $*-tmp.tex 

install : ods.pdf ods-gs.pdf
	scp ods.pdf ods-gs.pdf morin@cg.scs.carleton.ca:public_html/ods/
	
clean:
	rm -f $(temps) *.log *.bbl *.out ods.pdf ods-gs.pdf
	(cd figs; make clean)
	(cd images; make clean)

# The following stuff is for making a black and white version
ods-gs.pdf : $(gstemps) ods-gs.tex figs images ods-gs.sty ods-gs.bbl
	(cd figs ; make )
	(cd images ; make )
	pdflatex $(LFLAGS) ods-gs
	pdflatex $(LFLAGS) ods-gs

ods-gs.tex : ods.tex
	sed -r 's/\\definecolor\{(\w+)\}\{named\}\{\w+\}/\\definecolor{\1}{named}{Black}/' < $< > $@
	sed -r -i 's/\\include\{(\w+)-tmp\}/\\include{\1-tmp-gs}/g' $@
	sed -r -i 's/\\usepackage\{ods\}/\\usepackage{ods-gs}/g' $@

ods-gs.bbl : ods.bib
	pdflatex $(LFLAGS) ods-gs
	bibtex ods-gs

ods-gs.sty : ods.sty
	sed -r 's/\\includegraphics((\[[^]]*\])?)\{([^}]+)\}/\\includegraphics\1{\3-gs}/g' < $< > $@

%-gs.tex : %.tex
	sed -r 's/\\includegraphics((\[[^]]*\])?)\{([^}]+)\}/\\includegraphics\1{\3-gs}/g' < $< > $@


