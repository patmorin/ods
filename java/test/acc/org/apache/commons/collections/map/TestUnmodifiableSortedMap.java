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

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Unmodifiable;

/**
 * Extension of {@link AbstractTestSortedMap} for exercising the 
 * {@link UnmodifiableSortedMap} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestUnmodifiableSortedMap extends AbstractTestSortedMap {
    
    public TestUnmodifiableSortedMap(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestUnmodifiableSortedMap.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestUnmodifiableSortedMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
    //-------------------------------------------------------------------
    
    public Map makeEmptyMap() {
        return UnmodifiableSortedMap.decorate(new TreeMap());
    }
    
    public boolean isPutChangeSupported() {
        return false;
    }
    
    public boolean isPutAddSupported() {
        return false;
    }
    
    public boolean isRemoveSupported() {
        return false;
    }
    
    public Map makeFullMap() {
        SortedMap m = new TreeMap();
        addSampleMappings(m);
        return UnmodifiableSortedMap.decorate(m);
    }
    
    //-----------------------------------------------------------------------
    public void testUnmodifiable() {
        assertTrue(makeEmptyMap() instanceof Unmodifiable);
        assertTrue(makeFullMap() instanceof Unmodifiable);
    }
    
    public void testDecorateFactory() {
        Map map = makeFullMap();
        assertSame(map, UnmodifiableSortedMap.decorate((SortedMap) map));
        
        try {
            UnmodifiableSortedMap.decorate(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/UnmodifiableSortedMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/UnmodifiableSortedMap.fullCollection.version3.1.obj");
//    }
}