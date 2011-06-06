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

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;

/**
 * Test FastHashMap in <strong>fast</strong> mode.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Jason van Zyl
 */
public class TestFastHashMap1 extends TestFastHashMap {
    
    public TestFastHashMap1(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestFastHashMap1.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestFastHashMap1.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        FastHashMap fhm = new FastHashMap();
        fhm.setFast(true);
        return (fhm);
    }

    public void setUp() {
        map = (HashMap) makeEmptyMap();
    }

}
