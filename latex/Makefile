
sources = intro.tex arrays.tex linkedlists.tex skiplists.tex hashing.tex binarytrees.tex rbs.tex scapegoat.tex 24rb.tex heaps.tex
temps = $(sources:.tex=-tmp.tex)
java = $(wildcard ../java/ods/*.java)

ods.pdf : $(temps) ods.tex figs ods.sty figs
	(cd figs ; make )
	pdflatex -interaction=nonstopmode ods
	pdflatex -interaction=nonstopmode ods

%-tmp.tex : %.tex $(java) snarf.pl Makefile
	cat $< | ./snarf.pl > $*-tmp.tex 

install : ods.pdf
	scp ods.pdf morin@cg.scs.carleton.ca:public_html/ods/
	
clean:
	rm -f $(temps) *.log *.bbl ods.pdf
	(cd figs; make clean)

