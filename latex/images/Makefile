svgs=$(wildcard *.svg)
pdfs=$(svgs:.svg=.pdf)


all: $(pdfs)

%.pdf : %.svg
	inkscape --export-pdf=$@ $<	

clean:
	rm -f $(pdfs)

