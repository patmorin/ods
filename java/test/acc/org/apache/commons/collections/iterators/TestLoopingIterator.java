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
 * Tests the LoopingIterator class.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Jonathan Carlson
 * @author Stephen Colebourne
 */
public class TestLoopingIterator extends TestCase {

    public TestLoopingIterator(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestLoopingIterator.class);
    }

    /**
     * Tests constructor exception.
     */
    public void testConstructorEx() throws Exception {
        try {
            new LoopingIterator(null);
            fail();
        } catch (NullPointerException ex) {
        }
    }
    
    /**
     * Tests whether an empty looping iterator works as designed.
     * @throws Exception  If something unexpected occurs.
     */
    public void testLooping0() throws Exception {
        List list = new ArrayList();
        LoopingIterator loop = new LoopingIterator(list);
        assertTrue("hasNext should return false", loop.hasNext() == false);

        try {
            loop.next();
            fail("NoSuchElementException was not thrown during next() call.");
        } catch (NoSuchElementException ex) {
        }
    }

    /**
     * Tests whether a populated looping iterator works as designed.
     * @throws Exception  If something unexpected occurs.
     */
    public void testLooping1() throws Exception {
        List list = new ArrayList(Arrays.asList(new String[] { "a" }));
        LoopingIterator loop = new LoopingIterator(list);

        assertTrue("1st hasNext should return true", loop.hasNext());
        assertEquals("a", loop.next());

        assertTrue("2nd hasNext should return true", loop.hasNext());
        assertEquals("a", loop.next());

        assertTrue("3rd hasNext should return true", loop.hasNext());
        assertEquals("a", loop.next());

    }

    /**
     * Tests whether a populated looping iterator works as designed.
     * @throws Exception  If something unexpected occurs.
     */
    public void testLooping2() throws Exception {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b" }));
        LoopingIterator loop = new LoopingIterator(list);

        assertTrue("1st hasNext should return true", loop.hasNext());
        assertEquals("a", loop.next());

        assertTrue("2nd hasNext should return true", loop.hasNext());
        assertEquals("b", loop.next());

        assertTrue("3rd hasNext should return true", loop.hasNext());
        assertEquals("a", loop.next());

    }

    /**
     * Tests whether a populated looping iterator works as designed.
     * @throws Exception  If something unexpected occurs.
     */
    public void testLooping3() throws Exception {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b", "c" }));
        LoopingIterator loop = new LoopingIterator(list);

        assertTrue("1st hasNext should return true", loop.hasNext());
        assertEquals("a", loop.next());

        assertTrue("2nd hasNext should return true", loop.hasNext());
        assertEquals("b", loop.next());

        assertTrue("3rd hasNext should return true", loop.hasNext());
        assertEquals("c", loop.next());

        assertTrue("4th hasNext should return true", loop.hasNext());
        assertEquals("a", loop.next());

    }

    /**
     * Tests the remove() method on a LoopingIterator wrapped ArrayList.
     * @throws Exception  If something unexpected occurs.
     */
    public void testRemoving1() throws Exception {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b", "c" }));
        LoopingIterator loop = new LoopingIterator(list);
        assertEquals("list should have 3 elements.", 3, list.size());

        assertTrue("1st hasNext should return true", loop.hasNext());
        assertEquals("a", loop.next());
        loop.remove();  // removes a
        assertEquals("list should have 2 elements.", 2, list.size());

        assertTrue("2nd hasNext should return true", loop.hasNext());
        assertEquals("b", loop.next());
        loop.remove();  // removes b
        assertEquals("list should have 1 elements.", 1, list.size());

        assertTrue("3rd hasNext should return true", loop.hasNext());
        assertEquals("c", loop.next());
        loop.remove();  // removes c
        assertEquals("list should have 0 elements.", 0, list.size());

        assertTrue("4th hasNext should return false", loop.hasNext() == false);
        try {
            loop.next();
            fail("Expected NoSuchElementException to be thrown.");
        } catch (NoSuchElementException ex) {
        }
    }

    /**
     * Tests the reset() method on a LoopingIterator wrapped ArrayList.
     * @throws Exception  If something unexpected occurs.
     */
    public void testReset() throws Exception {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b", "c" }));
        LoopingIterator loop = new LoopingIterator(list);

        assertEquals("a", loop.next());
        assertEquals("b", loop.next());
        loop.reset();
        assertEquals("a", loop.next());
        loop.reset();
        assertEquals("a", loop.next());
        assertEquals("b", loop.next());
        assertEquals("c", loop.next());
        loop.reset();
        assertEquals("a", loop.next());
        assertEquals("b", loop.next());
        assertEquals("c", loop.next());
    }
    
    /**
     * Tests the size() method on a LoopingIterator wrapped ArrayList.
     * @throws Exception  If something unexpected occurs.
     */
    public void testSize() throws Exception {
        List list = new ArrayList(Arrays.asList(new String[] { "a", "b", "c" }));
        LoopingIterator loop = new LoopingIterator(list);

        assertEquals(3, loop.size());
        loop.next();
        loop.next();
        assertEquals(3, loop.size());
        loop.reset();
        assertEquals(3, loop.size());
        loop.next();
        loop.remove();
        assertEquals(2, loop.size());
    }

}
