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
package org.apache.commons.collections.comparators;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.AbstractTestObject;

/**
 * Abstract test class for testing the Comparator interface.
 * <p>
 * Concrete subclasses declare the comparator to be tested.
 * They also declare certain aspects of the tests.
 * 
 * @author Stephen Colebourne
 */
public abstract class AbstractTestComparator extends AbstractTestObject {

    /**
     * JUnit constructor.
     * 
     * @param testName  the test class name
     */
    public AbstractTestComparator(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------
    /**
     * Implement this method to return the comparator to test.
     * 
     * @return the comparator to test
     */
    public abstract Comparator makeComparator();
    
    /**
     * Implement this method to return a list of sorted objects.
     * 
     * @return sorted objects
     */
    public abstract List getComparableObjectsOrdered();

    //-----------------------------------------------------------------------
    /**
     * Implements the abstract superclass method to return the comparator.
     * 
     * @return a full iterator
     */
    public Object makeObject() {
        return makeComparator();
    }

    /**
     * Overrides superclass to block tests.
     */
    public boolean supportsEmptyCollections() {
        return false;
    }

    /**
     * Overrides superclass to block tests.
     */
    public boolean supportsFullCollections() {
        return false;
    }

    /**
     * Overrides superclass to set the compatability to version 2
     * as there were no Comparators in version 1.x.
     */
    public String getCompatibilityVersion() {
        return "2";
    }

    //-----------------------------------------------------------------------
    /**
     * Reverse the list.
     */
    protected void reverseObjects(List list) {
        Collections.reverse(list);
    }

    /**
     * Randomize the list.
     */
    protected void randomizeObjects(List list) {
        Collections.shuffle(list);
    }

    /**
     * Sort the list.
     */
    protected void sortObjects(List list, Comparator comparator) {
        Collections.sort(list,comparator);

    }

    //-----------------------------------------------------------------------
    /**
     * Test sorting an empty list
     */
    public void testEmptyListSort() {
        List list = new LinkedList();
        sortObjects(list, makeComparator());

        List list2 = new LinkedList();
        
        assertTrue("Comparator cannot sort empty lists",
                   list2.equals(list));
    }

    /**
     * Test sorting a reversed list.
     */
    public void testReverseListSort() {
        Comparator comparator = makeComparator();

        List randomList = getComparableObjectsOrdered();
        reverseObjects(randomList);
        sortObjects(randomList,comparator);

        List orderedList = getComparableObjectsOrdered();

        assertTrue("Comparator did not reorder the List correctly",
                   orderedList.equals(randomList));

    }

    /**
     * Test sorting a random list.
     */
    public void testRandomListSort() {
        Comparator comparator = makeComparator();

        List randomList = getComparableObjectsOrdered();
        randomizeObjects(randomList);
        sortObjects(randomList,comparator);

        List orderedList = getComparableObjectsOrdered();

        /* debug 
        Iterator i = randomList.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }
        */

        assertTrue("Comparator did not reorder the List correctly",
                   orderedList.equals(randomList));

    }

    /**
     * Nearly all Comparators should be Serializable.
     */
    public void testComparatorIsSerializable() {
        Comparator comparator = makeComparator();
        assertTrue("This comparator should be Serializable.",
                   comparator instanceof Serializable);
    }

    public String getCanonicalComparatorName(Object object) {
        StringBuffer retval = new StringBuffer();
        retval.append("data/test/");
        String colName = object.getClass().getName();
        colName = colName.substring(colName.lastIndexOf(".")+1,colName.length());
        retval.append(colName);
        retval.append(".version");
        retval.append(getCompatibilityVersion());
        retval.append(".obj");
        return retval.toString();
    }

    /**
     * Compare the current serialized form of the Comparator
     * against the canonical version in CVS.
     */
    public void testComparatorCompatibility() throws IOException, ClassNotFoundException {
        if(!skipSerializedCanonicalTests()) {
            Comparator comparator = null;
    
            // test to make sure the canonical form has been preserved
            try {
                comparator = (Comparator) readExternalFormFromDisk(getCanonicalComparatorName(makeComparator()));
        	} catch (FileNotFoundException exception) {
    
                boolean autoCreateSerialized = false;
    
        	    if(autoCreateSerialized) {
    	          	comparator = makeComparator();
            		String fileName = getCanonicalComparatorName(comparator);
            		writeExternalFormToDisk((Serializable) comparator, fileName);
            		fail("Serialized form could not be found.  A serialized version " +
            		     "has now been written (and should be added to CVS): " + fileName);
                } else {
                    fail("The Serialized form could be located to test serialization " +
                        "compatibility: " + exception.getMessage());
                }
            }
    
            
            // make sure the canonical form produces the ordering we currently
            // expect
            List randomList = getComparableObjectsOrdered();
            reverseObjects(randomList);
            sortObjects(randomList,comparator);
    
            List orderedList = getComparableObjectsOrdered();
    
            assertTrue("Comparator did not reorder the List correctly",
                       orderedList.equals(randomList));
        }
    }

}
