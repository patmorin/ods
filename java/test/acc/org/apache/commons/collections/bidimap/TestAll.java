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
package org.apache.commons.collections.bidimap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Entry point for tests.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestAll extends TestCase {
    
    public TestAll(String testName) {
        super(testName);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestAll.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTest(TestDualHashBidiMap.suite());
        suite.addTest(TestDualTreeBidiMap.suite());
        suite.addTest(TestDualTreeBidiMap2.suite());
        suite.addTest(TestTreeBidiMap.suite());
        
        suite.addTest(TestAbstractOrderedBidiMapDecorator.suite());
        suite.addTest(TestUnmodifiableBidiMap.suite());
        suite.addTest(TestUnmodifiableOrderedBidiMap.suite());
        suite.addTest(TestUnmodifiableSortedBidiMap.suite());
        
        return suite;
    }
        
}
