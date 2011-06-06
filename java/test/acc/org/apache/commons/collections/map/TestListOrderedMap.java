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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.list.AbstractTestList;

/**
 * Extension of {@link TestMap} for exercising the {@link ListOrderedMap}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Henri Yandell
 * @author Stephen Colebourne
 * @author Matt Benson
 */
public class TestListOrderedMap extends AbstractTestOrderedMap {

    public TestListOrderedMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestListOrderedMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestListOrderedMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        return ListOrderedMap.decorate(new HashMap());
    }
    
    //-----------------------------------------------------------------------
    public void testGetByIndex() {
        resetEmpty();
        ListOrderedMap lom = (ListOrderedMap) map;
        try {
            lom.get(0);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.get(-1);
        } catch (IndexOutOfBoundsException ex) {}
        
        resetFull();
        lom = (ListOrderedMap) map;
        try {
            lom.get(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.get(lom.size());
        } catch (IndexOutOfBoundsException ex) {}
        
        int i = 0;
        for (MapIterator it = lom.mapIterator(); it.hasNext(); i++) {
            assertSame(it.next(), lom.get(i));
        }
    }

    public void testGetValueByIndex() {
        resetEmpty();
        ListOrderedMap lom = (ListOrderedMap) map;
        try {
            lom.getValue(0);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.getValue(-1);
        } catch (IndexOutOfBoundsException ex) {}
        
        resetFull();
        lom = (ListOrderedMap) map;
        try {
            lom.getValue(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.getValue(lom.size());
        } catch (IndexOutOfBoundsException ex) {}
        
        int i = 0;
        for (MapIterator it = lom.mapIterator(); it.hasNext(); i++) {
            it.next();
            assertSame(it.getValue(), lom.getValue(i));
        }
    }

    public void testIndexOf() {
        resetEmpty();
        ListOrderedMap lom = (ListOrderedMap) map;
        assertEquals(-1, lom.indexOf(getOtherKeys()));
        
        resetFull();
        lom = (ListOrderedMap) map;
        List list = new ArrayList();
        for (MapIterator it = lom.mapIterator(); it.hasNext();) {
            list.add(it.next());
        }
        for (int i = 0; i < list.size(); i++) {
            assertEquals(i, lom.indexOf(list.get(i)));
        }
    }

    public void testSetValueByIndex() {
        resetEmpty();
        ListOrderedMap lom = (ListOrderedMap) map;
        try {
            lom.setValue(0, "");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.setValue(-1, "");
        } catch (IndexOutOfBoundsException ex) {}
        
        resetFull();
        lom = (ListOrderedMap) map;
        try {
            lom.setValue(-1, "");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.setValue(lom.size(), "");
        } catch (IndexOutOfBoundsException ex) {}
        
        for (int i = 0; i < lom.size(); i++) {
            Object value = lom.getValue(i);
            Object input = new Integer(i);
            assertEquals(value, lom.setValue(i, input));
            assertEquals(input, lom.getValue(i));
        }
    }

    public void testRemoveByIndex() {
        resetEmpty();
        ListOrderedMap lom = (ListOrderedMap) map;
        try {
            lom.remove(0);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.remove(-1);
        } catch (IndexOutOfBoundsException ex) {}
        
        resetFull();
        lom = (ListOrderedMap) map;
        try {
            lom.remove(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.remove(lom.size());
        } catch (IndexOutOfBoundsException ex) {}
        
        List list = new ArrayList();
        for (MapIterator it = lom.mapIterator(); it.hasNext();) {
            list.add(it.next());
        }
        for (int i = 0; i < list.size(); i++) {
            Object key = list.get(i);
            Object value = lom.get(key);
            assertEquals(value, lom.remove(i));
            list.remove(i);
            assertEquals(false, lom.containsKey(key));
        }
    }

    public void testPut_intObjectObject() {
        resetEmpty();
        ListOrderedMap lom = (ListOrderedMap) map;
        
        try {
            lom.put(1, "testInsert1", "testInsert1v");
            fail("should not be able to insert at pos 1 in empty Map");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.put(-1, "testInsert-1", "testInsert-1v");
            fail("should not be able to insert at pos -1 in empty Map");
        } catch (IndexOutOfBoundsException ex) {}
        
        // put where key doesn't exist
        lom.put(0, "testInsert1", "testInsert1v");
        assertEquals("testInsert1v", lom.getValue(0));
        
        lom.put("testInsertPut", "testInsertPutv");
        assertEquals("testInsert1v", lom.getValue(0));
        assertEquals("testInsertPutv", lom.getValue(1));
        
        lom.put(0, "testInsert0", "testInsert0v");
        assertEquals("testInsert0v", lom.getValue(0));
        assertEquals("testInsert1v", lom.getValue(1));
        assertEquals("testInsertPutv", lom.getValue(2));
        
        lom.put(3, "testInsert3", "testInsert3v");
        assertEquals("testInsert0v", lom.getValue(0));
        assertEquals("testInsert1v", lom.getValue(1));
        assertEquals("testInsertPutv", lom.getValue(2));
        assertEquals("testInsert3v", lom.getValue(3));
        
        // put in a full map        
        resetFull();
        lom = (ListOrderedMap) map;
        ListOrderedMap lom2 = new ListOrderedMap();
        lom2.putAll(lom);
        
        lom2.put(0, "testInsert0", "testInsert0v");
        assertEquals("testInsert0v", lom2.getValue(0));
        for (int i = 0; i < lom.size(); i++) {
            assertEquals(lom2.getValue(i + 1), lom.getValue(i));
        }
        
        // put where key does exist
        Integer i1 = new Integer(1);
        Integer i1b = new Integer(1);
        Integer i2 = new Integer(2);
        Integer i3 = new Integer(3);
        
        resetEmpty();
        lom = (ListOrderedMap) map;
        lom.put(i1, "1");
        lom.put(i2, "2");
        lom.put(i3, "3");
        lom.put(0, i1, "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("One", lom.getValue(0));
        assertSame(i1, lom.get(0));
        
        resetEmpty();
        lom = (ListOrderedMap) map;
        lom.put(i1, "1");
        lom.put(i2, "2");
        lom.put(i3, "3");
        lom.put(0, i1b, "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("One", lom.getValue(0));
        assertEquals("2", lom.getValue(1));
        assertEquals("3", lom.getValue(2));
        assertSame(i1b, lom.get(0));
        
        resetEmpty();
        lom = (ListOrderedMap) map;
        lom.put(i1, "1");
        lom.put(i2, "2");
        lom.put(i3, "3");
        lom.put(1, i1b, "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("One", lom.getValue(0));
        assertEquals("2", lom.getValue(1));
        assertEquals("3", lom.getValue(2));
        
        resetEmpty();
        lom = (ListOrderedMap) map;
        lom.put(i1, "1");
        lom.put(i2, "2");
        lom.put(i3, "3");
        lom.put(2, i1b, "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("2", lom.getValue(0));
        assertEquals("One", lom.getValue(1));
        assertEquals("3", lom.getValue(2));
        
        resetEmpty();
        lom = (ListOrderedMap) map;
        lom.put(i1, "1");
        lom.put(i2, "2");
        lom.put(i3, "3");
        lom.put(3, i1b, "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("2", lom.getValue(0));
        assertEquals("3", lom.getValue(1));
        assertEquals("One", lom.getValue(2));
    }

    //-----------------------------------------------------------------------
    public void testValueList_getByIndex() {
        resetFull();
        ListOrderedMap lom = (ListOrderedMap) map;
        for (int i = 0; i < lom.size(); i++) {
            Object expected = lom.getValue(i);
            assertEquals(expected, lom.valueList().get(i));
        }
    }

    public void testValueList_setByIndex() {
        resetFull();
        ListOrderedMap lom = (ListOrderedMap) map;
        for (int i = 0; i < lom.size(); i++) {
            Object input = new Integer(i);
            Object expected = lom.getValue(i);
            assertEquals(expected, lom.valueList().set(i, input));
            assertEquals(input, lom.getValue(i));
            assertEquals(input, lom.valueList().get(i));
        }
    }

    public void testValueList_removeByIndex() {
        resetFull();
        ListOrderedMap lom = (ListOrderedMap) map;
        while (lom.size() > 1) {
            Object expected = lom.getValue(1);
            assertEquals(expected, lom.valueList().remove(1));
        }
    }

    //-----------------------------------------------------------------------
    public BulkTest bulkTestKeyListView() {
        return new TestKeyListView();
    }

    public BulkTest bulkTestValueListView() {
        return new TestValueListView();
    }

    //-----------------------------------------------------------------------
    public class TestKeyListView extends AbstractTestList {
        TestKeyListView() {
            super("TestKeyListView");
        }

        public List makeEmptyList() {
            return ((ListOrderedMap) TestListOrderedMap.this.makeEmptyMap()).keyList();
        }
        public List makeFullList() {
            return ((ListOrderedMap) TestListOrderedMap.this.makeFullMap()).keyList();
        }

        public Object[] getFullElements() {
            return TestListOrderedMap.this.getSampleKeys();
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
            return TestListOrderedMap.this.isAllowNullKey();
        }
        public boolean isTestSerialization() {
            return false;
        }
    }

    //-----------------------------------------------------------------------
    public class TestValueListView extends AbstractTestList {
        TestValueListView() {
            super("TestValueListView");
        }

        public List makeEmptyList() {
            return ((ListOrderedMap) TestListOrderedMap.this.makeEmptyMap()).valueList();
        }
        public List makeFullList() {
            return ((ListOrderedMap) TestListOrderedMap.this.makeFullMap()).valueList();
        }

        public Object[] getFullElements() {
            return TestListOrderedMap.this.getSampleValues();
        }
        public boolean isAddSupported() {
            return false;
        }
        public boolean isRemoveSupported() {
            return true;
        }
        public boolean isSetSupported() {
            return true;
        }
        public boolean isNullSupported() {
            return TestListOrderedMap.this.isAllowNullKey();
        }
        public boolean isTestSerialization() {
            return false;
        }
    }

    //-----------------------------------------------------------------------
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/ListOrderedMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/ListOrderedMap.fullCollection.version3.1.obj");
//    }
}
