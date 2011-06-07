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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.collections.ResettableListIterator;

/**
 * Tests the ListIteratorWrapper to insure that it simulates
 * a ListIterator correctly.
 *
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Morgan Delagrange
 */
public class TestListIteratorWrapper extends AbstractTestIterator {

    protected String[] testArray = {
        "One", "Two", "Three", "Four", "Five", "Six"
    };

    protected List list1 = null;

    public static Test suite() {
        return new TestSuite(TestListIteratorWrapper.class);
    }

    public TestListIteratorWrapper(String testName) {
        super(testName);
    }

    public void setUp() {
        list1 = new ArrayList();
        list1.add("One");
        list1.add("Two");
        list1.add("Three");
        list1.add("Four");
        list1.add("Five");
        list1.add("Six");
    }

    public Iterator makeEmptyIterator() {
        ArrayList list = new ArrayList();
        return new ListIteratorWrapper(list.iterator());
    }

    public Iterator makeFullIterator() {
        Iterator i = list1.iterator();

        return new ListIteratorWrapper(i);
    }

    public void testIterator() {
        ListIterator iter = (ListIterator) makeFullIterator();
        for ( int i = 0; i < testArray.length; i++ ) {
            Object testValue = testArray[i];            
            Object iterValue = iter.next();

            assertEquals( "Iteration value is correct", testValue, iterValue );
        }

        assertTrue("Iterator should now be empty", ! iter.hasNext() );

        try {
            iter.next();
        } catch (Exception e) {
            assertTrue("NoSuchElementException must be thrown", 
                       e.getClass().equals((new NoSuchElementException()).getClass()));
        }

        // now, read it backwards
        for (int i = testArray.length - 1; i > -1; --i) {
            Object testValue = testArray[i];
            Object iterValue = iter.previous();

            assertEquals( "Iteration value is correct", testValue, iterValue );
        }

        try {
            iter.previous();
        } catch (Exception e) {
            assertTrue("NoSuchElementException must be thrown", 
                       e.getClass().equals((new NoSuchElementException()).getClass()));
        }

        // now, read it forwards again
        for ( int i = 0; i < testArray.length; i++ ) {
            Object testValue = testArray[i];            
            Object iterValue = iter.next();

            assertEquals( "Iteration value is correct", testValue, iterValue );
        }

    }

    public void testRemove() {
        Iterator iter = (Iterator) makeFullIterator();

        try {
            iter.remove();
            fail("FilterIterator does not support the remove() method");
        } catch (UnsupportedOperationException e) {

        }

    }

    public void testReset() {
        ResettableListIterator iter = (ResettableListIterator) makeFullIterator();
        Object first = iter.next();
        Object second = iter.next();
        
        iter.reset();
        
        // after reset, there shouldn't be any previous elements
        assertFalse("No previous elements after reset()", iter.hasPrevious());

        // after reset, the results should be the same as before
        assertEquals("First element should be the same", first, iter.next());
        assertEquals("Second elment should be the same", second, iter.next());
        
        // after passing the point, where we resetted, continuation should work as expected
        for (int i = 2; i < testArray.length; i++) {
            Object testValue = testArray[i];
            Object iterValue = iter.next();

            assertEquals("Iteration value is correct", testValue, iterValue);
        }
    }

}
