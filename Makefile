
all:
	(cd java ; make)
	(cd latex ; make)

clean:
	(cd java ; make clean)
	(cd latex ; make clean)

tarball:
	tar czvf ods.tgz java/ods/

install: all tarball
	(cd latex ; make install)
	scp ods.tgz morin@cg.scs.carleton.ca:public_html/ods/


