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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUnderflowException;
import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.collection.AbstractTestCollection;

/**
 * Test cases for CircularFifoBuffer.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestCircularFifoBuffer extends AbstractTestCollection {

    public TestCircularFifoBuffer(String n) {
        super(n);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestCircularFifoBuffer.class);
    }

    public static void main(String args[]) {
        TestRunner.run(TestCircularFifoBuffer.class);
    }

    //-----------------------------------------------------------------------
    /**
     *  Runs through the regular verifications, but also verifies that 
     *  the buffer contains the same elements in the same sequence as the
     *  list.
     */
    public void verify() {
        super.verify();
        Iterator iterator1 = collection.iterator();
        Iterator iterator2 = confirmed.iterator();
        while (iterator2.hasNext()) {
            assertTrue(iterator1.hasNext());
            Object o1 = iterator1.next();
            Object o2 = iterator2.next();
            assertEquals(o1, o2);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Overridden because UnboundedFifoBuffer doesn't allow null elements.
     * @return false
     */
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Overridden because UnboundedFifoBuffer isn't fail fast.
     * @return false
     */
    public boolean isFailFastSupported() {
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns an empty ArrayList.
     *
     * @return an empty ArrayList
     */
    public Collection makeConfirmedCollection() {
        return new ArrayList();
    }

    /**
     * Returns a full ArrayList.
     *
     * @return a full ArrayList
     */
    public Collection makeConfirmedFullCollection() {
        Collection c = makeConfirmedCollection();
        c.addAll(java.util.Arrays.asList(getFullElements()));
        return c;
    }

    /**
     * Returns an empty BoundedFifoBuffer that won't overflow.  
     *  
     * @return an empty BoundedFifoBuffer
     */
    public Collection makeCollection() {
        return new CircularFifoBuffer(100);
    }

    //-----------------------------------------------------------------------
    /**
     * Tests that the removal operation actually removes the first element.
     */
    public void testCircularFifoBufferCircular() {
        List list = new ArrayList();
        list.add("A");
        list.add("B");
        list.add("C");
        Buffer buf = new CircularFifoBuffer(list);
        
        assertEquals(true, buf.contains("A"));
        assertEquals(true, buf.contains("B"));
        assertEquals(true, buf.contains("C"));
        
        buf.add("D");
        
        assertEquals(false, buf.contains("A"));
        assertEquals(true, buf.contains("B"));
        assertEquals(true, buf.contains("C"));
        assertEquals(true, buf.contains("D"));
        
        assertEquals("B", buf.get());
        assertEquals("B", buf.remove());
        assertEquals("C", buf.remove());
        assertEquals("D", buf.remove());
    }

    /**
     * Tests that the removal operation actually removes the first element.
     */
    public void testCircularFifoBufferRemove() {
        resetFull();
        int size = confirmed.size();
        for (int i = 0; i < size; i++) {
            Object o1 = ((CircularFifoBuffer) collection).remove();
            Object o2 = ((ArrayList) confirmed).remove(0);
            assertEquals("Removed objects should be equal", o1, o2);
            verify();
        }

        try {
            ((CircularFifoBuffer) collection).remove();
            fail("Empty buffer should raise Underflow.");
        } catch (BufferUnderflowException e) {
            // expected
        }
    }

    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException1() {
        try {
            new CircularFifoBuffer(0);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException2() {
        try {
            new CircularFifoBuffer(-20);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }
    
    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException3() {
        try {
            new CircularFifoBuffer(null);
        } catch (NullPointerException ex) {
            return;
        }
        fail();
    }
    
    public void testRemoveError1() throws Exception {
        // based on bug 33071
        CircularFifoBuffer fifo = new CircularFifoBuffer(5);
        fifo.add("1");
        fifo.add("2");
        fifo.add("3");
        fifo.add("4");
        fifo.add("5");
        
        assertEquals("[1, 2, 3, 4, 5]", fifo.toString());
        
        fifo.remove("3");
        assertEquals("[1, 2, 4, 5]", fifo.toString());
        
        fifo.remove("4");
        assertEquals("[1, 2, 5]", fifo.toString());
    }

    public void testRemoveError2() throws Exception {
        // based on bug 33071
        CircularFifoBuffer fifo = new CircularFifoBuffer(5);
        fifo.add("1");
        fifo.add("2");
        fifo.add("3");
        fifo.add("4");
        fifo.add("5");
        fifo.add("6");
        
        assertEquals(5, fifo.size());
        assertEquals("[2, 3, 4, 5, 6]", fifo.toString());
        
        fifo.remove("3");
        assertEquals("[2, 4, 5, 6]", fifo.toString());
        
        fifo.remove("4");
        assertEquals("[2, 5, 6]", fifo.toString());
    }

    public void testRemoveError3() throws Exception {
        // based on bug 33071
        CircularFifoBuffer fifo = new CircularFifoBuffer(5);
        fifo.add("1");
        fifo.add("2");
        fifo.add("3");
        fifo.add("4");
        fifo.add("5");
        
        assertEquals("[1, 2, 3, 4, 5]", fifo.toString());
        
        fifo.remove("3");
        assertEquals("[1, 2, 4, 5]", fifo.toString());
        
        fifo.add("6");
        fifo.add("7");
        assertEquals("[2, 4, 5, 6, 7]", fifo.toString());
        
        fifo.remove("4");
        assertEquals("[2, 5, 6, 7]", fifo.toString());
    }

    public void testRemoveError4() throws Exception {
        // based on bug 33071
        CircularFifoBuffer fifo = new CircularFifoBuffer(5);
        fifo.add("1");
        fifo.add("2");
        fifo.add("3");
        fifo.add("4");
        fifo.add("5");  // end=0
        fifo.add("6");  // end=1
        fifo.add("7");  // end=2
        
        assertEquals("[3, 4, 5, 6, 7]", fifo.toString());
        
        fifo.remove("4");  // remove element in middle of array, after start
        assertEquals("[3, 5, 6, 7]", fifo.toString());
    }

    public void testRemoveError5() throws Exception {
        // based on bug 33071
        CircularFifoBuffer fifo = new CircularFifoBuffer(5);
        fifo.add("1");
        fifo.add("2");
        fifo.add("3");
        fifo.add("4");
        fifo.add("5");  // end=0
        fifo.add("6");  // end=1
        fifo.add("7");  // end=2
        
        assertEquals("[3, 4, 5, 6, 7]", fifo.toString());
        
        fifo.remove("5");  // remove element at last pos in array
        assertEquals("[3, 4, 6, 7]", fifo.toString());
    }

    public void testRemoveError6() throws Exception {
        // based on bug 33071
        CircularFifoBuffer fifo = new CircularFifoBuffer(5);
        fifo.add("1");
        fifo.add("2");
        fifo.add("3");
        fifo.add("4");
        fifo.add("5");  // end=0
        fifo.add("6");  // end=1
        fifo.add("7");  // end=2
        
        assertEquals("[3, 4, 5, 6, 7]", fifo.toString());
        
        fifo.remove("6");  // remove element at position zero in array
        assertEquals("[3, 4, 5, 7]", fifo.toString());
    }

    public void testRemoveError7() throws Exception {
        // based on bug 33071
        CircularFifoBuffer fifo = new CircularFifoBuffer(5);
        fifo.add("1");
        fifo.add("2");
        fifo.add("3");
        fifo.add("4");
        fifo.add("5");  // end=0
        fifo.add("6");  // end=1
        fifo.add("7");  // end=2
        
        assertEquals("[3, 4, 5, 6, 7]", fifo.toString());
        
        fifo.remove("7");  // remove element at position one in array
        assertEquals("[3, 4, 5, 6]", fifo.toString());
    }

    public void testRemoveError8() throws Exception {
        // based on bug 33071
        CircularFifoBuffer fifo = new CircularFifoBuffer(5);
        fifo.add("1");
        fifo.add("2");
        fifo.add("3");
        fifo.add("4");
        fifo.add("5");  // end=0
        fifo.add("6");  // end=1
        fifo.add("7");  // end=2
        fifo.add("8");  // end=3
        
        assertEquals("[4, 5, 6, 7, 8]", fifo.toString());
        
        fifo.remove("7");  // remove element at position one in array, need to shift 8
        assertEquals("[4, 5, 6, 8]", fifo.toString());
    }

    public void testRemoveError9() throws Exception {
        // based on bug 33071
        CircularFifoBuffer fifo = new CircularFifoBuffer(5);
        fifo.add("1");
        fifo.add("2");
        fifo.add("3");
        fifo.add("4");
        fifo.add("5");  // end=0
        fifo.add("6");  // end=1
        fifo.add("7");  // end=2
        fifo.add("8");  // end=3
        
        assertEquals("[4, 5, 6, 7, 8]", fifo.toString());
        
        fifo.remove("8");  // remove element at position two in array
        assertEquals("[4, 5, 6, 7]", fifo.toString());
    }

    //-----------------------------------------------------------------------
    public void testRepeatedSerialization() throws Exception {
        // bug 31433
        CircularFifoBuffer b = new CircularFifoBuffer(2);
        b.add("a");
        assertEquals(1, b.size());
        assertEquals(true, b.contains("a"));
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(b);
        
        CircularFifoBuffer b2 = (CircularFifoBuffer) new ObjectInputStream(
            new ByteArrayInputStream(bos.toByteArray())).readObject();
        
        assertEquals(1, b2.size());
        assertEquals(true, b2.contains("a"));
        b2.add("b");
        assertEquals(2, b2.size());
        assertEquals(true, b2.contains("a"));
        assertEquals(true, b2.contains("b"));
        
        bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(b2);
        
        CircularFifoBuffer b3 = (CircularFifoBuffer) new ObjectInputStream(
            new ByteArrayInputStream(bos.toByteArray())).readObject();
        
        assertEquals(2, b3.size());
        assertEquals(true, b3.contains("a"));
        assertEquals(true, b3.contains("b"));
        b3.add("c");
        assertEquals(2, b3.size());
        assertEquals(true, b3.contains("b"));
        assertEquals(true, b3.contains("c"));
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/CircularFifoBuffer.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/CircularFifoBuffer.fullCollection.version3.1.obj");
//    }

}
