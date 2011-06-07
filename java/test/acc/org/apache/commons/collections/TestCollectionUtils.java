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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.collections.buffer.BoundedFifoBuffer;
import org.apache.commons.collections.collection.AbstractTestCollection;
import org.apache.commons.collections.collection.PredicatedCollection;
import org.apache.commons.collections.collection.SynchronizedCollection;
import org.apache.commons.collections.collection.TransformedCollection;
import org.apache.commons.collections.collection.UnmodifiableCollection;

/**
 * Tests for CollectionUtils.
 * 
 * @author Rodney Waldhoff
 * @author Matthew Hawthorne
 * @author Stephen Colebourne
 * @author Phil Steitz
 * @author Steven Melzer
 * @author Neil O'Toole
 * @author Stephen Smith
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 */
public class TestCollectionUtils extends TestCase {
    
    public TestCollectionUtils(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestCollectionUtils.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestCollectionUtils.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    private Collection collectionA = null;
    private Collection collectionB = null;

    public void setUp() {
        collectionA = new ArrayList();
        collectionA.add("a");
        collectionA.add("b");
        collectionA.add("b");
        collectionA.add("c");
        collectionA.add("c");
        collectionA.add("c");
        collectionA.add("d");
        collectionA.add("d");
        collectionA.add("d");
        collectionA.add("d");
        collectionB = new LinkedList();
        collectionB.add("e");
        collectionB.add("d");
        collectionB.add("d");
        collectionB.add("c");
        collectionB.add("c");
        collectionB.add("c");
        collectionB.add("b");
        collectionB.add("b");
        collectionB.add("b");
        collectionB.add("b");

    }

    public void testGetCardinalityMap() {
        Map freq = CollectionUtils.getCardinalityMap(collectionA);
        assertEquals(new Integer(1),freq.get("a"));
        assertEquals(new Integer(2),freq.get("b"));
        assertEquals(new Integer(3),freq.get("c"));
        assertEquals(new Integer(4),freq.get("d"));
        assertNull(freq.get("e"));

        freq = CollectionUtils.getCardinalityMap(collectionB);
        assertNull(freq.get("a"));
        assertEquals(new Integer(4),freq.get("b"));
        assertEquals(new Integer(3),freq.get("c"));
        assertEquals(new Integer(2),freq.get("d"));
        assertEquals(new Integer(1),freq.get("e"));
    }

    public void testCardinality() {
        assertEquals(1, CollectionUtils.cardinality("a", collectionA));
        assertEquals(2, CollectionUtils.cardinality("b", collectionA));
        assertEquals(3, CollectionUtils.cardinality("c", collectionA));
        assertEquals(4, CollectionUtils.cardinality("d", collectionA));
        assertEquals(0, CollectionUtils.cardinality("e", collectionA));

        assertEquals(0, CollectionUtils.cardinality("a", collectionB));
        assertEquals(4, CollectionUtils.cardinality("b", collectionB));
        assertEquals(3, CollectionUtils.cardinality("c", collectionB));
        assertEquals(2, CollectionUtils.cardinality("d", collectionB));
        assertEquals(1, CollectionUtils.cardinality("e", collectionB));

        Set set = new HashSet();
        set.add("A");
        set.add("C");
        set.add("E");
        set.add("E");
        assertEquals(1, CollectionUtils.cardinality("A", set));
        assertEquals(0, CollectionUtils.cardinality("B", set));
        assertEquals(1, CollectionUtils.cardinality("C", set));
        assertEquals(0, CollectionUtils.cardinality("D", set));
        assertEquals(1, CollectionUtils.cardinality("E", set));

        Bag bag = new HashBag();
        bag.add("A", 3);
        bag.add("C");
        bag.add("E");
        bag.add("E");
        assertEquals(3, CollectionUtils.cardinality("A", bag));
        assertEquals(0, CollectionUtils.cardinality("B", bag));
        assertEquals(1, CollectionUtils.cardinality("C", bag));
        assertEquals(0, CollectionUtils.cardinality("D", bag));
        assertEquals(2, CollectionUtils.cardinality("E", bag));
    }
    
    public void testCardinalityOfNull() {
        List list = new ArrayList();
        assertEquals(0,CollectionUtils.cardinality(null,list));
        {
            Map freq = CollectionUtils.getCardinalityMap(list);
            assertNull(freq.get(null));
        }
        list.add("A");
        assertEquals(0,CollectionUtils.cardinality(null,list));
        {
            Map freq = CollectionUtils.getCardinalityMap(list);
            assertNull(freq.get(null));
        }
        list.add(null);
        assertEquals(1,CollectionUtils.cardinality(null,list));
        {
            Map freq = CollectionUtils.getCardinalityMap(list);
            assertEquals(new Integer(1),freq.get(null));
        }
        list.add("B");
        assertEquals(1,CollectionUtils.cardinality(null,list));
        {
            Map freq = CollectionUtils.getCardinalityMap(list);
            assertEquals(new Integer(1),freq.get(null));
        }
        list.add(null);
        assertEquals(2,CollectionUtils.cardinality(null,list));
        {
            Map freq = CollectionUtils.getCardinalityMap(list);
            assertEquals(new Integer(2),freq.get(null));
        }
        list.add("B");
        assertEquals(2,CollectionUtils.cardinality(null,list));
        {
            Map freq = CollectionUtils.getCardinalityMap(list);
            assertEquals(new Integer(2),freq.get(null));
        }
        list.add(null);
        assertEquals(3,CollectionUtils.cardinality(null,list));
        {
            Map freq = CollectionUtils.getCardinalityMap(list);
            assertEquals(new Integer(3),freq.get(null));
        }
    }

    public void testContainsAny() {
        Collection empty = new ArrayList(0);
        Collection one = new ArrayList(1);
        one.add("1");
        Collection two = new ArrayList(1);
        two.add("2");
        Collection three = new ArrayList(1);
        three.add("3");
        Collection odds = new ArrayList(2);
        odds.add("1");
        odds.add("3");
        
        assertTrue("containsAny({1},{1,3}) should return true.",
            CollectionUtils.containsAny(one,odds));
        assertTrue("containsAny({1,3},{1}) should return true.",
            CollectionUtils.containsAny(odds,one));
        assertTrue("containsAny({3},{1,3}) should return true.",
            CollectionUtils.containsAny(three,odds));
        assertTrue("containsAny({1,3},{3}) should return true.",
            CollectionUtils.containsAny(odds,three));
        assertTrue("containsAny({2},{2}) should return true.",
            CollectionUtils.containsAny(two,two));
        assertTrue("containsAny({1,3},{1,3}) should return true.",
            CollectionUtils.containsAny(odds,odds));
        
        assertTrue("containsAny({2},{1,3}) should return false.",
            !CollectionUtils.containsAny(two,odds));
        assertTrue("containsAny({1,3},{2}) should return false.",
            !CollectionUtils.containsAny(odds,two));
        assertTrue("containsAny({1},{3}) should return false.",
            !CollectionUtils.containsAny(one,three));
        assertTrue("containsAny({3},{1}) should return false.",
            !CollectionUtils.containsAny(three,one));
        assertTrue("containsAny({1,3},{}) should return false.",
            !CollectionUtils.containsAny(odds,empty));
        assertTrue("containsAny({},{1,3}) should return false.",
            !CollectionUtils.containsAny(empty,odds));
        assertTrue("containsAny({},{}) should return false.",
            !CollectionUtils.containsAny(empty,empty));
    }

    public void testUnion() {
        Collection col = CollectionUtils.union(collectionA,collectionB);
        Map freq = CollectionUtils.getCardinalityMap(col);
        assertEquals(new Integer(1),freq.get("a"));
        assertEquals(new Integer(4),freq.get("b"));
        assertEquals(new Integer(3),freq.get("c"));
        assertEquals(new Integer(4),freq.get("d"));
        assertEquals(new Integer(1),freq.get("e"));

        Collection col2 = CollectionUtils.union(collectionB,collectionA);
        Map freq2 = CollectionUtils.getCardinalityMap(col2);
        assertEquals(new Integer(1),freq2.get("a"));
        assertEquals(new Integer(4),freq2.get("b"));
        assertEquals(new Integer(3),freq2.get("c"));
        assertEquals(new Integer(4),freq2.get("d"));
        assertEquals(new Integer(1),freq2.get("e"));        
    }

    public void testIntersection() {
        Collection col = CollectionUtils.intersection(collectionA,collectionB);
        Map freq = CollectionUtils.getCardinalityMap(col);
        assertNull(freq.get("a"));
        assertEquals(new Integer(2),freq.get("b"));
        assertEquals(new Integer(3),freq.get("c"));
        assertEquals(new Integer(2),freq.get("d"));
        assertNull(freq.get("e"));

        Collection col2 = CollectionUtils.intersection(collectionB,collectionA);
        Map freq2 = CollectionUtils.getCardinalityMap(col2);
        assertNull(freq2.get("a"));
        assertEquals(new Integer(2),freq2.get("b"));
        assertEquals(new Integer(3),freq2.get("c"));
        assertEquals(new Integer(2),freq2.get("d"));
        assertNull(freq2.get("e"));      
    }

    public void testDisjunction() {
        Collection col = CollectionUtils.disjunction(collectionA,collectionB);
        Map freq = CollectionUtils.getCardinalityMap(col);
        assertEquals(new Integer(1),freq.get("a"));
        assertEquals(new Integer(2),freq.get("b"));
        assertNull(freq.get("c"));
        assertEquals(new Integer(2),freq.get("d"));
        assertEquals(new Integer(1),freq.get("e"));

        Collection col2 = CollectionUtils.disjunction(collectionB,collectionA);
        Map freq2 = CollectionUtils.getCardinalityMap(col2);
        assertEquals(new Integer(1),freq2.get("a"));
        assertEquals(new Integer(2),freq2.get("b"));
        assertNull(freq2.get("c"));
        assertEquals(new Integer(2),freq2.get("d"));
        assertEquals(new Integer(1),freq2.get("e"));
    }

    public void testDisjunctionAsUnionMinusIntersection() {
        Collection dis = CollectionUtils.disjunction(collectionA,collectionB);
        Collection un = CollectionUtils.union(collectionA,collectionB);
        Collection inter = CollectionUtils.intersection(collectionA,collectionB);
        assertTrue(CollectionUtils.isEqualCollection(dis,CollectionUtils.subtract(un,inter)));
    }

    public void testDisjunctionAsSymmetricDifference() {
        Collection dis = CollectionUtils.disjunction(collectionA,collectionB);
        Collection amb = CollectionUtils.subtract(collectionA,collectionB);
        Collection bma = CollectionUtils.subtract(collectionB,collectionA);
        assertTrue(CollectionUtils.isEqualCollection(dis,CollectionUtils.union(amb,bma)));
    }

    public void testSubtract() {
        Collection col = CollectionUtils.subtract(collectionA,collectionB);
        Map freq = CollectionUtils.getCardinalityMap(col);
        assertEquals(new Integer(1),freq.get("a"));
        assertNull(freq.get("b"));
        assertNull(freq.get("c"));
        assertEquals(new Integer(2),freq.get("d"));
        assertNull(freq.get("e"));

        Collection col2 = CollectionUtils.subtract(collectionB,collectionA);
        Map freq2 = CollectionUtils.getCardinalityMap(col2);
        assertEquals(new Integer(1),freq2.get("e"));
        assertNull(freq2.get("d"));
        assertNull(freq2.get("c"));
        assertEquals(new Integer(2),freq2.get("b"));
        assertNull(freq2.get("a"));
    }

    public void testIsSubCollectionOfSelf() {
        assertTrue(CollectionUtils.isSubCollection(collectionA,collectionA));
        assertTrue(CollectionUtils.isSubCollection(collectionB,collectionB));
    }

    public void testIsSubCollection() {
        assertTrue(!CollectionUtils.isSubCollection(collectionA,collectionB));
        assertTrue(!CollectionUtils.isSubCollection(collectionB,collectionA));
    }

    public void testIsSubCollection2() {
        Collection c = new ArrayList();
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("a");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("b");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("b");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("c");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("c");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("c");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("d");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("d");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("d");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(!CollectionUtils.isSubCollection(collectionA,c));
        c.add("d");
        assertTrue(CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(CollectionUtils.isSubCollection(collectionA,c));
        c.add("e");
        assertTrue(!CollectionUtils.isSubCollection(c,collectionA));
        assertTrue(CollectionUtils.isSubCollection(collectionA,c));
    }

    public void testIsEqualCollectionToSelf() {
        assertTrue(CollectionUtils.isEqualCollection(collectionA,collectionA));
        assertTrue(CollectionUtils.isEqualCollection(collectionB,collectionB));
    }

    public void testIsEqualCollection() {
        assertTrue(!CollectionUtils.isEqualCollection(collectionA,collectionB));
        assertTrue(!CollectionUtils.isEqualCollection(collectionB,collectionA));
    }

    public void testIsEqualCollection2() {
        Collection a = new ArrayList();
        Collection b = new ArrayList();
        assertTrue(CollectionUtils.isEqualCollection(a,b));
        assertTrue(CollectionUtils.isEqualCollection(b,a));
        a.add("1");
        assertTrue(!CollectionUtils.isEqualCollection(a,b));
        assertTrue(!CollectionUtils.isEqualCollection(b,a));
        b.add("1");
        assertTrue(CollectionUtils.isEqualCollection(a,b));
        assertTrue(CollectionUtils.isEqualCollection(b,a));
        a.add("2");
        assertTrue(!CollectionUtils.isEqualCollection(a,b));
        assertTrue(!CollectionUtils.isEqualCollection(b,a));
        b.add("2");
        assertTrue(CollectionUtils.isEqualCollection(a,b));
        assertTrue(CollectionUtils.isEqualCollection(b,a));
        a.add("1");
        assertTrue(!CollectionUtils.isEqualCollection(a,b));
        assertTrue(!CollectionUtils.isEqualCollection(b,a));
        b.add("1");
        assertTrue(CollectionUtils.isEqualCollection(a,b));
        assertTrue(CollectionUtils.isEqualCollection(b,a));
    }
    
    public void testIsProperSubCollection() {
        Collection a = new ArrayList();
        Collection b = new ArrayList();
        assertTrue(!CollectionUtils.isProperSubCollection(a,b));
        b.add("1");
        assertTrue(CollectionUtils.isProperSubCollection(a,b));
        assertTrue(!CollectionUtils.isProperSubCollection(b,a));
        assertTrue(!CollectionUtils.isProperSubCollection(b,b));
        assertTrue(!CollectionUtils.isProperSubCollection(a,a));
        a.add("1");
        a.add("2");
        b.add("2");
        assertTrue(!CollectionUtils.isProperSubCollection(b,a));
        assertTrue(!CollectionUtils.isProperSubCollection(a,b));
        a.add("1");
        assertTrue(CollectionUtils.isProperSubCollection(b,a));
        assertTrue(CollectionUtils.isProperSubCollection(
            CollectionUtils.intersection(collectionA, collectionB), collectionA));
        assertTrue(CollectionUtils.isProperSubCollection(
            CollectionUtils.subtract(a, b), a));
        assertTrue(!CollectionUtils.isProperSubCollection(
            a, CollectionUtils.subtract(a, b)));
    }
    
    public void testFind() {
        Predicate testPredicate = PredicateUtils.equalPredicate("d");
        Object test = CollectionUtils.find(collectionA, testPredicate);
        assertTrue(test.equals("d"));
        testPredicate = PredicateUtils.equalPredicate("de");
        test = CollectionUtils.find(collectionA, testPredicate);
        assertTrue(test == null);
        assertEquals(CollectionUtils.find(null,testPredicate), null);
        assertEquals(CollectionUtils.find(collectionA, null), null);
    }
    
    public void testForAllDo() {
        Closure testClosure = ClosureUtils.invokerClosure("clear");
        Collection col = new ArrayList();
        col.add(collectionA);
        col.add(collectionB);
        CollectionUtils.forAllDo(col, testClosure);
        assertTrue(collectionA.isEmpty() && collectionB.isEmpty());
        CollectionUtils.forAllDo(col, null);
        assertTrue(collectionA.isEmpty() && collectionB.isEmpty());
        CollectionUtils.forAllDo(null, testClosure);
        col.add(null);
        // null should be OK
        CollectionUtils.forAllDo(col, testClosure);
        col.add("x");
        // This will lead to FunctorException
        try {
            CollectionUtils.forAllDo(col, testClosure);
            fail("Expecting FunctorException");
        } catch (FunctorException ex) {
            // expected from invoker -- method not found
        }
    }

    public void testIndex() {     
        // normal map behavior when index is an Integer and a key
        Map map = new HashMap();
        map.put(new Integer(0), "zero");
        map.put(new Integer(-1), "minusOne");
        Object test = CollectionUtils.index(map, 0);
        assertTrue(test.equals("zero"));
        test = CollectionUtils.index(map, new Integer(-1));
        assertTrue(test.equals("minusOne"));
        
        // map, non-integer key that does not exist -- map returned
        test = CollectionUtils.index(map, "missing");
        assertTrue(test.equals(map));
        
        // map, integer not a key, valid index -- "some" element of keyset returned
        test = CollectionUtils.index(map, new Integer(1));   
        assertTrue(map.keySet().contains(test)); 
        
        // map, integer not a key, not valid index -- "dead" keyset iterator returned
        test = CollectionUtils.index(map, new Integer(4));         
        assertTrue((test instanceof Iterator) && !((Iterator) test).hasNext());  

        // sorted map, integer not a key, valid index -- ith key returned
        SortedMap map2 = new TreeMap();
        map2.put(new Integer(23), "u");
        map2.put(new Integer(21), "x");
        map2.put(new Integer(17), "v");
        map2.put(new Integer(42), "w");
        Integer val = (Integer) CollectionUtils.index(map2, 0);
        assertTrue(val.intValue() == 17);
        val = (Integer) CollectionUtils.index(map2, 1);
        assertTrue(val.intValue() == 21);
        val = (Integer) CollectionUtils.index(map2, 2);
        assertTrue(val.intValue() == 23);
        val = (Integer) CollectionUtils.index(map2, 3);
        assertTrue(val.intValue() == 42);   
                
        // list, entry exists
        List list = new ArrayList();
        list.add("zero");
        list.add("one");
        test = CollectionUtils.index(list, 0);
        assertTrue(test.equals("zero"));
        test = CollectionUtils.index(list, 1);
        assertTrue(test.equals("one"));
        
        // list, non-existent entry -- IndexOutOfBoundsException
        try {
            test = CollectionUtils.index(list, 2);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        
        // iterator, entry exists
        Iterator iterator = list.iterator();
        test = CollectionUtils.index(iterator,0);
        assertTrue(test.equals("zero"));
        iterator = list.iterator();
        test = CollectionUtils.index(iterator,1);
        assertTrue(test.equals("one"));
        
        // iterator, non-existent entry -- "dead" iterator returned
        test = CollectionUtils.index(iterator,3);
        assertTrue(test.equals(iterator) && !iterator.hasNext());
        
        // Enumeration, entry exists
        Vector vector = new Vector(list);
        Enumeration en = vector.elements();
        test = CollectionUtils.index(en,0);
        assertTrue(test.equals("zero"));
        en = vector.elements();
        test = CollectionUtils.index(en,1);
        assertTrue(test.equals("one"));
        
        // Enumeration, non-existent entry -- "dead" enumerator returned
        test = CollectionUtils.index(en,3);
        assertTrue(test.equals(en) && !en.hasMoreElements());
        
        // Collection, entry exists
        Bag bag = new HashBag();
        bag.add("element", 1);
        test = CollectionUtils.index(bag, 0);
        assertTrue(test.equals("element"));
        
        // Collection, non-existent entry -- "dead" iterator returned
        test = CollectionUtils.index(bag, 2);
        assertTrue((test instanceof Iterator) && !((Iterator) test).hasNext()); 
        
        // Object array, entry exists
        Object[] objArray = new Object[2];
        objArray[0] = "zero";
        objArray[1] = "one";
        test = CollectionUtils.index(objArray,0);
        assertTrue(test.equals("zero"));
        test = CollectionUtils.index(objArray,1);
        assertTrue(test.equals("one"));
        
        // Object array, non-existent entry -- ArrayIndexOutOfBoundsException
        try {
            test = CollectionUtils.index(objArray,2);
            fail("Expecting ArrayIndexOutOfBoundsException.");
        } catch (ArrayIndexOutOfBoundsException ex) {
            // expected
        }
        
        // Non-collection object -- returned unchanged
        Object obj = new Object();
        test = CollectionUtils.index(obj, obj);
        assertTrue(test.equals(obj));
    }
    
    public void testGet() {     
        {
            // Unordered map, entries exist
            Map expected = new HashMap();
            expected.put("zeroKey", "zero");
            expected.put("oneKey", "one");
        
            Map found = new HashMap();
            Map.Entry entry = (Map.Entry)(CollectionUtils.get(expected, 0));
            found.put(entry.getKey(),entry.getValue());
            entry = (Map.Entry)(CollectionUtils.get(expected, 1));
            found.put(entry.getKey(),entry.getValue());
            assertEquals(expected,found);
        
            // Map index out of range
            try {
                CollectionUtils.get(expected,  2);
                fail("Expecting IndexOutOfBoundsException.");
            } catch (IndexOutOfBoundsException e) {
                // expected
            }
            try {
                CollectionUtils.get(expected,  -2);
                fail("Expecting IndexOutOfBoundsException.");
            } catch (IndexOutOfBoundsException e) {
                // expected
            }
        }

        {
            // Sorted map, entries exist, should respect order
            SortedMap map = new TreeMap();
            map.put("zeroKey", "zero");
            map.put("oneKey", "one");
            Object test = CollectionUtils.get(map, 1);
            assertEquals("zeroKey",((Map.Entry) test).getKey());
            assertEquals("zero",((Map.Entry) test).getValue());
            test = CollectionUtils.get(map, 0);
            assertEquals("oneKey",((Map.Entry) test).getKey());
            assertEquals("one",((Map.Entry) test).getValue());
        }
                
        {
            // List, entry exists
            List list = new ArrayList();
            list.add("zero");
            list.add("one");
            assertEquals("zero",CollectionUtils.get(list, 0));
            assertEquals("one",CollectionUtils.get(list, 1));
            // list, non-existent entry -- IndexOutOfBoundsException
            try {
                CollectionUtils.get(list, 2);
                fail("Expecting IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException e) {
                // expected
            }

            // Iterator, entry exists
            Iterator iterator = list.iterator();
            assertEquals("zero",CollectionUtils.get(iterator,0));
            iterator = list.iterator();
            assertEquals("one",CollectionUtils.get(iterator,1));
        
            // Iterator, non-existent entry 
            try {
                CollectionUtils.get(iterator,3);
                fail("Expecting IndexOutOfBoundsException.");
            } catch (IndexOutOfBoundsException e) {
                // expected
            }
            assertTrue(!iterator.hasNext());
        }
        
        {
            // Enumeration, entry exists
            Vector vector = new Vector();
            vector.addElement("zero");
            vector.addElement("one");
            Enumeration en = vector.elements();
            assertEquals("zero",CollectionUtils.get(en,0));
            en = vector.elements();
            assertEquals("one",CollectionUtils.get(en,1));
        
            // Enumerator, non-existent entry 
            try {
                CollectionUtils.get(en,3);
                fail("Expecting IndexOutOfBoundsException.");
            } catch (IndexOutOfBoundsException e) {
                // expected
            }
            assertTrue(!en.hasMoreElements());
        }
        
        {
            // Collection, entry exists
            Bag bag = new HashBag();
            bag.add("element", 1);
            assertEquals("element",CollectionUtils.get(bag, 0));
        
            // Collection, non-existent entry
            try {
                CollectionUtils.get(bag, 1);
                fail("Expceting IndexOutOfBoundsException.");
            } catch (IndexOutOfBoundsException e) {
                // expected
            }
        }
        
        {
            // Object array, entry exists
            Object[] objArray = new Object[2];
            objArray[0] = "zero";
            objArray[1] = "one";
            assertEquals("zero",CollectionUtils.get(objArray,0));
            assertEquals("one",CollectionUtils.get(objArray,1));
        
            // Object array, non-existent entry -- ArrayIndexOutOfBoundsException
            try {
                CollectionUtils.get(objArray,2);
                fail("Expecting IndexOutOfBoundsException.");
            } catch (IndexOutOfBoundsException ex) {
                // expected
            }
        }
        
        {
            // Primitive array, entry exists
            int[] array = new int[2];
            array[0] = 10;
            array[1] = 20;
            assertEquals(new Integer(10), CollectionUtils.get(array,0));
            assertEquals(new Integer(20), CollectionUtils.get(array,1));
        
            // Object array, non-existent entry -- ArrayIndexOutOfBoundsException
            try {
                CollectionUtils.get(array,2);
                fail("Expecting IndexOutOfBoundsException.");
            } catch (IndexOutOfBoundsException ex) {
                // expected
            }
        }
        
        {
            // Invalid object
            Object obj = new Object();
            try {
                CollectionUtils.get(obj, 0);
                fail("Expecting IllegalArgumentException.");
            } catch (IllegalArgumentException e) {
                // expected
            }
            try {
                CollectionUtils.get(null, 0);
                fail("Expecting IllegalArgumentException.");
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    //-----------------------------------------------------------------------
    public void testSize_List() {
        List list = new ArrayList();
        assertEquals(0, CollectionUtils.size(list));
        list.add("a");
        assertEquals(1, CollectionUtils.size(list));
        list.add("b");
        assertEquals(2, CollectionUtils.size(list));
    }
    public void testSize_Map() {
        Map map = new HashMap();
        assertEquals(0, CollectionUtils.size(map));
        map.put("1", "a");
        assertEquals(1, CollectionUtils.size(map));
        map.put("2", "b");
        assertEquals(2, CollectionUtils.size(map));
    }
    public void testSize_Array() {
        Object[] objectArray = new Object[0];
        assertEquals(0, CollectionUtils.size(objectArray));
        
        String[] stringArray = new String[3];
        assertEquals(3, CollectionUtils.size(stringArray));
        stringArray[0] = "a";
        stringArray[1] = "b";
        stringArray[2] = "c";
        assertEquals(3, CollectionUtils.size(stringArray));
    }
    public void testSize_PrimitiveArray() {
        int[] intArray = new int[0];
        assertEquals(0, CollectionUtils.size(intArray));
        
        double[] doubleArray = new double[3];
        assertEquals(3, CollectionUtils.size(doubleArray));
        doubleArray[0] = 0.0d;
        doubleArray[1] = 1.0d;
        doubleArray[2] = 2.5d;
        assertEquals(3, CollectionUtils.size(doubleArray));
    }
    public void testSize_Enumeration() {
        Vector list = new Vector();
        assertEquals(0, CollectionUtils.size(list.elements()));
        list.add("a");
        assertEquals(1, CollectionUtils.size(list.elements()));
        list.add("b");
        assertEquals(2, CollectionUtils.size(list.elements()));
    }
    public void testSize_Iterator() {
        List list = new ArrayList();
        assertEquals(0, CollectionUtils.size(list.iterator()));
        list.add("a");
        assertEquals(1, CollectionUtils.size(list.iterator()));
        list.add("b");
        assertEquals(2, CollectionUtils.size(list.iterator()));
    }
    public void testSize_Other() {
        try {
            CollectionUtils.size(null);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
        try {
            CollectionUtils.size("not a list");
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    //-----------------------------------------------------------------------
    public void testSizeIsEmpty_List() {
        List list = new ArrayList();
        assertEquals(true, CollectionUtils.sizeIsEmpty(list));
        list.add("a");
        assertEquals(false, CollectionUtils.sizeIsEmpty(list));
    }
    public void testSizeIsEmpty_Map() {
        Map map = new HashMap();
        assertEquals(true, CollectionUtils.sizeIsEmpty(map));
        map.put("1", "a");
        assertEquals(false, CollectionUtils.sizeIsEmpty(map));
    }
    public void testSizeIsEmpty_Array() {
        Object[] objectArray = new Object[0];
        assertEquals(true, CollectionUtils.sizeIsEmpty(objectArray));
        
        String[] stringArray = new String[3];
        assertEquals(false, CollectionUtils.sizeIsEmpty(stringArray));
        stringArray[0] = "a";
        stringArray[1] = "b";
        stringArray[2] = "c";
        assertEquals(false, CollectionUtils.sizeIsEmpty(stringArray));
    }
    public void testSizeIsEmpty_PrimitiveArray() {
        int[] intArray = new int[0];
        assertEquals(true, CollectionUtils.sizeIsEmpty(intArray));
        
        double[] doubleArray = new double[3];
        assertEquals(false, CollectionUtils.sizeIsEmpty(doubleArray));
        doubleArray[0] = 0.0d;
        doubleArray[1] = 1.0d;
        doubleArray[2] = 2.5d;
        assertEquals(false, CollectionUtils.sizeIsEmpty(doubleArray));
    }
    public void testSizeIsEmpty_Enumeration() {
        Vector list = new Vector();
        assertEquals(true, CollectionUtils.sizeIsEmpty(list.elements()));
        list.add("a");
        assertEquals(false, CollectionUtils.sizeIsEmpty(list.elements()));
        Enumeration en = list.elements();
        en.nextElement();
        assertEquals(true, CollectionUtils.sizeIsEmpty(en));
    }
    public void testSizeIsEmpty_Iterator() {
        List list = new ArrayList();
        assertEquals(true, CollectionUtils.sizeIsEmpty(list.iterator()));
        list.add("a");
        assertEquals(false, CollectionUtils.sizeIsEmpty(list.iterator()));
        Iterator it = list.iterator();
        it.next();
        assertEquals(true, CollectionUtils.sizeIsEmpty(it));
    }
    public void testSizeIsEmpty_Other() {
        try {
            CollectionUtils.sizeIsEmpty(null);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {}
        try {
            CollectionUtils.sizeIsEmpty("not a list");
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testIsEmptyWithEmptyCollection() {
        Collection coll = new ArrayList();
        assertEquals(true, CollectionUtils.isEmpty(coll));
    }

    public void testIsEmptyWithNonEmptyCollection() {
        Collection coll = new ArrayList();
        coll.add("item");
        assertEquals(false, CollectionUtils.isEmpty(coll));
    }

    public void testIsEmptyWithNull() {
        Collection coll = null;
        assertEquals(true, CollectionUtils.isEmpty(coll));
    }

    public void testIsNotEmptyWithEmptyCollection() {
        Collection coll = new ArrayList();
        assertEquals(false, CollectionUtils.isNotEmpty(coll));
    }

    public void testIsNotEmptyWithNonEmptyCollection() {
        Collection coll = new ArrayList();
        coll.add("item");
        assertEquals(true, CollectionUtils.isNotEmpty(coll));
    }

    public void testIsNotEmptyWithNull() {
        Collection coll = null;
        assertEquals(false, CollectionUtils.isNotEmpty(coll));
    }

    //-----------------------------------------------------------------------
    private static Predicate EQUALS_TWO = new Predicate() {
        public boolean evaluate(Object input) {
            return (input.equals("Two"));
        }
    };
    
    public void testFilter() {
        List list = new ArrayList();
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        CollectionUtils.filter(list, EQUALS_TWO);
        assertEquals(1, list.size());
        assertEquals("Two", list.get(0));
        
        list = new ArrayList();
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        CollectionUtils.filter(list, null);
        assertEquals(4, list.size());
        CollectionUtils.filter(null, EQUALS_TWO);
        assertEquals(4, list.size());
        CollectionUtils.filter(null, null);
        assertEquals(4, list.size());
    }

    public void testCountMatches() {
        List list = new ArrayList();
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        int count = CollectionUtils.countMatches(list, EQUALS_TWO);
        assertEquals(4, list.size());
        assertEquals(1, count);
        assertEquals(0, CollectionUtils.countMatches(list, null));
        assertEquals(0, CollectionUtils.countMatches(null, EQUALS_TWO));
        assertEquals(0, CollectionUtils.countMatches(null, null));
    }

    public void testExists() {
        List list = new ArrayList();
        assertEquals(false, CollectionUtils.exists(null, null));
        assertEquals(false, CollectionUtils.exists(list, null));
        assertEquals(false, CollectionUtils.exists(null, EQUALS_TWO));
        assertEquals(false, CollectionUtils.exists(list, EQUALS_TWO));
        list.add("One");
        list.add("Three");
        list.add("Four");
        assertEquals(false, CollectionUtils.exists(list, EQUALS_TWO));

        list.add("Two");
        assertEquals(true, CollectionUtils.exists(list, EQUALS_TWO));
    }
    
    public void testSelect() {
        List list = new ArrayList();
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        Collection output = CollectionUtils.select(list, EQUALS_TWO);
        assertEquals(4, list.size());
        assertEquals(1, output.size());
        assertEquals("Two", output.iterator().next());
    }

    public void testSelectRejected() {
        List list = new ArrayList();
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        Collection output = CollectionUtils.selectRejected(list, EQUALS_TWO);
        assertEquals(4, list.size());
        assertEquals(3, output.size());
        assertTrue(output.contains("One"));
        assertTrue(output.contains("Three"));
        assertTrue(output.contains("Four"));
    }
    
    public void testCollect() {
        Transformer transformer = TransformerUtils.constantTransformer("z");
        Collection collection = CollectionUtils.collect(collectionA, transformer);
        assertTrue(collection.size() == collectionA.size());
        assertTrue(collectionA.contains("a") && ! collectionA.contains("z"));
        assertTrue(collection.contains("z") && !collection.contains("a"));
        
        collection = new ArrayList();
        CollectionUtils.collect(collectionA, transformer, collection);
        assertTrue(collection.size() == collectionA.size());
        assertTrue(collectionA.contains("a") && ! collectionA.contains("z"));
        assertTrue(collection.contains("z") && !collection.contains("a"));
        
        Iterator iterator = null;
        collection = new ArrayList();
        CollectionUtils.collect(iterator, transformer, collection);
        
        iterator = collectionA.iterator();
        CollectionUtils.collect(iterator, transformer, collection);
        assertTrue(collection.size() == collectionA.size());
        assertTrue(collectionA.contains("a") && ! collectionA.contains("z"));
        assertTrue(collection.contains("z") && !collection.contains("a")); 
        
        iterator = collectionA.iterator();
        collection = CollectionUtils.collect(iterator, transformer);
        assertTrue(collection.size() == collectionA.size());
        assertTrue(collection.contains("z") && !collection.contains("a")); 
        collection = CollectionUtils.collect((Iterator) null, (Transformer) null);
        assertTrue(collection.size() == 0);
           
        int size = collectionA.size();
        CollectionUtils.collect((Collection) null, transformer, collectionA);
        assertTrue(collectionA.size() == size && collectionA.contains("a"));
        CollectionUtils.collect(collectionB, null, collectionA);
        assertTrue(collectionA.size() == size && collectionA.contains("a"));
        
    }

    Transformer TRANSFORM_TO_INTEGER = new Transformer() {
        public Object transform(Object input) {
            return new Integer((String) input);
        }
    };
    
    public void testTransform1() {
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        CollectionUtils.transform(list, TRANSFORM_TO_INTEGER);
        assertEquals(3, list.size());
        assertEquals(new Integer(1), list.get(0));
        assertEquals(new Integer(2), list.get(1));
        assertEquals(new Integer(3), list.get(2));
        
        list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        CollectionUtils.transform(null, TRANSFORM_TO_INTEGER);
        assertEquals(3, list.size());
        CollectionUtils.transform(list, null);
        assertEquals(3, list.size());
        CollectionUtils.transform(null, null);
        assertEquals(3, list.size());
    }
    
    public void testTransform2() {
        Set set = new HashSet();
        set.add("1");
        set.add("2");
        set.add("3");
        CollectionUtils.transform(set, new Transformer() {
            public Object transform(Object input) {
                return new Integer(4);
            }
        });
        assertEquals(1, set.size());
        assertEquals(new Integer(4), set.iterator().next());
    }

    //-----------------------------------------------------------------------
    public void testAddIgnoreNull() {
        Set set = new HashSet();
        set.add("1");
        set.add("2");
        set.add("3");
        assertEquals(false, CollectionUtils.addIgnoreNull(set, null));
        assertEquals(3, set.size());
        assertEquals(false, CollectionUtils.addIgnoreNull(set, "1"));
        assertEquals(3, set.size());
        assertEquals(true, CollectionUtils.addIgnoreNull(set, "4"));
        assertEquals(4, set.size());
        assertEquals(true, set.contains("4"));
    }

    //-----------------------------------------------------------------------
    public void testPredicatedCollection() {
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object o) {
                return o instanceof String;
            }
        };
        Collection collection = 
            CollectionUtils.predicatedCollection(new ArrayList(), predicate);
        assertTrue("returned object should be a PredicatedCollection",
            collection instanceof PredicatedCollection);
        try { 
           collection = 
                CollectionUtils.predicatedCollection(new ArrayList(), null); 
           fail("Expecting IllegalArgumentException for null predicate.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        try { 
           collection = 
                CollectionUtils.predicatedCollection(null, predicate); 
           fail("Expecting IllegalArgumentException for null collection.");
        } catch (IllegalArgumentException ex) {
            // expected
        }             
    }
        
   

    public BulkTest bulkTestTypedCollection() {
        return new TestTypedCollection("") {
            public Collection typedCollection() {
                return CollectionUtils.typedCollection(
                    new ArrayList(),
                    super.getType());
            }
 
            public BulkTest bulkTestAll() {
                return new AbstractTestCollection("") {
                    public Collection makeCollection() {
                        return typedCollection();
                    }
 
                    public Collection makeConfirmedCollection() {
                        return new ArrayList();
                    }
 
                    public Collection makeConfirmedFullCollection() {
                        ArrayList list = new ArrayList();
                        list.addAll(java.util.Arrays.asList(getFullElements()));
                        return list;
                    }
 
                    public Object[] getFullElements() {
                        return getFullNonNullStringElements();
                    }
 
                    public Object[] getOtherElements() {
                        return getOtherNonNullStringElements();
                    }
 
                };
            }
        };
    }
    
    public void testIsFull() {
        Set set = new HashSet();
        set.add("1");
        set.add("2");
        set.add("3");
        try {
            CollectionUtils.isFull(null);
            fail();
        } catch (NullPointerException ex) {}
        assertEquals(false, CollectionUtils.isFull(set));
        
        BoundedFifoBuffer buf = new BoundedFifoBuffer(set);
        assertEquals(true, CollectionUtils.isFull(buf));
        buf.remove("2");
        assertEquals(false, CollectionUtils.isFull(buf));
        buf.add("2");
        assertEquals(true, CollectionUtils.isFull(buf));
        
        Buffer buf2 = BufferUtils.synchronizedBuffer(buf);
        assertEquals(true, CollectionUtils.isFull(buf2));
        buf2.remove("2");
        assertEquals(false, CollectionUtils.isFull(buf2));
        buf2.add("2");
        assertEquals(true, CollectionUtils.isFull(buf2));
    }

    public void testMaxSize() {
        Set set = new HashSet();
        set.add("1");
        set.add("2");
        set.add("3");
        try {
            CollectionUtils.maxSize(null);
            fail();
        } catch (NullPointerException ex) {}
        assertEquals(-1, CollectionUtils.maxSize(set));
        
        BoundedFifoBuffer buf = new BoundedFifoBuffer(set);
        assertEquals(3, CollectionUtils.maxSize(buf));
        buf.remove("2");
        assertEquals(3, CollectionUtils.maxSize(buf));
        buf.add("2");
        assertEquals(3, CollectionUtils.maxSize(buf));
        
        Buffer buf2 = BufferUtils.synchronizedBuffer(buf);
        assertEquals(3, CollectionUtils.maxSize(buf2));
        buf2.remove("2");
        assertEquals(3, CollectionUtils.maxSize(buf2));
        buf2.add("2");
        assertEquals(3, CollectionUtils.maxSize(buf2));
    }

    public void testIntersectionUsesMethodEquals() {
        // Let elta and eltb be objects...
        Object elta = new Integer(17);
        Object eltb = new Integer(17);
        
        // ...which are equal...
        assertEquals(elta,eltb);
        assertEquals(eltb,elta);
        
        // ...but not the same (==).
        assertTrue(elta != eltb);
        
        // Let cola and colb be collections...
        Collection cola = new ArrayList();
        Collection colb = new ArrayList();
        
        // ...which contain elta and eltb, 
        // respectively.
        cola.add(elta);
        colb.add(eltb);
        
        // Then the intersection of the two
        // should contain one element.
        Collection intersection = CollectionUtils.intersection(cola,colb);
        assertEquals(1,intersection.size());
        
        // In practice, this element will be the same (==) as elta
        // or eltb, although this isn't strictly part of the
        // contract.
        Object eltc = intersection.iterator().next();
        assertTrue((eltc == elta  && eltc != eltb) || (eltc != elta  && eltc == eltb));
        
        // In any event, this element remains equal,
        // to both elta and eltb.
        assertEquals(elta,eltc);
        assertEquals(eltc,elta);
        assertEquals(eltb,eltc);
        assertEquals(eltc,eltb);
    }
    
     public void testTransformedCollection() {
        Transformer transformer = TransformerUtils.nopTransformer();
        Collection collection = 
            CollectionUtils.transformedCollection(new ArrayList(), transformer);
        assertTrue("returned object should be a TransformedCollection",
            collection instanceof TransformedCollection);
        try { 
           collection = 
                CollectionUtils.transformedCollection(new ArrayList(), null); 
           fail("Expecting IllegalArgumentException for null transformer.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        try { 
           collection = 
                CollectionUtils.transformedCollection(null, transformer); 
           fail("Expecting IllegalArgumentException for null collection.");
        } catch (IllegalArgumentException ex) {
            // expected
        }             
    }

    public void testTransformedCollection_2() {
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        Collection result = CollectionUtils.transformedCollection(list, TRANSFORM_TO_INTEGER);
        assertEquals(true, result.contains("1"));  // untransformed
        assertEquals(true, result.contains("2"));  // untransformed
        assertEquals(true, result.contains("3"));  // untransformed
    }

    public void testSynchronizedCollection() {
        Collection col = CollectionUtils.synchronizedCollection(new ArrayList());
        assertTrue("Returned object should be a SynchronizedCollection.",
            col instanceof SynchronizedCollection);
        try {
            col = CollectionUtils.synchronizedCollection(null);
            fail("Expecting IllegalArgumentException for null collection.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
    public void testUnmodifiableCollection() {
        Collection col = CollectionUtils.unmodifiableCollection(new ArrayList());
        assertTrue("Returned object should be a UnmodifiableCollection.",
            col instanceof UnmodifiableCollection);
        try {
            col = CollectionUtils.unmodifiableCollection(null);
            fail("Expecting IllegalArgumentException for null collection.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
}
