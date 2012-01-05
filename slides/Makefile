
java = $(wildcard ../java/ods/*.java)

LFLAGS=-interaction=nonstopmode -halt-on-error

all: sorting.pdf bigoh.pdf heaps.pdf

%.pdf : %-tmp.tex $(java) snarf.pl Makefile
	pdflatex $(LFLAGS) $<
	mv $*-tmp.pdf $*.pdf
	
.PHONY: all figs images install clean

figs:
	cd figs ; make

images:
	cd images ; make

%-tmp.tex : %.tex $(java) snarf.pl Makefile
	cat $< | ./snarf.pl > $*-tmp.tex 

clean:
	rm -f *-tmp.tex *.log *.bbl *.out ods.toc ods.pdf 
	cd figs; make clean
	cd images; make clean


