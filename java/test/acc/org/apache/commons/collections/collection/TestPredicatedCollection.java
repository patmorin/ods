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
package org.apache.commons.collections.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;

/**
 * Extension of {@link TestCollection} for exercising the 
 * {@link PredicatedCollection} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestPredicatedCollection extends AbstractTestCollection {

    public TestPredicatedCollection(String name) {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(TestPredicatedCollection.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestPredicatedCollection.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
 
   //------------------------------------------------------------------------
        
    protected Predicate truePredicate = PredicateUtils.truePredicate();
    
    protected Collection decorateCollection(Collection collection, 
        Predicate predicate) {
        return PredicatedCollection.decorate(collection, predicate);
    }
    
    public Collection makeCollection() {
        return decorateCollection(new ArrayList(), truePredicate);
    }
    
    public Collection makeConfirmedCollection() {
        return new ArrayList();
    }
    
    public Object[] getFullElements() {
        return new Object[] {"1", "3", "5", "7", "2", "4", "6"};
    }
    
    public Collection makeFullCollection() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return decorateCollection(list, truePredicate);
    }
    
    public Collection makeConfirmedFullCollection() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

 //-----------------------------------------------------------------
    protected Predicate testPredicate =  
        new Predicate() {
            public boolean evaluate(Object o) {
                return o instanceof String;
            }
        };
    
    public Collection makeTestCollection() {
        return decorateCollection(new ArrayList(), testPredicate);
    }
     
    public void testIllegalAdd() {
        Collection c = makeTestCollection();
        Integer i = new Integer(3);
        try {
            c.add(i);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Collection shouldn't contain illegal element", 
         !c.contains(i));   
    }

    public void testIllegalAddAll() {
        Collection c = makeTestCollection();
        List elements = new ArrayList();
        elements.add("one");
        elements.add("two");
        elements.add(new Integer(3));
        elements.add("four");
        try {
            c.addAll(elements);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Collection shouldn't contain illegal element", 
         !c.contains("one"));   
        assertTrue("Collection shouldn't contain illegal element", 
         !c.contains("two"));   
        assertTrue("Collection shouldn't contain illegal element", 
         !c.contains(new Integer(3)));   
        assertTrue("Collection shouldn't contain illegal element", 
         !c.contains("four"));   
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedCollection.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedCollection.fullCollection.version3.1.obj");
//    }

}
