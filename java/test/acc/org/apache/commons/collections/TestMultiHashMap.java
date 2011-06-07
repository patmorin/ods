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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.map.AbstractTestMap;

/**
 * Unit Tests for <code>MultiHashMap</code>.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Unknown
 */
public class TestMultiHashMap extends AbstractTestMap {

    public TestMultiHashMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestMultiHashMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestMultiHashMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    // MutltiHashMap was introduced in Collections 2.x
    public String getCompatibilityVersion() {
        return "2";
    }

    public Map makeEmptyMap() {
        return new MultiHashMap();
    }
    
    //----------------------------
    //          Tests
    //----------------------------
    public void testPutNGet() {
        MultiHashMap map = new MultiHashMap();
        loadMap(map);
        checkMap(map);

        assertTrue(map.get(new Integer(99)) == null);

        map.clear();
        assertTrue(map.size() == 0);
    }

    public void testContainsValue() {
        MultiHashMap map = new MultiHashMap();
        loadMap(map);

        assertTrue(map.containsValue("uno"));
        assertTrue(map.containsValue("quatro"));
        assertTrue(map.containsValue("two"));

        assertTrue(!map.containsValue("uggaBugga"));

        map.clear();
    }
    
    public void testValues() {
        MultiHashMap map = new MultiHashMap();
        loadMap(map);

        Collection vals = map.values();
        assertTrue(vals.size() == getFullSize());

        map.clear();
    }

    static private class MapPair {
        MapPair(int key, String val) {
            mKey = new Integer(key);
            mValue = val;
        }

        Integer mKey = null;
        String mValue = null;
    }
    
    static private MapPair[][] sMapPairs =
    {
        {new MapPair(0,"zero")},
        {new MapPair(1,"one"), new MapPair(1,"ONE"), new MapPair(1,"uno")},
        {new MapPair(2,"two"), new MapPair(2,"two") },
        {new MapPair(3,"three"), new MapPair(3,"THREE"), new MapPair(3,"tres")},
        {new MapPair(4,"four"), new MapPair(4,"quatro")}
    };
    
    private void loadMap(MultiHashMap map) {
        // Set up so that we load the keys "randomly"
        // (i.e. we don't want to load int row-order, so that all like keys
        // load together. We want to mix it up...)

        int numRows = sMapPairs.length;
        int maxCols = 0;
        for (int ii = 0; ii < sMapPairs.length; ii++) {
            if (sMapPairs[ii].length > maxCols) {
                maxCols = sMapPairs[ii].length;
            }
        }
        for (int ii = 0; ii < maxCols; ii++) {
            for (int jj = 0; jj < numRows; jj++) {
                if (ii < sMapPairs[jj].length) {
                    map.put(sMapPairs[jj][ii].mKey, sMapPairs[jj][ii].mValue);
                    //---------------------------------------------------------
                }
            }
        }
        assertTrue(map.size() == sMapPairs.length);
    }
    
    private void checkMap(MultiHashMap map) {
        for (int ii = 0; ii < sMapPairs.length; ii++) {
            checkKeyList(map, ii);
        }
    }

    private void checkKeyList(MultiHashMap map, int index) {
        assertTrue(index < sMapPairs.length);
        Integer key = sMapPairs[index][0].mKey;

        Object obj = map.get(key);
        //--------------------------

        assertTrue(obj != null);
        assertTrue(obj instanceof Collection);
        Collection keyList = (Collection) obj;

        assertTrue(keyList.size() == sMapPairs[index].length);
        Iterator iter = keyList.iterator();
        while (iter.hasNext()) {
            Object oval = iter.next();
            assertTrue(oval != null);
            assertTrue(oval instanceof String);
            String val = (String) oval;
            boolean foundIt = false;
            for (int ii = 0; ii < sMapPairs[index].length; ii++) {
                if (val.equals(sMapPairs[index][ii].mValue)) {
                    foundIt = true;
                }
            }
            assertTrue(foundIt);
        }
    }
    
    public int getFullSize() {
        int len = 0;
        for (int ii = 0; ii < sMapPairs.length; ii++) {
            len += sMapPairs[ii].length;
        }
        return len;
    }
    

    public void testEntrySetIterator() {
    }
    public void testEntrySetContainsProperMappings() {
    }
    public void testEntrySetIteratorHasProperMappings() {
        // override and ignore test -- it will fail when verifying the iterator for
        // the set contains the right value -- we're not returning the value, we're
        // returning a collection.
        // TODO: re-implement this test to ensure the values of the iterator match
        // the proper collection rather than the value the superclass is checking
        // for.
        return;
    }

    // Next methods are overriden because MultiHashMap values are always a
    // collection, and deviate from the Map contract because of this.

