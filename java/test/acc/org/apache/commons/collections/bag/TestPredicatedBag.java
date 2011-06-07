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
package org.apache.commons.collections.bag;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;

/**
 * Extension of {@link TestBag} for exercising the {@link PredicatedBag}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestPredicatedBag extends AbstractTestBag {
    
    public TestPredicatedBag(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestPredicatedBag.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestPredicatedBag.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
    //--------------------------------------------------------------------------

    protected Predicate stringPredicate() {
        return new Predicate() {
            public boolean evaluate(Object o) {
                return o instanceof String;
            }
        };
    }   
    
    protected Predicate truePredicate = PredicateUtils.truePredicate();
    
    protected Bag decorateBag(HashBag bag, Predicate predicate) {
        return PredicatedBag.decorate(bag, predicate);
    }

    public Bag makeBag() {
        return decorateBag(new HashBag(), truePredicate);
    }
    
    protected Bag makeTestBag() {
        return decorateBag(new HashBag(), stringPredicate());
    }
    
    //--------------------------------------------------------------------------

    public void testlegalAddRemove() {
        Bag bag = makeTestBag();
        assertEquals(0, bag.size());
        Object[] els = new Object[] {"1", "3", "5", "7", "2", "4", "1"};
        for (int i = 0; i < els.length; i++) {
            bag.add(els[i]);
            assertEquals(i + 1, bag.size());
            assertEquals(true, bag.contains(els[i]));
        }
        Set set = ((PredicatedBag) bag).uniqueSet();
        assertTrue("Unique set contains the first element",set.contains(els[0]));
        assertEquals(true, bag.remove(els[0])); 
        set = ((PredicatedBag) bag).uniqueSet();
        assertTrue("Unique set now does not contain the first element",
            !set.contains(els[0])); 
    }
 
    public void testIllegalAdd() {
        Bag bag = makeTestBag();
        Integer i = new Integer(3);
        try {
            bag.add(i);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Collection shouldn't contain illegal element", 
         !bag.contains(i));   
    }

    public void testIllegalDecorate() {
        HashBag elements = new HashBag();
        elements.add("one");
        elements.add("two");
        elements.add(new Integer(3));
        elements.add("four");
        try {
            Bag bag = decorateBag(elements, stringPredicate());
            fail("Bag contains an element that should fail the predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            Bag bag = decorateBag(new HashBag(), null);
            fail("Expectiing IllegalArgumentException for null predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }              
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        Bag bag = makeBag();
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/PredicatedBag.emptyCollection.version3.1.obj");
//        bag = makeBag();
//        bag.add("A");
//        bag.add("A");
//        bag.add("B");
//        bag.add("B");
//        bag.add("C");
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/PredicatedBag.fullCollection.version3.1.obj");
//    }

}
