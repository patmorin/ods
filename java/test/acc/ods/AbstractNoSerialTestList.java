package ods;

import org.apache.commons.collections.list.AbstractTestList;

public abstract class AbstractNoSerialTestList extends AbstractTestList {
	public AbstractNoSerialTestList(String testname) {
		super(testname);
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}
}
