package ods;

import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.ArrayStack;
import java.util.List;

public class TestArrayStack extends AbstractNoSerialTestList {
	public TestArrayStack(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestArrayStack.class);
	}
	
	public List makeEmptyList() {
		return new ArrayStack(Object.class);
	}
	
	public static void main(String args[]) {
		String[] junitArgs = { TestArrayStack.class.getName() };
		TestRunner.main(junitArgs);
	}
}
