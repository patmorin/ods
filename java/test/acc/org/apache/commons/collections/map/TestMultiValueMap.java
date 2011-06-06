/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.TestMultiHashMap;

/**
 * TestMultiValueMap.
 *
 * @author <a href="mailto:jcarman@apache.org">James Carman</a>
 * @author Stephen Colebourne
 * @since Commons Collections 3.2
 */
public class TestMultiValueMap extends TestCase {

    public TestMultiValueMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestMultiHashMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestMultiHashMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public void testNoMappingReturnsNull() {
        final MultiValueMap map = createTestMap();
        assertNull(map.get("whatever"));
    }

    public void testValueCollectionType() {
        final MultiValueMap map = createTestMap(LinkedList.class);
        assertTrue(map.get("one") instanceof LinkedList);
    }

    public void testMultipleValues() {
        final MultiValueMap map = createTestMap(HashSet.class);
        final HashSet expected = new HashSet();
        expected.add("uno");
        expected.add("un");
        assertEquals(expected, map.get("one"));
    }

    public void testContainsValue() {
        final MultiValueMap map = createTestMap(HashSet.class);
        assertTrue(map.containsValue("uno"));
        assertTrue(map.containsValue("un"));
        assertTrue(map.containsValue("dos"));
        assertTrue(map.containsValue("deux"));
        assertTrue(map.containsValue("tres"));
        assertTrue(map.containsValue("trois"));
        assertFalse(map.containsValue("quatro"));
    }

    public void testKeyContainsValue() {
        final MultiValueMap map = createTestMap(HashSet.class);
        assertTrue(map.containsValue("one", "uno"));
        assertTrue(map.containsValue("one", "un"));
        assertTrue(map.containsValue("two", "dos"));
        assertTrue(map.containsValue("two", "deux"));
        assertTrue(map.containsValue("three", "tres"));
        assertTrue(map.containsValue("three", "trois"));
        assertFalse(map.containsValue("four", "quatro"));
    }

    public void testValues() {
        final MultiValueMap map = createTestMap(HashSet.class);
        final HashSet expected = new HashSet();
        expected.add("uno");
        expected.add("dos");
        expected.add("tres");
        expected.add("un");
        expected.add("deux");
        expected.add("trois");
        final Collection c = map.values();
        assertEquals(6, c.size());
        assertEquals(expected, new HashSet(c));
    }

    private MultiValueMap createTestMap() {
        return createTestMap(ArrayList.class);
    }

    private MultiValueMap createTestMap(Class collectionClass) {
        final MultiValueMap map = MultiValueMap.decorate(new HashMap(), collectionClass);
        map.put("one", "uno");
        map.put("one", "un");
        map.put("two", "dos");
        map.put("two", "deux");
        map.put("three", "tres");
        map.put("three", "trois");
        return map;
    }

    public void testKeyedIterator() {
        final MultiValueMap map = createTestMap();
        final ArrayList actual = new ArrayList(IteratorUtils.toList(map.iterator("one")));
        final ArrayList expected = new ArrayList(Arrays.asList(new String[]{"uno", "un"}));
        assertEquals(expected, actual);
    }

    public void testRemoveAllViaIterator() {
        final MultiValueMap map = createTestMap();
        for(Iterator i = map.values().iterator(); i.hasNext();) {
            i.next();
            i.remove();
        }
        assertNull(map.get("one"));
        assertTrue(map.isEmpty());
    }

    public void testRemoveAllViaKeyedIterator() {
        final MultiValueMap map = createTestMap();
        for(Iterator i = map.iterator("one"); i.hasNext();) {
            i.next();
            i.remove();
        }
        assertNull(map.get("one"));
        assertEquals(4, map.totalSize());
    }

    public void testTotalSizeA() {
        assertEquals(6, createTestMap().totalSize());
    }

    //-----------------------------------------------------------------------
    public void testMapEquals() {
        MultiValueMap one = new MultiValueMap();
        Integer value = new Integer(1);
        one.put("One", value);
        one.remove("One", value);
        
        MultiValueMap two = new MultiValueMap();
        assertEquals(two, one);
    }

    //-----------------------------------------------------------------------
    public void testGetCollection() {
        MultiValueMap map = new MultiValueMap();
        map.put("A", "AA");
        assertSame(map.get("A"), map.getCollection("A"));
    }
    
    public void testTotalSize() {
        MultiValueMap map = new MultiValueMap();
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
    
    public void testSize() {
        MultiValueMap map = new MultiValueMap();
        assertEquals(0, map.size());
        map.put("A", "AA");
        assertEquals(1, map.size());
        map.put("B", "BA");
        assertEquals(2, map.size());
        map.put("B", "BB");
        assertEquals(2, map.size());
        map.put("B", "BC");
        assertEquals(2, map.size());
        map.remove("A");
        assertEquals(2, map.size());
        map.remove("B", "BC");
        assertEquals(2, map.size());
    }
    
    public void testSize_Key() {
        MultiValueMap map = new MultiValueMap();
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
        MultiValueMap map = new MultiValueMap();
        assertEquals(false, map.iterator("A").hasNext());
        map.put("A", "AA");
        Iterator it = map.iterator("A");
        assertEquals(true, it.hasNext());
        it.next();
        assertEquals(false, it.hasNext());
    }
    
    public void testContainsValue_Key() {
        MultiValueMap map = new MultiValueMap();
        assertEquals(false, map.containsValue("A", "AA"));
        assertEquals(false, map.containsValue("B", "BB"));
        map.put("A", "AA");
        assertEquals(true, map.containsValue("A", "AA"));
        assertEquals(false, map.containsValue("A", "AB"));
    }

    public void testPutAll_Map1() {
        MultiMap original = new MultiValueMap();
        original.put("key", "object1");
        original.put("key", "object2");

        MultiValueMap test = new MultiValueMap();
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

        MultiValueMap test = new MultiValueMap();
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
        MultiValueMap map = new MultiValueMap();
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

    public void testRemove_KeyItem() {
        MultiValueMap map = new MultiValueMap();
        map.put("A", "AA");
        map.put("A", "AB");
        map.put("A", "AC");
        assertEquals(null, map.remove("C", "CA"));
        assertEquals(null, map.remove("A", "AD"));
        assertEquals("AC", map.remove("A", "AC"));
        assertEquals("AB", map.remove("A", "AB"));
        assertEquals("AA", map.remove("A", "AA"));
        assertEquals(new MultiValueMap(), map);
    }

}
