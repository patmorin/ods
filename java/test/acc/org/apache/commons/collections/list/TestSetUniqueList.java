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
package org.apache.commons.collections.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * JUnit tests.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Matthew Hawthorne
 * @author Tom Dunham
 */
public class TestSetUniqueList extends AbstractTestList {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestSetUniqueList.class);
    }

    public TestSetUniqueList(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------
    public List makeEmptyList() {
        return new SetUniqueList(new ArrayList(), new HashSet());
    }

    public void testListIteratorSet() {
        // override to block
        resetFull();
        ListIterator it = getList().listIterator();
        it.next();
        try {
            it.set(null);
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
    
    public Object[] getFullNonNullElements() {
        // override to avoid duplicate "One"
        return new Object[] {
            new String(""),
            new String("One"),
            new Integer(2),
            "Three",
            new Integer(4),
            new Double(5),
            new Float(6),
            "Seven",
            "Eight",
            new String("Nine"),
            new Integer(10),
            new Short((short)11),
            new Long(12),
            "Thirteen",
            "14",
            "15",
            new Byte((byte)16)
        };
    }
    
    public void testListIteratorAdd() {
        // override to cope with Set behaviour
        resetEmpty();
        List list1 = getList();
        List list2 = getConfirmedList();

        Object[] elements = getOtherElements();  // changed here
        ListIterator iter1 = list1.listIterator();
        ListIterator iter2 = list2.listIterator();

        for (int i = 0; i < elements.length; i++) {
            iter1.add(elements[i]);
            iter2.add(elements[i]);
            super.verify();  // changed here
        }

        resetFull();
        iter1 = getList().listIterator();
        iter2 = getConfirmedList().listIterator();
        for (int i = 0; i < elements.length; i++) {
            iter1.next();
            iter2.next();
            iter1.add(elements[i]);
            iter2.add(elements[i]);
            super.verify();  // changed here
        }
    }
    
    public void testCollectionAddAll() {
        // override for set behaviour
        resetEmpty();
        Object[] elements = getFullElements();
        boolean r = collection.addAll(Arrays.asList(elements));
        confirmed.addAll(Arrays.asList(elements));
        verify();
        assertTrue("Empty collection should change after addAll", r);
        for (int i = 0; i < elements.length; i++) {
            assertTrue("Collection should contain added element",
                       collection.contains(elements[i]));
        }

        resetFull();
        int size = collection.size();
        elements = getOtherElements();
        r = collection.addAll(Arrays.asList(elements));
        confirmed.addAll(Arrays.asList(elements));
        verify();
        assertTrue("Full collection should change after addAll", r);
        for (int i = 0; i < elements.length; i++) {
            assertTrue("Full collection should contain added element " + i,
                       collection.contains(elements[i]));
        }
        assertEquals("Size should increase after addAll", 
                     size + elements.length, collection.size());
    }
    
    public void testListSetByIndex() {
        // override for set behaviour
        resetFull();
        int size = collection.size();
        getList().set(0, new Long(1000));
        assertEquals(size, collection.size());

        getList().set(2, new Long(1000));
        assertEquals(size - 1, collection.size());
        assertEquals(new Long(1000), getList().get(1));  // set into 2, but shifted down to 1
    }
    
    boolean extraVerify = true;
    public void testCollectionIteratorRemove() {
        try {
            extraVerify = false;
            super.testCollectionIteratorRemove();
        } finally {
            extraVerify = true;
        }
    }
    
    public void verify() {
        super.verify();
        
        if (extraVerify) {
            int size = collection.size();
            getList().add(new Long(1000));
            assertEquals(size + 1, collection.size());

            getList().add(new Long(1000));
            assertEquals(size + 1, collection.size());
            assertEquals(new Long(1000), getList().get(size));
        
            getList().remove(size);
        }
    }
    
    //-----------------------------------------------------------------------
    public void testFactory() {
        Integer[] array = new Integer[] {new Integer(1), new Integer(2), new Integer(1)};
        ArrayList list = new ArrayList(Arrays.asList(array));
        final SetUniqueList lset = SetUniqueList.decorate(list);

        assertEquals("Duplicate element was added.", 2, lset.size());
        assertEquals(new Integer(1), lset.get(0));
        assertEquals(new Integer(2), lset.get(1));
        assertEquals(new Integer(1), list.get(0));
        assertEquals(new Integer(2), list.get(1));
    }

    public void testAdd() {
        final SetUniqueList lset = new SetUniqueList(new ArrayList(), new HashSet());

        // Duplicate element
        final Object obj = new Integer(1);
        lset.add(obj);
        lset.add(obj);
        assertEquals("Duplicate element was added.", 1, lset.size());

        // Unique element
        lset.add(new Integer(2));
        assertEquals("Unique element was not added.", 2, lset.size());
    }

    public void testAddAll() {
        final SetUniqueList lset = new SetUniqueList(new ArrayList(), new HashSet());

        lset.addAll(
            Arrays.asList(new Integer[] { new Integer(1), new Integer(1)}));

        assertEquals("Duplicate element was added.", 1, lset.size());
    }

    public void testSet() {
        final SetUniqueList lset = new SetUniqueList(new ArrayList(), new HashSet());

        // Duplicate element
        final Object obj1 = new Integer(1);
        final Object obj2 = new Integer(2);
        final Object obj3 = new Integer(3);

        lset.add(obj1);
        lset.add(obj2);
        lset.set(0, obj1);
        assertEquals(2, lset.size());
        assertSame(obj1, lset.get(0));
        assertSame(obj2, lset.get(1));

        lset.clear();
        lset.add(obj1);
        lset.add(obj2);
        lset.set(0, obj2);
        assertEquals(1, lset.size());
        assertSame(obj2, lset.get(0));

        lset.clear();
        lset.add(obj1);
        lset.add(obj2);
        lset.set(0, obj3);
        assertEquals(2, lset.size());
        assertSame(obj3, lset.get(0));
        assertSame(obj2, lset.get(1));

        lset.clear();
        lset.add(obj1);
        lset.add(obj2);
        lset.set(1, obj1);
        assertEquals(1, lset.size());
        assertSame(obj1, lset.get(0));
    }

    public void testListIterator() {
        final SetUniqueList lset = new SetUniqueList(new ArrayList(), new HashSet());

        final Object obj1 = new Integer(1);
        final Object obj2 = new Integer(2);
        lset.add(obj1);
        lset.add(obj2);

        // Attempts to add a duplicate object
        for (final ListIterator it = lset.listIterator(); it.hasNext();) {
            it.next();

            if (!it.hasNext()) {
                it.add(obj1);
                break;
            }
        }

        assertEquals("Duplicate element was added", 2, lset.size());
    }

    public void testUniqueListReInsert() {
        List l = SetUniqueList.decorate(new LinkedList());
        l.add(new Object());
        l.add(new Object());
        
        Object a = l.get(0);
        
        // duplicate is removed
        l.set(0, l.get(1)); 
        assertEquals(1, l.size());
        
        // old object is added back in 
        l.add(1, a); 
        assertEquals(2, l.size());
    }
    
    public void testUniqueListDoubleInsert() {
        List l = SetUniqueList.decorate(new LinkedList());
        l.add(new Object());
        l.add(new Object());
        
        // duplicate is removed
        l.set(0, l.get(1)); 
        assertEquals(1, l.size());
        
        // duplicate should be removed again
        l.add(1, l.get(0));
        assertEquals(1, l.size());
    }

    public void testSetDownwardsInList() {
        /*
         * Checks the following semantics
         * [a,b]
         * set(0,b): [b]->a
         * So UniqList contains [b] and a is returned
         */
        ArrayList l = new ArrayList();
        HashSet s = new HashSet();
        final SetUniqueList ul = new SetUniqueList(l, s);

        Object a = new Object();
        Object b = new Object();
        ul.add(a);
        ul.add(b);
        assertEquals(a, l.get(0));
        assertEquals(b, l.get(1));
        assertTrue(s.contains(a)); 
        assertTrue(s.contains(b));
        
        assertEquals(a, ul.set(0, b));
        assertEquals(1, s.size());
        assertEquals(1, l.size());
        assertEquals(b, l.get(0));
        assertTrue(s.contains(b));
        assertFalse(s.contains(a));
    }

    public void testSetInBiggerList() {
        /*
         * Checks the following semantics
         * [a,b,c]
         * set(0,b): [b,c]->a
         * So UniqList contains [b,c] and a is returned
         */
        ArrayList l = new ArrayList();
        HashSet s = new HashSet();
        final SetUniqueList ul = new SetUniqueList(l, s);

        Object a = new Object();
        Object b = new Object();
        Object c = new Object();

        ul.add(a);
        ul.add(b);
        ul.add(c);
        assertEquals(a, l.get(0));
        assertEquals(b, l.get(1));
        assertEquals(c, l.get(2));
        assertTrue(s.contains(a)); 
        assertTrue(s.contains(b));
        assertTrue(s.contains(c));
        
        assertEquals(a, ul.set(0, b));
        assertEquals(2, s.size());
        assertEquals(2, l.size());
        assertEquals(b, l.get(0));
        assertEquals(c, l.get(1));
        assertFalse(s.contains(a));
        assertTrue(s.contains(b));
        assertTrue(s.contains(c));
    }    

    public void testSetUpwardsInList() {
        /*
         * Checks the following semantics
         * [a,b,c]
         * set(1,a): [a,c]->b
         * So UniqList contains [a,c] and b is returned
         */
        ArrayList l = new ArrayList();
        HashSet s = new HashSet();
        final SetUniqueList ul = new SetUniqueList(l, s);

        Object a = new String("A");
        Object b = new String("B");
        Object c = new String("C");

        ul.add(a);
        ul.add(b);
        ul.add(c);
        assertEquals(a, l.get(0));
        assertEquals(b, l.get(1));
        assertEquals(c, l.get(2));
        assertTrue(s.contains(a)); 
        assertTrue(s.contains(b));
        assertTrue(s.contains(c));
        
        assertEquals(b, ul.set(1, a));
        assertEquals(2, s.size());
        assertEquals(2, l.size());
        assertEquals(a, l.get(0));
        assertEquals(c, l.get(1));
        assertTrue(s.contains(a));
        assertFalse(s.contains(b));
        assertTrue(s.contains(c));
    }

    //-----------------------------------------------------------------------
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/SetUniqueList.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/SetUniqueList.fullCollection.version3.1.obj");
//    }

}
