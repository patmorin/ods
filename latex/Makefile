
sources = intro.tex arrays.tex linkedlists.tex skiplists.tex hashing.tex binarytrees.tex rbs.tex scapegoat.tex redblack.tex heaps.tex
temps = $(sources:.tex=-tmp.tex)
java = $(wildcard ../java/ods/*.java)

LFLAGS=-interaction=nonstopmode -halt-on-error

all: ods.pdf

.PHONY: all figs images install clean

figs:
	cd figs ; make

images:
	cd images ; make

ods.pdf : figs images $(temps) ods.tex ods.sty ods.bbl
	pdflatex $(LFLAGS) ods
	pdflatex $(LFLAGS) ods


ods.bbl : figs images ods.bib
	pdflatex $(LFLAGS) ods
	bibtex ods

%-tmp.tex : %.tex $(java) snarf.pl Makefile
	cat $< | ./snarf.pl > $*-tmp.tex 

install : ods.pdf ods-grayscale.pdf
	scp ods.pdf ods-grayscale.pdf morin@cg.scs.carleton.ca:public_html/ods/

clean:
	rm -f $(temps) *.log *.bbl *.out ods.toc ods.pdf 
	cd figs; make clean
	cd images; make clean

# A dirty hack for getting a grayscale file
ods-grayscale.pdf : ods.pdf
	gs -sOutputFile=$@ -sDEVICE=pdfwrite \
	-sColorConversionStrategy=Gray -dProcessColorModel=/DeviceGray \
	-dCompatibilityLevel=1.4 $< < /dev/null
