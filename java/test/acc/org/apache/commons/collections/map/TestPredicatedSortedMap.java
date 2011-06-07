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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;

/**
 * Extension of {@link TestPredicatedMap} for exercising the 
 * {@link PredicatedSortedMap} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestPredicatedSortedMap extends AbstractTestSortedMap{
    
    protected static final Predicate truePredicate = PredicateUtils.truePredicate();
    protected static final Predicate testPredicate = new Predicate() {
        public boolean evaluate(Object o) {
            return (o instanceof String);
        }
    };
    
    public TestPredicatedSortedMap(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestPredicatedSortedMap.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestPredicatedSortedMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    //-----------------------------------------------------------------------
    protected SortedMap decorateMap(SortedMap map, Predicate keyPredicate, 
        Predicate valuePredicate) {
        return PredicatedSortedMap.decorate(map, keyPredicate, valuePredicate);
    }
    
    public Map makeEmptyMap() {
        return decorateMap(new TreeMap(), truePredicate, truePredicate);
    }
   
    public Map makeTestMap() {
        return decorateMap(new TreeMap(), testPredicate, testPredicate);
    } 
    
    public SortedMap makeTestSortedMap() {
        return decorateMap(new TreeMap(), testPredicate, testPredicate);
    }
    
    public boolean isSubMapViewsSerializable() {
        // TreeMap sub map views have a bug in deserialization.
        return false;
    }

    public boolean isAllowNullKey() {
        return false;
    }

    // from TestPredicatedMap
    //-----------------------------------------------------------------------
    public void testEntrySet() {
        SortedMap map = makeTestSortedMap();
        assertTrue("returned entryset should not be null",
            map.entrySet() != null);
        map = decorateMap(new TreeMap(), null, null);
        map.put("oneKey", "oneValue");
        assertTrue("returned entryset should contain one entry",
            map.entrySet().size() == 1); 
        map = decorateMap(map, null, null);
    }
    
    public void testPut() {
        Map map = makeTestMap();
        try {
            map.put("Hi", new Integer(3));
            fail("Illegal value should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            map.put(new Integer(3), "Hi");
            fail("Illegal key should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }

        assertTrue(!map.containsKey(new Integer(3)));
        assertTrue(!map.containsValue(new Integer(3)));

        Map map2 = new HashMap();
        map2.put("A", "a");
        map2.put("B", "b");
        map2.put("C", "c");
        map2.put("c", new Integer(3));

        try {
            map.putAll(map2);
            fail("Illegal value should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }

        map.put("E", "e");
        Iterator iterator = map.entrySet().iterator();
        try {
            Map.Entry entry = (Map.Entry)iterator.next();
            entry.setValue(new Integer(3));
            fail("Illegal value should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        map.put("F", "f");
        iterator = map.entrySet().iterator();
        Map.Entry entry = (Map.Entry)iterator.next();
        entry.setValue("x");
        
    }

    //-----------------------------------------------------------------------
    public void testSortOrder() {
        SortedMap map = makeTestSortedMap();
        map.put("A",  "a");
        map.put("B", "b");
        try {
            map.put(null, "c");
            fail("Null key should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }
        map.put("C", "c");
        try {
            map.put("D", null);
            fail("Null value should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals("First key should be A", map.firstKey(), "A");
        assertEquals("Last key should be C", map.lastKey(), "C");
        assertEquals("First key in tail map should be B", 
            map.tailMap("B").firstKey(), "B");
        assertEquals("Last key in head map should be B", 
            map.headMap("C").lastKey(), "B");
        assertEquals("Last key in submap should be B",
           map.subMap("A","C").lastKey(), "B");
        
        Comparator c = map.comparator();
        assertTrue("natural order, so comparator should be null", 
            c == null);
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/PredicatedSortedMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/PredicatedSortedMap.fullCollection.version3.1.obj");
//    }
}
