all: interfaces.pdf


.PHONY: all figs

figs:
	cd figs ; make


%.pdf : %.tex figs images
	./cpdflatex $<


