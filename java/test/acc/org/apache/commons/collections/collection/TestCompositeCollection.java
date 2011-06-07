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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Extension of {@link AbstractTestCollection} for exercising the 
 * {@link CompositeCollection} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Brian McCallister
 * @author Phil Steitz
 */
public class TestCompositeCollection extends AbstractTestCollection {
    
    public TestCompositeCollection(String name) {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(TestCompositeCollection.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestCompositeCollection.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
 
 //-----------------------------------------------------------------------------
    /**
     * Run stock collection tests without Mutator, so turn off add, remove
     */
    public boolean isAddSupported() {
        return false;
    }
    
    public boolean isRemoveSupported() {
        return false;
    }
    
    /**
     * Empty collection is empty composite
     */
    public Collection makeCollection() {
        return new CompositeCollection();
    }
    
    public Collection makeConfirmedCollection() {
        return new HashSet();
    }
    
    public Object[] getFullElements() {
        return new Object[] {"1", "2", "3", "4"};
    }
    
    /**
     * Full collection consists of 4 collections, each with one element
     */
    public Collection makeFullCollection() {
        CompositeCollection compositeCollection = new CompositeCollection();
        Object[] elements = getFullElements();
        for (int i = 0; i < elements.length; i++) {
            Collection summand = new HashSet();
            summand.add(elements[i]);
            compositeCollection.addComposited(summand);
        }
        return compositeCollection;
    }
    
    /**
     * Full collection should look like a collection with 4 elements
     */
    public Collection makeConfirmedFullCollection() {
        Collection collection = new HashSet();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }
    
    /**
     * Override testUnsupportedRemove, since the default impl expects removeAll,
     * retainAll and iterator().remove to throw
     */
    public void testUnsupportedRemove() {    
        resetFull();
        try {
            collection.remove(null);
            fail("remove should raise UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        verify();
    }
    
    //--------------------------------------------------------------------------
    
    protected CompositeCollection c;
    protected Collection one;
    protected Collection two;
    
    protected void setUpTest() {
        c = new CompositeCollection();
        one = new HashSet();
        two = new HashSet();
    }
    
    protected void setUpMutatorTest() {
        setUpTest();
        c.setMutator(new CompositeCollection.CollectionMutator() {
            public boolean add(CompositeCollection composite, 
            Collection[] collections, Object obj) {
                for (int i = 0; i < collections.length; i++) {
                    collections[i].add(obj);
                }
                return true;
            }
            
            public boolean addAll(CompositeCollection composite, 
            Collection[] collections, Collection coll) {
                for (int i = 0; i < collections.length; i++) {
                    collections[i].addAll(coll);
                }
                return true;
            }
            
            public boolean remove(CompositeCollection composite, 
            Collection[] collections, Object obj) {
                for (int i = 0; i < collections.length; i++) {
                    collections[i].remove(obj);
                }
                return true;
            }
        });
    }
            
    public void testSize() {
        setUpTest();
        HashSet set = new HashSet();
        set.add("a");
        set.add("b");
        c.addComposited(set);
        assertEquals(set.size(), c.size());
    }
    
    public void testMultipleCollectionsSize() {
        setUpTest();
        HashSet set = new HashSet();
        set.add("a");
        set.add("b");
        c.addComposited(set);
        HashSet other = new HashSet();
        other.add("c");
        c.addComposited(other);
        assertEquals(set.size() + other.size(), c.size());
    }
    
    public void testIsEmpty() {
        setUpTest();
        assertTrue(c.isEmpty());
        HashSet empty = new HashSet();
        c.addComposited(empty);
        assertTrue(c.isEmpty());
        empty.add("a");
        assertTrue(!c.isEmpty());
    }
    
    
    public void testIterator() {
        setUpTest();
        one.add("1");
        two.add("2");
        c.addComposited(one);
        c.addComposited(two);
        Iterator i = c.iterator();
        Object next = i.next();
        assertTrue(c.contains(next));
        assertTrue(one.contains(next));
        next = i.next();
        i.remove();
        assertTrue(!c.contains(next));
        assertTrue(!two.contains(next));
    }
    
    public void testClear() {
        setUpTest();
        one.add("1");
        two.add("2");
        c.addComposited(one, two);
        c.clear();
        assertTrue(one.isEmpty());
        assertTrue(two.isEmpty());
        assertTrue(c.isEmpty());
    }
    
    public void testContainsAll() {
        setUpTest();
        one.add("1");
        two.add("1");
        c.addComposited(one);
        assertTrue(c.containsAll(two));
    }
    
    public void testRetainAll() {
        setUpTest();
        one.add("1");
        one.add("2");
        two.add("1");
        c.addComposited(one);
        c.retainAll(two);
        assertTrue(!c.contains("2"));
        assertTrue(!one.contains("2"));
        assertTrue(c.contains("1"));
        assertTrue(one.contains("1"));
    }
    
    public void testAddAllMutator() {
        setUpTest();
        c.setMutator(new CompositeCollection.CollectionMutator() {
            public boolean add(CompositeCollection composite, 
            Collection[] collections, Object obj) {
                for (int i = 0; i < collections.length; i++) {
                    collections[i].add(obj);
                }
                return true;
            }
            
            public boolean addAll(CompositeCollection composite, 
            Collection[] collections, Collection coll) {
                for (int i = 0; i < collections.length; i++) {
                    collections[i].addAll(coll);
                }
                return true;
            }
            
            public boolean remove(CompositeCollection composite, 
            Collection[] collections, Object obj) {
                return false;
            }
        });
        
        c.addComposited(one);
        two.add("foo");
        c.addAll(two);
        assertTrue(c.contains("foo"));
        assertTrue(one.contains("foo"));
    }
    
    public void testAddMutator() {
        setUpTest();
        c.setMutator(new CompositeCollection.CollectionMutator() {
            public boolean add(CompositeCollection composite, 
            Collection[] collections, Object obj) {
                for (int i = 0; i < collections.length; i++) {
                    collections[i].add(obj);
                }
                return true;
            }
            
            public boolean addAll(CompositeCollection composite, 
            Collection[] collections, Collection coll) {
                for (int i = 0; i < collections.length; i++) {
                    collections[i].addAll(coll);
                }
                return true;
            }
            
            public boolean remove(CompositeCollection composite, 
            Collection[] collections, Object obj) {
                return false;
            }
        });
        
        c.addComposited(one);
        c.add("foo");
        assertTrue(c.contains("foo"));
        assertTrue(one.contains("foo"));
    }
    
    public void testToCollection() {
        setUpTest();
        one.add("1");
        two.add("2");
        c.addComposited(one, two);
        Collection foo = c.toCollection();
        assertTrue(foo.containsAll(c));
        assertEquals(c.size(), foo.size());
        one.add("3");
        assertTrue(!foo.containsAll(c));
    }
    
    public void testAddAllToCollection() {
        setUpTest();
        one.add("1");
        two.add("2");
        c.addComposited(one, two);
        Collection toCollection = new HashSet();
        toCollection.addAll(c);
        assertTrue(toCollection.containsAll(c));
        assertEquals(c.size(), toCollection.size());
    }   
    
    public void testRemove() {
        setUpMutatorTest();
        one.add("1");
        two.add("2");
        two.add("1");
        c.addComposited(one, two);
        c.remove("1");
        assertTrue(!c.contains("1"));
        assertTrue(!one.contains("1"));
        assertTrue(!two.contains("1"));
    }
    
    public void testRemoveAll() {
        setUpMutatorTest();
        one.add("1");
        two.add("2");
        two.add("1");
        c.addComposited(one, two);
        c.removeAll(one);
        assertTrue(!c.contains("1"));
        assertTrue(!one.contains("1"));
        assertTrue(!two.contains("1"));
    }
    
    public void testRemoveComposited() {
        setUpMutatorTest();
        one.add("1");
        two.add("2");
        two.add("1");
        c.addComposited(one, two);    
        c.removeComposited(one);
        assertTrue(c.contains("1"));
        assertEquals(c.size(), 2);
    }
}
