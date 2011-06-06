package ods;

import org.apache.commons.collections.list.AbstractTestList;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.DualArrayDeque;
import java.util.List;

public class TestDualArrayDeque extends AbstractTestList {
	public TestDualArrayDeque(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestDualArrayDeque.class);
	}
	
	public List makeEmptyList() {
		return new DualArrayDeque(Object.class);
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestDualArrayDeque.class.getName() };
		TestRunner.main(junitArgs);
	}
}
