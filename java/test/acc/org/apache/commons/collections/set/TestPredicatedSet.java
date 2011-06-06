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
package org.apache.commons.collections.set;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;

/**
 * Extension of {@link TestSet} for exercising the 
 * {@link PredicatedSet} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestPredicatedSet extends AbstractTestSet{
    
    public TestPredicatedSet(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestPredicatedSet.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestPredicatedSet.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
 //-------------------------------------------------------------------
    
    protected Predicate truePredicate = PredicateUtils.truePredicate();
    
    protected Set decorateSet(Set set, Predicate predicate) {
        return PredicatedSet.decorate(set, predicate);
    }
    
    public Set makeEmptySet() {
        return decorateSet(new HashSet(), truePredicate);
    }
    
    public Object[] getFullElements() {
        return new Object[] {"1", "3", "5", "7", "2", "4", "6"};
    }
    
//--------------------------------------------------------------------   
    
    protected Predicate testPredicate =  
        new Predicate() {
            public boolean evaluate(Object o) {
                return o instanceof String;
            }
        };      
    
    protected Set makeTestSet() {
        return decorateSet(new HashSet(), testPredicate);
    }
    
    public void testGetSet() {
         Set set = makeTestSet();
        assertTrue("returned set should not be null",
            ((PredicatedSet) set).getSet() != null);
    }
    
    public void testIllegalAdd() {
        Set set = makeTestSet();
        Integer i = new Integer(3);
        try {
            set.add(i);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Collection shouldn't contain illegal element", 
         !set.contains(i));   
    }

    public void testIllegalAddAll() {
        Set set = makeTestSet();
        Set elements = new HashSet();
        elements.add("one");
        elements.add("two");
        elements.add(new Integer(3));
        elements.add("four");
        try {
            set.addAll(elements);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Set shouldn't contain illegal element", 
         !set.contains("one"));   
        assertTrue("Set shouldn't contain illegal element", 
         !set.contains("two"));   
        assertTrue("Set shouldn't contain illegal element", 
         !set.contains(new Integer(3)));   
        assertTrue("Set shouldn't contain illegal element", 
         !set.contains("four"));   
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedSet.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedSet.fullCollection.version3.1.obj");
//    }

}