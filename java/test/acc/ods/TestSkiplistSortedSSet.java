package ods;

import org.apache.commons.collections.set.AbstractTestSortedSet;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.SortedSSet;
import ods.SkiplistSet;
import java.util.Set;

public class TestSkiplistSortedSSet extends AbstractTestSortedSet {
	public TestSkiplistSortedSSet(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestSkiplistSortedSSet.class);
	}
	
	public Set makeEmptySet() {
		return new SortedSSet(new SkiplistSet());
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestSkiplistSortedSSet.class.getName() };
		TestRunner.main(junitArgs);
	}
}
