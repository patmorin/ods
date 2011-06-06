package ods;

import org.apache.commons.collections.set.AbstractTestSet;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.MultiplicativeHashSet;
import java.util.Set;

public class TestMultiplicativeHashSet extends AbstractTestSet {
	public TestMultiplicativeHashSet(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestMultiplicativeHashSet.class);
	}
	
	public Set makeEmptySet() {
		return new MultiplicativeHashSet();
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}

	public static void main(String args[]) {
		String[] junitArgs = { TestMultiplicativeHashSet.class.getName() };
		TestRunner.main(junitArgs);
	}
}
