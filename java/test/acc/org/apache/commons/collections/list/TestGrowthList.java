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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Extension of {@link TestList} for exercising the {@link GrowthList}.
 *
 * @since Commons Collections 3.2
 * @version $Revision: 155406 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestGrowthList extends AbstractTestList {

    public TestGrowthList(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestGrowthList.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestGrowthList.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public List makeEmptyList() {
        return new GrowthList();
    }

    public List makeFullList() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return GrowthList.decorate(list);
    }

    //-----------------------------------------------------------------------
    public void testGrowthAdd() {
        Integer one = new Integer(1);
        GrowthList grower = new GrowthList();
        assertEquals(0, grower.size());
        grower.add(1, one);
        assertEquals(2, grower.size());
        assertEquals(null, grower.get(0));
        assertEquals(one, grower.get(1));
    }

    public void testGrowthAddAll() {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        Collection coll = new ArrayList();
        coll.add(one);
        coll.add(two);
        GrowthList grower = new GrowthList();
        assertEquals(0, grower.size());
        grower.addAll(1, coll);
        assertEquals(3, grower.size());
        assertEquals(null, grower.get(0));
        assertEquals(one, grower.get(1));
        assertEquals(two, grower.get(2));
    }

    public void testGrowthSet1() {
        Integer one = new Integer(1);
        GrowthList grower = new GrowthList();
        assertEquals(0, grower.size());
        grower.set(1, one);
        assertEquals(2, grower.size());
        assertEquals(null, grower.get(0));
        assertEquals(one, grower.get(1));
    }

    public void testGrowthSet2() {
        Integer one = new Integer(1);
        GrowthList grower = new GrowthList();
        assertEquals(0, grower.size());
        grower.set(0, one);
        assertEquals(1, grower.size());
        assertEquals(one, grower.get(0));
    }

    //-----------------------------------------------------------------------
    /**
     * Override.
     */
    public void testListAddByIndexBoundsChecking() {
        List list;
        Object element = getOtherElements()[0];
        try {
            list = makeEmptyList();
            list.add(-1, element);
            fail("List.add should throw IndexOutOfBoundsException [-1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     * Override.
     */
    public void testListAddByIndexBoundsChecking2() {
        List list;
        Object element = getOtherElements()[0];
        try {
            list = makeFullList();
            list.add(-1, element);
            fail("List.add should throw IndexOutOfBoundsException [-1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     * Override.
     */
    public void testListSetByIndexBoundsChecking() {
        List list = makeEmptyList();
        Object element = getOtherElements()[0];
        try {
            list.set(-1, element);
            fail("List.set should throw IndexOutOfBoundsException [-1]");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     * Override.
     */
    public void testListSetByIndexBoundsChecking2() {
        List list = makeFullList();
        Object element = getOtherElements()[0];
        try {
            list.set(-1, element);
            fail("List.set should throw IndexOutOfBoundsException [-1]");
        } catch(IndexOutOfBoundsException e) {
            // expected
        } 
    }

    //-----------------------------------------------------------------------
    public String getCompatibilityVersion() {
        return "3.2";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "C:/commons/collections/data/test/GrowthList.emptyCollection.version3.2.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "C:/commons/collections/data/test/GrowthList.fullCollection.version3.2.obj");
//    }

}
