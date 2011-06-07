package ods;

import org.apache.commons.collections.collection.AbstractTestCollection;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.ArrayQueue;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

public class TestArrayQueue extends AbstractTestCollection {
	public TestArrayQueue(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestArrayQueue.class);
	}
	
	public Collection makeCollection() {
		return new ArrayQueue(Object.class);
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}
	
	public Collection makeConfirmedCollection() {
		Collection c = new ArrayList();
		return c;
	}
	
	public Collection makeConfirmedFullCollection() {
		Collection c = new ArrayList();
		c.addAll(Arrays.asList(getFullElements()));
		return c;
	}
	
	public static void main(String args[]) {
		String[] junitArgs = { TestArrayQueue.class.getName() };
		TestRunner.main(junitArgs);
	}
}
