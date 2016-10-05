sources=$(wildcard *.ipe)

pdfs=$(sources:.ipe=.pdf)
burstpdfs=$(sources:.ipe=-1.pdf)
svgs=$(sources:.ipe=.svg)

all: $(pdfs) $(burstpdfs) $(svgs)

epss: $(pdfs) $(burstpdfs)
	./makeepss

%-1.pdf : %.pdf
	pdftk $< burst output $(<:.pdf=-%d.pdf)

%.pdf : %.ipe
	ipetoipe -pdf -export $<

%.svg : %.ipe
	./ipetosvgs.py $<

clean :
	rm -f ./*.pdf ./*.svg ./*.eps

