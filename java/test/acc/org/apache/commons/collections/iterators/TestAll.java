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
package org.apache.commons.collections.iterators;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Entry point for all iterator tests.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Rodney Waldhoff
 */
public class TestAll extends TestCase {
    
    public TestAll(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(TestArrayIterator.suite());
        suite.addTest(TestArrayIterator2.suite());
        suite.addTest(TestArrayListIterator.suite());
        suite.addTest(TestArrayListIterator2.suite());
        suite.addTest(TestObjectArrayIterator.suite());
        suite.addTest(TestObjectArrayListIterator.suite());
        suite.addTest(TestObjectArrayListIterator2.suite());
        suite.addTest(TestCollatingIterator.suite());
        suite.addTest(TestFilterIterator.suite());
        suite.addTest(TestFilterListIterator.suite());
        suite.addTest(TestIteratorChain.suite());
        suite.addTest(TestListIteratorWrapper.suite());
        suite.addTest(TestLoopingIterator.suite());
        suite.addTest(TestLoopingListIterator.suite());
        suite.addTest(TestReverseListIterator.suite());
        suite.addTest(TestSingletonIterator.suite());
        suite.addTest(TestSingletonIterator2.suite());
        suite.addTest(TestSingletonListIterator.suite());
        suite.addTest(TestObjectGraphIterator.suite());
        suite.addTest(TestUniqueFilterIterator.suite());
        suite.addTest(TestUnmodifiableIterator.suite());
        suite.addTest(TestUnmodifiableListIterator.suite());
        suite.addTest(TestUnmodifiableMapIterator.suite());
        suite.addTest(TestUnmodifiableOrderedMapIterator.suite());
        return suite;
    }
        
    public static void main(String args[]) {
        String[] testCaseName = { TestAll.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }
}
