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
package org.apache.commons.collections;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Entry point for all Collections package tests.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Rodney Waldhoff
 * @author Stephen Colebourne
 */
public class TestAll extends TestCase {
    public TestAll(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(TestBagUtils.suite());
        suite.addTest(TestClosureUtils.suite());
        suite.addTest(TestCollectionUtils.suite());
        suite.addTest(TestBufferUtils.suite());
        suite.addTest(TestEnumerationUtils.suite());
        suite.addTest(TestFactoryUtils.suite());
        suite.addTest(TestListUtils.suite());
        suite.addTest(TestMapUtils.suite());
        suite.addTest(TestPredicateUtils.suite());
        suite.addTest(TestSetUtils.suite());
        suite.addTest(TestTransformerUtils.suite());
        
        suite.addTest(TestArrayStack.suite());
        suite.addTest(TestBeanMap.suite());
        suite.addTest(TestBinaryHeap.suite());
        suite.addTest(TestBoundedFifoBuffer.suite());
        suite.addTest(TestBoundedFifoBuffer2.suite());
        suite.addTest(TestCursorableLinkedList.suite());
        suite.addTest(TestDoubleOrderedMap.suite());
        suite.addTest(TestExtendedProperties.suite());
        suite.addTest(TestFastArrayList.suite());
        suite.addTest(TestFastArrayList1.suite());
        suite.addTest(TestFastHashMap.suite());
        suite.addTest(TestFastHashMap1.suite());
        suite.addTest(TestFastTreeMap.suite());
        suite.addTest(TestFastTreeMap1.suite());
        suite.addTest(TestHashBag.suite());
        suite.addTest(TestIteratorUtils.suite());
        suite.addTest(TestLRUMap.suite());
        suite.addTest(TestMultiHashMap.suite());
        suite.addTest(TestReferenceMap.suite());
        suite.addTest(TestSequencedHashMap.suite());
        suite.addTest(TestStaticBucketMap.suite());
        suite.addTest(TestTreeBag.suite());
        suite.addTest(TestUnboundedFifoBuffer.suite());
        return suite;
    }
        
    public static void main(String args[]) {
        String[] testCaseName = { TestAll.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }
}
