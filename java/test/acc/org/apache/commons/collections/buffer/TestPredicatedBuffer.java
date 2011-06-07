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

import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUnderflowException;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.collection.TestPredicatedCollection;

/**
 * Extension of {@link TestPredicatedCollection} for exercising the 
 * {@link PredicatedBuffer} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestPredicatedBuffer extends TestPredicatedCollection {
    
    public TestPredicatedBuffer(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestPredicatedBuffer.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestPredicatedBuffer.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
    //---------------------------------------------------------------
    
    protected Buffer decorateBuffer(Buffer buffer, Predicate predicate) {
        return PredicatedBuffer.decorate(buffer, predicate);
    }
    
    public Collection makeCollection() {
        return decorateBuffer(new ArrayStack(), truePredicate);
    }
    
    public Collection makeConfirmedCollection() {
        return new ArrayStack();
    }
    
    public Collection makeConfirmedFullCollection() {
        ArrayStack list = new ArrayStack();
        list.addAll(java.util.Arrays.asList(getFullElements()));
        return list;
    }
    
    //------------------------------------------------------------
    
    public Buffer makeTestBuffer() {
        return decorateBuffer(new ArrayStack(), testPredicate);
    }
    
    public void testGet() {
        Buffer buffer = makeTestBuffer();
        try {
            Object o = buffer.get();
            fail("Expecting BufferUnderflowException");
        } catch (BufferUnderflowException ex) {
            // expected
        }
        buffer.add("one");
        buffer.add("two");
        buffer.add("three");
        assertEquals("Buffer get", buffer.get(), "three");
    }
    
    public void testRemove() {
        Buffer buffer = makeTestBuffer();
        buffer.add("one");
        assertEquals("Buffer get", buffer.remove(), "one");
        try {
            buffer.remove();
            fail("Expecting BufferUnderflowException");
        } catch (BufferUnderflowException ex) {
            // expected
        }      
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedBuffer.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedBuffer.fullCollection.version3.1.obj");
//    }

}
