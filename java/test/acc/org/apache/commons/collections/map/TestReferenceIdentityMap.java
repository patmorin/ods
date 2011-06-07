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

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.IterableMap;

/**
 * Tests for ReferenceIdentityMap. 
 * 
 * @version $Revision: 646780 $
 *
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Guilhem Lavaux
 */
public class TestReferenceIdentityMap extends AbstractTestIterableMap {

    private static final Integer I1A = new Integer(1);
    private static final Integer I1B = new Integer(1);
    private static final Integer I2A = new Integer(2);
    private static final Integer I2B = new Integer(2);

    public TestReferenceIdentityMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestReferenceIdentityMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestReferenceIdentityMap.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        ReferenceIdentityMap map = new ReferenceIdentityMap(ReferenceIdentityMap.WEAK, ReferenceIdentityMap.WEAK);
        return map;
    }
    
    public Map makeConfirmedMap() {
        // Testing against another [collections] class generally isn't a good idea,
        // but the alternative is a JDK1.4 dependency in the tests
        return new IdentityMap();
    }

    public boolean isAllowNullKey() {
        return false;
    }

    public boolean isAllowNullValue() {
        return false;
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

    //-----------------------------------------------------------------------
    public void testBasics() {
        IterableMap map = new ReferenceIdentityMap(ReferenceIdentityMap.HARD, ReferenceIdentityMap.HARD);
        assertEquals(0, map.size());
        
        map.put(I1A, I2A);
        assertEquals(1, map.size());
        assertSame(I2A, map.get(I1A));
        assertSame(null, map.get(I1B));
        assertEquals(true, map.containsKey(I1A));
        assertEquals(false, map.containsKey(I1B));
        assertEquals(true, map.containsValue(I2A));
        assertEquals(false, map.containsValue(I2B));
        
        map.put(I1A, I2B);
        assertEquals(1, map.size());
        assertSame(I2B, map.get(I1A));
        assertSame(null, map.get(I1B));
        assertEquals(true, map.containsKey(I1A));
        assertEquals(false, map.containsKey(I1B));
        assertEquals(false, map.containsValue(I2A));
        assertEquals(true, map.containsValue(I2B));
        
        map.put(I1B, I2B);
        assertEquals(2, map.size());
        assertSame(I2B, map.get(I1A));
        assertSame(I2B, map.get(I1B));
        assertEquals(true, map.containsKey(I1A));
        assertEquals(true, map.containsKey(I1B));
        assertEquals(false, map.containsValue(I2A));
        assertEquals(true, map.containsValue(I2B));
    }
    
    //-----------------------------------------------------------------------
    public void testHashEntry() {
        IterableMap map = new ReferenceIdentityMap(ReferenceIdentityMap.HARD, ReferenceIdentityMap.HARD);
        
        map.put(I1A, I2A);
        map.put(I1B, I2A);
        
        Map.Entry entry1 = (Map.Entry) map.entrySet().iterator().next();
        Iterator it = map.entrySet().iterator();
        Map.Entry entry2 = (Map.Entry) it.next();
        Map.Entry entry3 = (Map.Entry) it.next();
        
        assertEquals(true, entry1.equals(entry2));
        assertEquals(true, entry2.equals(entry1));
        assertEquals(false, entry1.equals(entry3));
    }
    
    
    //-----------------------------------------------------------------------
    public void testNullHandling() {
        resetFull();
        assertEquals(null, map.get(null));
        assertEquals(false, map.containsKey(null));
        assertEquals(false, map.containsValue(null));
        assertEquals(null, map.remove(null));
        assertEquals(false, map.entrySet().contains(null));
        assertEquals(false, map.keySet().contains(null));
        assertEquals(false, map.values().contains(null));
        try {
            map.put(null, null);
            fail();
        } catch (NullPointerException ex) {}
        try {
            map.put(new Object(), null);
            fail();
        } catch (NullPointerException ex) {}
        try {
            map.put(null, new Object());
            fail();
        } catch (NullPointerException ex) {}
    }

    //-----------------------------------------------------------------------
/*
    // Tests often fail because gc is uncontrollable

    public void testPurge() {
        ReferenceIdentityMap map = new ReferenceIdentityMap(ReferenceIdentityMap.WEAK, ReferenceIdentityMap.WEAK);
        Object[] hard = new Object[10];
        for (int i = 0; i < hard.length; i++) {
            hard[i] = new Object();
            map.put(hard[i], new Object());
        }
        gc();
        assertTrue("map should be empty after purge of weak values", map.isEmpty());

        for (int i = 0; i < hard.length; i++) {
            map.put(new Object(), hard[i]);
        }
        gc();
        assertTrue("map should be empty after purge of weak keys", map.isEmpty());

        for (int i = 0; i < hard.length; i++) {
            map.put(new Object(), hard[i]);
            map.put(hard[i], new Object());
        }

        gc();
        assertTrue("map should be empty after purge of weak keys and values", map.isEmpty());
    }


    public void testGetAfterGC() {
        ReferenceIdentityMap map = new ReferenceIdentityMap(ReferenceIdentityMap.WEAK, ReferenceIdentityMap.WEAK);
        for (int i = 0; i < 10; i++) {
            map.put(new Integer(i), new Integer(i));
        }

        gc();
        for (int i = 0; i < 10; i++) {
            Integer I = new Integer(i);
            assertTrue("map.containsKey should return false for GC'd element", !map.containsKey(I));
            assertTrue("map.get should return null for GC'd element", map.get(I) == null);
        }
    }


    public void testEntrySetIteratorAfterGC() {
        ReferenceIdentityMap map = new ReferenceIdentityMap(ReferenceIdentityMap.WEAK, ReferenceIdentityMap.WEAK);
        Object[] hard = new Object[10];
        for (int i = 0; i < 10; i++) {
            hard[i] = new Integer(10 + i);
            map.put(new Integer(i), new Integer(i));
            map.put(hard[i], hard[i]);
        }

        gc();
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            Integer key = (Integer)entry.getKey();
            Integer value = (Integer)entry.getValue();
            assertTrue("iterator should skip GC'd keys", key.intValue() >= 10);
            assertTrue("iterator should skip GC'd values", value.intValue() >= 10);
        }

    }

    public void testMapIteratorAfterGC() {
        ReferenceIdentityMap map = new ReferenceIdentityMap(ReferenceIdentityMap.WEAK, ReferenceIdentityMap.WEAK);
        Object[] hard = new Object[10];
        for (int i = 0; i < 10; i++) {
            hard[i] = new Integer(10 + i);
            map.put(new Integer(i), new Integer(i));
            map.put(hard[i], hard[i]);
        }

        gc();
        MapIterator iterator = map.mapIterator();
        while (iterator.hasNext()) {
            Object key1 = iterator.next();
            Integer key = (Integer) iterator.getKey();
            Integer value = (Integer) iterator.getValue();
            assertTrue("iterator keys should match", key == key1);
            assertTrue("iterator should skip GC'd keys", key.intValue() >= 10);
            assertTrue("iterator should skip GC'd values", value.intValue() >= 10);
        }

    }

    public void testMapIteratorAfterGC2() {
        ReferenceIdentityMap map = new ReferenceIdentityMap(ReferenceIdentityMap.WEAK, ReferenceIdentityMap.WEAK);
        Object[] hard = new Object[10];
        for (int i = 0; i < 10; i++) {
            hard[i] = new Integer(10 + i);
            map.put(new Integer(i), new Integer(i));
            map.put(hard[i], hard[i]);
        }

        MapIterator iterator = map.mapIterator();
        while (iterator.hasNext()) {
            Object key1 = iterator.next();
            gc();
            Integer key = (Integer) iterator.getKey();
            Integer value = (Integer) iterator.getValue();
            assertTrue("iterator keys should match", key == key1);
            assertTrue("iterator should skip GC'd keys", key.intValue() >= 10);
            assertTrue("iterator should skip GC'd values", value.intValue() >= 10);
        }

    }
*/

    WeakReference keyReference;
    WeakReference valueReference;

    public Map buildRefMap() {
        Object key = new Object();
        Object value = new Object();
        
        keyReference = new WeakReference(key);
        valueReference = new WeakReference(value);
        
        Map testMap = new ReferenceIdentityMap(ReferenceMap.WEAK, ReferenceMap.HARD, true);
        testMap.put(key, value);
 
        assertEquals("In map", value, testMap.get(key));
        assertNotNull("Weak reference released early (1)", keyReference.get());
        assertNotNull("Weak reference released early (2)", valueReference.get());
        return testMap;
    }

    /** Tests whether purge values setting works */
    public void testPurgeValues() throws Exception {
        // many thanks to Juozas Baliuka for suggesting this method
        Map testMap = buildRefMap();
        
        int iterations = 0;
        int bytz = 2;
        while(true) {
            System.gc();
            if(iterations++ > 50){
                fail("Max iterations reached before resource released.");
            }
            testMap.isEmpty();
            if( 
                keyReference.get() == null &&
                valueReference.get() == null) {
                break;
                
            } else {
                // create garbage:
                byte[] b =  new byte[bytz];
                bytz = bytz * 2;
            }
        }
    }

    private static void gc() {
        try {
            // trigger GC
            byte[][] tooLarge = new byte[1000000000][1000000000];
            fail("you have too much RAM");
        } catch (OutOfMemoryError ex) {
            System.gc(); // ignore
        }
    }

}
