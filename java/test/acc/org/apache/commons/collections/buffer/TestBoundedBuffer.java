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

import org.apache.commons.collections.AbstractTestObject;
import org.apache.commons.collections.BoundedCollection;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferOverflowException;

import java.util.Iterator;
import java.util.Collections;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestBoundedBuffer extends AbstractTestObject {

    public TestBoundedBuffer(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestBoundedBuffer.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestBoundedBuffer.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public String getCompatibilityVersion() {
        return "3.2";
    }

    public boolean isEqualsCheckable() {
        return false;
    }

    public Object makeObject() {
        return BoundedBuffer.decorate(new UnboundedFifoBuffer(), 1);
    }

    //-----------------------------------------------------------------------
    public void testMaxSize() {
        final Buffer bounded = BoundedBuffer.decorate(new UnboundedFifoBuffer(), 2, 500);
        BoundedCollection bc = (BoundedCollection) bounded;
        assertEquals(2, bc.maxSize());
        assertEquals(false, bc.isFull());
        bounded.add("A");
        assertEquals(false, bc.isFull());
        bounded.add("B");
        assertEquals(true, bc.isFull());
        bounded.remove();
        assertEquals(false, bc.isFull());
        try {
            BoundedBuffer.decorate(new UnboundedFifoBuffer(), 0);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            BoundedBuffer.decorate(new UnboundedFifoBuffer(), -1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testAddToFullBufferNoTimeout() {
        final Buffer bounded = BoundedBuffer.decorate(new UnboundedFifoBuffer(), 1);
        bounded.add( "Hello" );
        try {
            bounded.add("World");
            fail();
        } catch (BufferOverflowException e) {
        }
    }

    public void testAddAllToFullBufferNoTimeout() {
        final Buffer bounded = BoundedBuffer.decorate(new UnboundedFifoBuffer(), 1);
        bounded.add( "Hello" );
        try {
            bounded.addAll(Collections.singleton("World"));
            fail();
        } catch (BufferOverflowException e) {
        }
    }

    public void testAddAllToEmptyBufferExceedMaxSizeNoTimeout() {
        final Buffer bounded = BoundedBuffer.decorate(new UnboundedFifoBuffer(), 1);
        try {
            bounded.addAll(Collections.nCopies(2, "test"));
            fail();
        } catch (BufferOverflowException e) {
        }
    }

    public void testAddToFullBufferRemoveViaIterator() {
        final Buffer bounded = BoundedBuffer.decorate(new UnboundedFifoBuffer(), 1, 500);
        bounded.add( "Hello" );
        new DelayedIteratorRemove( bounded, 200 ).start();
        bounded.add( "World" );
        assertEquals( 1, bounded.size() );
        assertEquals( "World", bounded.get() );

    }

    public void testAddAllToFullBufferRemoveViaIterator() {
        final Buffer bounded = BoundedBuffer.decorate(new UnboundedFifoBuffer(), 2, 500);
        bounded.add( "Hello" );
        bounded.add( "World" );
        new DelayedIteratorRemove( bounded, 200, 2 ).start();
        bounded.addAll( Arrays.asList( new String[] { "Foo", "Bar" } ) );
        assertEquals( 2, bounded.size() );
        assertEquals( "Foo", bounded.remove() );
        assertEquals( "Bar", bounded.remove() );
    }

    public void testAddToFullBufferWithTimeout() {
        final Buffer bounded = BoundedBuffer.decorate(new UnboundedFifoBuffer(), 1, 500);
        bounded.add( "Hello" );
        new DelayedRemove( bounded, 200 ).start();
        bounded.add( "World" );
        assertEquals( 1, bounded.size() );
        assertEquals( "World", bounded.get() );
        try {
            bounded.add( "!" );
            fail();
        }
        catch( BufferOverflowException e ) {
        }
    }

    public void testAddAllToFullBufferWithTimeout() {
        final Buffer bounded = BoundedBuffer.decorate(new UnboundedFifoBuffer(), 2, 500);
        bounded.add( "Hello" );
        bounded.add( "World" );
        new DelayedRemove( bounded, 200, 2 ).start();

        bounded.addAll( Arrays.asList( new String[] { "Foo", "Bar" } ) );
        assertEquals( 2, bounded.size() );
        assertEquals( "Foo", bounded.get() );
        try {
            bounded.add( "!" );
            fail();
        }
        catch( BufferOverflowException e ) {
        }
    }

    private class DelayedIteratorRemove extends Thread {

        private final Buffer buffer;

        private final long delay;

        private final int nToRemove;

        public DelayedIteratorRemove(Buffer buffer, long delay, int nToRemove) {
            this.buffer = buffer;
            this.delay = delay;
            this.nToRemove = nToRemove;
        }

        public DelayedIteratorRemove(Buffer buffer, long delay) {
            this(buffer, delay, 1);
        }

        public void run() {
            try {
                Thread.sleep(delay);
                Iterator iter = buffer.iterator();
                for (int i = 0; i < nToRemove; ++i) {
                    iter.next();
                    iter.remove();
                }

            } catch (InterruptedException e) {
            }
        }
    }

    private class DelayedRemove extends Thread {

        private final Buffer buffer;

        private final long delay;

        private final int nToRemove;

        public DelayedRemove(Buffer buffer, long delay, int nToRemove) {
            this.buffer = buffer;
            this.delay = delay;
            this.nToRemove = nToRemove;
        }

        public DelayedRemove(Buffer buffer, long delay) {
            this(buffer, delay, 1);
        }

        public void run() {
            try {
                Thread.sleep(delay);
                for (int i = 0; i < nToRemove; ++i) {
                    buffer.remove();
                }
            } catch (InterruptedException e) {
            }
        }
    }
}