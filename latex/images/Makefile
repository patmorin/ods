svgs=$(wildcard *.svg)
pdfs=$(svgs:.svg=.pdf)
epss=$(svgs:.svg=.eps)


all: $(pdfs) $(epss) bigoh.tex

bigoh.tex : bigoh.gp
	gnuplot $<

%.pdf : %.svg
	inkscape --export-pdf=$@ $<	

%.eps : %.svg
	inkscape --export-eps=$@ $<	

clean:
	rm -f $(pdfs)

