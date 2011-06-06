
sources=$(wildcard *.ipe)

pdfs=$(sources:.ipe=.pdf)
burstpdfs=$(sources:.ipe=-1.pdf)

all: $(pdfs) $(burstpdfs)

%-1.pdf : %.ipe
	./burstit $<

%.pdf : %.ipe
	ipetoipe -pdf $<

