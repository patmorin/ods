package ods;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import junit.framework.Test;

import ods.TestDLList;
import ods.TestArrayStack;
import ods.TestArrayDeque;

/**
 *  This file is currently unused.  Tests are run individually by the
 *  Makefile test target.
 */

public class TestAll extends TestCase {
	public TestAll(String testName) {
		super(testName);
	}
	
	public static Test suite() {
		TestSuite s = new TestSuite();
		
		// s.addTest(TestDLList.suite());
		// s.addTest(TestArrayStack.suite());
		// s.addTest(TestArrayDeque.suite());
		// s.addTest(TestSkiplistList.suite());
		
		return s;
	}
	
	public static void main(String args[]) {
		String[] junitArgs = { TestAll.class.getName() };
		TestRunner.main(junitArgs);
	}
}
