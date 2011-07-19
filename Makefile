
all:
	(cd java ; make)
	(cd latex ; make)

clean:
	(cd java ; make clean)
	(cd latex ; make clean)

tarball:
	tar czvf ods.tgz java/ods/

install: all tarball
	scp ods.tgz latex/ods.pdf morin@cg.scs.carleton.ca:public_html/ods/


