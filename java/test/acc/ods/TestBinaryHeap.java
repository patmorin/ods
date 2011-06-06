package ods;

import org.apache.commons.collections.collection.AbstractTestCollection;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.BinaryHeap;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Arrays;

public class TestBinaryHeap extends AbstractTestCollection {
	public TestBinaryHeap(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestBinaryHeap.class);
	}
	
	public Collection makeCollection() {
		return new BinaryHeap(String.class);
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}
	
	public Collection makeConfirmedCollection() {
		Collection c = new PriorityQueue();
		return c;
	}
	
	public Collection makeConfirmedFullCollection() {
		Collection c = new PriorityQueue();
		c.addAll(Arrays.asList(getFullElements()));
		return c;
	}
	
	public Object[] getFullElements() {
		return getFullNonNullStringElements();
	}
	
	public static void main(String args[]) {
		String[] junitArgs = { TestBinaryHeap.class.getName() };
		TestRunner.main(junitArgs);
	}
}
