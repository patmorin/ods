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
package org.apache.commons.collections.list;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;

/**
 * Extension of {@link TestList} for exercising the 
 * {@link PredicatedList} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestPredicatedList extends AbstractTestList {
    
    public TestPredicatedList(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestPredicatedList.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestPredicatedList.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
 //-------------------------------------------------------------------
    
    protected Predicate truePredicate = PredicateUtils.truePredicate();
    
    protected List decorateList(List list, Predicate predicate) {
        return PredicatedList.decorate(list, predicate);
    }
    
    public List makeEmptyList() {
        return decorateList(new ArrayList(), truePredicate);
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
    
    public List makeTestList() {
        return decorateList(new ArrayList(), testPredicate);
    }
    
    public void testIllegalAdd() {
        List list = makeTestList();
        Integer i = new Integer(3);
        try {
            list.add(i);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Collection shouldn't contain illegal element", 
         !list.contains(i));   
    }

    public void testIllegalAddAll() {
        List list = makeTestList();
        List elements = new ArrayList();
        elements.add("one");
        elements.add("two");
        elements.add(new Integer(3));
        elements.add("four");
        try {
            list.addAll(0,elements);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("List shouldn't contain illegal element", 
         !list.contains("one"));   
        assertTrue("List shouldn't contain illegal element", 
         !list.contains("two"));   
        assertTrue("List shouldn't contain illegal element", 
         !list.contains(new Integer(3)));   
        assertTrue("List shouldn't contain illegal element", 
         !list.contains("four"));   
    }
    
    public void testIllegalSet() {
        List list = makeTestList();
        try {
            list.set(0,new Integer(3));
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testLegalAddAll() {
        List list = makeTestList();
        list.add("zero");
        List elements = new ArrayList();
        elements.add("one");
        elements.add("two");
        elements.add("three");
        list.addAll(1,elements);
        assertTrue("List should contain legal element", 
         list.contains("zero"));   
        assertTrue("List should contain legal element", 
         list.contains("one"));   
        assertTrue("List should contain legal element", 
         list.contains("two"));   
        assertTrue("List should contain legal element", 
         list.contains("three"));   
    }       

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedList.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedList.fullCollection.version3.1.obj");
//    }

}
