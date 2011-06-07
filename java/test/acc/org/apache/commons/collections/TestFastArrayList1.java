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

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;

/**
 * Test FastArrayList implementation in <strong>fast</strong> mode.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Jason van Zyl
 */
public class TestFastArrayList1 extends TestFastArrayList {
    
    public TestFastArrayList1(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestFastArrayList1.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestFastArrayList1.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public void setUp() {
        list = (ArrayList) makeEmptyList();
    }

    public List makeEmptyList() {
        FastArrayList fal = new FastArrayList();
        fal.setFast(true);
        return (fal);
    }

    public void testIterateModify1() {
        List list = makeEmptyList();
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals(3, list.size());
        
        Iterator it = list.iterator();
        assertEquals("A", it.next());
        assertEquals(3, list.size());
        list.add(1, "Z");
        assertEquals(4, list.size());
        assertEquals("B", it.next());
        assertEquals("C", it.next());
        assertEquals(false, it.hasNext());
    }

    public void testIterateModify2() {
        List list = makeEmptyList();
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals(3, list.size());
        
        ListIterator it = list.listIterator();
        assertEquals("A", it.next());
        it.add("M");  // change via Iterator interface
        assertEquals(4, list.size());
        list.add(2, "Z");  // change via List interface
        assertEquals(5, list.size());
        assertEquals("B", it.next());
        try {
            it.set("N"); // fails as previously changed via List interface
            fail();
        } catch (ConcurrentModificationException ex) {}
        try {
            it.remove();
            fail();
        } catch (ConcurrentModificationException ex) {}
        try {
            it.add("N");
            fail();
        } catch (ConcurrentModificationException ex) {}
        assertEquals("C", it.next());
        assertEquals(false, it.hasNext());
    }

}
