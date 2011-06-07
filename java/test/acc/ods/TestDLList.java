package ods;

import org.apache.commons.collections.list.AbstractTestList;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.DLList;
import java.util.List;

public class TestDLList extends AbstractTestList {
	public TestDLList(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestDLList.class);
	}
	
	public List makeEmptyList() {
		return new DLList();
	}

	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestDLList.class.getName() };
		TestRunner.main(junitArgs);
	}
}
