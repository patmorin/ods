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
package org.apache.commons.collections.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.iterators.AbstractTestOrderedMapIterator;

/**
 * Abstract test class for {@link OrderedMap} methods and contracts.
 *
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public abstract class AbstractTestOrderedMap extends AbstractTestIterableMap {

    /**
     * JUnit constructor.
     * 
     * @param testName  the test name
     */
    public AbstractTestOrderedMap(String testName) {
        super(testName);
    }
    
    //-----------------------------------------------------------------------
    /**
     * OrderedMap uses TreeMap as its known comparison.
     * 
     * @return a map that is known to be valid
     */
    public Map makeConfirmedMap() {
        return new TreeMap(new NullComparator());
    }
    
    /**
     * The only confirmed collection we have that is ordered is the sorted one.
     * Thus, sort the keys.
     */
    public Object[] getSampleKeys() {
        List list = new ArrayList(Arrays.asList(super.getSampleKeys()));
        Collections.sort(list, new NullComparator());
        return list.toArray();
    }

    //-----------------------------------------------------------------------
    public void testFirstKey() {
        resetEmpty();
        OrderedMap ordered = (OrderedMap) map;
        try {
            ordered.firstKey();
            fail();
        } catch (NoSuchElementException ex) {}
        
        resetFull();
        ordered = (OrderedMap) map;
        Object confirmedFirst = confirmed.keySet().iterator().next();
        assertEquals(confirmedFirst, ordered.firstKey());
    }
    
    public void testLastKey() {
        resetEmpty();
        OrderedMap ordered = (OrderedMap) map;
        try {
            ordered.lastKey();
            fail();
        } catch (NoSuchElementException ex) {}
        
        resetFull();
        ordered = (OrderedMap) map;
        Object confirmedLast = null;
        for (Iterator it = confirmed.keySet().iterator(); it.hasNext();) {
            confirmedLast = it.next();
        }
        assertEquals(confirmedLast, ordered.lastKey());
    }

    //-----------------------------------------------------------------------    
    public void testNextKey() {
        resetEmpty();
        OrderedMap ordered = (OrderedMap) map;
        assertEquals(null, ordered.nextKey(getOtherKeys()[0]));
        if (isAllowNullKey() == false) {
            try {
                assertEquals(null, ordered.nextKey(null)); // this is allowed too
            } catch (NullPointerException ex) {}
        } else {
            assertEquals(null, ordered.nextKey(null));
        }
        
        resetFull();
        ordered = (OrderedMap) map;
        Iterator it = confirmed.keySet().iterator();
        Object confirmedLast = it.next();
        while (it.hasNext()) {
            Object confirmedObject = it.next();
            assertEquals(confirmedObject, ordered.nextKey(confirmedLast));
            confirmedLast = confirmedObject;
        }
        assertEquals(null, ordered.nextKey(confirmedLast));
        
        if (isAllowNullKey() == false) {
            try {
                ordered.nextKey(null);
                fail();
            } catch (NullPointerException ex) {}
        } else {
            assertEquals(null, ordered.nextKey(null));
        }
    }
    
    public void testPreviousKey() {
        resetEmpty();
        OrderedMap ordered = (OrderedMap) map;
        assertEquals(null, ordered.previousKey(getOtherKeys()[0]));
        if (isAllowNullKey() == false) {
            try {
                assertEquals(null, ordered.previousKey(null)); // this is allowed too
            } catch (NullPointerException ex) {}
        } else {
            assertEquals(null, ordered.previousKey(null));
        }
        
        resetFull();
        ordered = (OrderedMap) map;
        List list = new ArrayList(confirmed.keySet());
        Collections.reverse(list);
        Iterator it = list.iterator();
        Object confirmedLast = it.next();
        while (it.hasNext()) {
            Object confirmedObject = it.next();
            assertEquals(confirmedObject, ordered.previousKey(confirmedLast));
            confirmedLast = confirmedObject;
        }
        assertEquals(null, ordered.previousKey(confirmedLast));
        
        if (isAllowNullKey() == false) {
            try {
                ordered.previousKey(null);
                fail();
            } catch (NullPointerException ex) {}
        } else {
            if (isAllowNullKey() == false) {
                assertEquals(null, ordered.previousKey(null));
            }
        }
    }
    
    //-----------------------------------------------------------------------
    public BulkTest bulkTestOrderedMapIterator() {
        return new InnerTestOrderedMapIterator();
    }
    
    public class InnerTestOrderedMapIterator extends AbstractTestOrderedMapIterator {
        public InnerTestOrderedMapIterator() {
            super("InnerTestOrderedMapIterator");
        }
        
        public boolean supportsRemove() {
            return AbstractTestOrderedMap.this.isRemoveSupported();
        }

        public boolean isGetStructuralModify() {
            return AbstractTestOrderedMap.this.isGetStructuralModify();
        }
        
        public boolean supportsSetValue() {
            return AbstractTestOrderedMap.this.isSetValueSupported();
        }

        public MapIterator makeEmptyMapIterator() {
            resetEmpty();
            return ((OrderedMap) AbstractTestOrderedMap.this.map).orderedMapIterator();
        }

        public MapIterator makeFullMapIterator() {
            resetFull();
            return ((OrderedMap) AbstractTestOrderedMap.this.map).orderedMapIterator();
        }
        
        public Map getMap() {
            // assumes makeFullMapIterator() called first
            return AbstractTestOrderedMap.this.map;
        }
        
        public Map getConfirmedMap() {
            // assumes makeFullMapIterator() called first
            return AbstractTestOrderedMap.this.confirmed;
        }
        
        public void verify() {
            super.verify();
            AbstractTestOrderedMap.this.verify();
        }
    }
    
}