    // TODO: implement the tests to ensure that Map.get(Object) returns the
    // appropriate collection of values

    public void testMapGet() {
    }

    public void testMapPut() {
    }

    public void testMapPutAll() {
    }

    public void testMapRemove() {
    }

    public void testMapEquals() {
        MultiHashMap one = new MultiHashMap();
        Integer value = new Integer(1);
        one.put("One", value);
        one.remove("One", value);
        
        MultiHashMap two = new MultiHashMap();
        assertEquals(two, one);
    }

    public void testMapHashCode() {
    }

    // The verification for the map and its entry set must also be overridden
    // because the values are not going to be the same as the values in the
    // confirmed map (they're going to be collections of values instead).
    public void verifyMap() {
        // TODO: implement test to ensure that map is the same as confirmed if
        // its values were converted into collections.
    }

    public void verifyEntrySet() {
        // TODO: implement test to ensure that each entry is the same as one in
        // the confirmed map, but with the value wrapped in a collection.
    }

    // The verification method must be overridden because MultiHashMap's
    // values() is not properly backed by the map (Bug 9573).

    public void verifyValues() {
        // update the values view to the latest version, then proceed to verify
        // as usual.  
        values = map.values();
        super.verifyValues();
    }
    
    //-----------------------------------------------------------------------
    public void testGetCollection() {
        MultiHashMap map = new MultiHashMap();
        map.put("A", "AA");
        assertSame(map.get("A"), map.getCollection("A"));
    }
    
    public void testTotalSize() {
        MultiHashMap map = new MultiHashMap();
        assertEquals(0, map.totalSize());
        map.put("A", "AA");
        assertEquals(1, map.totalSize());
        map.put("B", "BA");
        assertEquals(2, map.totalSize());
        map.put("B", "BB");
        assertEquals(3, map.totalSize());
        map.put("B", "BC");
        assertEquals(4, map.totalSize());
        map.remove("A");
        assertEquals(3, map.totalSize());
        map.remove("B", "BC");
        assertEquals(2, map.totalSize());
    }
    
    public void testSize_Key() {
        MultiHashMap map = new MultiHashMap();
        assertEquals(0, map.size("A"));
        assertEquals(0, map.size("B"));
        map.put("A", "AA");
        assertEquals(1, map.size("A"));
        assertEquals(0, map.size("B"));
        map.put("B", "BA");
        assertEquals(1, map.size("A"));
        assertEquals(1, map.size("B"));
        map.put("B", "BB");
        assertEquals(1, map.size("A"));
        assertEquals(2, map.size("B"));
        map.put("B", "BC");
        assertEquals(1, map.size("A"));
        assertEquals(3, map.size("B"));
        map.remove("A");
        assertEquals(0, map.size("A"));
        assertEquals(3, map.size("B"));
        map.remove("B", "BC");
        assertEquals(0, map.size("A"));
        assertEquals(2, map.size("B"));
    }
    
    public void testIterator_Key() {
        MultiHashMap map = new MultiHashMap();
        assertEquals(false, map.iterator("A").hasNext());
        map.put("A", "AA");
        Iterator it = map.iterator("A");
        assertEquals(true, it.hasNext());
        it.next();
        assertEquals(false, it.hasNext());
    }
    
    public void testContainsValue_Key() {
        MultiHashMap map = new MultiHashMap();
        assertEquals(false, map.containsValue("A", "AA"));
        assertEquals(false, map.containsValue("B", "BB"));
        map.put("A", "AA");
        assertEquals(true, map.containsValue("A", "AA"));
        assertEquals(false, map.containsValue("A", "AB"));
    }

    public void testPutAll_Map1() {
        MultiMap original = new MultiHashMap();
        original.put("key", "object1");
        original.put("key", "object2");

        MultiHashMap test = new MultiHashMap();
        test.put("keyA", "objectA");
        test.put("key", "object0");
        test.putAll(original);

        assertEquals(2, test.size());
        assertEquals(4, test.totalSize());
        assertEquals(1, test.getCollection("keyA").size());
        assertEquals(3, test.getCollection("key").size());
        assertEquals(true, test.containsValue("objectA"));
        assertEquals(true, test.containsValue("object0"));
        assertEquals(true, test.containsValue("object1"));
        assertEquals(true, test.containsValue("object2"));
    }

    public void testPutAll_Map2() {
        Map original = new HashMap();
        original.put("keyX", "object1");
        original.put("keyY", "object2");

        MultiHashMap test = new MultiHashMap();
        test.put("keyA", "objectA");
        test.put("keyX", "object0");
        test.putAll(original);

        assertEquals(3, test.size());
        assertEquals(4, test.totalSize());
        assertEquals(1, test.getCollection("keyA").size());
        assertEquals(2, test.getCollection("keyX").size());
        assertEquals(1, test.getCollection("keyY").size());
        assertEquals(true, test.containsValue("objectA"));
        assertEquals(true, test.containsValue("object0"));
        assertEquals(true, test.containsValue("object1"));
        assertEquals(true, test.containsValue("object2"));
    }

