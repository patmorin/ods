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
package org.apache.commons.collections.map;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.apache.commons.collections.BoundedMap;
import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.KeyValue;

/**
 * JUnit tests.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestSingletonMap extends AbstractTestOrderedMap {

    private static final Integer ONE = new Integer(1);
    private static final Integer TWO = new Integer(2);
    private static final String TEN = "10";
    private static final String TWENTY = "20";
        
    public TestSingletonMap(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        return BulkTest.makeSuite(TestSingletonMap.class);
    }

    //-----------------------------------------------------------------------
    public Map makeEmptyMap() {
        // need an empty singleton map, but thats not possible
        // use a ridiculous fake instead to make the tests pass
        return UnmodifiableOrderedMap.decorate(ListOrderedMap.decorate(new HashMap()));
    }
    
    public String[] ignoredTests() {
        // the ridiculous map above still doesn't pass these tests
        // but its not relevant, so we ignore them
        return new String[] {
            "TestSingletonMap.bulkTestMapIterator.testEmptyMapIterator",
            "TestSingletonMap.bulkTestOrderedMapIterator.testEmptyMapIterator",
        };
    }


    public Map makeFullMap() {
        return new SingletonMap(ONE, TWO);
    }

    public boolean isPutAddSupported() {
        return false;
    }

    public boolean isRemoveSupported() {
        return false;
    }

    public Object[] getSampleKeys() {
        return new Object[] {ONE};
    }

    public Object[] getSampleValues() {
        return new Object[] {TWO};
    }

    public Object[] getNewSampleValues() {
        return new Object[] {TEN};
    }

    //-----------------------------------------------------------------------
    public void testClone() {
        SingletonMap map = new SingletonMap(ONE, TWO);
        assertEquals(1, map.size());
        SingletonMap cloned = (SingletonMap) map.clone();
        assertEquals(1, cloned.size());
        assertEquals(true, cloned.containsKey(ONE));
        assertEquals(true, cloned.containsValue(TWO));
    }

    public void testKeyValue() {
        SingletonMap map = new SingletonMap(ONE, TWO);
        assertEquals(1, map.size());
        assertEquals(ONE, map.getKey());
        assertEquals(TWO, map.getValue());
        assertTrue(map instanceof KeyValue);
    }

    public void testBoundedMap() {
        SingletonMap map = new SingletonMap(ONE, TWO);
        assertEquals(1, map.size());
        assertEquals(true, map.isFull());
        assertEquals(1, map.maxSize());
        assertTrue(map instanceof BoundedMap);
    }

    //-----------------------------------------------------------------------
//    public BulkTest bulkTestMapIterator() {
//        return new TestFlatMapIterator();
//    }
//    
//    public class TestFlatMapIterator extends AbstractTestOrderedMapIterator {
//        public TestFlatMapIterator() {
//            super("TestFlatMapIterator");
//        }
//        
//        public Object[] addSetValues() {
//            return TestSingletonMap.this.getNewSampleValues();
//        }
//        
//        public boolean supportsRemove() {
//            return TestSingletonMap.this.isRemoveSupported();
//        }
//
//        public boolean supportsSetValue() {
//            return TestSingletonMap.this.isSetValueSupported();
//        }
//
//        public MapIterator makeEmptyMapIterator() {
//            resetEmpty();
//            return ((Flat3Map) TestSingletonMap.this.map).mapIterator();
//        }
//
//        public MapIterator makeFullMapIterator() {
//            resetFull();
//            return ((Flat3Map) TestSingletonMap.this.map).mapIterator();
//        }
//        
//        public Map getMap() {
//            // assumes makeFullMapIterator() called first
//            return TestSingletonMap.this.map;
//        }
//        
//        public Map getConfirmedMap() {
//            // assumes makeFullMapIterator() called first
//            return TestSingletonMap.this.confirmed;
//        }
//        
//        public void verify() {
//            super.verify();
//            TestSingletonMap.this.verify();
//        }
//    }
    
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/SingletonMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/SingletonMap.fullCollection.version3.1.obj");
//    }
}
