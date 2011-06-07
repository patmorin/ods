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
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;

import org.apache.commons.collections.collection.AbstractTestCollection;

/**
 * Test cases for BoundedFifoBuffer.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Paul Jack
 */
public class TestBoundedFifoBuffer extends AbstractTestCollection {

    public TestBoundedFifoBuffer(String n) {
        super(n);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestBoundedFifoBuffer.class);
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
     *  Returns an empty BoundedFifoBuffer that won't overflow.  
     *  
     *  @return an empty BoundedFifoBuffer
     */
    public Collection makeCollection() {
        return new BoundedFifoBuffer(100);
    }

    //-----------------------------------------------------------------------  
    /**
     * Tests that the removal operation actually removes the first element.
     */
    public void testBoundedFifoBufferRemove() {
        resetFull();
        int size = confirmed.size();
        for (int i = 0; i < size; i++) {
            Object o1 = ((BoundedFifoBuffer)collection).remove();
            Object o2 = ((ArrayList)confirmed).remove(0);
            assertEquals("Removed objects should be equal", o1, o2);
            verify();
        }

        try {
            ((BoundedFifoBuffer)collection).remove();
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
            new BoundedFifoBuffer(0);
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
            new BoundedFifoBuffer(-20);
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
            new BoundedFifoBuffer(null);
        } catch (NullPointerException ex) {
            return;
        }
        fail();
    }
}
