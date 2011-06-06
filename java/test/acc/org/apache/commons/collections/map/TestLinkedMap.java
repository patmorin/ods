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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.list.AbstractTestList;

/**
 * JUnit tests.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestLinkedMap extends AbstractTestOrderedMap {

    public TestLinkedMap(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        return BulkTest.makeSuite(TestLinkedMap.class);
    }

    public Map makeEmptyMap() {
        return new LinkedMap();
    }

    public String getCompatibilityVersion() {
        return "3";
    }

    //-----------------------------------------------------------------------    
    public void testReset() {
        resetEmpty();
        OrderedMap ordered = (OrderedMap) map;
        ((ResettableIterator) ordered.mapIterator()).reset();
        
        resetFull();
        ordered = (OrderedMap) map;
        List list = new ArrayList(ordered.keySet());
        ResettableIterator it = (ResettableIterator) ordered.mapIterator();
        assertSame(list.get(0), it.next());
        assertSame(list.get(1), it.next());
        it.reset();
        assertSame(list.get(0), it.next());
    }
    
    //-----------------------------------------------------------------------
    public void testInsertionOrder() {
        if (isPutAddSupported() == false || isPutChangeSupported() == false) return;
        Object[] keys = getSampleKeys();
        Object[] values = getSampleValues();
        Iterator it = null;
        
        resetEmpty();
        map.put(keys[0], values[0]);
        map.put(keys[1], values[1]);
        it = map.keySet().iterator();
        assertSame(keys[0], it.next());
        assertSame(keys[1], it.next());
        it = map.values().iterator();
        assertSame(values[0], it.next());
        assertSame(values[1], it.next());

        // no change to order
        map.put(keys[1], values[1]);
        it = map.keySet().iterator();
        assertSame(keys[0], it.next());
        assertSame(keys[1], it.next());
        it = map.values().iterator();
        assertSame(values[0], it.next());
        assertSame(values[1], it.next());

        // no change to order
        map.put(keys[1], values[2]);
        it = map.keySet().iterator();
        assertSame(keys[0], it.next());
        assertSame(keys[1], it.next());
        it = map.values().iterator();
        assertSame(values[0], it.next());
        assertSame(values[2], it.next());

        // no change to order
        map.put(keys[0], values[3]);
        it = map.keySet().iterator();
        assertSame(keys[0], it.next());
        assertSame(keys[1], it.next());
        it = map.values().iterator();
        assertSame(values[3], it.next());
        assertSame(values[2], it.next());
    }
    
    //-----------------------------------------------------------------------
    public void testGetByIndex() {
        resetEmpty();
        LinkedMap lm = (LinkedMap) map;
        try {
            lm.get(0);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lm.get(-1);
        } catch (IndexOutOfBoundsException ex) {}
        
        resetFull();
        lm = (LinkedMap) map;
        try {
            lm.get(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lm.get(lm.size());
        } catch (IndexOutOfBoundsException ex) {}
        
        int i = 0;
        for (MapIterator it = lm.mapIterator(); it.hasNext(); i++) {
            assertSame(it.next(), lm.get(i));
        }
    }

    public void testGetValueByIndex() {
        resetEmpty();
        LinkedMap lm = (LinkedMap) map;
        try {
            lm.getValue(0);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lm.getValue(-1);
        } catch (IndexOutOfBoundsException ex) {}
        
        resetFull();
        lm = (LinkedMap) map;
        try {
            lm.getValue(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lm.getValue(lm.size());
        } catch (IndexOutOfBoundsException ex) {}
        
        int i = 0;
        for (MapIterator it = lm.mapIterator(); it.hasNext(); i++) {
            it.next();
            assertSame(it.getValue(), lm.getValue(i));
        }
    }

    public void testIndexOf() {
        resetEmpty();
        LinkedMap lm = (LinkedMap) map;
        assertEquals(-1, lm.indexOf(getOtherKeys()));
        
        resetFull();
        lm = (LinkedMap) map;
        List list = new ArrayList();
        for (MapIterator it = lm.mapIterator(); it.hasNext();) {
            list.add(it.next());
        }
        for (int i = 0; i < list.size(); i++) {
            assertEquals(i, lm.indexOf(list.get(i)));
        }
    }

    public void testRemoveByIndex() {
        resetEmpty();
        LinkedMap lm = (LinkedMap) map;
        try {
            lm.remove(0);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lm.remove(-1);
        } catch (IndexOutOfBoundsException ex) {}
        
        resetFull();
        lm = (LinkedMap) map;
        try {
            lm.remove(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lm.remove(lm.size());
        } catch (IndexOutOfBoundsException ex) {}
        
        List list = new ArrayList();
        for (MapIterator it = lm.mapIterator(); it.hasNext();) {
            list.add(it.next());
        }
        for (int i = 0; i < list.size(); i++) {
            Object key = list.get(i);
            Object value = lm.get(key);
            assertEquals(value, lm.remove(i));
            list.remove(i);
            assertEquals(false, lm.containsKey(key));
        }
    }
    
    public BulkTest bulkTestListView() {
        return new TestListView();
    }
    
    public class TestListView extends AbstractTestList {
        
        TestListView() {
            super("TestListView");
        }

        public List makeEmptyList() {
            return ((LinkedMap) TestLinkedMap.this.makeEmptyMap()).asList();
        }
        
        public List makeFullList() {
            return ((LinkedMap) TestLinkedMap.this.makeFullMap()).asList();
        }
        
        public Object[] getFullElements() {
            return TestLinkedMap.this.getSampleKeys();
        }
        public boolean isAddSupported() {
            return false;
        }
        public boolean isRemoveSupported() {
            return false;
        }
        public boolean isSetSupported() {
            return false;
        }
        public boolean isNullSupported() {
            return TestLinkedMap.this.isAllowNullKey();
        }
        public boolean isTestSerialization() {
            return false;
        }
    }

    public void testClone() {
        LinkedMap map = new LinkedMap(10);
        map.put("1", "1");
        Map cloned = (Map) map.clone();
        assertEquals(map.size(), cloned.size());
        assertSame(map.get("1"), cloned.get("1"));
    }
    
//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) map, "D:/dev/collections/data/test/LinkedMap.emptyCollection.version3.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) map, "D:/dev/collections/data/test/LinkedMap.fullCollection.version3.obj");
//    }
}
