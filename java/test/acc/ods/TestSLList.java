package ods;

import org.apache.commons.collections.collection.AbstractTestCollection;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.SLList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Arrays;

public class TestSLList extends AbstractTestCollection {
	public TestSLList(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestSLList.class);
	}
	
	public Collection makeCollection() {
		return new SLList();
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}
	
	public Collection makeConfirmedCollection() {
		Collection c = new LinkedList();
		return c;
	}
	
	public Collection makeConfirmedFullCollection() {
		Collection c = new LinkedList();
		c.addAll(Arrays.asList(getFullElements()));
		return c;
	}
	
	public static void main(String args[]) {
		String[] junitArgs = { TestSLList.class.getName() };
		TestRunner.main(junitArgs);
	}
}
