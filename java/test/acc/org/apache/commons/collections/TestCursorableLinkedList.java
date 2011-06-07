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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;

import org.apache.commons.collections.list.AbstractTestList;

/**
 * Test class.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Rodney Waldhoff
 * @author Simon Kitching
 */
public class TestCursorableLinkedList extends AbstractTestList {
    public TestCursorableLinkedList(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestCursorableLinkedList.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestCursorableLinkedList.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    private CursorableLinkedList list = null;

    public void setUp() {
        list = new CursorableLinkedList();
    }

    public List makeEmptyList() {
        return new CursorableLinkedList();
    }

    public void testAdd() {
        assertEquals("[]",list.toString());
        assertTrue(list.add(new Integer(1)));
        assertEquals("[1]",list.toString());
        assertTrue(list.add(new Integer(2)));
        assertEquals("[1, 2]",list.toString());
        assertTrue(list.add(new Integer(3)));
        assertEquals("[1, 2, 3]",list.toString());
        assertTrue(list.addFirst(new Integer(0)));
        assertEquals("[0, 1, 2, 3]",list.toString());
        assertTrue(list.addLast(new Integer(4)));
        assertEquals("[0, 1, 2, 3, 4]",list.toString());
        list.add(0,new Integer(-2));
        assertEquals("[-2, 0, 1, 2, 3, 4]",list.toString());
        list.add(1,new Integer(-1));
        assertEquals("[-2, -1, 0, 1, 2, 3, 4]",list.toString());
        list.add(7,new Integer(5));
        assertEquals("[-2, -1, 0, 1, 2, 3, 4, 5]",list.toString());

        java.util.List list2 = new java.util.LinkedList();
        list2.add("A");
        list2.add("B");
        list2.add("C");

        assertTrue(list.addAll(list2));
        assertEquals("[-2, -1, 0, 1, 2, 3, 4, 5, A, B, C]",list.toString());
        assertTrue(list.addAll(3,list2));
        assertEquals("[-2, -1, 0, A, B, C, 1, 2, 3, 4, 5, A, B, C]",list.toString());
    }

    public void testClear() {
        assertEquals(0,list.size());
        assertTrue(list.isEmpty());
        list.clear();
        assertEquals(0,list.size());
        assertTrue(list.isEmpty());

        list.add("element");
        assertEquals(1,list.size());
        assertTrue(!list.isEmpty());

        list.clear();
        assertEquals(0,list.size());
        assertTrue(list.isEmpty());

        list.add("element1");
        list.add("element2");
        assertEquals(2,list.size());
        assertTrue(!list.isEmpty());

        list.clear();
        assertEquals(0,list.size());
        assertTrue(list.isEmpty());

        for(int i=0;i<1000;i++) {
            list.add(new Integer(i));
        }
        assertEquals(1000,list.size());
        assertTrue(!list.isEmpty());

        list.clear();
        assertEquals(0,list.size());
        assertTrue(list.isEmpty());
    }

    public void testContains() {
        assertTrue(!list.contains("A"));
        assertTrue(list.add("A"));
        assertTrue(list.contains("A"));
        assertTrue(list.add("B"));
        assertTrue(list.contains("A"));
        assertTrue(list.addFirst("a"));
        assertTrue(list.contains("A"));
        assertTrue(list.remove("a"));
        assertTrue(list.contains("A"));
        assertTrue(list.remove("A"));
        assertTrue(!list.contains("A"));
    }

    public void testContainsAll() {
        assertTrue(list.containsAll(list));
        java.util.List list2 = new java.util.LinkedList();
        assertTrue(list.containsAll(list2));
        list2.add("A");
        assertTrue(!list.containsAll(list2));
        list.add("B");
        list.add("A");
        assertTrue(list.containsAll(list2));
        list2.add("B");
        assertTrue(list.containsAll(list2));
        list2.add("C");
        assertTrue(!list.containsAll(list2));
        list.add("C");
        assertTrue(list.containsAll(list2));
        list2.add("C");
        assertTrue(list.containsAll(list2));
        assertTrue(list.containsAll(list));
    }

    public void testCursorNavigation() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        CursorableLinkedList.Cursor it = list.cursor();
        assertTrue(it.hasNext());
        assertTrue(!it.hasPrevious());
        assertEquals("1",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("1",it.previous());
        assertTrue(it.hasNext());
        assertTrue(!it.hasPrevious());
        assertEquals("1",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("2",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("2",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("2",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("3",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("4",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("5",it.next());
        assertTrue(!it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("5",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("4",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("3",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("2",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("1",it.previous());
        assertTrue(it.hasNext());
        assertTrue(!it.hasPrevious());
        it.close();
    }

    public void testCursorSet() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        CursorableLinkedList.Cursor it = list.cursor();
        assertEquals("1",it.next());
        it.set("a");
        assertEquals("a",it.previous());
        it.set("A");
        assertEquals("A",it.next());
        assertEquals("2",it.next());
        it.set("B");
        assertEquals("3",it.next());
        assertEquals("4",it.next());
        it.set("D");
        assertEquals("5",it.next());
        it.set("E");
        assertEquals("[A, B, 3, D, E]",list.toString());
        it.close();
    }

    public void testCursorRemove() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        CursorableLinkedList.Cursor it = list.cursor();
        try {
            it.remove();
        } catch(IllegalStateException e) {
            // expected
        }
        assertEquals("1",it.next());
        assertEquals("2",it.next());
        assertEquals("[1, 2, 3, 4, 5]",list.toString());
        it.remove();
        assertEquals("[1, 3, 4, 5]",list.toString());
        assertEquals("3",it.next());
        assertEquals("3",it.previous());
        assertEquals("1",it.previous());
        it.remove();
        assertEquals("[3, 4, 5]",list.toString());
        assertTrue(!it.hasPrevious());
        assertEquals("3",it.next());
        it.remove();
        assertEquals("[4, 5]",list.toString());
        try {
            it.remove();
        } catch(IllegalStateException e) {
            // expected
        }
        assertEquals("4",it.next());
        assertEquals("5",it.next());
        it.remove();
        assertEquals("[4]",list.toString());
        assertEquals("4",it.previous());
        it.remove();
        assertEquals("[]",list.toString());
        it.close();
    }

    public void testCursorAdd() {
        CursorableLinkedList.Cursor it = list.cursor();
        it.add("1");
        assertEquals("[1]",list.toString());
        it.add("3");
        assertEquals("[1, 3]",list.toString());
        it.add("5");
        assertEquals("[1, 3, 5]",list.toString());
        assertEquals("5",it.previous());
        it.add("4");
        assertEquals("[1, 3, 4, 5]",list.toString());
        assertEquals("4",it.previous());
        assertEquals("3",it.previous());
        it.add("2");
        assertEquals("[1, 2, 3, 4, 5]",list.toString());
        it.close();
    }

    public void testCursorConcurrentModification() {
        // this test verifies that cursors remain valid when the list
        // is modified via other means.
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("5");
        list.add("7");
        list.add("9");

        CursorableLinkedList.Cursor c1 = list.cursor();
        CursorableLinkedList.Cursor c2 = list.cursor();
        ListIterator li = list.listIterator();
        
        // test cursors remain valid when list modified by std ListIterator
        // test cursors skip elements removed via ListIterator
        assertEquals("1",li.next());
        assertEquals("2",li.next());
        li.remove();
        assertEquals("3",li.next());
        assertEquals("1",c1.next());
        assertEquals("3",c1.next());
        assertEquals("1",c2.next());
        
        // test cursor c1 can remove elements from previously modified list
        // test cursor c2 skips elements removed via different cursor
        c1.remove();
        assertEquals("5",c2.next());
        c2.add("6");
        assertEquals("5",c1.next());
        assertEquals("6",c1.next());
        assertEquals("7",c1.next());
        
        // test cursors remain valid when list mod via CursorableLinkedList
        // test cursor remains valid when elements inserted into list before
        // the current position of the cursor.
        list.add(0, "0");

        // test cursor remains valid when element inserted immediately after
        // current element of a cursor, and the element is seen on the
        // next call to the next method of that cursor.
        list.add(5, "8");

        assertEquals("8",c1.next());
        assertEquals("9",c1.next());
        c1.add("10");
        assertEquals("7",c2.next());
        assertEquals("8",c2.next());
        assertEquals("9",c2.next());
        assertEquals("10",c2.next());
        
        boolean nosuch = false;
        try {
            c2.next();
        }
        catch (java.util.NoSuchElementException nse) {
           nosuch = true; // expected
        }
        assertTrue(nosuch);
        
        boolean listIteratorInvalid = false;
        try {
            li.next();
        }
        catch(java.util.ConcurrentModificationException cme) {
            listIteratorInvalid = true; // expected
        }
        assertTrue(listIteratorInvalid);
        
        c1.close();  // not necessary
        c2.close();  // not necessary
    }
    
    public void testEqualsAndHashCode() {
        assertTrue(list.equals(list));
        assertEquals(list.hashCode(),list.hashCode());
        list.add("A");
        assertTrue(list.equals(list));
        assertEquals(list.hashCode(),list.hashCode());

        CursorableLinkedList list2 = new CursorableLinkedList();
        assertTrue(!list.equals(list2));
        assertTrue(!list2.equals(list));

        java.util.List list3 = new java.util.LinkedList();
        assertTrue(!list.equals(list3));
        assertTrue(!list3.equals(list));
        assertTrue(list2.equals(list3));
        assertTrue(list3.equals(list2));
        assertEquals(list2.hashCode(),list3.hashCode());

        list2.add("A");
        assertTrue(list.equals(list2));
        assertTrue(list2.equals(list));
        assertTrue(!list2.equals(list3));
        assertTrue(!list3.equals(list2));

        list3.add("A");
        assertTrue(list2.equals(list3));
        assertTrue(list3.equals(list2));
        assertEquals(list2.hashCode(),list3.hashCode());

        list.add("B");
        assertTrue(list.equals(list));
        assertTrue(!list.equals(list2));
        assertTrue(!list2.equals(list));
        assertTrue(!list.equals(list3));
        assertTrue(!list3.equals(list));

        list2.add("B");
        list3.add("B");
        assertTrue(list.equals(list));
        assertTrue(list.equals(list2));
        assertTrue(list2.equals(list));
        assertTrue(list2.equals(list3));
        assertTrue(list3.equals(list2));
        assertEquals(list2.hashCode(),list3.hashCode());

        list.add("C");
        list2.add("C");
        list3.add("C");
        assertTrue(list.equals(list));
        assertTrue(list.equals(list2));
        assertTrue(list2.equals(list));
        assertTrue(list2.equals(list3));
        assertTrue(list3.equals(list2));
        assertEquals(list.hashCode(),list2.hashCode());
        assertEquals(list2.hashCode(),list3.hashCode());

        list.add("D");
        list2.addFirst("D");
        assertTrue(list.equals(list));
        assertTrue(!list.equals(list2));
        assertTrue(!list2.equals(list));
    }

    public void testGet() {
        try {
            list.get(0);
            fail("shouldn't get here");
        } catch(IndexOutOfBoundsException e) {
            // expected
        }

        assertTrue(list.add("A"));
        assertEquals("A",list.get(0));
        assertTrue(list.add("B"));
        assertEquals("A",list.get(0));
        assertEquals("B",list.get(1));

        try {
            list.get(-1);
            fail("shouldn't get here");
        } catch(IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.get(2);
            fail("shouldn't get here");
        } catch(IndexOutOfBoundsException e) {
            // expected
        }
    }

    public void testIndexOf() {
        assertEquals(-1,list.indexOf("A"));
        assertEquals(-1,list.lastIndexOf("A"));
        list.add("A");
        assertEquals(0,list.indexOf("A"));
        assertEquals(0,list.lastIndexOf("A"));
        assertEquals(-1,list.indexOf("B"));
        assertEquals(-1,list.lastIndexOf("B"));
        list.add("B");
        assertEquals(0,list.indexOf("A"));
        assertEquals(0,list.lastIndexOf("A"));
        assertEquals(1,list.indexOf("B"));
        assertEquals(1,list.lastIndexOf("B"));
        list.addFirst("B");
        assertEquals(1,list.indexOf("A"));
        assertEquals(1,list.lastIndexOf("A"));
        assertEquals(0,list.indexOf("B"));
        assertEquals(2,list.lastIndexOf("B"));
    }

    public void testIsEmpty() {
        assertTrue(list.isEmpty());
        list.add("element");
        assertTrue(!list.isEmpty());
        list.remove("element");
        assertTrue(list.isEmpty());
        list.add("element");
        assertTrue(!list.isEmpty());
        list.clear();
        assertTrue(list.isEmpty());
    }

    public void testIterator() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        Iterator it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("1",it.next());
        assertTrue(it.hasNext());
        assertEquals("2",it.next());
        assertTrue(it.hasNext());
        assertEquals("3",it.next());
        assertTrue(it.hasNext());
        assertEquals("4",it.next());
        assertTrue(it.hasNext());
        assertEquals("5",it.next());
        assertTrue(!it.hasNext());

        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("1",it.next());
        it.remove();
        assertEquals("[2, 3, 4, 5]",list.toString());
        assertTrue(it.hasNext());
        assertEquals("2",it.next());
        it.remove();
        assertEquals("[3, 4, 5]",list.toString());
        assertTrue(it.hasNext());
        assertEquals("3",it.next());
        it.remove();
        assertEquals("[4, 5]",list.toString());
        assertTrue(it.hasNext());
        assertEquals("4",it.next());
        it.remove();
        assertEquals("[5]",list.toString());
        assertTrue(it.hasNext());
        assertEquals("5",it.next());
        it.remove();
        assertEquals("[]",list.toString());
        assertTrue(!it.hasNext());
    }

    public void testListIteratorNavigation() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        ListIterator it = list.listIterator();
        assertTrue(it.hasNext());
        assertTrue(!it.hasPrevious());
        assertEquals(-1,it.previousIndex());
        assertEquals(0,it.nextIndex());
        assertEquals("1",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(0,it.previousIndex());
        assertEquals(1,it.nextIndex());
        assertEquals("1",it.previous());
        assertTrue(it.hasNext());
        assertTrue(!it.hasPrevious());
        assertEquals(-1,it.previousIndex());
        assertEquals(0,it.nextIndex());
        assertEquals("1",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(0,it.previousIndex());
        assertEquals(1,it.nextIndex());
        assertEquals("2",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(1,it.previousIndex());
        assertEquals(2,it.nextIndex());
        assertEquals("2",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(0,it.previousIndex());
        assertEquals(1,it.nextIndex());
        assertEquals("2",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(1,it.previousIndex());
        assertEquals(2,it.nextIndex());
        assertEquals("3",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(2,it.previousIndex());
        assertEquals(3,it.nextIndex());
        assertEquals("4",it.next());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(3,it.previousIndex());
        assertEquals(4,it.nextIndex());
        assertEquals("5",it.next());
        assertTrue(!it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(4,it.previousIndex());
        assertEquals(5,it.nextIndex());
        assertEquals("5",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(3,it.previousIndex());
        assertEquals(4,it.nextIndex());
        assertEquals("4",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(2,it.previousIndex());
        assertEquals(3,it.nextIndex());
        assertEquals("3",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(1,it.previousIndex());
        assertEquals(2,it.nextIndex());
        assertEquals("2",it.previous());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(0,it.previousIndex());
        assertEquals(1,it.nextIndex());
        assertEquals("1",it.previous());
        assertTrue(it.hasNext());
        assertTrue(!it.hasPrevious());
        assertEquals(-1,it.previousIndex());
        assertEquals(0,it.nextIndex());
    }

    public void testListIteratorSet() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        ListIterator it = list.listIterator();
        assertEquals("1",it.next());
        it.set("a");
        assertEquals("a",it.previous());
        it.set("A");
        assertEquals("A",it.next());
        assertEquals("2",it.next());
        it.set("B");
        assertEquals("3",it.next());
        assertEquals("4",it.next());
        it.set("D");
        assertEquals("5",it.next());
        it.set("E");
        assertEquals("[A, B, 3, D, E]",list.toString());
    }

    public void testListIteratorRemove() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        ListIterator it = list.listIterator();
        try {
            it.remove();
        } catch(IllegalStateException e) {
            // expected
        }
        assertEquals("1",it.next());
        assertEquals("2",it.next());
        assertEquals("[1, 2, 3, 4, 5]",list.toString());
        it.remove();
        assertEquals("[1, 3, 4, 5]",list.toString());
        assertEquals("3",it.next());
        assertEquals("3",it.previous());
        assertEquals("1",it.previous());
        it.remove();
        assertEquals("[3, 4, 5]",list.toString());
        assertTrue(!it.hasPrevious());
        assertEquals("3",it.next());
        it.remove();
        assertEquals("[4, 5]",list.toString());
        try {
            it.remove();
        } catch(IllegalStateException e) {
            // expected
        }
        assertEquals("4",it.next());
        assertEquals("5",it.next());
        it.remove();
        assertEquals("[4]",list.toString());
        assertEquals("4",it.previous());
        it.remove();
        assertEquals("[]",list.toString());
    }

    public void testListIteratorAdd() {
        ListIterator it = list.listIterator();
        it.add("1");
        assertEquals("[1]",list.toString());
        it.add("3");
        assertEquals("[1, 3]",list.toString());
        it.add("5");
        assertEquals("[1, 3, 5]",list.toString());
        assertEquals("5",it.previous());
        it.add("4");
        assertEquals("[1, 3, 4, 5]",list.toString());
        assertEquals("4",it.previous());
        assertEquals("3",it.previous());
        it.add("2");
        assertEquals("[1, 2, 3, 4, 5]",list.toString());
    }

    public void testRemoveAll() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        HashSet set = new HashSet();
        set.add("A");
        set.add("2");
        set.add("C");
        set.add("4");
        set.add("D");

        assertTrue(list.removeAll(set));
        assertEquals("[1, 3, 5]",list.toString());
        assertTrue(!list.removeAll(set));
    }

    public void testRemoveByIndex() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        assertEquals("[1, 2, 3, 4, 5]",list.toString());
        assertEquals("1",list.remove(0));
        assertEquals("[2, 3, 4, 5]",list.toString());
        assertEquals("3",list.remove(1));
        assertEquals("[2, 4, 5]",list.toString());
        assertEquals("4",list.remove(1));
        assertEquals("[2, 5]",list.toString());
        assertEquals("5",list.remove(1));
        assertEquals("[2]",list.toString());
        assertEquals("2",list.remove(0));
        assertEquals("[]",list.toString());
    }

    public void testRemove() {
        list.add("1");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        assertEquals("[1, 1, 2, 3, 4, 5, 2, 3, 4, 5]",list.toString());
        assertTrue(!list.remove("6"));
        assertTrue(list.remove("5"));
        assertEquals("[1, 1, 2, 3, 4, 2, 3, 4, 5]",list.toString());
        assertTrue(list.remove("5"));
        assertEquals("[1, 1, 2, 3, 4, 2, 3, 4]",list.toString());
        assertTrue(!list.remove("5"));
        assertTrue(list.remove("1"));
        assertEquals("[1, 2, 3, 4, 2, 3, 4]",list.toString());
        assertTrue(list.remove("1"));
        assertEquals("[2, 3, 4, 2, 3, 4]",list.toString());
        assertTrue(list.remove("2"));
        assertEquals("[3, 4, 2, 3, 4]",list.toString());
        assertTrue(list.remove("2"));
        assertEquals("[3, 4, 3, 4]",list.toString());
        assertTrue(list.remove("3"));
        assertEquals("[4, 3, 4]",list.toString());
        assertTrue(list.remove("3"));
        assertEquals("[4, 4]",list.toString());
        assertTrue(list.remove("4"));
        assertEquals("[4]",list.toString());
        assertTrue(list.remove("4"));
        assertEquals("[]",list.toString());
    }

    public void testRetainAll() {
        list.add("1");
        list.add("1");
        list.add("2");
        list.add("2");
        list.add("3");
        list.add("3");
        list.add("4");
        list.add("4");
        list.add("5");
        list.add("5");

        HashSet set = new HashSet();
        set.add("A");
        set.add("2");
        set.add("C");
        set.add("4");
        set.add("D");

        assertTrue(list.retainAll(set));
        assertEquals("[2, 2, 4, 4]",list.toString());
        assertTrue(!list.retainAll(set));
    }

    public void testSet() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        assertEquals("[1, 2, 3, 4, 5]",list.toString());
        list.set(0,"A");
        assertEquals("[A, 2, 3, 4, 5]",list.toString());
        list.set(1,"B");
        assertEquals("[A, B, 3, 4, 5]",list.toString());
        list.set(2,"C");
        assertEquals("[A, B, C, 4, 5]",list.toString());
        list.set(3,"D");
        assertEquals("[A, B, C, D, 5]",list.toString());
        list.set(4,"E");
        assertEquals("[A, B, C, D, E]",list.toString());
    }

    public void testSubList() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        assertEquals("[A, B, C, D, E]",list.toString());
        assertEquals("[A, B, C, D, E]",list.subList(0,5).toString());
        assertEquals("[B, C, D, E]",list.subList(1,5).toString());
        assertEquals("[C, D, E]",list.subList(2,5).toString());
        assertEquals("[D, E]",list.subList(3,5).toString());
        assertEquals("[E]",list.subList(4,5).toString());
        assertEquals("[]",list.subList(5,5).toString());
    }

    public void testSubListAddEnd() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        List sublist = list.subList(5,5);
        sublist.add("F");
        assertEquals("[A, B, C, D, E, F]",list.toString());
        assertEquals("[F]",sublist.toString());
        sublist.add("G");
        assertEquals("[A, B, C, D, E, F, G]",list.toString());
        assertEquals("[F, G]",sublist.toString());
    }

    public void testSubListAddBegin() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        List sublist = list.subList(0,0);
        sublist.add("a");
        assertEquals("[a, A, B, C, D, E]",list.toString());
        assertEquals("[a]",sublist.toString());
        sublist.add("b");
        assertEquals("[a, b, A, B, C, D, E]",list.toString());
        assertEquals("[a, b]",sublist.toString());
    }

    public void testSubListAddMiddle() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        List sublist = list.subList(1,3);
        sublist.add("a");
        assertEquals("[A, B, C, a, D, E]",list.toString());
        assertEquals("[B, C, a]",sublist.toString());
        sublist.add("b");
        assertEquals("[A, B, C, a, b, D, E]",list.toString());
        assertEquals("[B, C, a, b]",sublist.toString());
    }

    public void testSubListRemove() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        List sublist = list.subList(1,4);
        assertEquals("[B, C, D]",sublist.toString());
        assertEquals("[A, B, C, D, E]",list.toString());
        sublist.remove("C");
        assertEquals("[B, D]",sublist.toString());
        assertEquals("[A, B, D, E]",list.toString());
        sublist.remove(1);
        assertEquals("[B]",sublist.toString());
        assertEquals("[A, B, E]",list.toString());
        sublist.clear();
        assertEquals("[]",sublist.toString());
        assertEquals("[A, E]",list.toString());
    }

    public void testToArray() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        Object[] elts = list.toArray();
        assertEquals("1",elts[0]);
        assertEquals("2",elts[1]);
        assertEquals("3",elts[2]);
        assertEquals("4",elts[3]);
        assertEquals("5",elts[4]);
        assertEquals(5,elts.length);

        String[] elts2 = (String[])(list.toArray(new String[0]));
        assertEquals("1",elts2[0]);
        assertEquals("2",elts2[1]);
        assertEquals("3",elts2[2]);
        assertEquals("4",elts2[3]);
        assertEquals("5",elts2[4]);
        assertEquals(5,elts2.length);

        String[] elts3 = new String[5];
        assertSame(elts3,list.toArray(elts3));
        assertEquals("1",elts3[0]);
        assertEquals("2",elts3[1]);
        assertEquals("3",elts3[2]);
        assertEquals("4",elts3[3]);
        assertEquals("5",elts3[4]);
        assertEquals(5,elts3.length);

        String[] elts4 = new String[3];
        String[] elts4b = (String[])(list.toArray(elts4));
        assertTrue(elts4 != elts4b);
        assertEquals("1",elts4b[0]);
        assertEquals("2",elts4b[1]);
        assertEquals("3",elts4b[2]);
        assertEquals("4",elts4b[3]);
        assertEquals("5",elts4b[4]);
        assertEquals(5,elts4b.length);
    }

    public void testSerialization() throws Exception {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(buf);
        out.writeObject(list);
        out.flush();
        out.close();

        java.io.ByteArrayInputStream bufin = new java.io.ByteArrayInputStream(buf.toByteArray());
        java.io.ObjectInputStream in = new java.io.ObjectInputStream(bufin);
        Object list2 = in.readObject();

        assertTrue(list != list2);
        assertTrue(list2.equals(list));
        assertTrue(list.equals(list2));
    }

    public void testSerializationWithOpenCursor() throws Exception {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        CursorableLinkedList.Cursor cursor = list.cursor();

        java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(buf);
        out.writeObject(list);
        out.flush();
        out.close();

        java.io.ByteArrayInputStream bufin = new java.io.ByteArrayInputStream(buf.toByteArray());
        java.io.ObjectInputStream in = new java.io.ObjectInputStream(bufin);
        Object list2 = in.readObject();

        assertTrue(list != list2);
        assertTrue(list2.equals(list));
        assertTrue(list.equals(list2));
    }

    public void testLongSerialization() throws Exception {
        // recursive serialization will cause a stack
        // overflow exception with long lists
        for(int i=0;i<10000;i++) {
            list.add(new Integer(i));
        }

        java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(buf);
        out.writeObject(list);
        out.flush();
        out.close();

        java.io.ByteArrayInputStream bufin = new java.io.ByteArrayInputStream(buf.toByteArray());
        java.io.ObjectInputStream in = new java.io.ObjectInputStream(bufin);
        Object list2 = in.readObject();

        assertTrue(list != list2);
        assertTrue(list2.equals(list));
        assertTrue(list.equals(list2));
    }


    /**
     *  Ignore the serialization tests for sublists and sub-sublists.
     *
     *  @return an array of sublist serialization test names 
     */
    public String[] ignoredTests() {
        ArrayList list = new ArrayList();
        String prefix = "TestCursorableLinkedList";
        String bulk = ".bulkTestSubList";
        String[] ignored = new String[] {
          ".testEmptyListSerialization",
          ".testFullListSerialization", 
          ".testEmptyListCompatibility", 
          ".testFullListCompatibility", 
          ".testSimpleSerialization",
          ".testCanonicalEmptyCollectionExists",
          ".testCanonicalFullCollectionExists",
          ".testSerializeDeserializeThenCompare"
        };
        for (int i = 0; i < ignored.length; i++) {
            list.add(prefix + bulk + ignored[i]);
            list.add(prefix + bulk + bulk + ignored[i]);
        }
        return (String[])list.toArray(new String[0]);
    }

}
