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

import java.lang.ref.WeakReference;
import java.util.Map;

import junit.framework.Test;

import org.apache.commons.collections.map.AbstractTestMap;

/**
 * Tests for ReferenceMap. 
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Paul Jack
 * @author Guilhem Lavaux
 */
public class TestReferenceMap extends AbstractTestMap {

    public TestReferenceMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestReferenceMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestReferenceMap.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        ReferenceMap map = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.WEAK);
        return map;
    }

    public boolean isAllowNullKey() {
        return false;
    }

    public boolean isAllowNullValue() {
        return false;
    }

    public String getCompatibilityVersion() {
        return "2.1";
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
        ReferenceMap map = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.WEAK);
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
        ReferenceMap map = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.WEAK);
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
        ReferenceMap map = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.WEAK);
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
*/

    WeakReference keyReference;
    WeakReference valueReference;

    public Map buildRefMap() {
        Object key = new Object();
        Object value = new Object();
        
        keyReference = new WeakReference(key);
        valueReference = new WeakReference(value);
        
        Map testMap = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.HARD, true);
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
