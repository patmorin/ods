sources=$(wildcard ../figs/*.ipe)
kek=$(notdir $(sources))
kak=$(kek:.ipe=.xml)
pdfs=$(kak:.xml=.pdf)
burstpdfs=$(kak:.xml=-1.pdf)

all : $(kak) $(pdfs) $(burstpdfs)

epss : $(pdfs) $(burstpdfs)
	./makeepss

%.xml : ../figs/%.ipe Makefile snarf-fig.py
	./snarf-fig.py $< > $@
    
%-1.pdf : %.pdf
	pdftk $< burst output $(<:.pdf=-%d.pdf)

%.pdf : %.xml
	ipetoipe -pdf $<

clean :
	rm -f *.xml ./*.pdf ./*.eps

