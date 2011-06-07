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
import java.util.Comparator;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.comparators.ComparableComparator;

/**
 * Unit test suite for {@link CollatingIterator}.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * @author Rodney Waldhoff
 */
public class TestCollatingIterator extends AbstractTestIterator {

    //------------------------------------------------------------ Conventional
    
    public TestCollatingIterator(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestCollatingIterator.class);
    }

    //--------------------------------------------------------------- Lifecycle

    private Comparator comparator = null;
    private ArrayList evens = null; 
    private ArrayList odds = null; 
    private ArrayList fib = null; 

    public void setUp() throws Exception {
        super.setUp();
        comparator = new ComparableComparator();
        evens = new ArrayList();
        odds = new ArrayList();
        for(int i=0;i<20;i++) {
            if(0 == i%2) {
                evens.add(new Integer(i));
            } else {
                odds.add(new Integer(i));
            }
        }
        fib = new ArrayList();
        fib.add(new Integer(1));
        fib.add(new Integer(1));
        fib.add(new Integer(2));
        fib.add(new Integer(3));
        fib.add(new Integer(5));
        fib.add(new Integer(8));
        fib.add(new Integer(13));
        fib.add(new Integer(21));
    }       

    //---------------------------------------------------- TestIterator Methods
    
    public Iterator makeEmptyIterator() {
        return new CollatingIterator(comparator);
    }

    public Iterator makeFullIterator() {
        CollatingIterator iter = new CollatingIterator(comparator);
        iter.addIterator(evens.iterator());
        iter.addIterator(odds.iterator());
        iter.addIterator(fib.iterator());
        return iter;
    }

    //------------------------------------------------------------------- Tests

    public void testGetSetComparator() {
        CollatingIterator iter = new CollatingIterator();
        assertNull(iter.getComparator());
        iter.setComparator(comparator);
        assertSame(comparator,iter.getComparator());
        iter.setComparator(null);
        assertNull(iter.getComparator());
    }

    public void testIterateEven() {
        CollatingIterator iter = new CollatingIterator(comparator);
        iter.addIterator(evens.iterator());
        for(int i=0;i<evens.size();i++) {
            assertTrue(iter.hasNext());
            assertEquals(evens.get(i),iter.next());
        }
        assertTrue(!iter.hasNext());
    }

    public void testIterateEvenOdd() {
        CollatingIterator iter = new CollatingIterator(comparator,evens.iterator(),odds.iterator());
        for(int i=0;i<20;i++) {
            assertTrue(iter.hasNext());
            assertEquals(new Integer(i),iter.next());
        }
        assertTrue(!iter.hasNext());
    }

    public void testIterateOddEven() {
        CollatingIterator iter = new CollatingIterator(comparator,odds.iterator(),evens.iterator());
        for(int i=0;i<20;i++) {
            assertTrue(iter.hasNext());
            assertEquals(new Integer(i),iter.next());
        }
        assertTrue(!iter.hasNext());
    }

    public void testIterateEvenEven() {
        CollatingIterator iter = new CollatingIterator(comparator);
        iter.addIterator(evens.iterator());
        iter.addIterator(evens.iterator());
        for(int i=0;i<evens.size();i++) {
            assertTrue(iter.hasNext());
            assertEquals(evens.get(i),iter.next());
            assertTrue(iter.hasNext());
            assertEquals(evens.get(i),iter.next());
        }
        assertTrue(!iter.hasNext());
    }


    public void testIterateFibEvenOdd() {
        CollatingIterator iter = new CollatingIterator(comparator);
        iter.addIterator(fib.iterator());
        iter.addIterator(evens.iterator());
        iter.addIterator(odds.iterator());
        
        assertEquals(new Integer(0),iter.next());  // even   0
        assertEquals(new Integer(1),iter.next());  // fib    1
        assertEquals(new Integer(1),iter.next());  // fib    1
        assertEquals(new Integer(1),iter.next());  // odd    1
        assertEquals(new Integer(2),iter.next());  // fib    2
        assertEquals(new Integer(2),iter.next());  // even   2
        assertEquals(new Integer(3),iter.next());  // fib    3
        assertEquals(new Integer(3),iter.next());  // odd    3
        assertEquals(new Integer(4),iter.next());  // even   4
        assertEquals(new Integer(5),iter.next());  // fib    5
        assertEquals(new Integer(5),iter.next());  // odd    5
        assertEquals(new Integer(6),iter.next());  // even   6
        assertEquals(new Integer(7),iter.next());  // odd    7
        assertEquals(new Integer(8),iter.next());  // fib    8
        assertEquals(new Integer(8),iter.next());  // even   8
        assertEquals(new Integer(9),iter.next());  // odd    9
        assertEquals(new Integer(10),iter.next()); // even  10
        assertEquals(new Integer(11),iter.next()); // odd   11
        assertEquals(new Integer(12),iter.next()); // even  12
        assertEquals(new Integer(13),iter.next()); // fib   13
        assertEquals(new Integer(13),iter.next()); // odd   13
        assertEquals(new Integer(14),iter.next()); // even  14
        assertEquals(new Integer(15),iter.next()); // odd   15
        assertEquals(new Integer(16),iter.next()); // even  16
        assertEquals(new Integer(17),iter.next()); // odd   17
        assertEquals(new Integer(18),iter.next()); // even  18
        assertEquals(new Integer(19),iter.next()); // odd   19
        assertEquals(new Integer(21),iter.next()); // fib   21

        assertTrue(!iter.hasNext());
    }

    public void testRemoveFromSingle() {
        CollatingIterator iter = new CollatingIterator(comparator);
        iter.addIterator(evens.iterator());
        int expectedSize = evens.size();
        while(iter.hasNext()) {
            Integer val = (Integer)(iter.next());
            if(val.intValue() % 4 == 0) {
                expectedSize--;
                iter.remove();
            }
        }
        assertEquals(expectedSize,evens.size());
    }

    public void testRemoveFromDouble() {
        CollatingIterator iter = new CollatingIterator(comparator);
        iter.addIterator(evens.iterator());
        iter.addIterator(odds.iterator());
        int expectedSize = evens.size() + odds.size();
        while(iter.hasNext()) {
            Integer val = (Integer)(iter.next());
            if(val.intValue() % 4 == 0 || val.intValue() % 3 == 0 ) {
                expectedSize--;
                iter.remove();
            }
        }
        assertEquals(expectedSize,(evens.size() + odds.size()));
    }   

}

