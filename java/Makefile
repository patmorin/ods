
sources=$(wildcard ods/*.java)

classes=$(sources:.java=.class)

ods.jar:  $(classes)
	jar cvf ods.jar ods/*.class

%.class : %.java
	javac -Xlint $< 

clean :
	rm -f ods.jar ods/*.class

