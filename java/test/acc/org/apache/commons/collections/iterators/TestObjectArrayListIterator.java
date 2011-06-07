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

import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the ObjectArrayListIterator class.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Neil O'Toole
 */
public class TestObjectArrayListIterator extends TestObjectArrayIterator {

    public TestObjectArrayListIterator(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestObjectArrayListIterator.class);
    }

    public Iterator makeEmptyIterator() {
        return new ObjectArrayListIterator(new Object[0]);
    }

    public Iterator makeFullIterator() {
        return new ObjectArrayListIterator(testArray);
    }

    public ListIterator makeArrayListIterator(Object[] array) {
        return new ObjectArrayListIterator(array);
    }

    /**
     * Test the basic ListIterator functionality - going backwards using
     * <code>previous()</code>.
     */
    public void testListIterator() {
        ListIterator iter = (ListIterator) makeFullIterator();

        // TestArrayIterator#testIterator() has already tested the iterator forward,
        //  now we need to test it in reverse

        // fast-forward the iterator to the end...
        while (iter.hasNext()) {
            iter.next();
        }

        for (int x = testArray.length - 1; x >= 0; x--) {
            Object testValue = testArray[x];
            Object iterValue = iter.previous();

            assertEquals("Iteration value is correct", testValue, iterValue);
        }

        assertTrue("Iterator should now be empty", !iter.hasPrevious());

        try {
            Object testValue = iter.previous();
        } catch (Exception e) {
            assertTrue(
                "NoSuchElementException must be thrown",
                e.getClass().equals((new NoSuchElementException()).getClass()));
        }

    }

    /**
     * Tests the {@link java.util.ListIterator#set} operation.
     */
    public void testListIteratorSet() {
        String[] testData = new String[] { "a", "b", "c" };

        String[] result = new String[] { "0", "1", "2" };

        ListIterator iter = (ListIterator) makeArrayListIterator(testData);
        int x = 0;

        while (iter.hasNext()) {
            iter.next();
            iter.set(Integer.toString(x));
            x++;
        }

        assertTrue("The two arrays should have the same value, i.e. {0,1,2}", Arrays.equals(testData, result));

        // a call to set() before a call to next() or previous() should throw an IllegalStateException
        iter = makeArrayListIterator(testArray);

        try {
            iter.set("should fail");
            fail("ListIterator#set should fail if next() or previous() have not yet been called.");
        } catch (IllegalStateException e) {
            // expected
        } catch (Throwable t) { // should never happen
            fail(t.toString());
        }

    }

}
