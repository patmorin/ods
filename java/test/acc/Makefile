CLASSPATH=.:${shell cd ../..; pwd}:/usr/share/java/junit.jar

SRCS=${shell find ods -name '*.java'}
OBJS=${SRCS:.java=.class}
CLASSES=${SRCS:.java=}

IGNORETESTS= ./ods/TestAll

CLEANFILES=${shell find . -name '*.class'}

JAVAC=javac
JCFLAGS=-classpath ${CLASSPATH} -Xlint -g
JAVA=java
JFLAGS=-classpath ${CLASSPATH} -ea

.SUFFIXES: .java .class
.PHONY: all build clean test

.java.class:
	${JAVAC} ${JCFLAGS} $<

all: test

build: ${OBJS}

test: build
	for test in ${CLASSES} ; do \
		if basename $${test} | grep -q '^Abstract' ; then \
			continue ; \
		fi ; \
		for ignore in ${IGNORETESTS} ; do \
			if [ "$${ignore}" = "$${test}" ] ; then \
			continue 2 ; \
			fi ; \
		done ; \
		echo $${test} ; \
		${JAVA} ${JFLAGS} $${test} ; \
		if [ "$$?" -ne "0" ] ; then exit 1 ; fi ; \
	done

clean:
	rm -f ${subst $$,\$$,${CLEANFILES}}
