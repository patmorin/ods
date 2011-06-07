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
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;

/**
 * Test FastArrayList.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Jason van Zyl
 */
public class TestFastArrayList extends TestArrayList {
    
    public TestFastArrayList(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestFastArrayList.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestFastArrayList.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public void setUp() {
        list = (ArrayList) makeEmptyList();
    }

    public List makeEmptyList() {
        FastArrayList fal = new FastArrayList();
        fal.setFast(false);
        return (fal);
    }

    public void testConcurrentModification_alwaysFast() {
        FastArrayList list = new FastArrayList();
        list.setFast(true);
        list.add("a");
        list.add("b");
        list.add("c");
        ListIterator iter = list.listIterator();
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        iter.remove();  // checking for no ConcurrentModificationException
        assertEquals("c", iter.next());
        assertEquals(false, iter.hasNext());
        assertEquals("c", iter.previous());
        assertEquals("a", iter.previous());
        assertEquals(false, iter.hasPrevious());
    }

    public void testConcurrentModification_alwaysFastModError() {
        FastArrayList list = new FastArrayList();
        list.setFast(true);
        list.add("a");
        list.add("b");
        list.add("c");
        ListIterator iter = list.listIterator();
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        list.remove(1);
        try {
            iter.remove();
        } catch (ConcurrentModificationException ex) {
            // expected
        }
        // iterator state now invalid
    }

    public void testConcurrentModification_delayedFast() {
        FastArrayList list = new FastArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        ListIterator iter = list.listIterator();
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        list.setFast(true);
        iter.remove();  // checking for no ConcurrentModificationException
        assertEquals("c", iter.next());
        assertEquals(false, iter.hasNext());
        assertEquals("c", iter.previous());
        assertEquals("a", iter.previous());
        assertEquals(false, iter.hasPrevious());
    }

    public void testConcurrentModification_delayedFastModError() {
        FastArrayList list = new FastArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        ListIterator iter = list.listIterator();
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        list.setFast(true);
        list.remove(1);
        try {
            iter.remove();
        } catch (ConcurrentModificationException ex) {
            // expected
        }
        // iterator state now invalid
    }

    public void testConcurrentModification_alwaysFastPrevious() {
        FastArrayList list = new FastArrayList();
        list.setFast(true);
        list.add("a");
        list.add("b");
        list.add("c");
        ListIterator iter = list.listIterator();
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertEquals("b", iter.previous());
        iter.remove();  // checking for no ConcurrentModificationException
        assertEquals("c", iter.next());
        assertEquals(false, iter.hasNext());
        assertEquals("c", iter.previous());
        assertEquals("a", iter.previous());
        assertEquals(false, iter.hasPrevious());
    }

    public void testConcurrentModification_alwaysFastModErrorPrevious() {
        FastArrayList list = new FastArrayList();
        list.setFast(true);
        list.add("a");
        list.add("b");
        list.add("c");
        ListIterator iter = list.listIterator();
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertEquals("b", iter.previous());
        list.remove(1);
        try {
            iter.remove();
        } catch (ConcurrentModificationException ex) {
            // expected
        }
        // iterator state now invalid
    }

}
