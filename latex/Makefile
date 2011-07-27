
sources = intro.tex arrays.tex linkedlists.tex skiplists.tex hashing.tex binarytrees.tex rbs.tex scapegoat.tex redblack.tex heaps.tex 
temps = $(sources:.tex=-tmp.tex)
java = $(wildcard ../java/ods/*.java)

LFLAGS=-interaction=nonstopmode -halt-on-error

ods.pdf : $(temps) ods.tex figs images ods.sty ods.bbl
	echo "hello world"
	(cd figs ; make )
	(cd images ; make )
	pdflatex $(LFLAGS) ods
	pdflatex $(LFLAGS) ods


ods.bbl : ods.bib
	pdflatex $(LFLAGS) ods
	bibtex ods

%-tmp.tex : %.tex $(java) snarf.pl Makefile
	cat $< | ./snarf.pl > $*-tmp.tex 

install : ods.pdf ods-grayscale.pdf
	scp ods.pdf ods-grayscale.pdf morin@cg.scs.carleton.ca:public_html/ods/
	
clean:
	rm -f $(temps) *.log *.bbl *.out ods.pdf 
	(cd figs; make clean)
	(cd images; make clean)

# A dirty hack for getting a grayscale file
ods-grayscale.pdf : ods.pdf
	gs -sOutputFile=$@ -sDEVICE=pdfwrite \
	-sColorConversionStrategy=Gray -dProcessColorModel=/DeviceGray \
	-dCompatibilityLevel=1.4 $< < /dev/null


