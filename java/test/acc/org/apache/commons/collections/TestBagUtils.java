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

import junit.framework.Test;

import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.collections.bag.PredicatedBag;
import org.apache.commons.collections.bag.PredicatedSortedBag;
import org.apache.commons.collections.bag.SynchronizedBag;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.collections.bag.TransformedBag;
import org.apache.commons.collections.bag.TransformedSortedBag;
import org.apache.commons.collections.bag.TreeBag;
import org.apache.commons.collections.bag.UnmodifiableBag;
import org.apache.commons.collections.bag.UnmodifiableSortedBag;

/**
 * Tests for BagUtils factory methods.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Phil Steitz
 */
public class TestBagUtils extends BulkTest {

    public TestBagUtils(String name) {
        super(name);
    }


    public static Test suite() {
        return BulkTest.makeSuite(TestBagUtils.class);
    }
    
    //----------------------------------------------------------------------

    protected Class stringClass = this.getName().getClass();
    protected Predicate truePredicate = PredicateUtils.truePredicate();
    protected Transformer nopTransformer = TransformerUtils.nopTransformer();
    
    //----------------------------------------------------------------------
    
    public void testSynchronizedBag() {
        Bag bag = BagUtils.synchronizedBag(new HashBag());
        assertTrue("Returned object should be a SynchronizedBag.",
            bag instanceof SynchronizedBag);
        try {
            bag = BagUtils.synchronizedBag(null);
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
    public void testUnmodifiableBag() {
        Bag bag = BagUtils.unmodifiableBag(new HashBag());
        assertTrue("Returned object should be an UnmodifiableBag.",
            bag instanceof UnmodifiableBag);
        try {
            bag = BagUtils.unmodifiableBag(null);
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
    public void testPredicatedBag() {
        Bag bag = BagUtils.predicatedBag(new HashBag(), truePredicate);
        assertTrue("Returned object should be a PredicatedBag.",
            bag instanceof PredicatedBag);
        try {
            bag = BagUtils.predicatedBag(null,truePredicate);
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        } 
        try {
            bag = BagUtils.predicatedBag(new HashBag(), null);
            fail("Expecting IllegalArgumentException for null predicate.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
    public void testTypedBag() {
        Bag bag = BagUtils.typedBag(new HashBag(), stringClass);      
        assertTrue("Returned object should be a TypedBag.",
            bag instanceof PredicatedBag);
        try {
            bag = BagUtils.typedBag(null, stringClass);
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        } 
        try {
            bag = BagUtils.typedBag(new HashBag(), null);
            fail("Expecting IllegalArgumentException for null type.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
     public void testTransformedBag() {
        Bag bag = BagUtils.transformedBag(new HashBag(), nopTransformer);      
        assertTrue("Returned object should be an TransformedBag.",
            bag instanceof TransformedBag);
        try {
            bag = BagUtils.transformedBag(null, nopTransformer);      
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        } 
        try {
            bag = BagUtils.transformedBag(new HashBag(), null);  
            fail("Expecting IllegalArgumentException for null transformer.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
     
    public void testSynchronizedSortedBag() {
        Bag bag = BagUtils.synchronizedSortedBag(new TreeBag());
        assertTrue("Returned object should be a SynchronizedSortedBag.",
            bag instanceof SynchronizedSortedBag);
        try {
            bag = BagUtils.synchronizedSortedBag(null);
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
    public void testUnmodifiableSortedBag() {
        Bag bag = BagUtils.unmodifiableSortedBag(new TreeBag());
        assertTrue("Returned object should be an UnmodifiableSortedBag.",
            bag instanceof UnmodifiableSortedBag);
        try {
            bag = BagUtils.unmodifiableSortedBag(null);
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
    public void testPredicatedSortedBag() {
        Bag bag = BagUtils.predicatedSortedBag(new TreeBag(), truePredicate);
        assertTrue("Returned object should be a PredicatedSortedBag.",
            bag instanceof PredicatedSortedBag);
        try {
            bag = BagUtils.predicatedSortedBag(null, truePredicate);
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        } 
        try {
            bag = BagUtils.predicatedSortedBag(new TreeBag(), null);
            fail("Expecting IllegalArgumentException for null predicate.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
    public void testTypedSortedBag() {
        Bag bag = BagUtils.typedSortedBag(new TreeBag(), stringClass);      
        assertTrue("Returned object should be a TypedSortedBag.",
            bag instanceof PredicatedSortedBag);
        try {
            bag = BagUtils.typedSortedBag(null, stringClass);
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        } 
        try {
            bag = BagUtils.typedSortedBag(new TreeBag(), null);
            fail("Expecting IllegalArgumentException for null type.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
    
    public void testTransformedSortedBag() {
        Bag bag = BagUtils.transformedSortedBag(new TreeBag(), nopTransformer);      
        assertTrue("Returned object should be an TransformedSortedBag",
            bag instanceof TransformedSortedBag);
        try {
            bag = BagUtils.transformedSortedBag(null, nopTransformer);      
            fail("Expecting IllegalArgumentException for null bag.");
        } catch (IllegalArgumentException ex) {
            // expected
        } 
        try {
            bag = BagUtils.transformedSortedBag(new TreeBag(), null);  
            fail("Expecting IllegalArgumentException for null transformer.");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
    }
}


