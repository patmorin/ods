package ods;

import org.apache.commons.collections.list.AbstractTestList;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.ArrayStack;
import java.util.List;

public class TestArrayStack extends AbstractTestList {
	public TestArrayStack(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestArrayStack.class);
	}
	
	public List makeEmptyList() {
		return new ArrayStack(Object.class);
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestArrayStack.class.getName() };
		TestRunner.main(junitArgs);
	}
}
