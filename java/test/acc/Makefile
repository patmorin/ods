CLASSPATH=.:${shell cd ../..; pwd}:/usr/share/java/junit.jar

SRCS-ods=${shell find ods -name '*.java'}
OBJS-ods=${SRCS-ods:.java=.class}
CLASSES-ods=${SRCS-ods:.java=}

SRCS-acc=org/apache/commons/collections/collection/AbstractTestCollection.java \
	org/apache/commons/collections/BulkTest.java \
	org/apache/commons/collections/set/AbstractTestSet.java \
	org/apache/commons/collections/set/AbstractTestSortedSet.java \
	org/apache/commons/collections/AbstractTestObject.java \
	org/apache/commons/collections/iterators/AbstractTestListIterator.java \
	org/apache/commons/collections/iterators/AbstractTestIterator.java \
	org/apache/commons/collections/BulkTestSuiteMaker.java \
	org/apache/commons/collections/list/AbstractTestList.java
OBJS-acc=${SRCS-acc:.java=.class}
CLASSES-acc=${SRCS-acc:.java=}

RUNTESTS ?= ${CLASSES-ods}
IGNORETESTS ?= ./ods/TestAll

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

build: ${OBJS-acc} ${OBJS-ods}

test: build
	for test in ${RUNTESTS} ; do \
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
