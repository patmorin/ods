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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.functors.TruePredicate;

/**
 * Test the filter iterator.
 *
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Jan Sorensen
 * @author Ralph Wagner
 * @author Huw Roberts
 */
public class TestFilterIterator extends AbstractTestIterator {

    /** Creates new TestFilterIterator */
    public TestFilterIterator(String name) {
        super(name);
    }

    private String[] array;
    private List list;
    private FilterIterator iterator;
    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
        array = new String[] { "a", "b", "c" };
        initIterator();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() throws Exception {
        iterator = null;
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(TestFilterIterator.class));
    }

    /**
     * Returns an full iterator wrapped in a
     * FilterIterator that blocks all the elements
     * 
     * @return "empty" FilterIterator
     */
    public Iterator makeEmptyIterator() {
        return makeBlockAllFilter(new ArrayIterator(array));
    }

    /**
     * Returns an array with elements wrapped in a pass-through
     * FilterIterator
     * 
     * @return 
     */
    public Iterator makeFullIterator() {
        array = new String[] { "a", "b", "c" };
        list = new ArrayList(Arrays.asList(array));
        return makePassThroughFilter(list.iterator());
    }

    public void testRepeatedHasNext() {
        for (int i = 0; i <= array.length; i++) {
            assertTrue(iterator.hasNext());
        }
    }

    public void testRepeatedNext() {
        for (int i = 0; i < array.length; i++)
            iterator.next();
        verifyNoMoreElements();
    }

    public void testReturnValues() {
        verifyElementsInPredicate(new String[0]);
        verifyElementsInPredicate(new String[] { "a" });
        verifyElementsInPredicate(new String[] { "b" });
        verifyElementsInPredicate(new String[] { "c" });
        verifyElementsInPredicate(new String[] { "a", "b" });
        verifyElementsInPredicate(new String[] { "a", "c" });
        verifyElementsInPredicate(new String[] { "b", "c" });
        verifyElementsInPredicate(new String[] { "a", "b", "c" });
    }

    /**
     * Test that when the iterator is changed, the hasNext method returns the
     * correct response for the new iterator.
     */
    public void testSetIterator() {
        Iterator iter1 = Collections.singleton(new Object()).iterator();
        Iterator iter2 = Collections.EMPTY_LIST.iterator();
        
        FilterIterator filterIterator = new FilterIterator(iter1);
        filterIterator.setPredicate(TruePredicate.getInstance());
        // this iterator has elements
        assertEquals(true, filterIterator.hasNext());
        
        // this iterator has no elements
        filterIterator.setIterator(iter2);
        assertEquals(false, filterIterator.hasNext());
    }

    /**
     * Test that when the predicate is changed, the hasNext method returns the
     * correct response for the new predicate.
     */
    public void testSetPredicate() {
        Iterator iter = Collections.singleton(null).iterator();

        FilterIterator filterIterator = new FilterIterator(iter);
        filterIterator.setPredicate(TruePredicate.getInstance());
        // this predicate matches
        assertEquals(true, filterIterator.hasNext());
        
        // this predicate doesn't match
        filterIterator.setPredicate(NotNullPredicate.getInstance());
        assertEquals(false, filterIterator.hasNext());
    }

    private void verifyNoMoreElements() {
        assertTrue(!iterator.hasNext());
        try {
            iterator.next();
            fail("NoSuchElementException expected");
        }
        catch (NoSuchElementException e) {
            // success
        }
    }

    private void verifyElementsInPredicate(final String[] elements) {
        Predicate pred = new Predicate() {
            public boolean evaluate(Object x) {
                for (int i = 0; i < elements.length; i++)
                    if (elements[i].equals(x))
                        return true;
                return false;
            }
        };
        initIterator();
        iterator.setPredicate(pred);
        for (int i = 0; i < elements.length; i++) {
            String s = (String)iterator.next();
            assertEquals(elements[i], s);
            assertTrue(i == elements.length - 1 ? !iterator.hasNext() : iterator.hasNext());
        }
        verifyNoMoreElements();

        // test removal
        initIterator();
        iterator.setPredicate(pred);
        if (iterator.hasNext()) {
            Object last = iterator.next();
            iterator.remove();
            assertTrue("Base of FilterIterator still contains removed element.", !list.contains(last));
        }
    }

    private void initIterator() {
        iterator = (FilterIterator) makeFullIterator();
    }

    /**
     * Returns a FilterIterator that does not filter
     * any of its elements
     * 
     * @param i      the Iterator to "filter"
     * @return "filtered" iterator
     */
    protected FilterIterator makePassThroughFilter(Iterator i) {
        Predicate pred = new Predicate() {
                public boolean evaluate(Object x) { return true; }
        };
        return new FilterIterator(i,pred);
    }

    /**
     * Returns a FilterIterator that blocks
     * all of its elements
     * 
     * @param i      the Iterator to "filter"
     * @return "filtered" iterator
     */
    protected FilterIterator makeBlockAllFilter(Iterator i) {
        Predicate pred = new Predicate() {
                public boolean evaluate(Object x) { return false; }
        };
        return new FilterIterator(i,pred);
    }
}

