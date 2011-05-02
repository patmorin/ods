
sources=$(wildcard *.ipe)

pdfs=$(sources:.ipe=.pdf)
epss=$(sources:.ipe=.eps)


all: $(pdfs) $(epss)

%.pdf : %.ipe
	ipetoipe -pdf $<

%.eps : %.ipe
	ipetoipe -eps $<


