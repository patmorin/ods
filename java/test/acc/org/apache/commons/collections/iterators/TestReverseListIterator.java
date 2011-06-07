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
package org.apache.commons.collections.iterators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.collections.ResettableListIterator;

/**
 * Tests the ReverseListIterator.
 *
 * @version $Revision: $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 */
public class TestReverseListIterator extends AbstractTestListIterator {

    protected String[] testArray = { "One", "Two", "Three", "Four" };

    public static void main(String args[]) {
        String[] testCaseName = { TestReverseListIterator.class.getName() };
        TestRunner.main(testCaseName);
    }

    public static Test suite() {
        return new TestSuite(TestReverseListIterator.class);
    }

    public TestReverseListIterator(String testName) {
        super(testName);
    }

    public ListIterator makeEmptyListIterator() {
        List list = new ArrayList();
        return new ReverseListIterator(list);
    }

    public ListIterator makeFullListIterator() {
        List list = new ArrayList(Arrays.asList(testArray));
        return new ReverseListIterator(list);
    }

    // overrides
    //-----------------------------------------------------------------------
    public void testEmptyListIteratorIsIndeedEmpty() {
        ListIterator it = makeEmptyListIterator();
        
        assertEquals(false, it.hasNext());
        assertEquals(-1, it.nextIndex());  // reversed index
        assertEquals(false, it.hasPrevious());
        assertEquals(0, it.previousIndex());  // reversed index
        
        // next() should throw a NoSuchElementException
        try {
            it.next();
            fail("NoSuchElementException must be thrown from empty ListIterator");
        } catch (NoSuchElementException e) {
        }
        
        // previous() should throw a NoSuchElementException
        try {
            it.previous();
            fail("NoSuchElementException must be thrown from empty ListIterator");
        } catch (NoSuchElementException e) {
        }
    }

    public void testWalkForwardAndBack() {
        ArrayList list = new ArrayList();
        ListIterator it = makeFullListIterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        
        // check state at end
        assertEquals(false, it.hasNext());
        assertEquals(true, it.hasPrevious());
        
        // this had to be commented out, as there is a bug in the JDK before JDK1.5
        // where calling previous at the start of an iterator would push the cursor
        // back to an invalid negative value
//        try {
//            it.next();
//            fail("NoSuchElementException must be thrown from next at end of ListIterator");
//        } catch (NoSuchElementException e) {
//        }
        
        // loop back through comparing
        for (int i = list.size() - 1; i >= 0; i--) {
            assertEquals("" + i, list.size() - i - 2, it.nextIndex());  // reversed index
            assertEquals(list.size() - i - 1, it.previousIndex());  // reversed index
            
            Object obj = list.get(i);
            assertEquals(obj, it.previous());
        }
        
        // check state at start
        assertEquals(true, it.hasNext());
        assertEquals(false, it.hasPrevious());
        try {
            it.previous();
            fail("NoSuchElementException must be thrown from previous at start of ListIterator");
        } catch (NoSuchElementException e) {
        }
    }

    //-----------------------------------------------------------------------
    public void testReverse() {
        ListIterator it = makeFullListIterator();
        assertEquals(true, it.hasNext());
        assertEquals(3, it.nextIndex());
        assertEquals(false, it.hasPrevious());
        assertEquals(4, it.previousIndex());
        assertEquals("Four", it.next());
        assertEquals(2, it.nextIndex());
        assertEquals(true, it.hasNext());
        assertEquals(3, it.previousIndex());
        assertEquals(true, it.hasPrevious());
        assertEquals("Three", it.next());
        assertEquals(true, it.hasNext());
        assertEquals(1, it.nextIndex());
        assertEquals(true, it.hasPrevious());
        assertEquals(2, it.previousIndex());
        assertEquals("Two", it.next());
        assertEquals(true, it.hasNext());
        assertEquals(0, it.nextIndex());
        assertEquals(true, it.hasPrevious());
        assertEquals(1, it.previousIndex());
        assertEquals("One", it.next());
        assertEquals(false, it.hasNext());
        assertEquals(-1, it.nextIndex());
        assertEquals(true, it.hasPrevious());
        assertEquals(0, it.previousIndex());
        assertEquals("One", it.previous());
        assertEquals("Two", it.previous());
        assertEquals("Three", it.previous());
        assertEquals("Four", it.previous());
    }

    public void testReset() {
        ResettableListIterator it = (ResettableListIterator) makeFullListIterator();
        assertEquals("Four", it.next());
        it.reset();
        assertEquals("Four", it.next());
        it.next();
        it.next();
        it.reset();
        assertEquals("Four", it.next());
    }

}
