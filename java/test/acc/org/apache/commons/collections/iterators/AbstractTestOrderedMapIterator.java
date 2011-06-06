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
package org.apache.commons.collections.iterators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections.OrderedMapIterator;

/**
 * Abstract class for testing the OrderedMapIterator interface.
 * <p>
 * This class provides a framework for testing an implementation of MapIterator.
 * Concrete subclasses must provide the list iterator to be tested.
 * They must also specify certain details of how the list iterator operates by
 * overriding the supportsXxx() methods if necessary.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public abstract class AbstractTestOrderedMapIterator extends AbstractTestMapIterator {

    /**
     * JUnit constructor.
     * 
     * @param testName  the test class name
     */
    public AbstractTestOrderedMapIterator(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------
    public final OrderedMapIterator makeEmptyOrderedMapIterator() {
        return (OrderedMapIterator) makeEmptyMapIterator();
    }

    public final OrderedMapIterator makeFullOrderedMapIterator() {
        return (OrderedMapIterator) makeFullMapIterator();
    }
    
    //-----------------------------------------------------------------------
    /**
     * Test that the empty list iterator contract is correct.
     */
    public void testEmptyMapIterator() {
        if (supportsEmptyIterator() == false) {
            return;
        }

        super.testEmptyMapIterator();
        
        OrderedMapIterator it = makeEmptyOrderedMapIterator();
        Map map = getMap();
        assertEquals(false, it.hasPrevious());
        try {
            it.previous();
            fail();
        } catch (NoSuchElementException ex) {}
    }

    //-----------------------------------------------------------------------
    /**
     * Test that the full list iterator contract is correct.
     */
    public void testFullMapIterator() {
        if (supportsFullIterator() == false) {
            return;
        }

        super.testFullMapIterator();
        
        OrderedMapIterator it = makeFullOrderedMapIterator();
        Map map = getMap();
        
        assertEquals(true, it.hasNext());
        assertEquals(false, it.hasPrevious());
        Set set = new HashSet();
        while (it.hasNext()) {
            // getKey
            Object key = it.next();
            assertSame("it.next() should equals getKey()", key, it.getKey());
            assertTrue("Key must be in map",  map.containsKey(key));
            assertTrue("Key must be unique", set.add(key));
            
            // getValue
            Object value = it.getValue();
            if (isGetStructuralModify() == false) {
                assertSame("Value must be mapped to key", map.get(key), value);
            }
            assertTrue("Value must be in map",  map.containsValue(value));

            assertEquals(true, it.hasPrevious());
            
            verify();
        }
        while (it.hasPrevious()) {
            // getKey
            Object key = it.previous();
            assertSame("it.previous() should equals getKey()", key, it.getKey());
            assertTrue("Key must be in map",  map.containsKey(key));
            assertTrue("Key must be unique", set.remove(key));
            
            // getValue
            Object value = it.getValue();
            if (isGetStructuralModify() == false) {
                assertSame("Value must be mapped to key", map.get(key), value);
            }
            assertTrue("Value must be in map",  map.containsValue(value));

            assertEquals(true, it.hasNext());
            
            verify();
        }
    }
    
    //-----------------------------------------------------------------------
    /**
     * Test that the iterator order matches the keySet order.
     */
    public void testMapIteratorOrder() {
        if (supportsFullIterator() == false) {
            return;
        }

        OrderedMapIterator it = makeFullOrderedMapIterator();
        Map map = getMap();
        
        assertEquals("keySet() not consistent", new ArrayList(map.keySet()), new ArrayList(map.keySet()));
        
        Iterator it2 = map.keySet().iterator();
        assertEquals(true, it.hasNext());
        assertEquals(true, it2.hasNext());
        List list = new ArrayList();
        while (it.hasNext()) {
            Object key = it.next();
            assertEquals(it2.next(), key);
            list.add(key);
        }
        assertEquals(map.size(), list.size());
        while (it.hasPrevious()) {
            Object key = it.previous();
            assertEquals(list.get(list.size() - 1), key);
            list.remove(list.size() - 1);
        }
        assertEquals(0, list.size());
    }
    
}
