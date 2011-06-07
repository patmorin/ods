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

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the ArrayIterator with primitive type arrays.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Morgan Delagrange
 * @author James Strachan
 */
public class TestArrayIterator2 extends AbstractTestIterator {

    protected int[] testArray = { 2, 4, 6, 8 };

    public static Test suite() {
        return new TestSuite(TestArrayIterator2.class);
    }

    public TestArrayIterator2(String testName) {
        super(testName);
    }

    public Iterator makeEmptyIterator() {
        return new ArrayIterator(new int[0]);
    }

    public Iterator makeFullIterator() {
        return new ArrayIterator(testArray);
    }

    /*
     * We use these <code>makeArrayIterator</code> factory methods instead of
     * directly calling the constructor so as to allow subclasses
     * (e.g. TestArrayListIterator2) to use the existing test code.
     * 
     * @return ArrayIterator
     */
    public ArrayIterator makeArrayIterator() {
        return (ArrayIterator) makeEmptyIterator();
    }
    public ArrayIterator makeArrayIterator(Object array) {
        return new ArrayIterator(array);
    }
    public ArrayIterator makeArrayIterator(Object array, int index) {
        return new ArrayIterator(array, index);
    }
    public ArrayIterator makeArrayIterator(Object array, int start, int end) {
        return new ArrayIterator(array, start, end);
    }

    public boolean supportsRemove() {
        return false;
    }


    public void testIterator() {
        Iterator iter = (Iterator) makeFullIterator();
        for (int i = 0; i < testArray.length; i++) {
            Integer testValue = new Integer(testArray[i]);
            Number iterValue = (Number) iter.next();

            assertEquals("Iteration value is correct", testValue, iterValue);
        }

        assertTrue("Iterator should now be empty", !iter.hasNext());

        try {
            Object testValue = iter.next();
        } catch (Exception e) {
            assertTrue(
                "NoSuchElementException must be thrown",
                e.getClass().equals((new NoSuchElementException()).getClass()));
        }
    }

    // proves that an ArrayIterator set with the constructor has the same number of elements
    // as an ArrayIterator set with setArray(Object) 
    public void testSetArray() {
        Iterator iter1 = makeArrayIterator(testArray);
        int count1 = 0;
        while (iter1.hasNext()) {
            ++count1;
            iter1.next();
        }

        assertEquals("the count should be right using the constructor", count1, testArray.length);

        ArrayIterator iter2 = makeArrayIterator();
        iter2.setArray(testArray);
        int count2 = 0;
        while (iter2.hasNext()) {
            ++count2;
            iter2.next();
        }

        assertEquals("the count should be right using setArray(Object)", count2, testArray.length);
    }

    public void testIndexedArray() {
        Iterator iter = makeArrayIterator(testArray, 2);
        int count = 0;
        while (iter.hasNext()) {
            ++count;
            iter.next();
        }

        assertEquals("the count should be right using ArrayIterator(Object,2) ", count, testArray.length - 2);

        iter = makeArrayIterator(testArray, 1, testArray.length - 1);
        count = 0;
        while (iter.hasNext()) {
            ++count;
            iter.next();
        }

        assertEquals(
            "the count should be right using ArrayIterator(Object,1," + (testArray.length - 1) + ") ",
            count,
            testArray.length - 2);

        try {
            iter = makeArrayIterator(testArray, -1);
            fail("new ArrayIterator(Object,-1) should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // expected
        }

        try {
            iter = makeArrayIterator(testArray, testArray.length + 1);
            fail("new ArrayIterator(Object,length+1) should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // expected
        }

        try {
            iter = makeArrayIterator(testArray, 0, -1);
            fail("new ArrayIterator(Object,0,-1) should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // expected
        }

        try {
            iter = makeArrayIterator(testArray, 0, testArray.length + 1);
            fail("new ArrayIterator(Object,0,length+1) should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // expected
        }

        try {
            iter = makeArrayIterator(testArray, 1, 1);
            // expected not to fail
        } catch (IllegalArgumentException iae) {
            // MODIFIED: an iterator over a zero-length section of array
            //  should be perfectly legal behavior
            fail("new ArrayIterator(Object,1,1) should NOT throw an IllegalArgumentException");
        }

        try {
            iter = makeArrayIterator(testArray, testArray.length - 1, testArray.length - 2);
            fail("new ArrayIterator(Object,length-2,length-1) should throw an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }
}
