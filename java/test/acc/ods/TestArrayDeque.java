package ods;

import org.apache.commons.collections.list.AbstractTestList;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.ArrayDeque;
import java.util.List;

public class TestArrayDeque extends AbstractTestList {
	public TestArrayDeque(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestArrayDeque.class);
	}
	
	public List makeEmptyList() {
		return new ArrayDeque(Object.class);
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestArrayDeque.class.getName() };
		TestRunner.main(junitArgs);
	}
}
