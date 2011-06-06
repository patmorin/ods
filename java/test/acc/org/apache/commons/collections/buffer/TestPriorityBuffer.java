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
package org.apache.commons.collections.buffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUnderflowException;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.collection.AbstractTestCollection;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * Tests the PriorityBuffer.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Michael A. Smith
 * @author Steve Phelps
 */
public class TestPriorityBuffer extends AbstractTestCollection {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestPriorityBuffer.class);
    }

    public TestPriorityBuffer(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------  
    public void verify() {
        super.verify();
        PriorityBuffer heap = (PriorityBuffer) collection;

        Comparator c = heap.comparator;
        if (c == null) {
            c = ComparatorUtils.naturalComparator();
        }
        if (!heap.ascendingOrder) {
            c = ComparatorUtils.reversedComparator(c);
        }

        Object[] tree = heap.elements;
        for (int i = 1; i <= heap.size; i++) {
            Object parent = tree[i];
            if (i * 2 <= heap.size) {
                assertTrue("Parent is less than or equal to its left child", c.compare(parent, tree[i * 2]) <= 0);
            }
            if (i * 2 + 1 < heap.size) {
                assertTrue("Parent is less than or equal to its right child", c.compare(parent, tree[i * 2 + 1]) <= 0);
            }
        }
    }

    //-----------------------------------------------------------------------  
    /**
     * Overridden because BinaryBuffer isn't fail fast.
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
        return new PriorityBuffer();
    }

    //-----------------------------------------------------------------------  
    public Object[] getFullElements() {
        return getFullNonNullStringElements();
    }

    public Object[] getOtherElements() {
        return getOtherNonNullStringElements();
    }

    //-----------------------------------------------------------------------  
    public void testBufferEmpty() {
        resetEmpty();
        Buffer buffer = (Buffer) collection;

        assertEquals(0, buffer.size());
        assertEquals(true, buffer.isEmpty());
        try {
            buffer.get();
            fail();
        } catch (BufferUnderflowException ex) {}

        try {
            buffer.remove();
            fail();
        } catch (BufferUnderflowException ex) {}
    }
    
    public void testBasicOps() {
        PriorityBuffer heap = new PriorityBuffer();

        heap.add("a");
        heap.add("c");
        heap.add("e");
        heap.add("b");
        heap.add("d");
        heap.add("n");
        heap.add("m");
        heap.add("l");
        heap.add("k");
        heap.add("j");
        heap.add("i");
        heap.add("h");
        heap.add("g");
        heap.add("f");

        assertTrue("heap should not be empty after adds", !heap.isEmpty());

        for (int i = 0; i < 14; i++) {
            assertEquals(
                "get using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('a' + i)),
                heap.get());

            assertEquals(
                "remove using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('a' + i)),
                heap.remove());

            if (i + 1 < 14) {
                assertTrue("heap should not be empty before all elements are removed", !heap.isEmpty());
            } else {
                assertTrue("heap should be empty after all elements are removed", heap.isEmpty());
            }
        }

        try {
            heap.get();
            fail("NoSuchElementException should be thrown if get is called after all elements are removed");
        } catch (BufferUnderflowException ex) {}

        try {
            heap.remove();
            fail("NoSuchElementException should be thrown if remove is called after all elements are removed");
        } catch (BufferUnderflowException ex) {}
    }

    public void testBasicComparatorOps() {
        PriorityBuffer heap = new PriorityBuffer(new ReverseComparator(new ComparableComparator()));

        assertTrue("heap should be empty after create", heap.isEmpty());

        try {
            heap.get();
            fail("NoSuchElementException should be thrown if get is called before any elements are added");
        } catch (BufferUnderflowException ex) {}

        try {
            heap.remove();
            fail("NoSuchElementException should be thrown if remove is called before any elements are added");
        } catch (BufferUnderflowException ex) {}

        heap.add("a");
        heap.add("c");
        heap.add("e");
        heap.add("b");
        heap.add("d");
        heap.add("n");
        heap.add("m");
        heap.add("l");
        heap.add("k");
        heap.add("j");
        heap.add("i");
        heap.add("h");
        heap.add("g");
        heap.add("f");

        assertTrue("heap should not be empty after adds", !heap.isEmpty());

        for (int i = 0; i < 14; i++) {

            // note: since we're using a comparator that reverses items, the
            // "minimum" item is "n", and the "maximum" item is "a".

            assertEquals(
                "get using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('n' - i)),
                heap.get());

            assertEquals(
                "remove using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('n' - i)),
                heap.remove());

            if (i + 1 < 14) {
                assertTrue("heap should not be empty before all elements are removed", !heap.isEmpty());
            } else {
                assertTrue("heap should be empty after all elements are removed", heap.isEmpty());
            }
        }

        try {
            heap.get();
            fail("NoSuchElementException should be thrown if get is called after all elements are removed");
        } catch (BufferUnderflowException ex) {}

        try {
            heap.remove();
            fail("NoSuchElementException should be thrown if remove is called after all elements are removed");
        } catch (BufferUnderflowException ex) {}
    }

    /**
     * Illustrates bad internal heap state reported in Bugzilla PR #235818. 
     */  
    public void testAddRemove() {
        resetEmpty();
        PriorityBuffer heap = (PriorityBuffer) collection;
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
        PriorityBuffer h = null;
        for(int i=0; i < iterations; i++) {
            if (i < iterations / 2) {          
                h = new PriorityBuffer(true);
            } else {
                h = new PriorityBuffer(false);
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
    protected void checkOrder(PriorityBuffer h) {
        Integer lastNum = null;
        Integer num = null;
        while (!h.isEmpty()) {
            num = (Integer) h.remove();
            if (h.ascendingOrder) {
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
    protected String showTree(PriorityBuffer h) {
        int count = 1;
        StringBuffer buffer = new StringBuffer();
        for (int offset = 1; count < h.size() + 1; offset *= 2) {
            for (int i = offset; i < offset * 2; i++) {
                if (i < h.elements.length && h.elements[i] != null) 
                    buffer.append(h.elements[i] + " ");
                count++;
            }
            buffer.append('\n');
        }
        return buffer.toString();
    }

    /**
     * Generates 500 randomly initialized heaps of size 100
     * and tests that after serializing and restoring them to a byte array
     * that the following conditions hold:
     * 
     *  - the size of the restored heap is the same 
     *      as the size of the orignal heap
     *  
     *  - all elements in the original heap are present in the restored heap
     *  
     *  - the heap order of the restored heap is intact as 
     *      verified by checkOrder()
     */
    public void testSerialization() {
        int iterations = 500;
        int heapSize = 100;
        PriorityBuffer h;
        Random randGenerator = new Random();
        for (int i = 0; i < iterations; i++) {
            if (i < iterations / 2) {
                h = new PriorityBuffer(true);
            } else {
                h = new PriorityBuffer(false);
            }
            for (int r = 0; r < heapSize; r++) {
                h.add(new Integer(randGenerator.nextInt(heapSize)));
            }
            assertTrue(h.size() == heapSize);
            PriorityBuffer h1 = serializeAndRestore(h);
            assertTrue(h1.size() == heapSize);
            Iterator hit = h.iterator();
            while (hit.hasNext()) {
                Integer n = (Integer) hit.next();
                assertTrue(h1.contains(n));
            }
            checkOrder(h1);
        }
    }

    public PriorityBuffer serializeAndRestore(PriorityBuffer h) {
        PriorityBuffer h1 = null;
        try {
            byte[] objekt = writeExternalFormToBytes(h);
            h1 = (PriorityBuffer) readExternalFormFromBytes(objekt);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        return h1;
    }

    public String getCompatibilityVersion() {
        return "3.2";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "C:/commons/collections/data/test/PriorityBuffer.emptyCollection.version3.2.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "C:/commons/collections/data/test/PriorityBuffer.fullCollection.version3.2.obj");
//    }

}
