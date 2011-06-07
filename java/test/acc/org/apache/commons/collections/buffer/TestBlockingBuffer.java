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

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.collections.AbstractTestObject;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUnderflowException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Extension of {@link AbstractTestObject} for exercising the {@link BlockingBuffer} implementation.
 *
 * @author Janek Bogucki
 * @author Phil Steitz
 * @version $Revision: 646780 $
 * @since Commons Collections 3.0
 */
public class TestBlockingBuffer extends AbstractTestObject {

    public TestBlockingBuffer( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( TestBlockingBuffer.class );
    }

    public static void main( String args[] ) {
        String[] testCaseName = {TestBlockingBuffer.class.getName()};
        junit.textui.TestRunner.main( testCaseName );
    }

    public Object makeObject() {
        return BlockingBuffer.decorate( new MyBuffer() );
    }

    public boolean isEqualsCheckable() {
        return false;
    }

    //-----------------------------------------------------------------------

    /**
     * Tests {@link BlockingBuffer#get()} in combination with {@link BlockingBuffer#add(Object)}.
     */
    public void testGetWithAdd() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();
        new DelayedAdd( blockingBuffer, obj ).start();

        // verify does not throw BufferUnderflowException; should block until other thread has added to the buffer .
        assertSame( obj, blockingBuffer.get() );
    }

    public void testGetWithAddTimeout() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer(), 500 );
        Object obj = new Object();
        new DelayedAdd( blockingBuffer, obj, 100 ).start();

        // verify does not throw BufferUnderflowException; should block until other thread has added to the buffer .
        assertSame( obj, blockingBuffer.get() );
    }

    //-----------------------------------------------------------------------

    /**
     * Tests {@link BlockingBuffer#get()} in combination with {@link BlockingBuffer#addAll(java.util.Collection)}.
     */
    public void testGetWithAddAll() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();
        new DelayedAddAll( blockingBuffer, obj ).start();

        // verify does not throw BufferUnderflowException; should block until other thread has added to the buffer .
        assertSame( obj, blockingBuffer.get() );
    }

    public void testGetWithAddAllTimeout() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer(), 500 );
        Object obj = new Object();
        new DelayedAddAll( blockingBuffer, obj, 100 ).start();

        // verify does not throw BufferUnderflowException; should block until other thread has added to the buffer .
        assertSame( obj, blockingBuffer.get() );
    }

    //-----------------------------------------------------------------------

    /**
     * Tests {@link BlockingBuffer#remove()} in combination with {@link BlockingBuffer#add(Object)}.
     */
    public void testRemoveWithAdd() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();
        new DelayedAdd( blockingBuffer, obj ).start();

        // verify does not throw BufferUnderflowException; should block until other thread has added to the buffer .
        assertSame( obj, blockingBuffer.remove() );
    }

    public void testRemoveWithAddTimeout() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer(), 100 );
        Object obj = new Object();
        new DelayedAdd( blockingBuffer, obj, 500 ).start();
        try {
            blockingBuffer.remove();
        }
        catch( BufferUnderflowException e ) {
        }
    }
    //-----------------------------------------------------------------------

    /**
     * Tests {@link BlockingBuffer#remove()} in combination with {@link BlockingBuffer#addAll(java.util.Collection)}.
     */
    public void testRemoveWithAddAll() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();
        new DelayedAddAll( blockingBuffer, obj ).start();

        // verify does not throw BufferUnderflowException; should block until other thread has added to the buffer .
        assertSame( obj, blockingBuffer.remove() );
    }

    public void testRemoveWithAddAllTimeout() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer(), 100 );
        Object obj = new Object();
        new DelayedAddAll( blockingBuffer, obj, 500 ).start();
        try {
            blockingBuffer.remove();
        }
        catch( BufferUnderflowException e ) {
        }
    }
    //-----------------------------------------------------------------------

    /**
     * Tests {@link BlockingBuffer#get()} in combination with {@link BlockingBuffer#add(Object)} using multiple read
     * threads.
     * <p/>
     * Two read threads should block on an empty buffer until one object is added then both threads should complete.
     */
    public void testBlockedGetWithAdd() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();

        // run methods will get and compare -- must wait for add
        Thread thread1 = new ReadThread( blockingBuffer, obj );
        Thread thread2 = new ReadThread( blockingBuffer, obj );
        thread1.start();
        thread2.start();

        // give hungry read threads ample time to hang
        delay();

        // notifyAll should allow both read threads to complete
        blockingBuffer.add( obj );

        // allow notified threads to complete 
        delay();

        // There should not be any threads waiting.
        if( thread1.isAlive() || thread2.isAlive() ) {
            fail( "Live thread(s) when both should be dead." );
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Tests {@link BlockingBuffer#get()} in combination with {@link BlockingBuffer#addAll(java.util.Collection)} using
     * multiple read threads.
     * <p/>
     * Two read threads should block on an empty buffer until a singleton is added then both threads should complete.
     */
    public void testBlockedGetWithAddAll() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();

        // run methods will get and compare -- must wait for addAll
        Thread thread1 = new ReadThread( blockingBuffer, obj );
        Thread thread2 = new ReadThread( blockingBuffer, obj );
        thread1.start();
        thread2.start();

        // give hungry read threads ample time to hang
        delay();

        // notifyAll should allow both read threads to complete
        blockingBuffer.addAll( Collections.singleton( obj ) );

        // allow notified threads to complete 
        delay();

        // There should not be any threads waiting.
        if( thread1.isAlive() || thread2.isAlive() ) {
            fail( "Live thread(s) when both should be dead." );
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Tests interrupted {@link BlockingBuffer#get()}.
     */
    public void testInterruptedGet() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();

        // spawn a read thread to wait on the empty buffer
        ArrayList exceptionList = new ArrayList();
        Thread thread = new ReadThread( blockingBuffer, obj, exceptionList );
        thread.start();

        // Interrupting the thread should cause it to throw BufferUnderflowException
        thread.interrupt();

        // Chill, so thread can throw and add message to exceptionList
        delay();
        assertTrue( "Thread interrupt should have led to underflow",
                    exceptionList.contains( "BufferUnderFlow" ) );
        if( thread.isAlive() ) {
            fail( "Read thread has hung." );
        }

    }

    //-----------------------------------------------------------------------

    /**
     * Tests {@link BlockingBuffer#remove()} in combination with {@link BlockingBuffer#add(Object)} using multiple read
     * threads.
     * <p/>
     * Two read threads should block on an empty buffer until one object is added then one thread should complete. The
     * remaining thread should complete after the addition of a second object.
     */
    public void testBlockedRemoveWithAdd() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();

        // run methods will remove and compare -- must wait for add
        Thread thread1 = new ReadThread( blockingBuffer, obj, null, "remove" );
        Thread thread2 = new ReadThread( blockingBuffer, obj, null, "remove" );
        thread1.start();
        thread2.start();

        // give hungry read threads ample time to hang
        delay();
        blockingBuffer.add( obj );

        // allow notified threads to complete 
        delay();

        // There should be one thread waiting.
        assertTrue( "There is one thread waiting", thread1.isAlive() ^ thread2.isAlive() );
        blockingBuffer.add( obj );

        // allow notified thread to complete 
        delay();

        // There should not be any threads waiting.
        if( thread1.isAlive() || thread2.isAlive() ) {
            fail( "Live thread(s) when both should be dead." );
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Tests {@link BlockingBuffer#remove()} in combination with {@link BlockingBuffer#addAll(java.util.Collection)}
     * using multiple read threads.
     * <p/>
     * Two read threads should block on an empty buffer until a singleton collection is added then one thread should
     * complete. The remaining thread should complete after the addition of a second singleton.
     */
    public void testBlockedRemoveWithAddAll1() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();

        // run methods will remove and compare -- must wait for addAll
        Thread thread1 = new ReadThread( blockingBuffer, obj, null, "remove" );
        Thread thread2 = new ReadThread( blockingBuffer, obj, null, "remove" );
        thread1.start();
        thread2.start();

        // give hungry read threads ample time to hang
        delay();
        blockingBuffer.addAll( Collections.singleton( obj ) );

        // allow notified threads to complete 
        delay();

        // There should be one thread waiting.
        assertTrue( "There is one thread waiting", thread1.isAlive() ^ thread2.isAlive() );
        blockingBuffer.addAll( Collections.singleton( obj ) );

        // allow notified thread to complete 
        delay();

        // There should not be any threads waiting.
        if( thread1.isAlive() || thread2.isAlive() ) {
            fail( "Live thread(s) when both should be dead." );
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Tests {@link BlockingBuffer#remove()} in combination with {@link BlockingBuffer#addAll(java.util.Collection)}
     * using multiple read threads.
     * <p/>
     * Two read threads should block on an empty buffer until a collection with two distinct objects is added then both
     * threads should complete. Each thread should have read a different object.
     */
    public void testBlockedRemoveWithAddAll2() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj1 = new Object();
        Object obj2 = new Object();
        Set objs = Collections.synchronizedSet( new HashSet() );
        objs.add( obj1 );
        objs.add( obj2 );

        // run methods will remove and compare -- must wait for addAll
        Thread thread1 = new ReadThread( blockingBuffer, objs, "remove" );
        Thread thread2 = new ReadThread( blockingBuffer, objs, "remove" );
        thread1.start();
        thread2.start();

        // give hungry read threads ample time to hang
        delay();
        blockingBuffer.addAll( objs );

        // allow notified threads to complete 
        delay();
        assertEquals( "Both objects were removed", 0, objs.size() );

        // There should not be any threads waiting.
        if( thread1.isAlive() || thread2.isAlive() ) {
            fail( "Live thread(s) when both should be dead." );
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Tests interrupted remove.
     */
    public void testInterruptedRemove() {
        Buffer blockingBuffer = BlockingBuffer.decorate( new MyBuffer() );
        Object obj = new Object();

        // spawn a read thread to wait on the empty buffer
        ArrayList exceptionList = new ArrayList();
        Thread thread = new ReadThread( blockingBuffer, obj, exceptionList, "remove" );
        thread.start();

        // Interrupting the thread should cause it to throw BufferUnderflowException
        thread.interrupt();

        // Chill, so thread can throw and add message to exceptionList
        delay();
        assertTrue( "Thread interrupt should have led to underflow",
                    exceptionList.contains( "BufferUnderFlow" ) );
        if( thread.isAlive() ) {
            fail( "Read thread has hung." );
        }

    }

    public void testTimeoutGet() {
        final BlockingBuffer buffer = new BlockingBuffer( new MyBuffer() );
        try {
            buffer.get( 100 );
            fail( "Get should have timed out." );
        }
        catch( BufferUnderflowException e ) {
        }
    }

    public void testTimeoutRemove() {
        final BlockingBuffer buffer = new BlockingBuffer( new MyBuffer() );
        try {
            buffer.remove( 100 );
            fail( "Get should have timed out." );
        }
        catch( BufferUnderflowException e ) {
        }
    }

    protected static class DelayedAdd extends Thread {

        Buffer buffer;

        Object obj;

        long delay = 1000;

        public DelayedAdd( Buffer buffer, Object obj, long delay ) {
            this.buffer = buffer;
            this.obj = obj;
            this.delay = delay;
        }

        DelayedAdd( Buffer buffer, Object obj ) {
            super();
            this.buffer = buffer;
            this.obj = obj;
        }

        public void run() {
            try {
                // wait for other thread to block on get() or remove()
                Thread.sleep( delay );
            }
            catch( InterruptedException e ) {
            }
            buffer.add( obj );
        }
    }

    protected static class DelayedAddAll extends Thread {

        Buffer buffer;

        Object obj;

        long delay = 100;

        public DelayedAddAll( Buffer buffer, Object obj, long delay ) {
            this.buffer = buffer;
            this.obj = obj;
            this.delay = delay;
        }

        DelayedAddAll( Buffer buffer, Object obj ) {
            super();
            this.buffer = buffer;
            this.obj = obj;
        }

        public void run() {
            try {
                // wait for other thread to block on get() or remove()
                Thread.sleep( delay );
            }
            catch( InterruptedException e ) {
            }
            buffer.addAll( Collections.singleton( obj ) );
        }
    }

    protected static class ReadThread extends Thread {

        Buffer buffer;

        Object obj;

        ArrayList exceptionList = null;

        String action = "get";

        Set objs;

        ReadThread( Buffer buffer, Object obj ) {
            super();
            this.buffer = buffer;
            this.obj = obj;
        }

        ReadThread( Buffer buffer, Object obj, ArrayList exceptionList ) {
            super();
            this.buffer = buffer;
            this.obj = obj;
            this.exceptionList = exceptionList;
        }

        ReadThread( Buffer buffer, Object obj, ArrayList exceptionList, String action ) {
            super();
            this.buffer = buffer;
            this.obj = obj;
            this.exceptionList = exceptionList;
            this.action = action;
        }

        ReadThread( Buffer buffer, Set objs, String action ) {
            super();
            this.buffer = buffer;
            this.objs = objs;
            this.action = action;
        }

        public void run() {
            try {
                if( action == "get" ) {
                    assertSame( obj, buffer.get() );
                }
                else {
                    if( null != obj ) {
                        assertSame( obj, buffer.remove() );
                    }
                    else {
                        assertTrue( objs.remove( buffer.remove() ) );
                    }
                }
            }
            catch( BufferUnderflowException ex ) {
                exceptionList.add( "BufferUnderFlow" );
            }
        }
    }

    protected static class MyBuffer extends LinkedList implements Buffer {

        public Object get() {
            if( isEmpty() ) {
                throw new BufferUnderflowException();
            }
            return get( 0 );
        }

        public Object remove() {
            if( isEmpty() ) {
                throw new BufferUnderflowException();
            }
            return remove( 0 );
        }
    }

    private void delay() {
        try {
            Thread.sleep( 100 );
        }
        catch( InterruptedException e ) {
        }
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        Buffer buffer = BlockingBuffer.decorate(new UnboundedFifoBuffer());
//        writeExternalFormToDisk((java.io.Serializable) buffer,
//        "D:/dev/collections/data/test/BlockingBuffer.emptyCollection.version3.1.obj");
//        buffer = BlockingBuffer.decorate(new UnboundedFifoBuffer());
//        buffer.add("A");
//        buffer.add("B");
//        buffer.add("C");
//        writeExternalFormToDisk((java.io.Serializable) buffer,
//        "D:/dev/collections/data/test/BlockingBuffer.fullCollection.version3.1.obj");
//    }
}
