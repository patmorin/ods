sources=$(wildcard *.ipe)

grayscales=$(sources:.ipe=.gipe)
pdfs=$(sources:.ipe=.pdf) 
gspdfs=$(grayscales:.gipe=-gs.pdf) 
burstpdfs=$(sources:.ipe=-1.pdf) $(grayscales:.gipe=-1-gs.pdf) 

all: $(pdfs) $(gspdfs) $(burstpdfs) 

%-1.pdf : %.pdf
	pdftk $< burst output $(<:.pdf=-%d.pdf)

%-1-gs.pdf : %-gs.pdf
	pdftk $< burst output $(<:-gs.pdf=-%d-gs.pdf)


%.pdf : %.ipe
	ipetoipe -pdf $< $@

%-gs.pdf : %.gipe
	ipetoipe -pdf $< $@

%.gipe : %.ipe
	sed -r 's/fill="(white|black|\w*gray)"/xxll=\"\1\"/g;s/fill="[a-z]+"/fill=\"lightgray\"/g;s/xxll/fill/g' < $< > $@
	sed -r -i 's/stroke="(white|black|\w*gray)"/xxroke=\"\1\"/g;s/stroke="[a-z]+"/stroke=\"black\"/g;s/xxroke/stroke/g' $@

clean :
	rm -f ./*.pdf $(grayscales)
