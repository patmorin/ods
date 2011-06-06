package ods;

import org.apache.commons.collections.list.AbstractTestList;

public abstract class AbstractNoSerialTestList extends AbstractTestList {
	public AbstractNoSerialTestList(String testname) {
		super(testname);
	}
	
	public void testEmptyListCompatibility() { }
	public void testFullListCompatibility() { }
	public void testCanonicalEmptyCollectionExists() { }
	public void testCanonicalFullCollectionExists() { }
}
