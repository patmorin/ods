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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;

import org.apache.commons.collections.list.PredicatedList;

/**
 * Tests for ListUtils.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 * @author Neil O'Toole
 * @author Matthew Hawthorne
 */
public class TestListUtils extends BulkTest {

    private static final String a = "a";
    private static final String b = "b";
    private static final String c = "c";
    private static final String d = "d";
    private static final String e = "e";
    private static final String x = "x";

    private String[] fullArray;
    private List fullList;
    
    public TestListUtils(String name) {
        super(name);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestListUtils.class);
    }

    public void setUp() {
        fullArray = new String[]{a, b, c, d, e};
        fullList = new ArrayList(Arrays.asList(fullArray));
    }
    
    
    public void testNothing() {
    }
    
    public void testpredicatedList() {
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object o) {
                return o instanceof String;
            }
        };
        List list =
        ListUtils.predicatedList(new ArrayStack(), predicate);
        assertTrue("returned object should be a PredicatedList",
            list instanceof PredicatedList);
        try {
            list =
            ListUtils.predicatedList(new ArrayStack(), null);
            fail("Expecting IllegalArgumentException for null predicate.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        try {
            list =
            ListUtils.predicatedList(null, predicate);
            fail("Expecting IllegalArgumentException for null list.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testLazyList() {
        List list = ListUtils.lazyList(new ArrayList(), new Factory() {

            private int index;

            public Object create() {
                index++;
                return new Integer(index);
            }
        });

        assertNotNull((Integer)list.get(5));
        assertEquals(6, list.size());

        assertNotNull((Integer)list.get(5));
        assertEquals(6, list.size());
    }

    public void testEquals() {
        Collection data = Arrays.asList( new String[] { "a", "b", "c" });
        
        List a = new ArrayList( data );
        List b = new ArrayList( data );
        
        assertEquals(true, a.equals(b));
        assertEquals(true, ListUtils.isEqualList(a, b));
        a.clear();
        assertEquals(false, ListUtils.isEqualList(a, b));
        assertEquals(false, ListUtils.isEqualList(a, null));
        assertEquals(false, ListUtils.isEqualList(null, b));
        assertEquals(true, ListUtils.isEqualList(null, null));
    }
    
    public void testHashCode() {
        Collection data = Arrays.asList( new String[] { "a", "b", "c" });
            
        List a = new ArrayList( data );
        List b = new ArrayList( data );
        
        assertEquals(true, a.hashCode() == b.hashCode());
        assertEquals(true, a.hashCode() == ListUtils.hashCodeForList(a));
        assertEquals(true, b.hashCode() == ListUtils.hashCodeForList(b));
        assertEquals(true, ListUtils.hashCodeForList(a) == ListUtils.hashCodeForList(b));
        a.clear();
        assertEquals(false, ListUtils.hashCodeForList(a) == ListUtils.hashCodeForList(b));
        assertEquals(0, ListUtils.hashCodeForList(null));
    }
    
    public void testRetainAll() {
        List sub = new ArrayList();
        sub.add(a);
        sub.add(b);
        sub.add(x);

        List retained = ListUtils.retainAll(fullList, sub);
        assertTrue(retained.size() == 2);
        sub.remove(x);
        assertTrue(retained.equals(sub));
        fullList.retainAll(sub);
        assertTrue(retained.equals(fullList));
        
        try {
            List list = ListUtils.retainAll(null, null);
            fail("expecting NullPointerException");
        } catch(NullPointerException npe){} // this is what we want
    }

    public void testRemoveAll() {
        List sub = new ArrayList();
        sub.add(a);
        sub.add(b);
        sub.add(x);

        List remainder = ListUtils.removeAll(fullList, sub);
        assertTrue(remainder.size() == 3);
        fullList.removeAll(sub);
        assertTrue(remainder.equals(fullList));
        
        try {
            List list = ListUtils.removeAll(null, null);
            fail("expecting NullPointerException");
        } catch(NullPointerException npe) {} // this is what we want
    }
    
}
