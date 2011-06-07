package ods;

import org.apache.commons.collections.set.AbstractTestSortedSet;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.SortedSSet;
import ods.ScapegoatTree;
import java.util.Set;

public class TestScapegoatTreeSortedSSet extends AbstractTestSortedSet {
	public TestScapegoatTreeSortedSSet(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestScapegoatTreeSortedSSet.class);
	}
	
	public Set makeEmptySet() {
		return new SortedSSet(new ScapegoatTree(new ScapegoatNode()));
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestScapegoatTreeSortedSSet.class.getName() };
		TestRunner.main(junitArgs);
	}
}
