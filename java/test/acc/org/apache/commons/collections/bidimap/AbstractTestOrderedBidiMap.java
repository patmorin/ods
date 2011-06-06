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
package org.apache.commons.collections.bidimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedBidiMap;
import org.apache.commons.collections.iterators.AbstractTestMapIterator;

/**
 * Abstract test class for {@link OrderedBidiMap} methods and contracts.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Matthew Hawthorne
 * @author Stephen Colebourne
 */
public abstract class AbstractTestOrderedBidiMap extends AbstractTestBidiMap {

    public AbstractTestOrderedBidiMap(String testName) {
        super(testName);
    }

    public AbstractTestOrderedBidiMap() {
        super();
    }

    //-----------------------------------------------------------------------
    public void testFirstKey() {
        resetEmpty();
        OrderedBidiMap bidi = (OrderedBidiMap) map;
        try {
            bidi.firstKey();
            fail();
        } catch (NoSuchElementException ex) {}
        
        resetFull();
        bidi = (OrderedBidiMap) map;
        Object confirmedFirst = confirmed.keySet().iterator().next();
        assertEquals(confirmedFirst, bidi.firstKey());
    }
    
    public void testLastKey() {
        resetEmpty();
        OrderedBidiMap bidi = (OrderedBidiMap) map;
        try {
            bidi.lastKey();
            fail();
        } catch (NoSuchElementException ex) {}
        
        resetFull();
        bidi = (OrderedBidiMap) map;
        Object confirmedLast = null;
        for (Iterator it = confirmed.keySet().iterator(); it.hasNext();) {
            confirmedLast = it.next();
        }
        assertEquals(confirmedLast, bidi.lastKey());
    }

    //-----------------------------------------------------------------------    
    public void testNextKey() {
        resetEmpty();
        OrderedBidiMap bidi = (OrderedBidiMap) map;
        assertEquals(null, bidi.nextKey(getOtherKeys()[0]));
        if (isAllowNullKey() == false) {
            try {
                assertEquals(null, bidi.nextKey(null)); // this is allowed too
            } catch (NullPointerException ex) {}
        } else {
            assertEquals(null, bidi.nextKey(null));
        }
        
        resetFull();
        bidi = (OrderedBidiMap) map;
        Iterator it = confirmed.keySet().iterator();
        Object confirmedLast = it.next();
        while (it.hasNext()) {
            Object confirmedObject = it.next();
            assertEquals(confirmedObject, bidi.nextKey(confirmedLast));
            confirmedLast = confirmedObject;
        }
        assertEquals(null, bidi.nextKey(confirmedLast));
        
        if (isAllowNullKey() == false) {
            try {
                bidi.nextKey(null);
                fail();
            } catch (NullPointerException ex) {}
        } else {
            assertEquals(null, bidi.nextKey(null));
        }
    }
    
    public void testPreviousKey() {
        resetEmpty();
        OrderedBidiMap bidi = (OrderedBidiMap) map;
        assertEquals(null, bidi.previousKey(getOtherKeys()[0]));
        if (isAllowNullKey() == false) {
            try {
                assertEquals(null, bidi.previousKey(null)); // this is allowed too
            } catch (NullPointerException ex) {}
        } else {
            assertEquals(null, bidi.previousKey(null));
        }
        
        resetFull();
        bidi = (OrderedBidiMap) map;
        List list = new ArrayList(confirmed.keySet());
        Collections.reverse(list);
        Iterator it = list.iterator();
        Object confirmedLast = it.next();
        while (it.hasNext()) {
            Object confirmedObject = it.next();
            assertEquals(confirmedObject, bidi.previousKey(confirmedLast));
            confirmedLast = confirmedObject;
        }
        assertEquals(null, bidi.previousKey(confirmedLast));
        
        if (isAllowNullKey() == false) {
            try {
                bidi.previousKey(null);
                fail();
            } catch (NullPointerException ex) {}
        } else {
            assertEquals(null, bidi.previousKey(null));
        }
    }
    
    //-----------------------------------------------------------------------
    public BulkTest bulkTestOrderedMapIterator() {
        return new TestBidiOrderedMapIterator();
    }
    
    public class TestBidiOrderedMapIterator extends AbstractTestMapIterator {
        public TestBidiOrderedMapIterator() {
            super("TestBidiOrderedMapIterator");
        }
        
        public Object[] addSetValues() {
            return AbstractTestOrderedBidiMap.this.getNewSampleValues();
        }
        
        public boolean supportsRemove() {
            return AbstractTestOrderedBidiMap.this.isRemoveSupported();
        }

        public boolean supportsSetValue() {
            return AbstractTestOrderedBidiMap.this.isSetValueSupported();
        }

        public MapIterator makeEmptyMapIterator() {
            resetEmpty();
            return ((OrderedBidiMap) AbstractTestOrderedBidiMap.this.map).orderedMapIterator();
        }

        public MapIterator makeFullMapIterator() {
            resetFull();
            return ((OrderedBidiMap) AbstractTestOrderedBidiMap.this.map).orderedMapIterator();
        }
        
        public Map getMap() {
            // assumes makeFullMapIterator() called first
            return AbstractTestOrderedBidiMap.this.map;
        }
        
        public Map getConfirmedMap() {
            // assumes makeFullMapIterator() called first
            return AbstractTestOrderedBidiMap.this.confirmed;
        }
        
        public void verify() {
            super.verify();
            AbstractTestOrderedBidiMap.this.verify();
        }
    }
    
}
