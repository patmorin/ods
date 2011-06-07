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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.map.TestPredicatedSortedMap;

/**
 * Extension of {@link AbstractTestSortedSet} for exercising the 
 * {@link PredicatedSortedSet} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestPredicatedSortedSet extends AbstractTestSortedSet{
    
    public TestPredicatedSortedSet(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return BulkTest.makeSuite(TestPredicatedSortedSet.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestPredicatedSortedMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
 //-------------------------------------------------------------------    
    
    protected Predicate truePredicate = PredicateUtils.truePredicate();
    
    public Set makeEmptySet() {
        return PredicatedSortedSet.decorate(new TreeSet(), truePredicate);
    }
    
    public Set makeFullSet() {
        TreeSet set = new TreeSet();
        set.addAll(Arrays.asList(getFullElements()));
        return PredicatedSortedSet.decorate(set, truePredicate);
    }
   
    
//--------------------------------------------------------------------   
    protected Predicate testPredicate =  
        new Predicate() {
            public boolean evaluate(Object o) {
                return (o instanceof String) && (((String) o).startsWith("A"));
            }
        };      
     
    
    protected SortedSet makeTestSet() {
        return PredicatedSortedSet.decorate(new TreeSet(), testPredicate);
    }
    
    public void testGetSet() {
        SortedSet set = makeTestSet();
        assertTrue("returned set should not be null",
            ((PredicatedSortedSet) set).getSet() != null);
    }
    
    public void testIllegalAdd() {
        SortedSet set = makeTestSet();
        String testString = "B";
        try {
            set.add(testString);
            fail("Should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Collection shouldn't contain illegal element", 
         !set.contains(testString));   
    }

    public void testIllegalAddAll() {
        SortedSet set = makeTestSet();
        Set elements = new TreeSet();
        elements.add("Aone");
        elements.add("Atwo");
        elements.add("Bthree");
        elements.add("Afour");
        try {
            set.addAll(elements);
            fail("Should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Set shouldn't contain illegal element", 
         !set.contains("Aone"));   
        assertTrue("Set shouldn't contain illegal element", 
         !set.contains("Atwo"));   
        assertTrue("Set shouldn't contain illegal element", 
         !set.contains("Bthree"));   
        assertTrue("Set shouldn't contain illegal element", 
         !set.contains("Afour"));   
    }
    
    public void testComparator() {
        SortedSet set = makeTestSet();
        Comparator c = set.comparator();
        assertTrue("natural order, so comparator should be null", c == null);
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedSortedSet.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedSortedSet.fullCollection.version3.1.obj");
//    }

}
