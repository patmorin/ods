package ods;

import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.SEListRaw;
import java.util.List;

public class TestSEListRaw extends AbstractNoSerialTestList {
	public TestSEListRaw(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestSEListRaw.class);
	}
	
	public List makeEmptyList() {
		return new SEListRaw(5, Object.class);
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestSEListRaw.class.getName() };
		TestRunner.main(junitArgs);
	}
}
