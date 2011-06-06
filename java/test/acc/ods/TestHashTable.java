package ods;

import org.apache.commons.collections.collection.AbstractTestCollection;
import org.apache.commons.collections.BulkTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import ods.HashTable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;

public class TestHashTable extends AbstractTestCollection {
	public TestHashTable(String testname) {
		super(testname);
	}
	
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestHashTable.class);
	}
	
	public Collection makeCollection() {
		return new HashTable();
	}
	
	protected boolean skipSerializedCanonicalTests() {
		return true;
	}
	
	public Collection makeConfirmedCollection() {
		Collection c = new HashSet();
		return c;
	}
	
	public Collection makeConfirmedFullCollection() {
		Collection c = new HashSet();
		c.addAll(Arrays.asList(getFullElements()));
		return c;
	}
	
	public static void main(String args[]) {
		String[] junitArgs = { TestHashTable.class.getName() };
		TestRunner.main(junitArgs);
	}
}
