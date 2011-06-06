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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.collection.AbstractTestCollection;

/**
 * Test cases for UnboundedFifoBuffer.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Unknown
 */
public class TestUnboundedFifoBuffer extends AbstractTestCollection {

    public TestUnboundedFifoBuffer(String n) {
        super(n);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestUnboundedFifoBuffer.class);
    }

    //-----------------------------------------------------------------------
    /**
     *  Verifies that the ArrayList has the same elements in the same 
     *  sequence as the UnboundedFifoBuffer.
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
     *  Returns an empty ArrayList.
     *
     *  @return an empty ArrayList
     */
    public Collection makeConfirmedCollection() {
        return new ArrayList();
    }

    /**
     *  Returns a full ArrayList.
     *
     *  @return a full ArrayList
     */
    public Collection makeConfirmedFullCollection() {
        Collection c = makeConfirmedCollection();
        c.addAll(java.util.Arrays.asList(getFullElements()));
        return c;
    }

    /**
     *  Returns an empty UnboundedFifoBuffer with a small capacity.
     *
     *  @return an empty UnboundedFifoBuffer
     */
    public Collection makeCollection() {
        return new UnboundedFifoBuffer(5);
    }

    //-----------------------------------------------------------------------
    /**
     *  Tests that UnboundedFifoBuffer removes elements in the right order.
     */
    public void testUnboundedFifoBufferRemove() {
        resetFull();
        int size = confirmed.size();
        for (int i = 0; i < size; i++) {
            Object o1 = ((UnboundedFifoBuffer)collection).remove();
            Object o2 = ((ArrayList)confirmed).remove(0);
            assertEquals("Removed objects should be equal", o1, o2);
            verify();
        }
    }

    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException1() {
        try {
            new UnboundedFifoBuffer(0);
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
            new UnboundedFifoBuffer(-20);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    //-----------------------------------------------------------------------
    public void testInternalStateAdd() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(2);
        assertEquals(3, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(0, test.tail);
        test.add("A");
        assertEquals(3, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(1, test.tail);
        test.add("B");
        assertEquals(3, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(2, test.tail);
        test.add("C");  // forces buffer increase
        assertEquals(5, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(3, test.tail);
        test.add("D");
        assertEquals(5, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(4, test.tail);
    }

    public void testInternalStateAddWithWrap() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(3);
        assertEquals(4, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(0, test.tail);
        test.add("A");
        assertEquals(4, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(1, test.tail);
        test.add("B");
        assertEquals(4, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(2, test.tail);
        test.add("C");
        assertEquals(4, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(3, test.tail);
        test.remove("A");
        assertEquals(4, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(3, test.tail);
        test.remove("B");
        assertEquals(4, test.buffer.length);
        assertEquals(2, test.head);
        assertEquals(3, test.tail);
        test.add("D");
        assertEquals(4, test.buffer.length);
        assertEquals(2, test.head);
        assertEquals(0, test.tail);
        test.add("E");
        assertEquals(4, test.buffer.length);
        assertEquals(2, test.head);
        assertEquals(1, test.tail);
    }

    public void testInternalStateRemove1() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(4);
        test.add("A");
        test.add("B");
        test.add("C");
        assertEquals(5, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(3, test.tail);
        
        test.remove("A");
        assertEquals(5, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(3, test.tail);
        
        test.add("D");
        assertEquals(5, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(4, test.tail);
    }

    public void testInternalStateRemove2() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(4);
        test.add("A");
        test.add("B");
        test.add("C");
        assertEquals(5, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(3, test.tail);
        
        test.remove("B");
        assertEquals(5, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(2, test.tail);
        
        test.add("D");
        assertEquals(5, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(3, test.tail);
    }

    public void testInternalStateIteratorRemove1() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(4);
        test.add("A");
        test.add("B");
        test.add("C");
        assertEquals(5, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(3, test.tail);
        
        Iterator it = test.iterator();
        it.next();
        it.remove();
        assertEquals(5, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(3, test.tail);
        
        test.add("D");
        assertEquals(5, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(4, test.tail);
    }

    public void testInternalStateIteratorRemove2() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(4);
        test.add("A");
        test.add("B");
        test.add("C");
        
        Iterator it = test.iterator();
        it.next();
        it.next();
        it.remove();
        assertEquals(5, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(2, test.tail);
        
        test.add("D");
        assertEquals(5, test.buffer.length);
        assertEquals(0, test.head);
        assertEquals(3, test.tail);
    }

    public void testInternalStateIteratorRemoveWithTailAtEnd1() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(3);
        test.add("A");
        test.add("B");
        test.add("C");
        test.remove("A");
        test.add("D");
        assertEquals(4, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(0, test.tail);
        
        Iterator it = test.iterator();
        assertEquals("B", it.next());
        it.remove();
        assertEquals(4, test.buffer.length);
        assertEquals(2, test.head);
        assertEquals(0, test.tail);
    }

    public void testInternalStateIteratorRemoveWithTailAtEnd2() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(3);
        test.add("A");
        test.add("B");
        test.add("C");
        test.remove("A");
        test.add("D");
        assertEquals(4, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(0, test.tail);
        
        Iterator it = test.iterator();
        assertEquals("B", it.next());
        assertEquals("C", it.next());
        it.remove();
        assertEquals(4, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(3, test.tail);
    }

    public void testInternalStateIteratorRemoveWithTailAtEnd3() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(3);
        test.add("A");
        test.add("B");
        test.add("C");
        test.remove("A");
        test.add("D");
        assertEquals(4, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(0, test.tail);
        
        Iterator it = test.iterator();
        assertEquals("B", it.next());
        assertEquals("C", it.next());
        assertEquals("D", it.next());
        it.remove();
        assertEquals(4, test.buffer.length);
        assertEquals(1, test.head);
        assertEquals(3, test.tail);
    }

    public void testInternalStateIteratorRemoveWithWrap1() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(3);
        test.add("A");
        test.add("B");
        test.add("C");
        test.remove("A");
        test.remove("B");
        test.add("D");
        test.add("E");
        assertEquals(4, test.buffer.length);
        assertEquals(2, test.head);
        assertEquals(1, test.tail);
        
        Iterator it = test.iterator();
        assertEquals("C", it.next());
        it.remove();
        assertEquals(4, test.buffer.length);
        assertEquals(3, test.head);
        assertEquals(1, test.tail);
    }

    public void testInternalStateIteratorRemoveWithWrap2() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(3);
        test.add("A");
        test.add("B");
        test.add("C");
        test.remove("A");
        test.remove("B");
        test.add("D");
        test.add("E");
        assertEquals(4, test.buffer.length);
        assertEquals(2, test.head);
        assertEquals(1, test.tail);
        
        Iterator it = test.iterator();
        assertEquals("C", it.next());
        assertEquals("D", it.next());
        it.remove();
        assertEquals(4, test.buffer.length);
        assertEquals(2, test.head);
        assertEquals(0, test.tail);
    }

    public void testInternalStateIteratorRemoveWithWrap3() {
        UnboundedFifoBuffer test = new UnboundedFifoBuffer(3);
        test.add("A");
        test.add("B");
        test.add("C");
        test.remove("A");
        test.remove("B");
        test.add("D");
        test.add("E");
        assertEquals(4, test.buffer.length);
        assertEquals(2, test.head);
        assertEquals(1, test.tail);
        
        Iterator it = test.iterator();
        assertEquals("C", it.next());
        assertEquals("D", it.next());
        assertEquals("E", it.next());
        it.remove();
        assertEquals(4, test.buffer.length);
        assertEquals(2, test.head);
        assertEquals(0, test.tail);
    }

    //-----------------------------------------------------------------------
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/UnboundedFifoBuffer.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/UnboundedFifoBuffer.fullCollection.version3.1.obj");
//    }

}
