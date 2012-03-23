svgs=$(wildcard *.svg)
pdfs=$(svgs:.svg=.pdf)
epss=$(svgs:.svg=.eps)


all: $(pdfs) $(epss)


%.pdf : %.svg
	inkscape --export-pdf=$@ $<	

%.eps : %.svg
	inkscape --export-eps=$@ $<	

clean:
	rm -f $(pdfs)

