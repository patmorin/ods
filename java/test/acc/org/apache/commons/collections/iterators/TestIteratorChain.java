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
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Predicate;

/**
 * Tests the IteratorChain class.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author James Strachan
 * @author Mauricio S. Moura
 * @author Morgan Delagrange
 */
public class TestIteratorChain extends AbstractTestIterator {

    protected String[] testArray = {
        "One", "Two", "Three", "Four", "Five", "Six"
    };

    protected List list1 = null;
    protected List list2 = null;
    protected List list3 = null;

    public static Test suite() {
        return new TestSuite(TestIteratorChain.class);
    }

    public TestIteratorChain(String testName) {
        super(testName);
    }

    public void setUp() {
        list1 = new ArrayList();
        list1.add("One");
        list1.add("Two");
        list1.add("Three");
        list2 = new ArrayList();
        list2.add("Four");
        list3 = new ArrayList();
        list3.add("Five");
        list3.add("Six");        
    }

    public Iterator makeEmptyIterator() {
        ArrayList list = new ArrayList();
        return new IteratorChain(list.iterator());
    }

    public Iterator makeFullIterator() {
        IteratorChain chain = new IteratorChain();

        chain.addIterator(list1.iterator());
        chain.addIterator(list2.iterator());
        chain.addIterator(list3.iterator());
        return chain;
    }

    public void testIterator() {
        Iterator iter = (Iterator) makeFullIterator();
        for ( int i = 0; i < testArray.length; i++ ) {
            Object testValue = testArray[i];            
            Object iterValue = iter.next();

            assertEquals( "Iteration value is correct", testValue, iterValue );
        }

        assertTrue("Iterator should now be empty", ! iter.hasNext() );

        try {
            Object testValue = iter.next();
        } catch (Exception e) {
            assertTrue("NoSuchElementException must be thrown", 
                       e.getClass().equals((new NoSuchElementException()).getClass()));
        }
    }

    public void testRemoveFromFilteredIterator() {

        final Predicate myPredicate = new Predicate() {
            public boolean evaluate( Object object ) {
                Integer i = (Integer) object;
                if (i.compareTo(new Integer(4)) < 0)
                    return true;
                return false;
            }
        };

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        list1.add(new Integer(1));
        list1.add(new Integer(2));
        list2.add(new Integer(3));
        list2.add(new Integer(4)); // will be ignored by the predicate

        Iterator it1 = IteratorUtils.filteredIterator(list1.iterator(), myPredicate );
        Iterator it2 = IteratorUtils.filteredIterator(list2.iterator(), myPredicate );

        Iterator it = IteratorUtils.chainedIterator(it1, it2);
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        assertEquals( 0, list1.size() );
        assertEquals( 1, list2.size() );

    }
    
    public void testRemove() {
        Iterator iter = (Iterator) makeFullIterator();

        try {
            iter.remove();
            fail("Calling remove before the first call to next() should throw an exception");
        } catch (IllegalStateException e) {

        }

        for ( int i = 0; i < testArray.length; i++ ) {
            Object testValue = testArray[i];            
            Object iterValue = iter.next();

            assertEquals( "Iteration value is correct", testValue, iterValue );

            if (! iterValue.equals("Four")) {
                iter.remove();
            }
        }

        assertTrue("List is empty",list1.size() == 0);
        assertTrue("List is empty",list2.size() == 1);
        assertTrue("List is empty",list3.size() == 0);
    }

    public void testFirstIteratorIsEmptyBug() {
        List empty = new ArrayList();
        List notEmpty = new ArrayList();
        notEmpty.add("A");
        notEmpty.add("B");
        notEmpty.add("C");
        IteratorChain chain = new IteratorChain();
        chain.addIterator(empty.iterator());
        chain.addIterator(notEmpty.iterator());
        assertTrue("should have next",chain.hasNext());
        assertEquals("A",chain.next());
        assertTrue("should have next",chain.hasNext());
        assertEquals("B",chain.next());
        assertTrue("should have next",chain.hasNext());
        assertEquals("C",chain.next());
        assertTrue("should not have next",!chain.hasNext());
    }
    
    public void testEmptyChain() {
        IteratorChain chain = new IteratorChain();
        assertEquals(false, chain.hasNext());
        try {
            chain.next();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            chain.remove();
            fail();
        } catch (IllegalStateException ex) {}
    }
        
}
