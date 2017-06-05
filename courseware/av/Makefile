texs = $(wildcard *.tex)
pdfs = $(texs:.tex=.pdf)
images = $(pdfs:.pdf=-images)


all : $(images)

.PHONY: images figs

figs :
	make -C figs


%.pdf : %.tex figs 
	./cpdflatex $<


clean:
	rm -f *-tmp.??? *-tmp.ttex *.log $(pdfs) *.aup *.out *.nav *.aux *~


%-images : %.pdf
	mkdir -p $@
	pdftoppm -scale-to-x 1920 -scale-to-y 1080 -png $< $@/$(@:-images=)

