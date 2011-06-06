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
import org.apache.commons.collections.SortedBag;

/**
 * Extension of {@link TestBag} for exercising the {@link TypedSortedBag}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestTypedSortedBag extends AbstractTestSortedBag {
       
    public TestTypedSortedBag(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestTypedSortedBag.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestTypedSortedBag.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
    //--------------------------------------------------------------------------
    
    protected Class stringClass = this.getName().getClass();
    private Object obj = new Object();
    protected Class objectClass = obj.getClass();
    protected SortedBag nullBag = null;
    
    protected SortedBag decorateBag(SortedBag bag, Class claz) {
        return TypedSortedBag.decorate(bag, claz);
    }

    public Bag makeBag() {
        return decorateBag(new TreeBag(), objectClass);
    }
    
    protected Bag makeTestBag() {
        return decorateBag(new TreeBag(), stringClass);
    }
    
    //--------------------------------------------------------------------------
    
    public void testDecorate() {
        SortedBag bag = decorateBag(new TreeBag(), stringClass);
        try {
            SortedBag bag3 = decorateBag(new TreeBag(), null);
            fail("Expecting IllegalArgumentException for null predicate");
        } catch (IllegalArgumentException e) {}
        try {
            SortedBag bag4 = decorateBag(nullBag, stringClass);
            fail("Expecting IllegalArgumentException for null bag");
        } catch (IllegalArgumentException e) {}
    }
    
    public void testSortOrder() {
        SortedBag bag = decorateBag(new TreeBag(), stringClass);
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

    protected boolean skipSerializedCanonicalTests() {
        return true;
    }

}