    public void testPutAll_KeyCollection() {
        MultiHashMap map = new MultiHashMap();
        Collection coll = Arrays.asList(new Object[] {"X", "Y", "Z"});
        
        assertEquals(true, map.putAll("A", coll));
        assertEquals(3, map.size("A"));
        assertEquals(true, map.containsValue("A", "X"));
        assertEquals(true, map.containsValue("A", "Y"));
        assertEquals(true, map.containsValue("A", "Z"));
        
        assertEquals(false, map.putAll("A", null));
        assertEquals(3, map.size("A"));
        assertEquals(true, map.containsValue("A", "X"));
        assertEquals(true, map.containsValue("A", "Y"));
        assertEquals(true, map.containsValue("A", "Z"));
        
        assertEquals(false, map.putAll("A", new ArrayList()));
        assertEquals(3, map.size("A"));
        assertEquals(true, map.containsValue("A", "X"));
        assertEquals(true, map.containsValue("A", "Y"));
        assertEquals(true, map.containsValue("A", "Z"));
        
        coll = Arrays.asList(new Object[] {"M"});
        assertEquals(true, map.putAll("A", coll));
        assertEquals(4, map.size("A"));
        assertEquals(true, map.containsValue("A", "X"));
        assertEquals(true, map.containsValue("A", "Y"));
        assertEquals(true, map.containsValue("A", "Z"));
        assertEquals(true, map.containsValue("A", "M"));
    }

    public void testClone() {
        MultiHashMap map = new MultiHashMap();
        map.put("A", "1");
        map.put("A", "2");
        Collection coll = (Collection) map.get("A");
        assertEquals(1, map.size());
        assertEquals(2, coll.size());
        
        MultiHashMap cloned = (MultiHashMap) map.clone();
        Collection clonedColl = (Collection) cloned.get("A");
        assertNotSame(map, cloned);
        assertNotSame(coll, clonedColl);
        assertEquals(1, map.size());
        assertEquals(2, coll.size());
        assertEquals(1, cloned.size());
        assertEquals(2, clonedColl.size());
        map.put("A", "3");
        assertEquals(1, map.size());
        assertEquals(3, coll.size());
        assertEquals(1, cloned.size());
        assertEquals(2, clonedColl.size());
    }

    public void testConstructorCopy1() {
        MultiHashMap map = new MultiHashMap();
        map.put("A", "1");
        map.put("A", "2");
        Collection coll = (Collection) map.get("A");
        assertEquals(1, map.size());
        assertEquals(2, coll.size());
        
        MultiHashMap newMap = new MultiHashMap(map);
        Collection newColl = (Collection) newMap.get("A");
        assertNotSame(map, newMap);
        assertNotSame(coll, newColl);
        assertEquals(1, map.size());
        assertEquals(2, coll.size());
        assertEquals(1, newMap.size());
        assertEquals(2, newColl.size());
        
        map.put("A", "3");
        assertEquals(1, map.size());
        assertEquals(3, coll.size());
        assertEquals(1, newMap.size());
        assertEquals(2, newColl.size());
    }

    public void testConstructorCopy2() {
        Map map = new HashMap();
        map.put("A", "1");
        map.put("B", "2");
        assertEquals(2, map.size());
        
        MultiHashMap newMap = new MultiHashMap(map);
        Collection newColl = (Collection) newMap.get("A");
        assertNotSame(map, newMap);
        assertEquals(2, map.size());
        assertEquals(2, newMap.size());
        assertEquals(1, newColl.size());
        
        map.put("A", "3");
        assertEquals(2, map.size());
        assertEquals(2, newMap.size());
        assertEquals(1, newColl.size());
        
        map.put("C", "4");
        assertEquals(3, map.size());
        assertEquals(2, newMap.size());
        assertEquals(1, newColl.size());
    }

    public void testRemove_KeyItem() {
        MultiHashMap map = new MultiHashMap();
        map.put("A", "AA");
        map.put("A", "AB");
        map.put("A", "AC");
        assertEquals(null, map.remove("C", "CA"));
        assertEquals(null, map.remove("A", "AD"));
        assertEquals("AC", map.remove("A", "AC"));
        assertEquals("AB", map.remove("A", "AB"));
        assertEquals("AA", map.remove("A", "AA"));
        assertEquals(new MultiHashMap(), map);
    }

}
