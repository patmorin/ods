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

import java.util.EmptyStackException;
import java.util.List;

import junit.framework.Test;

/**
 * Tests ArrayStack.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Craig McClanahan
 */
public class TestArrayStack extends TestArrayList {
    
    protected ArrayStack stack = null;

    public TestArrayStack(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestArrayStack.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestArrayStack.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public List makeEmptyList() {
        return new ArrayStack();
    }

    public void setUp() {
        stack = (ArrayStack) makeEmptyList();
        list = stack;
    }

    //-----------------------------------------------------------------------
    public void testNewStack() {

        assertTrue("New stack is empty", stack.empty());
        assertEquals("New stack has size zero", stack.size(), 0);

        try {
            stack.peek();
            fail("peek() should have thrown EmptyStackException");
        } catch (EmptyStackException e) {
            ; // Expected result
        }

        try {
            stack.pop();
            fail("pop() should have thrown EmptyStackException");
        } catch (EmptyStackException e) {
            ; // Expected result
        }

    }

    public void testPushPeekPop() {

        stack.push("First Item");
        assertTrue("Stack is not empty", !stack.empty());
        assertEquals("Stack size is one", stack.size(), 1);
        assertEquals("Top item is 'First Item'",
                     (String) stack.peek(), "First Item");
        assertEquals("Stack size is one", stack.size(), 1);

        stack.push("Second Item");
        assertEquals("Stack size is two", stack.size(), 2);
        assertEquals("Top item is 'Second Item'",
                     (String) stack.peek(), "Second Item");
        assertEquals("Stack size is two", stack.size(), 2);

        assertEquals("Popped item is 'Second Item'",
                     (String) stack.pop(), "Second Item");
        assertEquals("Top item is 'First Item'",
                     (String) stack.peek(), "First Item");
        assertEquals("Stack size is one", stack.size(), 1);

        assertEquals("Popped item is 'First Item'",
                     (String) stack.pop(), "First Item");
        assertEquals("Stack size is zero", stack.size(), 0);

    }

    public void testSearch() {

        stack.push("First Item");
        stack.push("Second Item");
        assertEquals("Top item is 'Second Item'",
                     stack.search("Second Item"), 1);
        assertEquals("Next Item is 'First Item'",
                     stack.search("First Item"), 2);
        assertEquals("Cannot find 'Missing Item'",
                     stack.search("Missing Item"), -1);

    }

}
