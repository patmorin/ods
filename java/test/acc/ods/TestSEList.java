package ods;

import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.SEList;
import java.util.List;

public class TestSEList extends AbstractNoSerialTestList {
	public TestSEList(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestSEList.class);
	}
	
	public List makeEmptyList() {
		return new SEList(5, Object.class);
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestSEList.class.getName() };
		TestRunner.main(junitArgs);
	}
}
