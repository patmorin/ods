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
 * @since Commons Collections 3.1
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Henri Yandell
 * @author Stephen Colebourne
 */
public class TestListOrderedMap2 extends AbstractTestOrderedMap {

    public TestListOrderedMap2(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestListOrderedMap2.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestListOrderedMap2.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        return new ListOrderedMap();
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
    
    public BulkTest bulkTestListView() {
        return new TestListView();
    }
    
    public class TestListView extends AbstractTestList {
        
        TestListView() {
            super("TestListView");
        }

        public List makeEmptyList() {
            return ((ListOrderedMap) TestListOrderedMap2.this.makeEmptyMap()).asList();
        }
        
        public List makeFullList() {
            return ((ListOrderedMap) TestListOrderedMap2.this.makeFullMap()).asList();
        }
        
        public Object[] getFullElements() {
            return TestListOrderedMap2.this.getSampleKeys();
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
            return TestListOrderedMap2.this.isAllowNullKey();
        }
        public boolean isTestSerialization() {
            return false;
        }
    }

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
