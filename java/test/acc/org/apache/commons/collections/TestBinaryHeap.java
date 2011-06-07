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
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.collection.AbstractTestCollection;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * Tests the BinaryHeap.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Michael A. Smith
 */
public class TestBinaryHeap extends AbstractTestCollection {

    public static Test suite() {
        return new TestSuite(TestBinaryHeap.class);
    }

    public TestBinaryHeap(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------  
    public void verify() {
        super.verify();
        BinaryHeap heap = (BinaryHeap) collection;

        Comparator c = heap.m_comparator;
        if (c == null)
            c = ComparatorUtils.naturalComparator();
        if (!heap.m_isMinHeap)
            c = ComparatorUtils.reversedComparator(c);

        Object[] tree = heap.m_elements;
        for (int i = 1; i <= heap.m_size; i++) {
            Object parent = tree[i];
            if (i * 2 <= heap.m_size) {
                assertTrue("Parent is less than or equal to its left child", c.compare(parent, tree[i * 2]) <= 0);
            }
            if (i * 2 + 1 < heap.m_size) {
                assertTrue("Parent is less than or equal to its right child", c.compare(parent, tree[i * 2 + 1]) <= 0);
            }
        }
    }

    //-----------------------------------------------------------------------  
    /**
     * Overridden because UnboundedFifoBuffer isn't fail fast.
     * @return false
     */
    public boolean isFailFastSupported() {
        return false;
    }

    //-----------------------------------------------------------------------  
    public Collection makeConfirmedCollection() {
        return new ArrayList();
    }

    public Collection makeConfirmedFullCollection() {
        ArrayList list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

    /**
     * Return a new, empty {@link Object} to used for testing.
     */
    public Collection makeCollection() {
        return new BinaryHeap();
    }

    //-----------------------------------------------------------------------  
    public Object[] getFullElements() {
        return getFullNonNullStringElements();
    }

    public Object[] getOtherElements() {
        return getOtherNonNullStringElements();
    }

    //-----------------------------------------------------------------------  
    public void testBasicOps() {
        BinaryHeap heap = new BinaryHeap();

        assertTrue("heap should be empty after create", heap.isEmpty());

        try {
            heap.peek();
            fail("NoSuchElementException should be thrown if peek is called before any elements are inserted");
        } catch (NoSuchElementException e) {
            // expected
        }

        try {
            heap.pop();
            fail("NoSuchElementException should be thrown if pop is called before any elements are inserted");
        } catch (NoSuchElementException e) {
            // expected
        }

        heap.insert("a");
        heap.insert("c");
        heap.insert("e");
        heap.insert("b");
        heap.insert("d");
        heap.insert("n");
        heap.insert("m");
        heap.insert("l");
        heap.insert("k");
        heap.insert("j");
        heap.insert("i");
        heap.insert("h");
        heap.insert("g");
        heap.insert("f");

        assertTrue("heap should not be empty after inserts", !heap.isEmpty());

        for (int i = 0; i < 14; i++) {
            assertEquals(
                "peek using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('a' + i)),
                heap.peek());

            assertEquals(
                "pop using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('a' + i)),
                heap.pop());

            if (i + 1 < 14) {
                assertTrue("heap should not be empty before all elements are popped", !heap.isEmpty());
            } else {
                assertTrue("heap should be empty after all elements are popped", heap.isEmpty());
            }
        }

        try {
            heap.peek();
            fail("NoSuchElementException should be thrown if peek is called after all elements are popped");
        } catch (NoSuchElementException e) {
            // expected
        }

        try {
            heap.pop();
            fail("NoSuchElementException should be thrown if pop is called after all elements are popped");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    public void testBasicComparatorOps() {
        BinaryHeap heap = new BinaryHeap(new ReverseComparator(new ComparableComparator()));

        assertTrue("heap should be empty after create", heap.isEmpty());

        try {
            heap.peek();
            fail("NoSuchElementException should be thrown if peek is called before any elements are inserted");
        } catch (NoSuchElementException e) {
            // expected
        }

        try {
            heap.pop();
            fail("NoSuchElementException should be thrown if pop is called before any elements are inserted");
        } catch (NoSuchElementException e) {
            // expected
        }

        heap.insert("a");
        heap.insert("c");
        heap.insert("e");
        heap.insert("b");
        heap.insert("d");
        heap.insert("n");
        heap.insert("m");
        heap.insert("l");
        heap.insert("k");
        heap.insert("j");
        heap.insert("i");
        heap.insert("h");
        heap.insert("g");
        heap.insert("f");

        assertTrue("heap should not be empty after inserts", !heap.isEmpty());

        for (int i = 0; i < 14; i++) {

            // note: since we're using a comparator that reverses items, the
            // "minimum" item is "n", and the "maximum" item is "a".

            assertEquals(
                "peek using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('n' - i)),
                heap.peek());

            assertEquals(
                "pop using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('n' - i)),
                heap.pop());

            if (i + 1 < 14) {
                assertTrue("heap should not be empty before all elements are popped", !heap.isEmpty());
            } else {
                assertTrue("heap should be empty after all elements are popped", heap.isEmpty());
            }
        }

        try {
            heap.peek();
            fail("NoSuchElementException should be thrown if peek is called after all elements are popped");
        } catch (NoSuchElementException e) {
            // expected
        }

        try {
            heap.pop();
            fail("NoSuchElementException should be thrown if pop is called after all elements are popped");
        } catch (NoSuchElementException e) {
            // expected
        }
    }
    
    /**
     * Illustrates bad internal heap state reported in Bugzilla PR #235818. 
     */  
    public void testAddRemove() {
        resetEmpty();
        BinaryHeap heap = (BinaryHeap) collection;
        heap.add(new Integer(0));
        heap.add(new Integer(2));
        heap.add(new Integer(4));
        heap.add(new Integer(3));
        heap.add(new Integer(8));
        heap.add(new Integer(10));
        heap.add(new Integer(12));
        heap.add(new Integer(3));
        confirmed.addAll(heap);
        // System.out.println(heap);
        Object obj = new Integer(10);
        heap.remove(obj);
        confirmed.remove(obj);
        // System.out.println(heap);
        verify();
    }
    
    /**
     * Generate heaps staring with Integers from 0 - heapSize - 1.
     * Then perform random add / remove operations, checking
     * heap order after modifications. Alternates minHeaps, maxHeaps.
     *
     * Based on code provided by Steve Phelps in PR #25818
     *
     */
    public void testRandom() {
        int iterations = 500;
        int heapSize = 100;
        int operations = 20;
        Random randGenerator = new Random();
        BinaryHeap h = null;
        for(int i=0; i < iterations; i++) {
            if (i < iterations / 2) {          
                h = new BinaryHeap(true);
            } else {
                h = new BinaryHeap(false);
            }
            for(int r = 0; r < heapSize; r++) {
                h.add( new Integer( randGenerator.nextInt(heapSize)) );
            }
            for( int r = 0; r < operations; r++ ) {
                h.remove(new Integer(r));
                h.add(new Integer(randGenerator.nextInt(heapSize)));
            }
            checkOrder(h);
        }
    }
     
    /**
     * Pops all elements from the heap and verifies that the elements come off
     * in the correct order.  NOTE: this method empties the heap.
     */
    protected void checkOrder(BinaryHeap h) {
        Integer lastNum = null;
        Integer num = null;
        boolean fail = false;
        while (!h.isEmpty()) {
            num = (Integer) h.pop();
            if (h.m_isMinHeap) {
                assertTrue(lastNum == null || num.intValue() >= lastNum.intValue());
            } else { // max heap
                assertTrue(lastNum == null || num.intValue() <= lastNum.intValue());
            }
            lastNum = num;
            num = null;
        }
    }
    
    /**
     * Returns a string showing the contents of the heap formatted as a tree.
     * Makes no attempt at padding levels or handling wrapping. 
     */
    protected String showTree(BinaryHeap h) {
        int count = 1;
        StringBuffer buffer = new StringBuffer();
        for (int offset = 1; count < h.size() + 1; offset *= 2) {
            for (int i = offset; i < offset * 2; i++) {
                if (i < h.m_elements.length && h.m_elements[i] != null) 
                    buffer.append(h.m_elements[i] + " ");
                count++;
            }
            buffer.append('\n');
        }
        return buffer.toString();
    }

}
