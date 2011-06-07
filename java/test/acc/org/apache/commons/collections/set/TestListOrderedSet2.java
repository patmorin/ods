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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Extension of {@link TestSet} for exercising the {@link ListOrderedSet}
 * implementation.
 *
 * @since Commons Collections 3.1
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Henning P. Schmiedehausen
 * @author Stephen Colebourne
 */
public class TestListOrderedSet2 extends AbstractTestSet {

    public TestListOrderedSet2(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestListOrderedSet2.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestListOrderedSet2.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public Set makeEmptySet() {
        return new ListOrderedSet();
    }

    protected Set setupSet() {
        Set set = makeEmptySet();

        for (int i = 0; i < 10; i++) {
            set.add(Integer.toString(i));
        }
        return set;
    }

    public void testOrdering() {
        Set set = setupSet();
        Iterator it = set.iterator();

        for (int i = 0; i < 10; i++) {
            assertEquals("Sequence is wrong", Integer.toString(i), it.next());
        }

        for (int i = 0; i < 10; i += 2) {
            assertTrue("Must be able to remove int", set.remove(Integer.toString(i)));
        }

        it = set.iterator();
        for (int i = 1; i < 10; i += 2) {
            assertEquals("Sequence is wrong after remove ", Integer.toString(i), it.next());
        }

        for (int i = 0; i < 10; i++) {
            set.add(Integer.toString(i));
        }

        assertEquals("Size of set is wrong!", 10, set.size());

        it = set.iterator();
        for (int i = 1; i < 10; i += 2) {
            assertEquals("Sequence is wrong", Integer.toString(i), it.next());
        }
        for (int i = 0; i < 10; i += 2) {
            assertEquals("Sequence is wrong", Integer.toString(i), it.next());
        }
    }
    
    private static final Integer ZERO = new Integer(0);
    private static final Integer ONE = new Integer(1);
    private static final Integer TWO = new Integer(2);
    private static final Integer THREE = new Integer(3);
    
    public void testListAddRemove() {
        ListOrderedSet set = (ListOrderedSet) makeEmptySet();
        List view = set.asList();
        set.add(ZERO);
        set.add(ONE);
        set.add(TWO);
        
        assertEquals(3, set.size());
        assertSame(ZERO, set.get(0));
        assertSame(ONE, set.get(1));
        assertSame(TWO, set.get(2));
        assertEquals(3, view.size());
        assertSame(ZERO, view.get(0));
        assertSame(ONE, view.get(1));
        assertSame(TWO, view.get(2));
        
        assertEquals(0, set.indexOf(ZERO));
        assertEquals(1, set.indexOf(ONE));
        assertEquals(2, set.indexOf(TWO));
        
        set.remove(1);
        assertEquals(2, set.size());
        assertSame(ZERO, set.get(0));
        assertSame(TWO, set.get(1));
        assertEquals(2, view.size());
        assertSame(ZERO, view.get(0));
        assertSame(TWO, view.get(1));
    }        
    
    public void testListAddIndexed() {
        ListOrderedSet set = (ListOrderedSet) makeEmptySet();
        List view = set.asList();
        set.add(ZERO);
        set.add(TWO);
        
        set.add(1, ONE);
        assertEquals(3, set.size());
        assertSame(ZERO, set.get(0));
        assertSame(ONE, set.get(1));
        assertSame(TWO, set.get(2));
        
        set.add(0, ONE);
        assertEquals(3, set.size());
        assertSame(ZERO, set.get(0));
        assertSame(ONE, set.get(1));
        assertSame(TWO, set.get(2));
        
        List list = new ArrayList();
        list.add(ZERO);
        list.add(TWO);
        
        set.addAll(0, list);
        assertEquals(3, set.size());
        assertSame(ZERO, set.get(0));
        assertSame(ONE, set.get(1));
        assertSame(TWO, set.get(2));
        
        list.add(0, THREE); // list = [3,0,2]
        set.remove(TWO);    //  set = [0,1]
        set.addAll(1, list);
        assertEquals(4, set.size());
        assertSame(ZERO, set.get(0));
        assertSame(THREE, set.get(1));
        assertSame(TWO, set.get(2));
        assertSame(ONE, set.get(3));
    }
    
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/ListOrderedSet.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/ListOrderedSet.fullCollection.version3.1.obj");
//    }

}
