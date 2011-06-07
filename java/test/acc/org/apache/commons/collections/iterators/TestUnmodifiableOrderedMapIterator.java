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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.map.ListOrderedMap;

/**
 * Tests the UnmodifiableOrderedMapIterator.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestUnmodifiableOrderedMapIterator extends AbstractTestOrderedMapIterator {

    public static Test suite() {
        return new TestSuite(TestUnmodifiableOrderedMapIterator.class);
    }

    public TestUnmodifiableOrderedMapIterator(String testName) {
        super(testName);
    }

    public MapIterator makeEmptyMapIterator() {
        return UnmodifiableOrderedMapIterator.decorate(
            ListOrderedMap.decorate(new HashMap()).orderedMapIterator());
    }

    public MapIterator makeFullMapIterator() {
        return UnmodifiableOrderedMapIterator.decorate(
            ((OrderedMap) getMap()).orderedMapIterator());
    }
    
    public Map getMap() {
        Map testMap = ListOrderedMap.decorate(new HashMap());
        testMap.put("A", "a");
        testMap.put("B", "b");
        testMap.put("C", "c");
        return testMap;
    }

    public Map getConfirmedMap() {
        Map testMap = new TreeMap();
        testMap.put("A", "a");
        testMap.put("B", "b");
        testMap.put("C", "c");
        return testMap;
    }

    public boolean supportsRemove() {
        return false;
    }

    public boolean supportsSetValue() {
        return false;
    }
    
    //-----------------------------------------------------------------------
    public void testOrderedMapIterator() {
        assertTrue(makeEmptyOrderedMapIterator() instanceof Unmodifiable);
    }
    
    public void testDecorateFactory() {
        OrderedMapIterator it = makeFullOrderedMapIterator();
        assertSame(it, UnmodifiableOrderedMapIterator.decorate(it));
        
        it = ((OrderedMap) getMap()).orderedMapIterator() ;
        assertTrue(it != UnmodifiableOrderedMapIterator.decorate(it));
        
        try {
            UnmodifiableOrderedMapIterator.decorate(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

}
