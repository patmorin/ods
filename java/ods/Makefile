
sources=$(wildcard *.java)

classes=$(sources:.java=.class)

ods.jar:  $(classes)
	jar cvf ssCC.jar *.class

%.class : %.java
	javac $< 

clean :
	rm -f ssCC.jar *.class

