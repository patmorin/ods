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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.TransformerUtils;

/**
 * Extension of {@link TestLazyMap} for exercising the 
 * {@link LazySortedMap} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestLazySortedMap extends AbstractTestSortedMap {
    
    protected static final Factory oneFactory = FactoryUtils.constantFactory("One");
    protected static final Factory nullFactory = FactoryUtils.nullFactory();
    
    public TestLazySortedMap(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestLazySortedMap.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestLazySortedMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    //-----------------------------------------------------------------------    
    protected SortedMap decorateMap(SortedMap map, Factory factory) {
        return LazySortedMap.decorate(map, factory);
    }
    
    public Map makeEmptyMap() {
        return decorateMap(new TreeMap(), nullFactory);
    }
    
    protected SortedMap makeTestSortedMap(Factory factory) {
        return decorateMap(new TreeMap(), factory);
    }
    
    public boolean isSubMapViewsSerializable() {
        // TreeMap sub map views have a bug in deserialization.
        return false;
    }

    public boolean isAllowNullKey() {
        return false;
    }

    // from TestLazyMap
    //-----------------------------------------------------------------------
    public void testMapGet() {
        Map map = makeTestSortedMap(oneFactory);
        assertEquals(0, map.size());
        String s1 = (String) map.get("Five");
        assertEquals("One", s1);
        assertEquals(1, map.size());
        String s2 = (String) map.get(new String(new char[] {'F','i','v','e'}));
        assertEquals("One", s2);
        assertEquals(1, map.size());
        assertSame(s1, s2);
        
        map = makeTestSortedMap(nullFactory);
        Object o = map.get("Five");
        assertEquals(null,o);
        assertEquals(1, map.size());
        
    }
    
    //-----------------------------------------------------------------------
    public void testSortOrder() {
        SortedMap map = makeTestSortedMap(oneFactory);
        map.put("A",  "a");
        map.get("B"); // Entry with value "One" created
        map.put("C", "c");
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
    
    public void testTransformerDecorate() {
        Transformer transformer = TransformerUtils.asTransformer(oneFactory);
        SortedMap map = LazySortedMap.decorate(new TreeMap(), transformer);     
        assertTrue(map instanceof LazySortedMap);  
         try {
            map = LazySortedMap.decorate(new TreeMap(), (Transformer) null);
            fail("Expecting IllegalArgumentException for null transformer");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            map = LazySortedMap.decorate(null, transformer);
            fail("Expecting IllegalArgumentException for null map");
        } catch (IllegalArgumentException e) {
            // expected
        } 
    }
    
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/LazySortedMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/LazySortedMap.fullCollection.version3.1.obj");
//    }
}
