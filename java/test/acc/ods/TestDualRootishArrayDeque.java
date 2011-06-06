package ods;

import org.apache.commons.collections.list.AbstractTestList;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.DualRootishArrayDeque;
import java.util.List;

public class TestDualRootishArrayDeque extends AbstractTestList {
	public TestDualRootishArrayDeque(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestDualRootishArrayDeque.class);
	}
	
	public List makeEmptyList() {
		return new DualRootishArrayDeque(Object.class);
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestDualRootishArrayDeque.class.getName() };
		TestRunner.main(junitArgs);
	}
}
