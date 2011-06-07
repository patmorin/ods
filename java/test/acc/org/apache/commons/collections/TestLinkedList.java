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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections.list.AbstractTestList;

/**
 * Tests base {@link java.util.LinkedList} methods and contracts.
 * <p>
 * To use, simply extend this class, and implement
 * the {@link #makeLinkedList} method.
 * <p>
 * If your {@link LinkedList} fails one of these tests by design,
 * you may still use this base set of cases.  Simply override the
 * test case (method) your {@link List} fails.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Rich Dougherty
 */
public abstract class TestLinkedList extends AbstractTestList {

    public TestLinkedList(String testName) {
        super(testName);
    }

    public List makeEmptyList() {
        return makeEmptyLinkedList();
    }

    public List makeFullList() {
        return makeFullLinkedList();
    }

    /**
     *  Return a new, empty {@link LinkedList} to be used for testing.
     *
     *  @return an empty list for testing.
     */
    protected abstract LinkedList makeEmptyLinkedList();

    /**
     *  Return a new, full {@link List} to be used for testing.
     *
     *  @return a full list for testing
     */
    protected LinkedList makeFullLinkedList() {
        // only works if list supports optional "addAll(Collection)" 
        LinkedList list = makeEmptyLinkedList();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

    /**
     *  Returns the {@link #collection} field cast to a {@link LinkedList}.
     *
     *  @return the collection field as a List
     */
    protected LinkedList getLinkedList() {
        return (LinkedList)collection;
    }

    /**
     *  Returns the {@link #confirmed} field cast to a {@link LinkedList}.
     *
     *  @return the confirmed field as a List
     */
    protected LinkedList getConfirmedLinkedList() {
        return (LinkedList)confirmed;
    }

    /**
     *  Tests {@link LinkedList#addFirst(Object)}.
     */
    public void testLinkedListAddFirst() {
        if (!isAddSupported()) return;
        Object o = "hello";

        resetEmpty();
        getLinkedList().addFirst(o);
        getConfirmedLinkedList().addFirst(o);
        verify();

        resetFull();
        getLinkedList().addFirst(o);
        getConfirmedLinkedList().addFirst(o);
        verify();
    }

    /**
     *  Tests {@link LinkedList#addLast(Object)}.
     */
    public void testLinkedListAddLast() {
        if (!isAddSupported()) return;
        Object o = "hello";

        resetEmpty();
        getLinkedList().addLast(o);
        getConfirmedLinkedList().addLast(o);
        verify();

        resetFull();
        getLinkedList().addLast(o);
        getConfirmedLinkedList().addLast(o);
        verify();
    }

    /**
     *  Tests {@link LinkedList#getFirst(Object)}.
     */
    public void testLinkedListGetFirst() {
        resetEmpty();
        try {
            getLinkedList().getFirst();
            fail("getFirst() should throw a NoSuchElementException for an " +
                    "empty list.");
        } catch (NoSuchElementException e) {
            // This is correct
        }
        verify();

        resetFull();
        Object first = getLinkedList().getFirst();
        Object confirmedFirst = getConfirmedLinkedList().getFirst();
        assertEquals("Result returned by getFirst() was wrong.",
                confirmedFirst, first);
        verify();
    }

    /**
     *  Tests {@link LinkedList#getLast(Object)}.
     */
    public void testLinkedListGetLast() {
        resetEmpty();
        try {
            getLinkedList().getLast();
            fail("getLast() should throw a NoSuchElementException for an " +
                    "empty list.");
        } catch (NoSuchElementException e) {
            // This is correct
        }
        verify();
        
        resetFull();
        Object last = getLinkedList().getLast();
        Object confirmedLast = getConfirmedLinkedList().getLast();
        assertEquals("Result returned by getLast() was wrong.",
                confirmedLast, last);
        verify();
    }

    /**
     *  Tests {@link LinkedList#removeFirst(Object)}.
     */
    public void testLinkedListRemoveFirst() {
        if (!isRemoveSupported()) return;

        resetEmpty();
        try {
            getLinkedList().removeFirst();
            fail("removeFirst() should throw a NoSuchElementException for " +
                    "an empty list.");
        } catch (NoSuchElementException e) {
            // This is correct
        }
        verify();
        
        resetFull();
        Object first = getLinkedList().removeFirst();
        Object confirmedFirst = getConfirmedLinkedList().removeFirst();
        assertEquals("Result returned by removeFirst() was wrong.",
                confirmedFirst, first);
        verify();
    }

    /**
     *  Tests {@link LinkedList#removeLast(Object)}.
     */
    public void testLinkedListRemoveLast() {
        if (!isRemoveSupported()) return;

        resetEmpty();
        try {
            getLinkedList().removeLast();
            fail("removeLast() should throw a NoSuchElementException for " +
                    "an empty list.");
        } catch (NoSuchElementException e) {
            // This is correct
        }
        verify();

        resetFull();
        Object last = getLinkedList().removeLast();
        Object confirmedLast = getConfirmedLinkedList().removeLast();
        assertEquals("Result returned by removeLast() was wrong.",
                confirmedLast, last);
        verify();
    }

    /**
     *  Returns an empty {@link ArrayList}.
     */
    public Collection makeConfirmedCollection() {
        return new LinkedList();
    }

    /**
     *  Returns a full {@link ArrayList}.
     */
    public Collection makeConfirmedFullCollection() {
        List list = new LinkedList();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }
}
