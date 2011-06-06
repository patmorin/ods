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
 * Entry point for all Collections project tests.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestAllPackages extends TestCase {
    public TestAllPackages(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(org.apache.commons.collections.TestAll.suite());
        suite.addTest(org.apache.commons.collections.bag.TestAll.suite());
        suite.addTest(org.apache.commons.collections.bidimap.TestAll.suite());
        suite.addTest(org.apache.commons.collections.buffer.TestAll.suite());
        suite.addTest(org.apache.commons.collections.collection.TestAll.suite());
        suite.addTest(org.apache.commons.collections.comparators.TestAll.suite());
        suite.addTest(org.apache.commons.collections.iterators.TestAll.suite());
        suite.addTest(org.apache.commons.collections.keyvalue.TestAll.suite());
        suite.addTest(org.apache.commons.collections.list.TestAll.suite());
        suite.addTest(org.apache.commons.collections.map.TestAll.suite());
        suite.addTest(org.apache.commons.collections.set.TestAll.suite());
        return suite;
    }
        
    public static void main(String args[]) {
        String[] testCaseName = { TestAllPackages.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }
    
}
