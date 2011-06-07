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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import junit.framework.Test;

import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.EmptyListIterator;
import org.apache.commons.collections.iterators.EmptyMapIterator;
import org.apache.commons.collections.iterators.EmptyOrderedIterator;
import org.apache.commons.collections.iterators.EmptyOrderedMapIterator;

/**
 * Tests for IteratorUtils.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Unknown
 */
public class TestIteratorUtils extends BulkTest {

    public TestIteratorUtils(String name) {
        super(name);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestIteratorUtils.class);
    }

    public void testToList() {
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add("Two");
        list.add(null);
        List result = IteratorUtils.toList(list.iterator());
        assertEquals(list, result);
    }

    public void testToArray() {
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add("Two");
        list.add(null);
        Object[] result = IteratorUtils.toArray(list.iterator());
        assertEquals(list, Arrays.asList(result));
    }

    public void testToArray2() {
        List list = new ArrayList();
        list.add("One");
        list.add("Two");
        list.add(null);
        String[] result = (String[]) IteratorUtils.toArray(list.iterator(), String.class);
        assertEquals(list, Arrays.asList(result));
    }
    
    public void testArrayIterator() {
        Object[] objArray = {"a", "b", "c"};
        ResettableIterator iterator = IteratorUtils.arrayIterator(objArray);
        assertTrue(iterator.next().equals("a"));
        assertTrue(iterator.next().equals("b"));
        iterator.reset();
        assertTrue(iterator.next().equals("a"));
        
        try {
            iterator = IteratorUtils.arrayIterator(new Integer(0));
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
                // expected
        }
        
        try {
            iterator = IteratorUtils.arrayIterator(null);
            fail("Expecting NullPointerException");
        } catch (NullPointerException ex) {
                // expected
        }
        
        iterator = IteratorUtils.arrayIterator(objArray, 1);
        assertTrue(iterator.next().equals("b"));
        
        try {
            iterator = IteratorUtils.arrayIterator(objArray, -1);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        iterator = IteratorUtils.arrayIterator(objArray, 3);
        assertTrue(!iterator.hasNext());
        iterator.reset();
        
        try {
            iterator = IteratorUtils.arrayIterator(objArray, 4);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        iterator = IteratorUtils.arrayIterator(objArray, 2, 3);
        assertTrue(iterator.next().equals("c"));
        
        try {
            iterator = IteratorUtils.arrayIterator(objArray, 2, 4);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        try {
            iterator = IteratorUtils.arrayIterator(objArray, -1, 1);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        try {
            iterator = IteratorUtils.arrayIterator(objArray, 2, 1);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        
        int[] intArray = {0, 1, 2};
        iterator = IteratorUtils.arrayIterator(intArray);
        assertTrue(iterator.next().equals(new Integer(0)));
        assertTrue(iterator.next().equals(new Integer(1)));
        iterator.reset();
        assertTrue(iterator.next().equals(new Integer(0)));
        
        iterator = IteratorUtils.arrayIterator(intArray, 1);
        assertTrue(iterator.next().equals(new Integer(1)));
        
        try {
            iterator = IteratorUtils.arrayIterator(intArray, -1);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        iterator = IteratorUtils.arrayIterator(intArray, 3);
        assertTrue(!iterator.hasNext());
        iterator.reset();
        
        try {
            iterator = IteratorUtils.arrayIterator(intArray, 4);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        iterator = IteratorUtils.arrayIterator(intArray, 2, 3);
        assertTrue(iterator.next().equals(new Integer(2)));
        
        try {
            iterator = IteratorUtils.arrayIterator(intArray, 2, 4);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        try {
            iterator = IteratorUtils.arrayIterator(intArray, -1, 1);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        try {
            iterator = IteratorUtils.arrayIterator(intArray, 2, 1);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }          
    }
    
    public void testArrayListIterator() {
        Object[] objArray = {"a", "b", "c", "d"};
        ResettableListIterator iterator = IteratorUtils.arrayListIterator(objArray);
        assertTrue(!iterator.hasPrevious());
        assertTrue(iterator.previousIndex() == -1);
        assertTrue(iterator.nextIndex() == 0);
        assertTrue(iterator.next().equals("a"));
        assertTrue(iterator.previous().equals("a"));
        assertTrue(iterator.next().equals("a"));
        assertTrue(iterator.previousIndex() == 0);
        assertTrue(iterator.nextIndex() == 1);
        assertTrue(iterator.next().equals("b"));
        assertTrue(iterator.next().equals("c"));
        assertTrue(iterator.next().equals("d"));
        assertTrue(iterator.nextIndex() == 4); // size of list
        assertTrue(iterator.previousIndex() == 3);
        
        try {
            iterator = IteratorUtils.arrayListIterator(new Integer(0));
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
                // expected
        }
        
        try {
            iterator = IteratorUtils.arrayListIterator(null);
            fail("Expecting NullPointerException");
        } catch (NullPointerException ex) {
                // expected
        }
        
        iterator = IteratorUtils.arrayListIterator(objArray, 1);
        assertTrue(iterator.previousIndex() == -1); 
        assertTrue(!iterator.hasPrevious());
        assertTrue(iterator.nextIndex() == 0); 
        assertTrue(iterator.next().equals("b"));
        assertTrue(iterator.previousIndex() == 0);        
        
        try {
            iterator = IteratorUtils.arrayListIterator(objArray, -1);
            fail("Expecting IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        iterator = IteratorUtils.arrayListIterator(objArray, 3);
        assertTrue(iterator.hasNext());
        try {
            Object x = iterator.previous();
            fail("Expecting NoSuchElementException.");
        } catch (NoSuchElementException ex) {
            // expected
        }
        
        try {
            iterator = IteratorUtils.arrayListIterator(objArray, 5);
            fail("Expecting IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        iterator = IteratorUtils.arrayListIterator(objArray, 2, 3);
        assertTrue(iterator.next().equals("c"));
        
        try {
            iterator = IteratorUtils.arrayListIterator(objArray, 2, 5);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        try {
            iterator = IteratorUtils.arrayListIterator(objArray, -1, 1);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        try {
            iterator = IteratorUtils.arrayListIterator(objArray, 2, 1);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        
        int[] intArray = {0, 1, 2};
        iterator = IteratorUtils.arrayListIterator(intArray);
        assertTrue(iterator.previousIndex() == -1); 
        assertTrue(!iterator.hasPrevious());
        assertTrue(iterator.nextIndex() == 0); 
        assertTrue(iterator.next().equals(new Integer(0)));
        assertTrue(iterator.previousIndex() == 0); 
        assertTrue(iterator.nextIndex() == 1); 
        assertTrue(iterator.next().equals(new Integer(1)));
        assertTrue(iterator.previousIndex() == 1); 
        assertTrue(iterator.nextIndex() == 2); 
        assertTrue(iterator.previous().equals(new Integer(1)));
        assertTrue(iterator.next().equals(new Integer(1)));
        
        iterator = IteratorUtils.arrayListIterator(intArray, 1);
        assertTrue(iterator.previousIndex() == -1); 
        assertTrue(!iterator.hasPrevious());
        assertTrue(iterator.nextIndex() == 0); 
        assertTrue(iterator.next().equals(new Integer(1)));
        assertTrue(iterator.previous().equals(new Integer(1)));
        assertTrue(iterator.next().equals(new Integer(1)));
        assertTrue(iterator.previousIndex() == 0); 
        assertTrue(iterator.nextIndex() == 1); 
        assertTrue(iterator.next().equals(new Integer(2)));
        assertTrue(iterator.previousIndex() == 1); 
        assertTrue(iterator.nextIndex() == 2); 
        assertTrue(iterator.previous().equals(new Integer(2)));
        assertTrue(iterator.previousIndex() == 0); 
        assertTrue(iterator.nextIndex() == 1); 
        
        try {
            iterator = IteratorUtils.arrayListIterator(intArray, -1);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        iterator = IteratorUtils.arrayListIterator(intArray, 3);
        assertTrue(!iterator.hasNext());
     
        try {
            iterator = IteratorUtils.arrayListIterator(intArray, 4);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        iterator = IteratorUtils.arrayListIterator(intArray, 2, 3);
        assertTrue(!iterator.hasPrevious());
        assertTrue(iterator.previousIndex() == -1);
        assertTrue(iterator.next().equals(new Integer(2)));
        assertTrue(iterator.hasPrevious());
        assertTrue(!iterator.hasNext());
        
        
        try {
            iterator = IteratorUtils.arrayListIterator(intArray, 2, 4);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        try {
            iterator = IteratorUtils.arrayListIterator(intArray, -1, 1);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        
        try {
            iterator = IteratorUtils.arrayListIterator(intArray, 2, 1);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }          
    }
        

    /**
     * Gets an immutable Iterator operating on the elements ["a", "b", "c", "d"].
     */
    private Iterator getImmutableIterator() {
        List list = new ArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        return IteratorUtils.unmodifiableIterator(list.iterator());
    }

    /**
     * Gets an immutable ListIterator operating on the elements ["a", "b", "c", "d"].
     */
    private ListIterator getImmutableListIterator() {
        List list = new ArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        return IteratorUtils.unmodifiableListIterator(list.listIterator());
    }

    //-----------------------------------------------------------------------
    /**
     * Test empty iterator
     */
    public void testEmptyIterator() {
        assertSame(EmptyIterator.INSTANCE, IteratorUtils.EMPTY_ITERATOR);
        assertSame(EmptyIterator.RESETTABLE_INSTANCE, IteratorUtils.EMPTY_ITERATOR);
        assertEquals(true, IteratorUtils.EMPTY_ITERATOR instanceof Iterator);
        assertEquals(true, IteratorUtils.EMPTY_ITERATOR instanceof ResettableIterator);
        assertEquals(false, IteratorUtils.EMPTY_ITERATOR instanceof OrderedIterator);
        assertEquals(false, IteratorUtils.EMPTY_ITERATOR instanceof ListIterator);
        assertEquals(false, IteratorUtils.EMPTY_ITERATOR instanceof MapIterator);
        assertEquals(false, IteratorUtils.EMPTY_ITERATOR.hasNext());
        IteratorUtils.EMPTY_ITERATOR.reset();
        assertSame(IteratorUtils.EMPTY_ITERATOR, IteratorUtils.EMPTY_ITERATOR);
        assertSame(IteratorUtils.EMPTY_ITERATOR, IteratorUtils.emptyIterator());
        try {
            IteratorUtils.EMPTY_ITERATOR.next();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            IteratorUtils.EMPTY_ITERATOR.remove();
            fail();
        } catch (IllegalStateException ex) {}
    }
    
    //-----------------------------------------------------------------------
    /**
     * Test empty list iterator
     */
    public void testEmptyListIterator() {
        assertSame(EmptyListIterator.INSTANCE, IteratorUtils.EMPTY_LIST_ITERATOR);
        assertSame(EmptyListIterator.RESETTABLE_INSTANCE, IteratorUtils.EMPTY_LIST_ITERATOR);
        assertEquals(true, IteratorUtils.EMPTY_LIST_ITERATOR instanceof Iterator);
        assertEquals(true, IteratorUtils.EMPTY_LIST_ITERATOR instanceof ListIterator);
        assertEquals(true, IteratorUtils.EMPTY_LIST_ITERATOR instanceof ResettableIterator);
        assertEquals(true, IteratorUtils.EMPTY_LIST_ITERATOR instanceof ResettableListIterator);
        assertEquals(false, IteratorUtils.EMPTY_LIST_ITERATOR instanceof MapIterator);
        assertEquals(false, IteratorUtils.EMPTY_LIST_ITERATOR.hasNext());
        assertEquals(0, IteratorUtils.EMPTY_LIST_ITERATOR.nextIndex());
        assertEquals(-1, IteratorUtils.EMPTY_LIST_ITERATOR.previousIndex());
        IteratorUtils.EMPTY_LIST_ITERATOR.reset();
        assertSame(IteratorUtils.EMPTY_LIST_ITERATOR, IteratorUtils.EMPTY_LIST_ITERATOR);
        assertSame(IteratorUtils.EMPTY_LIST_ITERATOR, IteratorUtils.emptyListIterator());
        try {
            IteratorUtils.EMPTY_LIST_ITERATOR.next();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            IteratorUtils.EMPTY_LIST_ITERATOR.previous();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            IteratorUtils.EMPTY_LIST_ITERATOR.remove();
            fail();
        } catch (IllegalStateException ex) {}
        try {
            IteratorUtils.EMPTY_LIST_ITERATOR.set(null);
            fail();
        } catch (IllegalStateException ex) {}
        try {
            IteratorUtils.EMPTY_LIST_ITERATOR.add(null);
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
    
    //-----------------------------------------------------------------------
    /**
     * Test empty map iterator
     */
    public void testEmptyMapIterator() {
        assertSame(EmptyMapIterator.INSTANCE, IteratorUtils.EMPTY_MAP_ITERATOR);
        assertEquals(true, IteratorUtils.EMPTY_MAP_ITERATOR instanceof Iterator);
        assertEquals(true, IteratorUtils.EMPTY_MAP_ITERATOR instanceof MapIterator);
        assertEquals(true, IteratorUtils.EMPTY_MAP_ITERATOR instanceof ResettableIterator);
        assertEquals(false, IteratorUtils.EMPTY_MAP_ITERATOR instanceof ListIterator);
        assertEquals(false, IteratorUtils.EMPTY_MAP_ITERATOR instanceof OrderedIterator);
        assertEquals(false, IteratorUtils.EMPTY_MAP_ITERATOR instanceof OrderedMapIterator);
        assertEquals(false, IteratorUtils.EMPTY_MAP_ITERATOR.hasNext());
        ((ResettableIterator) IteratorUtils.EMPTY_MAP_ITERATOR).reset();
        assertSame(IteratorUtils.EMPTY_MAP_ITERATOR, IteratorUtils.EMPTY_MAP_ITERATOR);
        assertSame(IteratorUtils.EMPTY_MAP_ITERATOR, IteratorUtils.emptyMapIterator());
        try {
            IteratorUtils.EMPTY_MAP_ITERATOR.next();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            IteratorUtils.EMPTY_MAP_ITERATOR.remove();
            fail();
        } catch (IllegalStateException ex) {}
        try {
            IteratorUtils.EMPTY_MAP_ITERATOR.getKey();
            fail();
        } catch (IllegalStateException ex) {}
        try {
            IteratorUtils.EMPTY_MAP_ITERATOR.getValue();
            fail();
        } catch (IllegalStateException ex) {}
        try {
            IteratorUtils.EMPTY_MAP_ITERATOR.setValue(null);
            fail();
        } catch (IllegalStateException ex) {}
    }
    
    //-----------------------------------------------------------------------
    /**
     * Test empty map iterator
     */
    public void testEmptyOrderedIterator() {
        assertSame(EmptyOrderedIterator.INSTANCE, IteratorUtils.EMPTY_ORDERED_ITERATOR);
        assertEquals(true, IteratorUtils.EMPTY_ORDERED_ITERATOR instanceof Iterator);
        assertEquals(true, IteratorUtils.EMPTY_ORDERED_ITERATOR instanceof OrderedIterator);
        assertEquals(true, IteratorUtils.EMPTY_ORDERED_ITERATOR instanceof ResettableIterator);
        assertEquals(false, IteratorUtils.EMPTY_ORDERED_ITERATOR instanceof ListIterator);
        assertEquals(false, IteratorUtils.EMPTY_ORDERED_ITERATOR instanceof MapIterator);
        assertEquals(false, IteratorUtils.EMPTY_ORDERED_ITERATOR.hasNext());
        assertEquals(false, IteratorUtils.EMPTY_ORDERED_ITERATOR.hasPrevious());
        ((ResettableIterator) IteratorUtils.EMPTY_ORDERED_ITERATOR).reset();
        assertSame(IteratorUtils.EMPTY_ORDERED_ITERATOR, IteratorUtils.EMPTY_ORDERED_ITERATOR);
        assertSame(IteratorUtils.EMPTY_ORDERED_ITERATOR, IteratorUtils.emptyOrderedIterator());
        try {
            IteratorUtils.EMPTY_ORDERED_ITERATOR.next();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            IteratorUtils.EMPTY_ORDERED_ITERATOR.previous();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            IteratorUtils.EMPTY_ORDERED_ITERATOR.remove();
            fail();
        } catch (IllegalStateException ex) {}
    }
    
    //-----------------------------------------------------------------------
    /**
     * Test empty map iterator
     */
    public void testEmptyOrderedMapIterator() {
        assertSame(EmptyOrderedMapIterator.INSTANCE, IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR);
        assertEquals(true, IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR instanceof Iterator);
        assertEquals(true, IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR instanceof MapIterator);
        assertEquals(true, IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR instanceof OrderedMapIterator);
        assertEquals(true, IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR instanceof ResettableIterator);
        assertEquals(false, IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR instanceof ListIterator);
        assertEquals(false, IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR.hasNext());
        assertEquals(false, IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR.hasPrevious());
        ((ResettableIterator) IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR).reset();
        assertSame(IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR, IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR);
        assertSame(IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR, IteratorUtils.emptyOrderedMapIterator());
        try {
            IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR.next();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR.previous();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR.remove();
            fail();
        } catch (IllegalStateException ex) {}
        try {
            IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR.getKey();
            fail();
        } catch (IllegalStateException ex) {}
        try {
            IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR.getValue();
            fail();
        } catch (IllegalStateException ex) {}
        try {
            IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR.setValue(null);
            fail();
        } catch (IllegalStateException ex) {}
    }
    
    //-----------------------------------------------------------------------
	/**
	 * Test next() and hasNext() for an immutable Iterator.
	 */
    public void testUnmodifiableIteratorIteration() {
        Iterator iterator = getImmutableIterator();

        assertTrue(iterator.hasNext());

        assertEquals("a", iterator.next());

        assertTrue(iterator.hasNext());

        assertEquals("b", iterator.next());

        assertTrue(iterator.hasNext());

        assertEquals("c", iterator.next());

        assertTrue(iterator.hasNext());

        assertEquals("d", iterator.next());

        assertTrue(!iterator.hasNext());
    }

    /**
     * Test next(), hasNext(), previous() and hasPrevious() for an immutable
     * ListIterator.
     */
    public void testUnmodifiableListIteratorIteration() {
        ListIterator listIterator = getImmutableListIterator();

        assertTrue(!listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertEquals("a", listIterator.next());

        assertTrue(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertEquals("b", listIterator.next());

        assertTrue(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertEquals("c", listIterator.next());

        assertTrue(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertEquals("d", listIterator.next());

        assertTrue(listIterator.hasPrevious());
        assertTrue(!listIterator.hasNext());

        assertEquals("d", listIterator.previous());

        assertTrue(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertEquals("c", listIterator.previous());

        assertTrue(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertEquals("b", listIterator.previous());

        assertTrue(listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());

        assertEquals("a", listIterator.previous());

        assertTrue(!listIterator.hasPrevious());
        assertTrue(listIterator.hasNext());
    }

    /**
     * Test remove() for an immutable Iterator.
     */
    public void testUnmodifiableIteratorImmutability() {
        Iterator iterator = getImmutableIterator();

        try {
            iterator.remove();
            // We shouldn't get to here.
            fail("remove() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // This is correct; ignore the exception.
        }

        iterator.next();

        try {
            iterator.remove();
            // We shouldn't get to here.
            fail("remove() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // This is correct; ignore the exception.
        }

    }

    /**
     * Test remove() for an immutable ListIterator.
     */
    public void testUnmodifiableListIteratorImmutability() {
    	ListIterator listIterator = getImmutableListIterator();

        try {
            listIterator.remove();
            // We shouldn't get to here.
            fail("remove() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // This is correct; ignore the exception.
        }

        try {
            listIterator.set("a");
            // We shouldn't get to here.
            fail("set(Object) should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // This is correct; ignore the exception.
        }

        try {
            listIterator.add("a");
            // We shouldn't get to here.
            fail("add(Object) should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // This is correct; ignore the exception.
        }

        listIterator.next();

        try {
            listIterator.remove();
            // We shouldn't get to here.
            fail("remove() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // This is correct; ignore the exception.
        }

        try {
            listIterator.set("a");
            // We shouldn't get to here.
            fail("set(Object) should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // This is correct; ignore the exception.
        }

        try {
            listIterator.add("a");
            // We shouldn't get to here.
            fail("add(Object) should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // This is correct; ignore the exception.
        }
    }
}
