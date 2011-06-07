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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;

import org.apache.commons.collections.set.PredicatedSet;

/**
 * Tests for SetUtils.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 * @author Neil O'Toole
 * @author Matthew Hawthorne
 */
public class TestSetUtils extends BulkTest {

    public TestSetUtils(String name) {
        super(name);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestSetUtils.class);
    }

    public void testNothing() {
    }
    
    public void testpredicatedSet() {
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object o) {
                return o instanceof String;
            }
        };
        Set set = SetUtils.predicatedSet(new HashSet(), predicate);
        assertTrue("returned object should be a PredicatedSet",
            set instanceof PredicatedSet);
        try {
            set = SetUtils.predicatedSet(new HashSet(), null);
            fail("Expecting IllegalArgumentException for null predicate.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        try {
            set = SetUtils.predicatedSet(null, predicate);
            fail("Expecting IllegalArgumentException for null set.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testEquals() {
        Collection data = Arrays.asList( new String[] { "a", "b", "c" });
        
        Set a = new HashSet( data );
        Set b = new HashSet( data );
        
        assertEquals(true, a.equals(b));
        assertEquals(true, SetUtils.isEqualSet(a, b));
        a.clear();
        assertEquals(false, SetUtils.isEqualSet(a, b));
        assertEquals(false, SetUtils.isEqualSet(a, null));
        assertEquals(false, SetUtils.isEqualSet(null, b));
        assertEquals(true, SetUtils.isEqualSet(null, null));
    }
    
    public void testHashCode() {
        Collection data = Arrays.asList( new String[] { "a", "b", "c" });
            
        Set a = new HashSet( data );
        Set b = new HashSet( data );
        
        assertEquals(true, a.hashCode() == b.hashCode());
        assertEquals(true, a.hashCode() == SetUtils.hashCodeForSet(a));
        assertEquals(true, b.hashCode() == SetUtils.hashCodeForSet(b));
        assertEquals(true, SetUtils.hashCodeForSet(a) == SetUtils.hashCodeForSet(b));
        a.clear();
        assertEquals(false, SetUtils.hashCodeForSet(a) == SetUtils.hashCodeForSet(b));
        assertEquals(0, SetUtils.hashCodeForSet(null));
    }   

}
