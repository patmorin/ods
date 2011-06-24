
all:
	(cd java ; make)
	(cd latex ; make)

clean:
	(cd java ; make clean)
	(cd latex ; make clean)


