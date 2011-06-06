package ods;

import org.apache.commons.collections.list.AbstractTestList;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.SkiplistList;
import java.util.List;

public class TestSkiplistList extends AbstractTestList {
	public TestSkiplistList(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestSkiplistList.class);
	}
	
	public List makeEmptyList() {
		return new SkiplistList();
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestSkiplistList.class.getName() };
		TestRunner.main(junitArgs);
	}
}
