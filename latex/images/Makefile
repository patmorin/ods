svgs=$(wildcard *.svg)
pdfs=$(svgs:.svg=.pdf)
epss=$(svgs:.svg=.eps)


all: $(pdfs) $(epss) bigoh-1.tex bigoh-2.tex

bigoh-1.tex : bigoh-1.gp
	gnuplot $<

bigoh-2.tex : bigoh-2.gp
	gnuplot $<

%.pdf : %.svg
	inkscape --export-pdf=$@ $<	

%.eps : %.svg
	inkscape --export-eps=$@ $<	

clean:
	rm -f $(pdfs)

