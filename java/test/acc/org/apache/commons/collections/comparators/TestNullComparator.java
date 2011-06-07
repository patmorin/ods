/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.collections.comparators;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test the NullComparator.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Michael A. Smith
 */
public abstract class TestNullComparator extends AbstractTestComparator {

    public TestNullComparator(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TestNullComparator.class.getName());
        suite.addTest(new TestSuite(TestNullComparator1.class));
        suite.addTest(new TestSuite(TestNullComparator2.class));
        return suite;
    }

    /**
     *  Test the NullComparator with nulls high, using comparable comparator
     **/
    public static class TestNullComparator1 extends TestNullComparator {

	public TestNullComparator1(String testName) {
	    super(testName);
	}

    public Comparator makeComparator() {
	    return new NullComparator();
	}
	
    public List getComparableObjectsOrdered() {
        List list = new LinkedList();
	    list.add(new Integer(1));
	    list.add(new Integer(2));
	    list.add(new Integer(3));
	    list.add(new Integer(4));
	    list.add(new Integer(5));
	    list.add(null);
	    return list;
	}

	public String getCanonicalComparatorName(Object object) {
	    return super.getCanonicalComparatorName(object) + "1";
	}
    }

    /**
     *  Test the NullComparator with nulls low using the comparable comparator
     **/
    public static class TestNullComparator2 extends TestNullComparator {
        
        public TestNullComparator2(String testName) {
            super(testName);
        }
        
        public Comparator makeComparator() {
            return new NullComparator(false);
        }
        
        public List getComparableObjectsOrdered() {
            List list = new LinkedList();
            list.add(null);
            list.add(new Integer(1));
            list.add(new Integer(2));
            list.add(new Integer(3));
            list.add(new Integer(4));
            list.add(new Integer(5));
            return list;
        }
        
        public String getCanonicalComparatorName(Object object) {
            return super.getCanonicalComparatorName(object) + "2";
        }
    }
}
