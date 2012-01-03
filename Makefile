
all:
	(cd java ; make)
	(cd latex ; make)

clean:
	(cd java ; make clean)
	(cd latex ; make clean)

tarballs:
	tar czvf ods-java.tgz java/ods/
	tar czvf ods-cpp.tgz cpp/

install: all tarballs
	(cd latex ; make install)
	scp ods-java.tgz ods-cpp.tgz morin@cg.scs.carleton.ca:public_html/ods/


