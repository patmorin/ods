package ods;

import org.apache.commons.collections.set.AbstractTestSortedSet;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.SortedSSet;
import ods.BinarySearchTree;
import java.util.Set;

public class TestBinarySearchTreeSortedSSet extends AbstractTestSortedSet {
	public TestBinarySearchTreeSortedSSet(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestBinarySearchTreeSortedSSet.class);
	}
	
	public Set makeEmptySet() {
		return new SortedSSet(new BinarySearchTree(new SimpleBSTNode()));
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestBinarySearchTreeSortedSSet.class.getName() };
		TestRunner.main(junitArgs);
	}
}
