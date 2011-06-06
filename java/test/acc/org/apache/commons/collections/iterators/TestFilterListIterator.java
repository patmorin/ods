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
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.Predicate;

/**
 * Tests the FilterListIterator class.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Rodney Waldhoff
 */
public class TestFilterListIterator extends TestCase {
    public TestFilterListIterator(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestFilterListIterator.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestFilterListIterator.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    private ArrayList list = null;
    private ArrayList odds = null;
    private ArrayList evens = null;
    private ArrayList threes = null;
    private ArrayList fours = null;
    private ArrayList sixes = null;
    private Predicate truePred = null;
    private Predicate falsePred = null;
    private Predicate evenPred = null;
    private Predicate oddPred = null;
    private Predicate threePred = null;
    private Predicate fourPred = null;
    private Random random = new Random();

    public void setUp() {
        list = new ArrayList();
        odds = new ArrayList();
        evens = new ArrayList();
        threes = new ArrayList();
        fours = new ArrayList();
        sixes = new ArrayList();
        for(int i=0;i<20;i++) {
            list.add(new Integer(i));
            if(i%2 == 0) { evens.add(new Integer(i)); }
            if(i%2 == 1) { odds.add(new Integer(i)); }
            if(i%3 == 0) { threes.add(new Integer(i)); }
            if(i%4 == 0) { fours.add(new Integer(i)); }
            if(i%6 == 0) { sixes.add(new Integer(i)); }
        }

        truePred = new Predicate() {
            public boolean evaluate(Object x) { 
                return true;
            }
        };

        falsePred = new Predicate() {
            public boolean evaluate(Object x) { 
                return true;
            }
        };

        evenPred = new Predicate() {
            public boolean evaluate(Object x) { 
                return (((Integer)x).intValue()%2 == 0);
            }
        };

        oddPred = new Predicate() {
            public boolean evaluate(Object x) { 
                return (((Integer)x).intValue()%2 == 1);
            }
        };

        threePred = new Predicate() {
            public boolean evaluate(Object x) { 
                return (((Integer)x).intValue()%3 == 0);
            }
        };

        fourPred = new Predicate() {
            public boolean evaluate(Object x) { 
                return (((Integer)x).intValue()%4 == 0);
            }
        };

    }

    public void tearDown() throws Exception {
        list = null;
        odds = null;
        evens = null;
        threes = null;
        fours = null;
        sixes = null;
        truePred = null;
        falsePred = null;
        evenPred = null;
        oddPred = null;
        threePred = null;
        fourPred = null;
    }

    public void testWalkLists() {
        // this just confirms that our walkLists method works OK
        walkLists(list,list.listIterator());
    }

    public void testManual() {
        // do this one "by hand" as a sanity check
        FilterListIterator filtered = new FilterListIterator(list.listIterator(),threePred);
        
        assertEquals(new Integer(0),filtered.next());
        assertEquals(new Integer(3),filtered.next());
        assertEquals(new Integer(6),filtered.next());
        assertEquals(new Integer(9),filtered.next());
        assertEquals(new Integer(12),filtered.next());
        assertEquals(new Integer(15),filtered.next());
        assertEquals(new Integer(18),filtered.next());

        assertEquals(new Integer(18),filtered.previous());
        assertEquals(new Integer(15),filtered.previous());
        assertEquals(new Integer(12),filtered.previous());
        assertEquals(new Integer(9),filtered.previous());
        assertEquals(new Integer(6),filtered.previous());
        assertEquals(new Integer(3),filtered.previous());
        assertEquals(new Integer(0),filtered.previous());
    
        assertTrue(!filtered.hasPrevious());

        assertEquals(new Integer(0),filtered.next());
        assertEquals(new Integer(3),filtered.next());
        assertEquals(new Integer(6),filtered.next());
        assertEquals(new Integer(9),filtered.next());
        assertEquals(new Integer(12),filtered.next());
        assertEquals(new Integer(15),filtered.next());
        assertEquals(new Integer(18),filtered.next());

        assertTrue(!filtered.hasNext());

        assertEquals(new Integer(18),filtered.previous());
        assertEquals(new Integer(15),filtered.previous());
        assertEquals(new Integer(12),filtered.previous());
        assertEquals(new Integer(9),filtered.previous());
        assertEquals(new Integer(6),filtered.previous());
        assertEquals(new Integer(3),filtered.previous());
        assertEquals(new Integer(0),filtered.previous());

        assertEquals(new Integer(0),filtered.next());
        assertEquals(new Integer(0),filtered.previous());
        assertEquals(new Integer(0),filtered.next());
        
        assertEquals(new Integer(3),filtered.next());
        assertEquals(new Integer(6),filtered.next());
        assertEquals(new Integer(6),filtered.previous());
        assertEquals(new Integer(3),filtered.previous());
        assertEquals(new Integer(3),filtered.next());
        assertEquals(new Integer(6),filtered.next());

        assertEquals(new Integer(9),filtered.next());
        assertEquals(new Integer(12),filtered.next());
        assertEquals(new Integer(15),filtered.next());
        assertEquals(new Integer(15),filtered.previous());
        assertEquals(new Integer(12),filtered.previous());
        assertEquals(new Integer(9),filtered.previous());

    }

    public void testTruePredicate() {
        FilterListIterator filtered = new FilterListIterator(list.listIterator(),truePred);
        walkLists(list,filtered);
    }
    
    public void testFalsePredicate() {
        FilterListIterator filtered = new FilterListIterator(list.listIterator(),falsePred);
        walkLists(new ArrayList(),filtered);
    }

    public void testEvens() {
        FilterListIterator filtered = new FilterListIterator(list.listIterator(),evenPred);
        walkLists(evens,filtered);
    }
    
    public void testOdds() {
        FilterListIterator filtered = new FilterListIterator(list.listIterator(),oddPred);
        walkLists(odds,filtered);
    }

    public void testThrees() {
        FilterListIterator filtered = new FilterListIterator(list.listIterator(),threePred);
        walkLists(threes,filtered);
    }

    public void testFours() {
        FilterListIterator filtered = new FilterListIterator(list.listIterator(),fourPred);
        walkLists(fours,filtered);
    }

    public void testNestedSixes() {
        FilterListIterator filtered = new FilterListIterator(
                                        new FilterListIterator(list.listIterator(),threePred),
                                        evenPred
                                      );
        walkLists(sixes,filtered);
    }

    public void testNestedSixes2() {
        FilterListIterator filtered = new FilterListIterator(
                                        new FilterListIterator(list.listIterator(),evenPred),
                                        threePred
                                      );
        walkLists(sixes,filtered);
    }

    public void testNestedSixes3() {        
        FilterListIterator filtered = new FilterListIterator(
                                        new FilterListIterator(list.listIterator(),threePred),
                                        evenPred
                                      );
        walkLists(sixes,new FilterListIterator(filtered,truePred));
    }

    public void testNextChangesPrevious() {
        {
            FilterListIterator filtered = new FilterListIterator(list.listIterator(),threePred);
            nextNextPrevious(threes.listIterator(),filtered);
        }
    
        {
            FilterListIterator filtered = new FilterListIterator(list.listIterator(),truePred);
            nextNextPrevious(list.listIterator(),filtered);
        }
    }

    public void testPreviousChangesNext() {
        {
            FilterListIterator filtered = new FilterListIterator(list.listIterator(),threePred);
            ListIterator expected = threes.listIterator();
            walkForward(expected,filtered);
            previousPreviousNext(expected,filtered);
        }
        {
            FilterListIterator filtered = new FilterListIterator(list.listIterator(),truePred);
            ListIterator expected = list.listIterator();
            walkForward(expected,filtered);
            previousPreviousNext(expected,filtered);
        }
    }

    public void testFailingHasNextBug() {
        FilterListIterator filtered = new FilterListIterator(list.listIterator(),fourPred);
        ListIterator expected = fours.listIterator();
        while(expected.hasNext()) {
            expected.next();
            filtered.next();
        }
        assertTrue(filtered.hasPrevious());
        assertTrue(!filtered.hasNext());
        assertEquals(expected.previous(),filtered.previous());
    }

    // Utilities

    private void walkForward(ListIterator expected, ListIterator testing) {
        while(expected.hasNext()) {
            assertEquals(expected.nextIndex(),testing.nextIndex());
            assertEquals(expected.previousIndex(),testing.previousIndex());
            assertTrue(testing.hasNext());
            assertEquals(expected.next(),testing.next());
        }
    }

    private void walkBackward(ListIterator expected, ListIterator testing) {
        while(expected.hasPrevious()) {
            assertEquals(expected.nextIndex(),testing.nextIndex());
            assertEquals(expected.previousIndex(),testing.previousIndex());
            assertTrue(testing.hasPrevious());
            assertEquals(expected.previous(),testing.previous());
        }
    }

    private void nextNextPrevious(ListIterator expected, ListIterator testing) {
        // calls to next() should change the value returned by previous()
        // even after previous() has been set by a call to hasPrevious()
        assertEquals(expected.next(),testing.next());
        assertEquals(expected.hasPrevious(),testing.hasPrevious());
        Object expecteda = expected.next();
        Object testinga = testing.next();
        assertEquals(expecteda,testinga);
        Object expectedb = expected.previous();
        Object testingb = testing.previous();
        assertEquals(expecteda,expectedb);
        assertEquals(testinga,testingb);
    }

    private void previousPreviousNext(ListIterator expected, ListIterator testing) {
        // calls to previous() should change the value returned by next()
        // even after next() has been set by a call to hasNext()
        assertEquals(expected.previous(),testing.previous());
        assertEquals(expected.hasNext(),testing.hasNext());
        Object expecteda = expected.previous();
        Object testinga = testing.previous();
        assertEquals(expecteda,testinga);
        Object expectedb = expected.next();
        Object testingb = testing.next();
        assertEquals(expecteda,testingb);
        assertEquals(expecteda,expectedb);
        assertEquals(testinga,testingb);
    }

    private void walkLists(List list, ListIterator testing) {
        ListIterator expected = list.listIterator();

        // walk all the way forward
        walkForward(expected,testing);

        // walk all the way back
        walkBackward(expected,testing);

        // forward,back,forward
        while(expected.hasNext()) {
            assertEquals(expected.nextIndex(),testing.nextIndex());
            assertEquals(expected.previousIndex(),testing.previousIndex());
            assertTrue(testing.hasNext());
            assertEquals(expected.next(),testing.next());
            assertTrue(testing.hasPrevious());
            assertEquals(expected.previous(),testing.previous());
            assertTrue(testing.hasNext());
            assertEquals(expected.next(),testing.next());
        }


        // walk all the way back
        walkBackward(expected,testing);

        for(int i=0;i<list.size();i++) {
            // walk forward i
            for(int j=0;j<i;j++) {
                assertEquals(expected.nextIndex(),testing.nextIndex());
                assertEquals(expected.previousIndex(),testing.previousIndex());
                assertTrue(expected.hasNext()); // if this one fails we've got a logic error in the test
                assertTrue(testing.hasNext());
                assertEquals(expected.next(),testing.next());
            }
            // walk back i/2
            for(int j=0;j<i/2;j++) {
                assertEquals(expected.nextIndex(),testing.nextIndex());
                assertEquals(expected.previousIndex(),testing.previousIndex());
                assertTrue(expected.hasPrevious()); // if this one fails we've got a logic error in the test
                assertTrue(testing.hasPrevious());
                assertEquals(expected.previous(),testing.previous());
            }
            // walk forward i/2
            for(int j=0;j<i/2;j++) {
                assertEquals(expected.nextIndex(),testing.nextIndex());
                assertEquals(expected.previousIndex(),testing.previousIndex());
                assertTrue(expected.hasNext()); // if this one fails we've got a logic error in the test
                assertTrue(testing.hasNext());
                assertEquals(expected.next(),testing.next());
            }
            // walk back i
            for(int j=0;j<i;j++) {
                assertEquals(expected.nextIndex(),testing.nextIndex());
                assertEquals(expected.previousIndex(),testing.previousIndex());
                assertTrue(expected.hasPrevious()); // if this one fails we've got a logic error in the test
                assertTrue(testing.hasPrevious());
                assertEquals(expected.previous(),testing.previous());
            }
        }

        // random walk
        StringBuffer walkdescr = new StringBuffer(500);
        for(int i=0;i<500;i++) {
            if(random.nextBoolean()) {
                // step forward
                walkdescr.append("+");
                if(expected.hasNext()) {
                    assertEquals(walkdescr.toString(),expected.next(),testing.next());
                }
            } else {
                // step backward
                walkdescr.append("-");
                if(expected.hasPrevious()) {
                    assertEquals(walkdescr.toString(),expected.previous(),testing.previous());
                }
            }
            assertEquals(walkdescr.toString(),expected.nextIndex(),testing.nextIndex());
            assertEquals(walkdescr.toString(),expected.previousIndex(),testing.previousIndex());
        }

    }

}
