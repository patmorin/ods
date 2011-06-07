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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;

/**
 * Extension of {@link AbstractTestSortedSet} for exercising the 
 * {@link UnmodifiableSortedSet} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestUnmodifiableSortedSet extends AbstractTestSortedSet{
    
    public TestUnmodifiableSortedSet(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return BulkTest.makeSuite(TestUnmodifiableSortedSet.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestUnmodifiableSortedSet.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
    //-------------------------------------------------------------------  
    public Set makeEmptySet() {
        return UnmodifiableSortedSet.decorate(new TreeSet());
    }
    
    public Set makeFullSet() {
        TreeSet set = new TreeSet();
        set.addAll(Arrays.asList(getFullElements()));
        return UnmodifiableSortedSet.decorate(set);
    }
    
    public boolean isAddSupported() {
        return false;
    }
    
    public boolean isRemoveSupported() {
        return false;
    }
           
    //--------------------------------------------------------------------
    protected UnmodifiableSortedSet set = null;
    protected ArrayList array = null;
    
    protected void setupSet() {
        set = (UnmodifiableSortedSet) makeFullSet();
        array = new ArrayList();
        array.add(new Integer(1));
    }
    
    /** 
     * Verify that base set and subsets are not modifiable
     */
    public void testUnmodifiable() {
        setupSet();
        verifyUnmodifiable(set);
        verifyUnmodifiable(set.headSet(new Integer(1)));
        verifyUnmodifiable(set.tailSet(new Integer(1)));
        verifyUnmodifiable(set.subSet(new Integer(1), new Integer(3)));    
    }
    
    /**
     * Verifies that a set is not modifiable
     */
    public void verifyUnmodifiable(Set set) {
        try {
            set.add("value");
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // expected  
        }
        try {
            set.addAll(new TreeSet());
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        try {
            set.clear();
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        try {
            set.remove("x");
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        try {
            set.removeAll(array);
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        try {
            set.retainAll(array);
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    public void testComparator() {
        setupSet();
        Comparator c = set.comparator();
        assertTrue("natural order, so comparator should be null", c == null);
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/UnmodifiableSortedSet.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/UnmodifiableSortedSet.fullCollection.version3.1.obj");
//    }

}
