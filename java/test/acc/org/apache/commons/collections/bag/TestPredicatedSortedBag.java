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

import java.util.Comparator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.SortedBag;

/**
 * Extension of {@link TestBag} for exercising the {@link PredicatedSortedBag}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestPredicatedSortedBag extends AbstractTestSortedBag {
    
    private SortedBag nullBag = null;
    
    public TestPredicatedSortedBag(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestPredicatedSortedBag.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestPredicatedSortedBag.class.getName()};
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
    
    protected SortedBag decorateBag(SortedBag bag, Predicate predicate) {
        return PredicatedSortedBag.decorate(bag, predicate);
    }
    
    public Bag makeBag() {
        return decorateBag(new TreeBag(), truePredicate);
    }
    
    protected Bag makeTestBag() {
        return decorateBag(new TreeBag(), stringPredicate());
    }
    
    //--------------------------------------------------------------------------
    
    public void testDecorate() {
        SortedBag bag = decorateBag(new TreeBag(), stringPredicate());
        SortedBag bag2 = ((PredicatedSortedBag) bag).getSortedBag();
        try {
            SortedBag bag3 = decorateBag(new TreeBag(), null);
            fail("Expecting IllegalArgumentException for null predicate");
        } catch (IllegalArgumentException e) {}
        try {
            SortedBag bag4 = decorateBag(nullBag, stringPredicate());
            fail("Expecting IllegalArgumentException for null bag");
        } catch (IllegalArgumentException e) {}
    }
    
    public void testSortOrder() {
        SortedBag bag = decorateBag(new TreeBag(), stringPredicate());
        String one = "one";
        String two = "two";
        String three = "three";
        bag.add(one);
        bag.add(two);
        bag.add(three);
        assertEquals("first element", bag.first(), one);
        assertEquals("last element", bag.last(), two); 
        Comparator c = bag.comparator();
        assertTrue("natural order, so comparator should be null", c == null);
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        Bag bag = makeBag();
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/PredicatedSortedBag.emptyCollection.version3.1.obj");
//        bag = makeBag();
//        bag.add("A");
//        bag.add("A");
//        bag.add("B");
//        bag.add("B");
//        bag.add("C");
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/PredicatedSortedBag.fullCollection.version3.1.obj");
//    }

}
