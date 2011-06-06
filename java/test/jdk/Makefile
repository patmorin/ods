CLASSPATH=.:${shell cd ../..; pwd}

SRCS=${shell for dir in `ls` ; do find $${dir} -name '*.java' ; done}
OBJS=${SRCS:.java=.class}
CLASSES=${SRCS:.java=}

IGNORETESTS= \
	AbstractList/CheckForComodification \
	AbstractList/FailFastIterator \
	AbstractList/HasNextAfterException \
	AbstractMap/AbstractMapClone \
	AbstractMap/Equals \
	AbstractMap/SimpleEntries \
	AbstractMap/ToString \
	AbstractSequentialList/AddAll \
	ArrayList/AddAll \
	ArrayList/Bug6533203 \
	ArrayList/IteratorMicroBenchmark \
	ArrayList/RangeCheckMicroBenchmark \
	Collection/BiggernYours \
	Collection/HotPotatoes \
	Collections/AsLifoQueue \
	Collections/BigBinarySearch \
	Collections/BinarySearchNullComparator \
	Collections/CheckedIdentityMap \
	Collections/CheckedListBash \
	Collections/CheckedMapBash \
	Collections/CheckedNull \
	Collections/CheckedSetBash \
	Collections/Disjoint \
	Collections/EmptyCollectionSerialization \
	Collections/Enum \
	Collections/Frequency \
	Collections/MinMax \
	Collections/NCopies \
	Collections/NullComparator \
	Collections/RacingCollections \
	Collections/ReverseOrder \
	Collections/RotateEmpty \
	Collections/Ser \
	Collections/SetFromMap \
	Collections/Swap \
	Collections/T5078378 \
	Collections/ViewSynch \
	Collections/WrappedNull \
	HashMap/SetValue \
	HashMap/ToString \
	Hashtable/EqualsCast \
	Hashtable/HashCode \
	Hashtable/IllegalLoadFactor \
	Hashtable/ReadObject \
	LinkedList/AddAll \
	LinkedList/Clone \
	LinkedList/ComodifiedRemove \
	LinkedList/Remove \
	PriorityQueue/ForgetMeNot \
	PriorityQueue/PriorityQueueSort \
	PriorityQueue/RemoveContains \
	TreeMap/ContainsValue \
	TreeMap/HeadTailTypeError \
	TreeMap/NullAtEnd \
	TreeMap/NullPermissiveComparator \
	TreeMap/SubMapClear \
	TreeMap/SubMap
#	AbstractCollection/ToString \
#	Collection/IteratorAtEnd \
#	Collection/MOAT \
#	Collections/AddAll \
#	Collections/FindSubList \
#	Collections/ReplaceAll \
#	Collections/ReverseOrder2 \
#	Collections/Rotate \
#	Collections/T6433170 \
#	HashMap/KeySetRemove \
#	Hashtable/SelfRef \
#	List/LockStep \
#	Map/Get

CLEANFILES=${shell find . -name '*.class'}

JAVAC=javac
JCFLAGS=-classpath ${CLASSPATH} -Xlint -g
JAVA=java
JFLAGS=-classpath ${CLASSPATH} -ea

.MAIN: all
.SUFFIXES: .java .class
.PHONY: all build clean test

.java.class:
	${JAVAC} ${JCFLAGS} $<

all: test

build: ${OBJS}

test: build
	for test in ${CLASSES} ; do \
		for ignore in ${IGNORETESTS} ; do \
			if [ "$${ignore}" = "$${test}" ] ; then \
			continue 2 ; \
			fi ; \
		done ; \
		echo '***' $${test} '***' ; \
		( \
			cd `dirname $${test}` ; \
			${JAVA} ${JFLAGS} `basename $${test}` ; \
		) ; \
		if [ "$$?" -ne "0" ] ; then exit 1 ; fi ; \
	done

clean:
	rm -f ${subst $$,\$$,${CLEANFILES}}
