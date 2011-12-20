
sources = intro.tex arrays.tex linkedlists.tex skiplists.tex \
	hashing.tex binarytrees.tex rbs.tex scapegoat.tex redblack.tex \
	heaps.tex sorting.tex
javs = $(sources:.tex=-java.tex)
cpps = $(sources:.tex=-cpp.tex)
java = $(wildcard ../java/ods/*.java)
cppsource = $(wildcard ../cpp/*.h)
pdfs = ods-java.pdf ods-cpp.pdf

LFLAGS=-interaction=nonstopmode -halt-on-error

all: $(pdfs)

.PHONY: all figs images install clean

figs:
	cd figs ; make

images:
	cd images ; make

ods-java.pdf : ods-java.tex $(javs) ods.sty images figs ods-java.bbl
	pdflatex $(LFLAGS) ods-java
	pdflatex $(LFLAGS) ods-java

ods-java.tex : ods.tex Makefile
	sed 's/-lang/-java/' ods.tex > ods-java.tex
	sed -i 's/%HEADCOMMAND/\\newcommand{\\cpponly}[1]{}\\newcommand{\\javaonly}[1]{#1}\\newcommand{\\lang}{Java}/' ods-java.tex

ods-java.bbl : ods.bib odsproc.bib
	pdflatex $(LFLAGS) ods-java
	bibtex ods-java

ods-cpp.pdf : ods-cpp.tex $(cpps) ods.sty images figs ods-cpp.bbl
	pdflatex $(LFLAGS) ods-cpp
	pdflatex $(LFLAGS) ods-cpp

ods-cpp.tex : ods.tex Makefile
	sed 's/-lang/-cpp/' ods.tex > ods-cpp.tex
	sed -i 's/%HEADCOMMAND/\\newcommand{\\javaonly}[1]{}\\newcommand{\\cpponly}[1]{#1}\\newcommand{\\lang}{C++}/' ods-cpp.tex

ods-cpp.bbl : ods.bib odsproc.bib 
	pdflatex $(LFLAGS) ods-cpp
	bibtex ods-cpp

%-java.tex : %.tex $(java) snarf-java.pl Makefile
	cat $< | ./snarf-java.pl > $*-java.tex 

%-cpp.tex : %.tex $(cppsource) snarf-cpp.pl Makefile
	cat $< | ./snarf-cpp.pl > $*-cpp.tex 

install : ods-java.pdf ods-cpp.pdf ods-java-grayscale.pdf ods-cpp-grayscale.pdf
	scp ods-java.pdf ods-java-grayscale.pdf ods-cpp.pdf ods-cpp-grayscale.pdf morin@cg.scs.carleton.ca:public_html/ods/

clean:
	rm -f $(javs) $(cpps) *.log *.bbl *.out ods.toc $(pdfs) 
	cd figs; make clean
	cd images; make clean

# A dirty hack for getting a grayscale file
%-grayscale.pdf : %.pdf
	gs -sOutputFile=$@ -sDEVICE=pdfwrite \
	-sColorConversionStrategy=Gray -dProcessColorModel=/DeviceGray \
	-dCompatibilityLevel=1.4 $< < /dev/null

