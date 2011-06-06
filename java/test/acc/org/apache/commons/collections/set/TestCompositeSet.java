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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.collection.CompositeCollection;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * Extension of {@link AbstractTestSet} for exercising the 
 * {@link CompositeSet} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Brian McCallister
 * @author Phil Steitz
 */

public class TestCompositeSet extends AbstractTestSet {
    public TestCompositeSet(String name) {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(TestCompositeSet.class);
    }
    
    public Set makeEmptySet() {
        final HashSet contained = new HashSet();
        CompositeSet set = new CompositeSet(contained);
        set.setMutator(new CompositeSet.SetMutator() {
            public void resolveCollision(CompositeSet comp, Set existing, 
                Set added, Collection intersects) {
                throw new IllegalArgumentException();
            }
            
            public boolean add(CompositeCollection composite, 
                Collection[] collections, Object obj) {
                return contained.add(obj);
            }
            
            public boolean addAll(CompositeCollection composite, 
                Collection[] collections, Collection coll) {
                return contained.addAll(coll);
            }
            
            public boolean remove(CompositeCollection composite, 
                Collection[] collections, Object obj) {
                return contained.remove(obj);
            }
        });
        return set;
    }
    
    public Set buildOne() {
        HashSet set = new HashSet();
        set.add("1");
        set.add("2");
        return set;
    }
    
    public Set buildTwo() {
        HashSet set = new HashSet();
        set.add("3");
        set.add("4");
        return set;
    }
    
    public void testContains() {
        CompositeSet set = new CompositeSet(new Set[]{buildOne(), buildTwo()});
        assertTrue(set.contains("1"));
    }
    
    public void testRemoveUnderlying() {
        Set one = buildOne();
        Set two = buildTwo();
        CompositeSet set = new CompositeSet(new Set[]{one, two});
        one.remove("1");
        assertFalse(set.contains("1"));
        
        two.remove("3");
        assertFalse(set.contains("3"));
    }
    
    public void testRemoveComposited() {
        Set one = buildOne();
        Set two = buildTwo();
        CompositeSet set = new CompositeSet(new Set[]{one, two});
        set.remove("1");
        assertFalse(one.contains("1"));
        
        set.remove("3");
        assertFalse(one.contains("3"));
    }
    
    public void testFailedCollisionResolution() {
        Set one = buildOne();
        Set two = buildTwo();
        CompositeSet set = new CompositeSet(new Set[]{one, two});
        set.setMutator(new CompositeSet.SetMutator() {
            public void resolveCollision(CompositeSet comp, Set existing, 
                Set added, Collection intersects) {
            }
            
            public boolean add(CompositeCollection composite, 
                Collection[] collections, Object obj) {
                throw new UnsupportedOperationException();
            }
            
            public boolean addAll(CompositeCollection composite, 
                Collection[] collections, Collection coll) {
                throw new UnsupportedOperationException();
            }
            
            public boolean remove(CompositeCollection composite, 
                Collection[] collections, Object obj) {
                throw new UnsupportedOperationException();
            }
        });
        
        HashSet three = new HashSet();
        three.add("1");
        try {
            set.addComposited(three);
            fail("IllegalArgumentException should have been thrown");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testAddComposited() {
        Set one = buildOne();
        Set two = buildTwo();
        CompositeSet set = new CompositeSet();
        set.addComposited(one, two);
        CompositeSet set2 = new CompositeSet(buildOne());
        set2.addComposited(buildTwo());
        assertTrue(set.equals(set2));
        HashSet set3 = new HashSet();
        set3.add("1");
        set3.add("2");
        set3.add("3");
        HashSet set4 = new HashSet();
        set4.add("4");
        CompositeSet set5 = new CompositeSet(set3);
        set5.addComposited(set4);
        assertTrue(set.equals(set5));
        try {
            set.addComposited(set3);
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }
}
