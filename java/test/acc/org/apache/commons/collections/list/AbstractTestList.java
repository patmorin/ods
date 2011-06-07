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

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.collection.AbstractTestCollection;
import org.apache.commons.collections.iterators.AbstractTestListIterator;

/**
 * Abstract test class for {@link java.util.List} methods and contracts.
 * <p>
 * To use, simply extend this class, and implement
 * the {@link #makeEmptyList} method.
 * <p>
 * If your {@link List} fails one of these tests by design,
 * you may still use this base set of cases.  Simply override the
 * test case (method) your {@link List} fails or override one of the
 * protected methods from AbstractTestCollection.
 *
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Rodney Waldhoff
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Neil O'Toole
 */
public abstract class AbstractTestList extends AbstractTestCollection {

    /**
     * JUnit constructor.
     * 
     * @param testName  the test class name
     */
    public AbstractTestList(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------
    /**
     *  Returns true if the collections produced by 
     *  {@link #makeCollection()} and {@link #makeFullCollection()}
     *  support the <code>set operation.<p>
     *  Default implementation returns true.  Override if your collection
     *  class does not support set.
     */
    public boolean isSetSupported() {
        return true;
    }

    //-----------------------------------------------------------------------
    /**
     *  Verifies that the test list implementation matches the confirmed list
     *  implementation.
     */
    public void verify() {
        super.verify();

        List list1 = getList();
        List list2 = getConfirmedList();

        assertEquals("List should equal confirmed", list1, list2);
        assertEquals("Confirmed should equal list", list2, list1);

        assertEquals("Hash codes should be equal", list1.hashCode(), list2.hashCode());

        int i = 0;
        Iterator iterator1 = list1.iterator();
        Iterator iterator2 = list2.iterator();
        Object[] array = list1.toArray();
        while (iterator2.hasNext()) {
            assertTrue("List iterator should have next", iterator1.hasNext());
            Object o1 = iterator1.next();
            Object o2 = iterator2.next();
            assertEquals("Iterator elements should be equal", o1, o2);
            o2 = list1.get(i);
            assertEquals("get should return correct element", o1, o2);
            o2 = array[i];
            assertEquals("toArray should have correct element", o1, o2);
            i++;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * List equals method is defined.
     */
    public boolean isEqualsCheckable() {
        return true;
    }

    /**
     * Returns an empty {@link ArrayList}.
     */
    public Collection makeConfirmedCollection() {
        ArrayList list = new ArrayList();
        return list;
    }

    /**
     * Returns a full {@link ArrayList}.
     */
    public Collection makeConfirmedFullCollection() {
        ArrayList list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

    /**
     * Return a new, empty {@link List} to be used for testing.
     *
     * @return an empty list for testing.
     */
    public abstract List makeEmptyList();

    /**
     * Return a new, full {@link List} to be used for testing.
     *
     * @return a full list for testing
     */
    public List makeFullList() {
        // only works if list supports optional "addAll(Collection)" 
        List list = makeEmptyList();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

    /**
     * Returns {@link #makeEmptyList()}.
     *
     * @return an empty list to be used for testing
     */
    public final Collection makeCollection() {
        return makeEmptyList();
    }

    /**
     * Returns {@link #makeFullList()}.
     *
     * @return a full list to be used for testing
     */
    public final Collection makeFullCollection() {
        return makeFullList();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the {@link #collection} field cast to a {@link List}.
     *
     * @return the collection field as a List
     */
    public List getList() {
        return (List) collection;
    }

    /**
     * Returns the {@link #confirmed} field cast to a {@link List}.
     *
     * @return the confirmed field as a List
     */
    public List getConfirmedList() {
        return (List) confirmed;
    }

    //-----------------------------------------------------------------------
    /**
     *  Tests bounds checking for {@link List#add(int, Object)} on an
     *  empty list.
     */
    public void testListAddByIndexBoundsChecking() {
        if (!isAddSupported()) {
            return;
        }

        List list;
        Object element = getOtherElements()[0];

        try {
            list = makeEmptyList();
            list.add(Integer.MIN_VALUE, element);
            fail("List.add should throw IndexOutOfBoundsException [Integer.MIN_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list = makeEmptyList();
            list.add(-1, element);
            fail("List.add should throw IndexOutOfBoundsException [-1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list = makeEmptyList();
            list.add(1, element);
            fail("List.add should throw IndexOutOfBoundsException [1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list = makeEmptyList();
            list.add(Integer.MAX_VALUE, element);
            fail("List.add should throw IndexOutOfBoundsException [Integer.MAX_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     *  Tests bounds checking for {@link List#add(int, Object)} on a
     *  full list.
     */
    public void testListAddByIndexBoundsChecking2() {
        if (!isAddSupported()) {
            return;
        }

        List list;
        Object element = getOtherElements()[0];

        try {
            list = makeFullList();
            list.add(Integer.MIN_VALUE, element);
            fail("List.add should throw IndexOutOfBoundsException [Integer.MIN_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list = makeFullList();
            list.add(-1, element);
            fail("List.add should throw IndexOutOfBoundsException [-1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list = makeFullList();
            list.add(list.size() + 1, element);
            fail("List.add should throw IndexOutOfBoundsException [size + 1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list = makeFullList();
            list.add(Integer.MAX_VALUE, element);
            fail("List.add should throw IndexOutOfBoundsException [Integer.MAX_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     *  Tests {@link List#add(int,Object)}.
     */
    public void testListAddByIndex() {
        if (!isAddSupported()) {
            return;
        }

        Object element = getOtherElements()[0];
        int max = getFullElements().length;

        for (int i = 0; i <= max; i++) {
            resetFull();
            ((List) collection).add(i, element);
            ((List) confirmed).add(i, element);
            verify();
        }
    }

    /**
     *  Tests {@link List#equals(Object)}.
     */
    public void testListEquals() {
        resetEmpty();
        List list = getList();
        assertEquals("Empty lists should be equal", true, list.equals(confirmed));
        verify();
        assertEquals("Empty list should equal self", true, list.equals(list));
        verify();

        List list2 = Arrays.asList(getFullElements());
        assertEquals("Empty list shouldn't equal full", false, list.equals(list2));
        verify();

        list2 = Arrays.asList(getOtherElements());
        assertEquals("Empty list shouldn't equal other", false, list.equals(list2));
        verify();

        resetFull();
        list = getList();
        assertEquals("Full lists should be equal", true, list.equals(confirmed));
        verify();
        assertEquals("Full list should equal self", true, list.equals(list));
        verify();

        list2 = makeEmptyList();
        assertEquals("Full list shouldn't equal empty", false, list.equals(list2));
        verify();

        list2 = Arrays.asList(getOtherElements());
        assertEquals("Full list shouldn't equal other", false, list.equals(list2));
        verify();

        list2 = Arrays.asList(getFullElements());
        if (list2.size() < 2 && isAddSupported()) {
            // main list is only size 1, so lets add other elements to get a better list
            list.addAll(Arrays.asList(getOtherElements()));
            confirmed.addAll(Arrays.asList(getOtherElements()));
            list2 = new ArrayList(list2);
            list2.addAll(Arrays.asList(getOtherElements()));
        }
        if (list2.size() > 1) {
            Collections.reverse(list2);
            assertEquals(
                "Full list shouldn't equal full list with same elements but different order",
                false, list.equals(list2));
            verify();
        }

        resetFull();
        list = getList();
        assertEquals("List shouldn't equal String", false, list.equals(""));
        verify();

        final List listForC = Arrays.asList(getFullElements());
        Collection c = new AbstractCollection() {
            public int size() {
                return listForC.size();
            }

            public Iterator iterator() {
                return listForC.iterator();
            }
        };

        assertEquals("List shouldn't equal nonlist with same elements in same order", false, list.equals(c));
        verify();
    }

    /**
     *  Tests {@link List#hashCode()}.
     */
    public void testListHashCode() {
        resetEmpty();
        int hash1 = collection.hashCode();
        int hash2 = confirmed.hashCode();
        assertEquals("Empty lists should have equal hashCodes", hash1, hash2);
        verify();

        resetFull();
        hash1 = collection.hashCode();
        hash2 = confirmed.hashCode();
        assertEquals("Full lists should have equal hashCodes", hash1, hash2);
        verify();
    }

    /**
     *  Tests {@link List#get(int)}.
     */
    public void testListGetByIndex() {
        resetFull();
        List list = getList();
        Object[] elements = getFullElements();
        for (int i = 0; i < elements.length; i++) {
            assertEquals("List should contain correct elements", elements[i], list.get(i));
            verify();
        }
    }

    /**
     *  Tests bounds checking for {@link List#get(int)} on an
     *  empty list.
     */
    public void testListGetByIndexBoundsChecking() {
        List list = makeEmptyList();

        try {
            list.get(Integer.MIN_VALUE);
            fail("List.get should throw IndexOutOfBoundsException [Integer.MIN_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.get(-1);
            fail("List.get should throw IndexOutOfBoundsException [-1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.get(0);
            fail("List.get should throw IndexOutOfBoundsException [0]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.get(1);
            fail("List.get should throw IndexOutOfBoundsException [1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.get(Integer.MAX_VALUE);
            fail("List.get should throw IndexOutOfBoundsException [Integer.MAX_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     *  Tests bounds checking for {@link List#get(int)} on a
     *  full list.
     */
    public void testListGetByIndexBoundsChecking2() {
        List list = makeFullList();

        try {
            list.get(Integer.MIN_VALUE);
            fail("List.get should throw IndexOutOfBoundsException [Integer.MIN_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.get(-1);
            fail("List.get should throw IndexOutOfBoundsException [-1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.get(getFullElements().length);
            fail("List.get should throw IndexOutOfBoundsException [size]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.get(Integer.MAX_VALUE);
            fail("List.get should throw IndexOutOfBoundsException [Integer.MAX_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     *  Tests {@link List#indexOf}.
     */
    public void testListIndexOf() {
        resetFull();
        List list1 = getList();
        List list2 = getConfirmedList();

        Iterator iterator = list2.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            assertEquals("indexOf should return correct result",
                list1.indexOf(element), list2.indexOf(element));
            verify();
        }

        Object[] other = getOtherElements();
        for (int i = 0; i < other.length; i++) {
            assertEquals("indexOf should return -1 for nonexistent element",
                list1.indexOf(other[i]), -1);
            verify();
        }
    }

    /**
     *  Tests {@link List#lastIndexOf}.
     */
    public void testListLastIndexOf() {
        resetFull();
        List list1 = getList();
        List list2 = getConfirmedList();

        Iterator iterator = list2.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            assertEquals("lastIndexOf should return correct result",
              list1.lastIndexOf(element), list2.lastIndexOf(element));
            verify();
        }

        Object[] other = getOtherElements();
        for (int i = 0; i < other.length; i++) {
            assertEquals("lastIndexOf should return -1 for nonexistent " +
              "element", list1.lastIndexOf(other[i]), -1);
            verify();
        }
    }

    /**
     *  Tests bounds checking for {@link List#set(int,Object)} on an
     *  empty list.
     */
    public void testListSetByIndexBoundsChecking() {
        if (!isSetSupported()) {
            return;
        }

        List list = makeEmptyList();
        Object element = getOtherElements()[0];

        try {
            list.set(Integer.MIN_VALUE, element);
            fail("List.set should throw IndexOutOfBoundsException [Integer.MIN_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.set(-1, element);
            fail("List.set should throw IndexOutOfBoundsException [-1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.set(0, element);
            fail("List.set should throw IndexOutOfBoundsException [0]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.set(1, element);
            fail("List.set should throw IndexOutOfBoundsException [1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.set(Integer.MAX_VALUE, element);
            fail("List.set should throw IndexOutOfBoundsException [Integer.MAX_VALUE]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }


    /**
     *  Tests bounds checking for {@link List#set(int,Object)} on a
     *  full list.
     */
    public void testListSetByIndexBoundsChecking2() {
        if (!isSetSupported()) return;

        List list = makeFullList();
        Object element = getOtherElements()[0];

        try {
            list.set(Integer.MIN_VALUE, element);
            fail("List.set should throw IndexOutOfBoundsException " +
              "[Integer.MIN_VALUE]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.set(-1, element);
            fail("List.set should throw IndexOutOfBoundsException [-1]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.set(getFullElements().length, element);
            fail("List.set should throw IndexOutOfBoundsException [size]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.set(Integer.MAX_VALUE, element);
            fail("List.set should throw IndexOutOfBoundsException " +
              "[Integer.MAX_VALUE]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 
    }


    /**
     *  Test {@link List#set(int,Object)}.
     */
    public void testListSetByIndex() {
        if (!isSetSupported()) return;

        resetFull();
        Object[] elements = getFullElements();
        Object[] other = getOtherElements();

        for (int i = 0; i < elements.length; i++) {
            Object n = other[i % other.length];
            Object v = ((List)collection).set(i, n);
            assertEquals("Set should return correct element", elements[i], v);
            ((List)confirmed).set(i, n);
            verify();
        }
    }


    /**
     *  If {@link #isSetSupported()} returns false, tests that set operation
     *  raises <Code>UnsupportedOperationException.
     */
    public void testUnsupportedSet() {
        if (isSetSupported()) return;
        
        resetFull();
        try {
            ((List) collection).set(0, new Object());
            fail("Emtpy collection should not support set.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        // make sure things didn't change even if the expected exception was
        // thrown.
        verify();
    }
    

    /**
     *  Tests bounds checking for {@link List#remove(int)} on an
     *  empty list.
     */
    public void testListRemoveByIndexBoundsChecking() {
        if (!isRemoveSupported()) return;

        List list = makeEmptyList();

        try {
            list.remove(Integer.MIN_VALUE);
            fail("List.remove should throw IndexOutOfBoundsException " +
              "[Integer.MIN_VALUE]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.remove(-1);
            fail("List.remove should throw IndexOutOfBoundsException [-1]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.remove(0);
            fail("List.remove should throw IndexOutOfBoundsException [0]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.remove(1);
            fail("List.remove should throw IndexOutOfBoundsException [1]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.remove(Integer.MAX_VALUE);
            fail("List.remove should throw IndexOutOfBoundsException " +
              "[Integer.MAX_VALUE]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        }
    }


    /**
     *  Tests bounds checking for {@link List#remove(int)} on a
     *  full list.
     */
    public void testListRemoveByIndexBoundsChecking2() {
        if (!isRemoveSupported()) return;

        List list = makeFullList();

        try {
            list.remove(Integer.MIN_VALUE);
            fail("List.remove should throw IndexOutOfBoundsException " +
              "[Integer.MIN_VALUE]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.remove(-1);
            fail("List.remove should throw IndexOutOfBoundsException [-1]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.remove(getFullElements().length);
            fail("List.remove should throw IndexOutOfBoundsException [size]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 

        try {
            list.remove(Integer.MAX_VALUE);
            fail("List.remove should throw IndexOutOfBoundsException " +
              "[Integer.MAX_VALUE]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 
    }


    /**
     *  Tests {@link List#remove(int)}.
     */
    public void testListRemoveByIndex() {
        if (!isRemoveSupported()) return;

        int max = getFullElements().length;
        for (int i = 0; i < max; i++) {
            resetFull();
            Object o1 = ((List)collection).remove(i);
            Object o2 = ((List)confirmed).remove(i);
            assertEquals("remove should return correct element", o1, o2);
            verify();
        }
    }


    /**
     *  Tests the read-only bits of {@link List#listIterator()}.
     */
    public void testListListIterator() {
        resetFull();
        forwardTest(getList().listIterator(), 0);
        backwardTest(getList().listIterator(), 0);
    }


    /**
     *  Tests the read-only bits of {@link List#listIterator(int)}.
     */
    public void testListListIteratorByIndex() {
        resetFull();
        try {
            getList().listIterator(-1);
        } catch (IndexOutOfBoundsException ex) {}
        resetFull();
        try {
            getList().listIterator(getList().size() + 1);
        } catch (IndexOutOfBoundsException ex) {}
        resetFull();
        for (int i = 0; i <= confirmed.size(); i++) {
            forwardTest(getList().listIterator(i), i);
            backwardTest(getList().listIterator(i), i);
        }
        resetFull();
        for (int i = 0; i <= confirmed.size(); i++) {
            backwardTest(getList().listIterator(i), i);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Tests remove on list iterator is correct.
     */
    public void testListListIteratorPreviousRemoveNext() {
        if (isRemoveSupported() == false) return;
        resetFull();
        if (collection.size() < 4) return;
        ListIterator it = getList().listIterator();
        Object zero = it.next();
        Object one = it.next();
        Object two = it.next();
        Object two2 = it.previous();
        Object one2 = it.previous();
        assertEquals(one, one2);
        assertEquals(two, two2);
        assertEquals(zero, getList().get(0));
        assertEquals(one, getList().get(1));
        assertEquals(two, getList().get(2));
        
        it.remove(); // removed element at index 1 (one)
        assertEquals(zero, getList().get(0));
        assertEquals(two, getList().get(1));
        Object two3 = it.next();  // do next after remove
        assertEquals(two, two3);
        assertEquals(collection.size() > 2, it.hasNext());
        assertEquals(true, it.hasPrevious());
    }

    /**
     * Tests remove on list iterator is correct.
     */
    public void testListListIteratorPreviousRemovePrevious() {
        if (isRemoveSupported() == false) return;
        resetFull();
        if (collection.size() < 4) return;
        ListIterator it = getList().listIterator();
        Object zero = it.next();
        Object one = it.next();
        Object two = it.next();
        Object two2 = it.previous();
        Object one2 = it.previous();
        assertEquals(one, one2);
        assertEquals(two, two2);
        assertEquals(zero, getList().get(0));
        assertEquals(one, getList().get(1));
        assertEquals(two, getList().get(2));
        
        it.remove(); // removed element at index 1 (one)
        assertEquals(zero, getList().get(0));
        assertEquals(two, getList().get(1));
        Object zero3 = it.previous();  // do previous after remove
        assertEquals(zero, zero3);
        assertEquals(false, it.hasPrevious());
        assertEquals(collection.size() > 2, it.hasNext());
    }

    /**
     * Tests remove on list iterator is correct.
     */
    public void testListListIteratorNextRemoveNext() {
        if (isRemoveSupported() == false) return;
        resetFull();
        if (collection.size() < 4) return;
        ListIterator it = getList().listIterator();
        Object zero = it.next();
        Object one = it.next();
        Object two = it.next();
        assertEquals(zero, getList().get(0));
        assertEquals(one, getList().get(1));
        assertEquals(two, getList().get(2));
        Object three = getList().get(3);
        
        it.remove(); // removed element at index 2 (two)
        assertEquals(zero, getList().get(0));
        assertEquals(one, getList().get(1));
        Object three2 = it.next();  // do next after remove
        assertEquals(three, three2);
        assertEquals(collection.size() > 3, it.hasNext());
        assertEquals(true, it.hasPrevious());
    }

    /**
     * Tests remove on list iterator is correct.
     */
    public void testListListIteratorNextRemovePrevious() {
        if (isRemoveSupported() == false) return;
        resetFull();
        if (collection.size() < 4) return;
        ListIterator it = getList().listIterator();
        Object zero = it.next();
        Object one = it.next();
        Object two = it.next();
        assertEquals(zero, getList().get(0));
        assertEquals(one, getList().get(1));
        assertEquals(two, getList().get(2));
        
        it.remove(); // removed element at index 2 (two)
        assertEquals(zero, getList().get(0));
        assertEquals(one, getList().get(1));
        Object one2 = it.previous();  // do previous after remove
        assertEquals(one, one2);
        assertEquals(true, it.hasNext());
        assertEquals(true, it.hasPrevious());
    }

    //-----------------------------------------------------------------------
    /**
     *  Traverses to the end of the given iterator.
     *
     *  @param iter  the iterator to traverse
     *  @param i     the starting index
     */
    private void forwardTest(ListIterator iter, int i) {
        List list = getList();
        int max = getFullElements().length;

        while (i < max) {
            assertTrue("Iterator should have next", iter.hasNext());
            assertEquals("Iterator.nextIndex should work", 
              iter.nextIndex(), i);
            assertEquals("Iterator.previousIndex should work",
              iter.previousIndex(), i - 1);
            Object o = iter.next();
            assertEquals("Iterator returned correct element", list.get(i), o);
            i++;
        }

        assertTrue("Iterator shouldn't have next", !iter.hasNext());
        assertEquals("nextIndex should be size", iter.nextIndex(), max);
        assertEquals("previousIndex should be size - 1", 
          iter.previousIndex(), max - 1);

        try {
            iter.next();
            fail("Exhausted iterator should raise NoSuchElement");
        } catch (NoSuchElementException e) {
            // expected
        }
    }


    /**
     *  Traverses to the beginning of the given iterator.
     *
     *  @param iter  the iterator to traverse
     *  @param i     the starting index
     */
    private void backwardTest(ListIterator iter, int i) {
        List list = getList();

        while (i > 0) {
            assertTrue("Iterator should have previous, i:" + i, iter.hasPrevious());
            assertEquals("Iterator.nextIndex should work, i:" + i, iter.nextIndex(), i);
            assertEquals("Iterator.previousIndex should work, i:" + i, iter.previousIndex(), i - 1);
            Object o = iter.previous();
            assertEquals("Iterator returned correct element", list.get(i - 1), o);
            i--;
        }

        assertTrue("Iterator shouldn't have previous", !iter.hasPrevious());
        int nextIndex = iter.nextIndex();
        assertEquals("nextIndex should be 0, actual value: " + nextIndex, nextIndex, 0);
        int prevIndex = iter.previousIndex();
        assertEquals("previousIndex should be -1, actual value: " + prevIndex, prevIndex, -1);

        try {
            iter.previous();
            fail("Exhausted iterator should raise NoSuchElement");
        } catch (NoSuchElementException e) {
            // expected
        }

    }


    /**
     *  Tests the {@link ListIterator#add(Object)} method of the list
     *  iterator.
     */
    public void testListIteratorAdd() {
        if (!isAddSupported()) return;

        resetEmpty();
        List list1 = getList();
        List list2 = getConfirmedList();

        Object[] elements = getFullElements();
        ListIterator iter1 = list1.listIterator();
        ListIterator iter2 = list2.listIterator();

        for (int i = 0; i < elements.length; i++) {
            iter1.add(elements[i]);
            iter2.add(elements[i]);
            verify();
        }

        resetFull();
        iter1 = getList().listIterator();
        iter2 = getConfirmedList().listIterator();
        for (int i = 0; i < elements.length; i++) {
            iter1.next();
            iter2.next();
            iter1.add(elements[i]);
            iter2.add(elements[i]);
            verify();
        }
    }


    /**
     *  Tests the {@link ListIterator#set(Object)} method of the list
     *  iterator.
     */
    public void testListIteratorSet() {
        if (!isSetSupported()) return;

        Object[] elements = getFullElements();

        resetFull();
        ListIterator iter1 = getList().listIterator();
        ListIterator iter2 = getConfirmedList().listIterator();
        for (int i = 0; i < elements.length; i++) {
            iter1.next();
            iter2.next();
            iter1.set(elements[i]);
            iter2.set(elements[i]);
            verify();
        }
    }


    public void testEmptyListSerialization() 
    throws IOException, ClassNotFoundException {
        List list = makeEmptyList();
        if (!(list instanceof Serializable && isTestSerialization())) return;
        
        byte[] objekt = writeExternalFormToBytes((Serializable) list);
        List list2 = (List) readExternalFormFromBytes(objekt);

        assertTrue("Both lists are empty",list.size()  == 0);
        assertTrue("Both lists are empty",list2.size() == 0);
    }

    public void testFullListSerialization() 
    throws IOException, ClassNotFoundException {
        List list = makeFullList();
        int size = getFullElements().length;
        if (!(list instanceof Serializable && isTestSerialization())) return;
        
        byte[] objekt = writeExternalFormToBytes((Serializable) list);
        List list2 = (List) readExternalFormFromBytes(objekt);

        assertEquals("Both lists are same size",list.size(), size);
        assertEquals("Both lists are same size",list2.size(), size);
    }

    /**
     * Compare the current serialized form of the List
     * against the canonical version in CVS.
     */
    public void testEmptyListCompatibility() throws IOException, ClassNotFoundException {
        /**
         * Create canonical objects with this code
        List list = makeEmptyList();
        if (!(list instanceof Serializable)) return;
        
        writeExternalFormToDisk((Serializable) list, getCanonicalEmptyCollectionName(list));
        */

        // test to make sure the canonical form has been preserved
        List list = makeEmptyList();
        if(list instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            List list2 = (List) readExternalFormFromDisk(getCanonicalEmptyCollectionName(list));
            assertTrue("List is empty",list2.size()  == 0);
            assertEquals(list, list2);
        }
    }

    /**
     * Compare the current serialized form of the List
     * against the canonical version in CVS.
     */
    public void testFullListCompatibility() throws IOException, ClassNotFoundException {
        /**
         * Create canonical objects with this code
        List list = makeFullList();
        if (!(list instanceof Serializable)) return;
        
        writeExternalFormToDisk((Serializable) list, getCanonicalFullCollectionName(list));
        */

        // test to make sure the canonical form has been preserved
        List list = makeFullList();
        if(list instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            List list2 = (List) readExternalFormFromDisk(getCanonicalFullCollectionName(list));
            if (list2.size() == 4) {
                // old serialized tests
                return;
            }
            assertEquals("List is the right size",list.size(), list2.size());
            assertEquals(list, list2);
        }
    }

    //-----------------------------------------------------------------------
    /**
     *  Returns a {@link BulkTest} for testing {@link List#subList(int,int)}.
     *  The returned bulk test will run through every <code>TestList</code>
     *  method, <i>including</i> another <code>bulkTestSubList</code>.
     *  Sublists are tested until the size of the sublist is less than 10.
     *  Each sublist is 6 elements smaller than its parent list.
     *  (By default this means that two rounds of sublists will be tested).
     *  The verify() method is overloaded to test that the original list is
     *  modified when the sublist is.
     */
    public BulkTest bulkTestSubList() {
        if (getFullElements().length - 6 < 10) return null;
        return new BulkTestSubList(this);
    }


   public static class BulkTestSubList extends AbstractTestList {

       private AbstractTestList outer;


       public BulkTestSubList(AbstractTestList outer) {
           super("");
           this.outer = outer;
       }


       public Object[] getFullElements() {
           List l = Arrays.asList(outer.getFullElements());
           return l.subList(3, l.size() - 3).toArray();
       }


       public Object[] getOtherElements() {
           return outer.getOtherElements();
       }


       public boolean isAddSupported() {
           return outer.isAddSupported();
       }

       public boolean isSetSupported() {
           return outer.isSetSupported();
       }

       public boolean isRemoveSupported() {
           return outer.isRemoveSupported();
       }


       public List makeEmptyList() { 
           return outer.makeFullList().subList(4, 4); 
       }


       public List makeFullList() {
           int size = getFullElements().length;
           return outer.makeFullList().subList(3, size - 3);
       }


       public void resetEmpty() {
           outer.resetFull();
           this.collection = outer.getList().subList(4, 4);
           this.confirmed = outer.getConfirmedList().subList(4, 4);
       }

       public void resetFull() {
           outer.resetFull();
           int size = outer.confirmed.size();
           this.collection = outer.getList().subList(3, size - 3);
           this.confirmed = outer.getConfirmedList().subList(3, size - 3);
       }


       public void verify() {
           super.verify();
           outer.verify();
       }

       public boolean isTestSerialization() {
           return false;
       }
   }


   /**
    *  Tests that a sublist raises a {@link java.util.ConcurrentModificationException ConcurrentModificationException}
    *  if elements are added to the original list.
    */
   public void testListSubListFailFastOnAdd() {
       if (!isFailFastSupported()) return;
       if (!isAddSupported()) return;

       resetFull();
       int size = collection.size();
       List sub = getList().subList(1, size);
       getList().add(getOtherElements()[0]);
       failFastAll(sub);

       resetFull();
       sub = getList().subList(1, size);
       getList().add(0, getOtherElements()[0]);
       failFastAll(sub);

       resetFull();
       sub = getList().subList(1, size);
       getList().addAll(Arrays.asList(getOtherElements()));
       failFastAll(sub);

       resetFull();
       sub = getList().subList(1, size);
       getList().addAll(0, Arrays.asList(getOtherElements()));
       failFastAll(sub);

   }


   /**
    *  Tests that a sublist raises a {@link java.util.ConcurrentModificationException ConcurrentModificationException}
    *  if elements are removed from the original list.
    */
   public void testListSubListFailFastOnRemove() {
       if (!isFailFastSupported()) return;
       if (!isRemoveSupported()) return;

       resetFull();
       int size = collection.size();
       List sub = getList().subList(1, size);
       getList().remove(0);
       failFastAll(sub);

       resetFull();
       sub = getList().subList(1, size);
       getList().remove(getFullElements()[2]);
       failFastAll(sub);

       resetFull();
       sub = getList().subList(1, size);
       getList().removeAll(Arrays.asList(getFullElements()));
       failFastAll(sub);

       resetFull();
       sub = getList().subList(1, size);
       getList().retainAll(Arrays.asList(getOtherElements()));
       failFastAll(sub);

       resetFull();
       sub = getList().subList(1, size);
       getList().clear();
       failFastAll(sub);
   }


   /**
    *  Invokes all the methods on the given sublist to make sure they raise
    *  a {@link java.util.ConcurrentModificationException ConcurrentModificationException}.
    */
   protected void failFastAll(List list) {
       Method[] methods = List.class.getMethods();
       for (int i = 0; i < methods.length; i++) {
           failFastMethod(list, methods[i]);
       }
   }


   /**
    *  Invokes the given method on the given sublist to make sure it raises
    *  a {@link java.util.ConcurrentModificationException ConcurrentModificationException}.
    *
    *  Unless the method happens to be the equals() method, in which case
    *  the test is skipped.  There seems to be a bug in
    *  java.util.AbstractList.subList(int,int).equals(Object) -- it never
    *  raises a ConcurrentModificationException.
    *
    *  @param list  the sublist to test
    *  @param m     the method to invoke
    */
   protected void failFastMethod(List list, Method m) {
       if (m.getName().equals("equals")) return;

       Object element = getOtherElements()[0];
       Collection c = Collections.singleton(element);

       Class[] types = m.getParameterTypes();
       Object[] params = new Object[types.length];
       for (int i = 0; i < params.length; i++) {
           if (types[i] == Integer.TYPE) params[i] = new Integer(0);
           else if (types[i] == Collection.class) params[i] = c;
           else if (types[i] == Object.class) params[i] = element;
           else if (types[i] == Object[].class) params[i] = new Object[0];
       }

       try {
           m.invoke(list, params);
           fail(m.getName() + " should raise ConcurrentModification");
       } catch (IllegalAccessException e) {
           // impossible
       } catch (InvocationTargetException e) {
           Throwable t = e.getTargetException();
           if (t instanceof ConcurrentModificationException) {
               // expected
               return;
           } else {
               fail(m.getName() + " raised unexpected " + e);
           }
       }
   }

   //-----------------------------------------------------------------------
   public BulkTest bulkTestListIterator() {
       return new TestListIterator();
   }
    
   public class TestListIterator extends AbstractTestListIterator {
       public TestListIterator() {
           super("TestListIterator");
       }
        
       public Object addSetValue() {
           return AbstractTestList.this.getOtherElements()[0];
       }
        
       public boolean supportsRemove() {
           return AbstractTestList.this.isRemoveSupported();
       }

       public boolean supportsAdd() {
           return AbstractTestList.this.isAddSupported();
       }

       public boolean supportsSet() {
           return AbstractTestList.this.isSetSupported();
       }

       public ListIterator makeEmptyListIterator() {
           resetEmpty();
           return ((List) AbstractTestList.this.collection).listIterator();
       }

       public ListIterator makeFullListIterator() {
           resetFull();
           return ((List) AbstractTestList.this.collection).listIterator();
       }
   }
    
}
