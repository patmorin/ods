package ods;

import org.apache.commons.collections.list.AbstractTestList;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.FastArrayStack;
import java.util.List;

public class TestFastArrayStack extends AbstractTestList {
	public TestFastArrayStack(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestFastArrayStack.class);
	}
	
	public List makeEmptyList() {
		return new FastArrayStack(Object.class);
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestFastArrayStack.class.getName() };
		TestRunner.main(junitArgs);
	}
}
