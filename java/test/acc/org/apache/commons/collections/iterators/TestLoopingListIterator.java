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
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the LoopingListIterator class.
 *
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Eric Crampton <ccesc@eonomine.com>
 */
public class TestLoopingListIterator extends TestCase {

    public TestLoopingListIterator(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestLoopingListIterator.class);
    }

    /**
     * Tests constructor exception.
     */
    public void testConstructorEx() throws Exception {
        try {
            new LoopingListIterator(null);
            fail();
        } catch (NullPointerException ex) {
        }
    }

    /**
     * Tests whether an empty looping list iterator works.
     */
    public void testLooping0() throws Exception {
        List list = new ArrayList();
        LoopingListIterator loop = new LoopingListIterator(list);
        assertFalse(loop.hasNext());
        assertFalse(loop.hasPrevious());
        
        try {
            loop.next();
            fail();
        } catch (NoSuchElementException ex) {
        }

        try {
            loop.previous();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    /**
     * Tests whether a looping list iterator works on a list with only
     * one element.
     */
    public void testLooping1() throws Exception {
        List list = new ArrayList(Arrays.asList(new String[] { "a" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <a>

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());     // <a>

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());     // <a>

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());     // <a>

        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous()); // <a>

        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous()); // <a>

        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous()); // <a>
    }

    /**
     * Tests whether a looping list iterator works on a list with two
     * elements.
     */
    public void testLooping2() throws Exception {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <a> b

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());     // a <b>

        assertTrue(loop.hasNext());
        assertEquals("b", loop.next());     // <a> b

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());     // a <b>

        // Reset the iterator and try using previous.
        loop.reset();                       // <a> b

        assertTrue(loop.hasPrevious());
        assertEquals("b", loop.previous()); // a <b>

        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous()); // <a> b

        assertTrue(loop.hasPrevious());
        assertEquals("b", loop.previous()); // a <b>
    }

    /**
     * Tests jogging back and forth between two elements, but not over
     * the begin/end boundary of the list.
     */
    public void testJoggingNotOverBoundary() {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <a> b
    
        // Try jogging back and forth between the elements, but not
        // over the begin/end boundary.
        loop.reset();
        assertEquals("a", loop.next());     // a <b>
        assertEquals("a", loop.previous()); // <a> b
        assertEquals("a", loop.next());     // a <b>

        assertEquals("b", loop.next());     // <a> b
        assertEquals("b", loop.previous()); // a <b>
        assertEquals("b", loop.next());     // <a> b
    }

    /**
     * Tests jogging back and forth between two elements over the
     * begin/end boundary of the list.
     */
    public void testJoggingOverBoundary() {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <a> b
    
        // Try jogging back and forth between the elements, but not
        // over the begin/end boundary.
        assertEquals("b", loop.previous()); // a <b>
        assertEquals("b", loop.next());     // <a> b
        assertEquals("b", loop.previous()); // a <b>

        assertEquals("a", loop.previous()); // <a> b
        assertEquals("a", loop.next());     // a <b>
        assertEquals("a", loop.previous()); // <a> b
    }

    /**
     * Tests removing an element from a wrapped ArrayList.
     */
    public void testRemovingElementsAndIteratingForward() {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b", "c" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <a> b c

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next()); // a <b> c
        loop.remove();                  // <b> c
        assertEquals(2, list.size());

        assertTrue(loop.hasNext());
        assertEquals("b", loop.next()); // b <c>
        loop.remove();                  // <c>
        assertEquals(1, list.size());

        assertTrue(loop.hasNext());
        assertEquals("c", loop.next()); // <c>
        loop.remove();                  // ---
        assertEquals(0, list.size());

        assertFalse(loop.hasNext());
        try {
            loop.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    /**
     * Tests removing an element from a wrapped ArrayList.
     */
    public void testRemovingElementsAndIteratingBackwards() {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b", "c" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <a> b c

        assertTrue(loop.hasPrevious());
        assertEquals("c", loop.previous()); // a b <c>
        loop.remove();                      // <a> b
        assertEquals(2, list.size());

        assertTrue(loop.hasPrevious());
        assertEquals("b", loop.previous()); // a <b>
        loop.remove();                      // <a>
        assertEquals(1, list.size());

        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous()); // <a>
        loop.remove();                      // ---
        assertEquals(0, list.size());

        assertFalse(loop.hasPrevious());
        try {
            loop.previous();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    /**
     * Tests the reset method.
     */
    public void testReset() {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b", "c" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <a> b c

        assertEquals("a", loop.next()); // a <b> c
        assertEquals("b", loop.next()); // a b <c>
        loop.reset();                   // <a> b c
        assertEquals("a", loop.next()); // a <b> c
        loop.reset();                   // <a> b c
        assertEquals("a", loop.next()); // a <b> c
        assertEquals("b", loop.next()); // a b <c>
        assertEquals("c", loop.next()); // <a> b c
        loop.reset();                   // <a> b c

        assertEquals("c", loop.previous()); // a b <c>
        assertEquals("b", loop.previous()); // a <b> c
        loop.reset();                       // <a> b c
        assertEquals("c", loop.previous()); // a b <c>
        loop.reset();                       // <a> b c
        assertEquals("c", loop.previous()); // a b <c>
        assertEquals("b", loop.previous()); // a <b> c
        assertEquals("a", loop.previous()); // <a> b c
    }

    /**
     * Tests the add method.
     */
    public void testAdd() {
        List list = new ArrayList(Arrays.asList(new String[] { "b", "e", "f" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <b> e f

        loop.add("a");                      // <a> b e f
        assertEquals("b", loop.next());     // a <b> e f
        loop.reset();                       // <a> b e f
        assertEquals("a", loop.next());     // a <b> e f
        assertEquals("b", loop.next());     // a b <e> f

        loop.add("c");                      // a b c <e> f
        assertEquals("e", loop.next());     // a b c e <f>
        assertEquals("e", loop.previous()); // a b c <e> f
        assertEquals("c", loop.previous()); // a b <c> e f
        assertEquals("c", loop.next());     // a b c <e> f
        
        loop.add("d");                      // a b c d <e> f
        loop.reset();                       // <a> b c d e f
        assertEquals("a", loop.next());     // a <b> c d e f
        assertEquals("b", loop.next());     // a b <c> d e f
        assertEquals("c", loop.next());     // a b c <d> e f
        assertEquals("d", loop.next());     // a b c d <e> f
        assertEquals("e", loop.next());     // a b c d e <f>
        assertEquals("f", loop.next());     // <a> b c d e f
        assertEquals("a", loop.next());     // a <b> c d e f

        list = new ArrayList(Arrays.asList(new String[] { "b", "e", "f" }));
        loop = new LoopingListIterator(list); // <b> e f        

        loop.add("a");                      // a <b> e f
        assertEquals("a", loop.previous()); // a b e <f>
        loop.reset();                       // <a> b e f
        assertEquals("f", loop.previous()); // a b e <f>
        assertEquals("e", loop.previous()); // a b <e> f

        loop.add("d");                      // a b d <e> f
        assertEquals("d", loop.previous()); // a b <d> e f

        loop.add("c");                      // a b c <d> e f
        assertEquals("c", loop.previous()); // a b <c> d e f

        loop.reset();
        assertEquals("a", loop.next());     // a <b> c d e f
        assertEquals("b", loop.next());     // a b <c> d e f
        assertEquals("c", loop.next());     // a b c <d> e f
        assertEquals("d", loop.next());     // a b c d <e> f
        assertEquals("e", loop.next());     // a b c d e <f>
        assertEquals("f", loop.next());     // <a> b c d e f
        assertEquals("a", loop.next());     // a <b> c d e f
    }

    /**
     * Tests nextIndex and previousIndex.
     */
    public void testNextAndPreviousIndex() {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b", "c" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <a> b c

        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());

        assertEquals("a", loop.next());        // a <b> c
        assertEquals(1, loop.nextIndex());
        assertEquals(0, loop.previousIndex());

        assertEquals("a", loop.previous());    // <a> b c
        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());

        assertEquals("c", loop.previous());    // a b <c>
        assertEquals(2, loop.nextIndex());
        assertEquals(1, loop.previousIndex());

        assertEquals("b", loop.previous());    // a <b> c
        assertEquals(1, loop.nextIndex());
        assertEquals(0, loop.previousIndex());

        assertEquals("a", loop.previous());    // <a> b c
        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());
    }

    /**
     * Tests using the set method to change elements.
     */
    public void testSet() {
        List list = new ArrayList(Arrays.asList(new String[] { "q", "r", "z" }));
        LoopingListIterator loop = new LoopingListIterator(list); // <q> r z

        assertEquals("z", loop.previous()); // q r <z>
        loop.set("c");                      // q r <c>

        loop.reset();                       // <q> r c
        assertEquals("q", loop.next());     // q <r> c
        loop.set("a");                      // a <r> c
        
        assertEquals("r", loop.next());     // a r <c>
        loop.set("b");                      // a b <c>

        loop.reset();                       // <a> b c
        assertEquals("a", loop.next());     // a <b> c
        assertEquals("b", loop.next());     // a b <c>
        assertEquals("c", loop.next());     // <a> b c
    }
    
}
