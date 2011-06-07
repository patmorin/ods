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

import java.util.Map;
import java.util.TreeMap;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.OrderedBidiMap;

/**
 * JUnit tests.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestUnmodifiableOrderedBidiMap extends AbstractTestOrderedBidiMap {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        return BulkTest.makeSuite(TestUnmodifiableOrderedBidiMap.class);
    }

    public TestUnmodifiableOrderedBidiMap(String testName) {
        super(testName);
    }

    public BidiMap makeEmptyBidiMap() {
        return UnmodifiableOrderedBidiMap.decorate(new TreeBidiMap());
    }
    public BidiMap makeFullBidiMap() {
        OrderedBidiMap bidi = new TreeBidiMap();
        for (int i = 0; i < entries.length; i++) {
            bidi.put(entries[i][0], entries[i][1]);
        }
        return UnmodifiableOrderedBidiMap.decorate(bidi);
    }
    public Map makeFullMap() {
        OrderedBidiMap bidi = new TreeBidiMap();
        addSampleMappings(bidi);
        return UnmodifiableOrderedBidiMap.decorate(bidi);
    }
    
    public Map makeConfirmedMap() {
        return new TreeMap();
    }

    /**
     * Override to prevent infinite recursion of tests.
     */
    public String[] ignoredTests() {
        return new String[] {"TestUnmodifiableOrderedBidiMap.bulkTestInverseMap.bulkTestInverseMap"};
    }
    
    public boolean isAllowNullKey() {
        return false;
    }
    public boolean isAllowNullValue() {
        return false;
    }
    public boolean isPutAddSupported() {
        return false;
    }
    public boolean isPutChangeSupported() {
        return false;
    }
    public boolean isRemoveSupported() {
        return false;
    }
    
}
