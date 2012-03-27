
sources = intro.tex arrays.tex linkedlists.tex skiplists.tex \
	hashing.tex binarytrees.tex rbs.tex scapegoat.tex redblack.tex \
	heaps.tex sorting.tex graphs.tex integers.tex
javs = $(sources:.tex=-java.tex)
javh = $(sources:.tex=-java-html.tex)
cpps = $(sources:.tex=-cpp.tex)
cpph = $(sources:.tex=-cpp-html.tex)
java = $(wildcard ../java/ods/*.java)
cppsource = $(wildcard ../cpp/*.h)
pdfs = ods-java.pdf ods-cpp.pdf

LFLAGS=-interaction=nonstopmode -halt-on-error

all: $(pdfs)

.PHONY: all figs images install clean

figs:
	cd figs ; make

epsfigs:
	cd figs ; make epss

images:
	cd images ; make

ods-java.pdf : ods-java.tex $(javs) ods.sty images figs
	pdflatex $(LFLAGS) ods-java
	bibtex ods-java
	pdflatex $(LFLAGS) ods-java
	pdflatex $(LFLAGS) ods-java

ods-java.tex : ods.tex Makefile
	sed 's/-lang/-java/' ods.tex > ods-java.tex
	sed -i 's/%HEADCOMMAND/\\newcommand{\\cpponly}[1]{}\\newcommand{\\javaonly}[1]{#1}\\newcommand{\\lang}{Java}/' ods-java.tex

ods-cpp.pdf : ods-cpp.tex $(cpps) ods.sty images figs
	pdflatex $(LFLAGS) ods-cpp
	bibtex ods-cpp
	pdflatex $(LFLAGS) ods-cpp
	pdflatex $(LFLAGS) ods-cpp

ods-cpp.tex : ods.tex Makefile
	sed 's/-lang/-cpp/' ods.tex > ods-cpp.tex
	sed -i 's/%HEADCOMMAND/\\newcommand{\\javaonly}[1]{}\\newcommand{\\cpponly}[1]{#1}\\newcommand{\\lang}{C++}/' ods-cpp.tex

%-java.tex : %.tex $(java) snarf-java.pl Makefile
	cat $< | ./snarf-java.pl > $*-java.tex 

%-cpp.tex : %.tex $(cppsource) snarf-cpp.pl Makefile
	cat $< | ./snarf-cpp.pl > $*-cpp.tex 

install-pdf : ods-java.pdf ods-cpp.pdf ods-java-grayscale.pdf ods-cpp-grayscale.pdf 
	scp -r ods-java.pdf ods-java-grayscale.pdf ods-cpp.pdf ods-cpp-grayscale.pdf ods-book.css morin@cg.scs.carleton.ca:public_html/ods/

install : ods-java.pdf ods-cpp.pdf ods-java-grayscale.pdf ods-cpp-grayscale.pdf java-html cpp-html
	scp -r  ods-java/ ods-java.pdf ods-java-grayscale.pdf ods-cpp/ ods-cpp.pdf ods-cpp-grayscale.pdf ods-book.css morin@cg.scs.carleton.ca:public_html/ods/

clean:
	rm -f $(javs) $(cpps) $(javh) $(cpph) *.log *.bbl *.out ods.toc $(pdfs) 
	cd figs; make clean
	cd images; make clean

# A dirty hack for getting a grayscale file
%-grayscale.pdf : %.pdf
	gs -sOutputFile=$@ -sDEVICE=pdfwrite \
	-sColorConversionStrategy=Gray -dProcessColorModel=/DeviceGray \
	-dCompatibilityLevel=1.4 $< < /dev/null

# Lots of hackery for generating html using latex2html
html: java-html cpp-html

java-html : $(javh) ods-java-html.tex
	mkdir -p ods-java
	pdflatex ods-java-html
	bibtex ods-java-html
	latex2html -init_file latex2html-init -dir ods-java ods-java-html
	
ods-java-html.tex : ods.tex Makefile
	sed 's/-lang/-java-html/' ods.tex > ods-java-html.tex
	sed -i 's/%HEADCOMMAND/\\newcommand{\\cpponly}[1]{}\\newcommand{\\javaonly}[1]{#1}\\newcommand{\\lang}{Java}/' ods-java-html.tex
	sed -i 's/\\include{cpp-preface}//' ods-java-html.tex

%-java-html.tex : %.tex $(java) snarf-java-html.pl Makefile
	cat $< | ./snarf-java-html.pl > $*-java-html.tex 

cpp-html : $(cpph) ods-cpp-html.tex
	mkdir -p ods-cpp
	pdflatex ods-cpp-html
	bibtex ods-cpp-html
	latex2html -init_file latex2html-init -dir ods-cpp ods-cpp-html
	
ods-cpp-html.tex : ods.tex Makefile
	sed 's/-lang/-cpp-html/' ods.tex > ods-cpp-html.tex
	sed -i 's/%HEADCOMMAND/\\newcommand{\\javaonly}[1]{}\\newcommand{\\cpponly}[1]{#1}\\newcommand{\\lang}{C++}/' ods-cpp-html.tex

%-cpp-html.tex : %.tex $(cpp) snarf-cpp-html.pl Makefile
	cat $< | ./snarf-cpp-html.pl > $*-cpp-html.tex 


