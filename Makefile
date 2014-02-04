
all:
	(cd java ; make)
	(cd latex ; make)

clean:
	(cd java ; make clean)
	(cd latex ; make clean)

tarballs:
	tar czvf ods-java.tgz java/ods/
	tar czvf ods-cpp.tgz cpp/
	tar czvf ods-python.tgz python/ods

install: all tarballs
	(cd latex ; make install)
	scp ods-java.tgz ods-cpp.tgz ods-python.tgz morin@cg.scs.carleton.ca:public_html/ods/


