package ods;

import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.RootishArrayStack;
import java.util.List;

public class TestRootishArrayStack extends AbstractNoSerialTestList {
	public TestRootishArrayStack(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestRootishArrayStack.class);
	}
	
	public List makeEmptyList() {
		return new RootishArrayStack(Object.class);
	}
	
	public static void main(String args[]) {
		String[] junitArgs = { TestRootishArrayStack.class.getName() };
		TestRunner.main(junitArgs);
	}
}
