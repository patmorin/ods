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

import java.util.Map;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.keyvalue.MultiKey;

/**
 * JUnit tests.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestMultiKeyMap extends AbstractTestIterableMap {
    
    static final Integer I1 = new Integer(1);
    static final Integer I2 = new Integer(2);
    static final Integer I3 = new Integer(3);
    static final Integer I4 = new Integer(4);
    static final Integer I5 = new Integer(5);
    static final Integer I6 = new Integer(6);
    static final Integer I7 = new Integer(7);
    static final Integer I8 = new Integer(8);

    public TestMultiKeyMap(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestMultiKeyMap.class);
    }

    public Map makeEmptyMap() {
        return new MultiKeyMap();
    }

    public Object[] getSampleKeys() {
        return getMultiKeyKeys();
    }

    private MultiKey[] getMultiKeyKeys() {
        return new MultiKey[] {
            new MultiKey(I1, I2),
            new MultiKey(I2, I3),
            new MultiKey(I3, I4),
            new MultiKey(I1, I1, I2),
            new MultiKey(I2, I3, I4),
            new MultiKey(I3, I7, I6),
            new MultiKey(I1, I1, I2, I3),
            new MultiKey(I2, I4, I5, I6),
            new MultiKey(I3, I6, I7, I8),
            new MultiKey(I1, I1, I2, I3, I4),
            new MultiKey(I2, I3, I4, I5, I6),
            new MultiKey(I3, I5, I6, I7, I8),
        };
    }

    public Object[] getSampleValues() {
        return new Object[] {
            "2A", "2B", "2C",
            "3D", "3E", "3F",
            "4G", "4H", "4I",
            "5J", "5K", "5L",
        };
    }

    public Object[] getNewSampleValues() {
        return new Object[] {
            "1a", "1b", "1c",
            "2d", "2e", "2f",
            "3g", "3h", "3i",
            "4j", "4k", "4l",
        };
    }

    public Object[] getOtherKeys() {
        return new Object[] {
            new MultiKey(I1, I7),
            new MultiKey(I1, I8),
            new MultiKey(I2, I4),
            new MultiKey(I2, I5),
        };
    }
    
    public boolean isAllowNullKey() {
        return false;
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
        assertEquals(null, map.put(new MultiKey(null, null), null));
        try {
            map.put(null, new Object());
            fail();
        } catch (NullPointerException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testMultiKeyGet() {
        resetFull();
        MultiKeyMap multimap = (MultiKeyMap) map;
        MultiKey[] keys = getMultiKeyKeys();
        Object[] values = getSampleValues();
        
        for (int i = 0; i < keys.length; i++) {
            MultiKey key = keys[i];
            Object value = values[i];
            
            switch (key.size()) {
                case 2:
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1)));
                assertEquals(null, multimap.get(null, key.getKey(1)));
                assertEquals(null, multimap.get(key.getKey(0), null));
                assertEquals(null, multimap.get(null, null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null, null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null, null, null));
                break;
                case 3:
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(null, multimap.get(null, key.getKey(1), key.getKey(2)));
                assertEquals(null, multimap.get(key.getKey(0), null, key.getKey(2)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null));
                assertEquals(null, multimap.get(null, null, null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), null, null));
                break;
                case 4:
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(null, multimap.get(null, key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(null, multimap.get(key.getKey(0), null, key.getKey(2), key.getKey(3)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null, key.getKey(3)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(null, multimap.get(null, null, null, null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                break;
                case 5:
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(null, multimap.get(null, key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(null, multimap.get(key.getKey(0), null, key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null, key.getKey(3), key.getKey(4)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), null, key.getKey(4)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                assertEquals(null, multimap.get(null, null, null, null, null));
                break;
                default:
                fail("Invalid key size");
            }
        }
    }
    
    public void testMultiKeyContainsKey() {
        resetFull();
        MultiKeyMap multimap = (MultiKeyMap) map;
        MultiKey[] keys = getMultiKeyKeys();
        Object[] values = getSampleValues();
        
        for (int i = 0; i < keys.length; i++) {
            MultiKey key = keys[i];
            Object value = values[i];
            
            switch (key.size()) {
                case 2:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1)));
                assertEquals(false, multimap.containsKey(null, key.getKey(1)));
                assertEquals(false, multimap.containsKey(key.getKey(0), null));
                assertEquals(false, multimap.containsKey(null, null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null, null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null, null, null));
                break;
                case 3:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(false, multimap.containsKey(null, key.getKey(1), key.getKey(2)));
                assertEquals(false, multimap.containsKey(key.getKey(0), null, key.getKey(2)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null));
                assertEquals(false, multimap.containsKey(null, null, null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), null, null));
                break;
                case 4:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(null, key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), null, key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null, key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(false, multimap.containsKey(null, null, null, null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                break;
                case 5:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(null, key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), null, key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null, key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), null, key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                assertEquals(false, multimap.containsKey(null, null, null, null, null));
                break;
                default:
                fail("Invalid key size");
            }
        }
    }
    
    public void testMultiKeyPut() {
        MultiKey[] keys = getMultiKeyKeys();
        Object[] values = getSampleValues();
        
        for (int i = 0; i < keys.length; i++) {
            MultiKeyMap multimap = new MultiKeyMap();
            
            MultiKey key = keys[i];
            Object value = values[i];
            
            switch (key.size()) {
                case 2:
                assertEquals(null, multimap.put(key.getKey(0), key.getKey(1), value));
                assertEquals(1, multimap.size());
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1)));
                assertEquals(true, multimap.containsKey(new MultiKey(key.getKey(0), key.getKey(1))));
                assertEquals(value, multimap.put(key.getKey(0), key.getKey(1), null));
                assertEquals(1, multimap.size());
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1)));
                break;
                case 3:
                assertEquals(null, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), value));
                assertEquals(1, multimap.size());
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(true, multimap.containsKey(new MultiKey(key.getKey(0), key.getKey(1), key.getKey(2))));
                assertEquals(value, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(1, multimap.size());
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                break;
                case 4:
                assertEquals(null, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), value));
                assertEquals(1, multimap.size());
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(true, multimap.containsKey(new MultiKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3))));
                assertEquals(value, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                assertEquals(1, multimap.size());
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                break;
                case 5:
                assertEquals(null, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4), value));
                assertEquals(1, multimap.size());
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(true, multimap.containsKey(new MultiKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4))));
                assertEquals(value, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4), null));
                assertEquals(1, multimap.size());
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                break;
                default:
                fail("Invalid key size");
            }
        }
    }
    
    public void testMultiKeyRemove() {
        MultiKey[] keys = getMultiKeyKeys();
        Object[] values = getSampleValues();
        
        for (int i = 0; i < keys.length; i++) {
            resetFull();
            MultiKeyMap multimap = (MultiKeyMap) map;
            int size = multimap.size();
            
            MultiKey key = keys[i];
            Object value = values[i];
            
            switch (key.size()) {
                case 2:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1)));
                assertEquals(value, multimap.remove(key.getKey(0), key.getKey(1)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1)));
                assertEquals(size - 1, multimap.size());
                assertEquals(null, multimap.remove(key.getKey(0), key.getKey(1)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1)));
                break;
                case 3:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(value, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(size - 1, multimap.size());
                assertEquals(null, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                break;
                case 4:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(value, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(size - 1, multimap.size());
                assertEquals(null, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                break;
                case 5:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(value, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(size - 1, multimap.size());
                assertEquals(null, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                break;
                default:
                fail("Invalid key size");
            }
        }
    }
    
    public void testMultiKeyRemoveAll1() {
        resetFull();
        MultiKeyMap multimap = (MultiKeyMap) map;
        assertEquals(12, multimap.size());
        
        multimap.removeAll(I1);
        assertEquals(8, multimap.size());
        for (MapIterator it = multimap.mapIterator(); it.hasNext();) {
            MultiKey key = (MultiKey) it.next();
            assertEquals(false, I1.equals(key.getKey(0)));
        }
    }
    
    public void testMultiKeyRemoveAll2() {
        resetFull();
        MultiKeyMap multimap = (MultiKeyMap) map;
        assertEquals(12, multimap.size());
        
        multimap.removeAll(I2, I3);
        assertEquals(9, multimap.size());
        for (MapIterator it = multimap.mapIterator(); it.hasNext();) {
            MultiKey key = (MultiKey) it.next();
            assertEquals(false, I2.equals(key.getKey(0)) && I3.equals(key.getKey(1)));
        }
    }
    
    public void testMultiKeyRemoveAll3() {
        resetFull();
        MultiKeyMap multimap = (MultiKeyMap) map;
        assertEquals(12, multimap.size());
        
        multimap.removeAll(I1, I1, I2);
        assertEquals(9, multimap.size());
        for (MapIterator it = multimap.mapIterator(); it.hasNext();) {
            MultiKey key = (MultiKey) it.next();
            assertEquals(false, I1.equals(key.getKey(0)) && I1.equals(key.getKey(1)) && I2.equals(key.getKey(2)));
        }
    }
    
    public void testMultiKeyRemoveAll4() {
        resetFull();
        MultiKeyMap multimap = (MultiKeyMap) map;
        assertEquals(12, multimap.size());
        
        multimap.removeAll(I1, I1, I2, I3);
        assertEquals(10, multimap.size());
        for (MapIterator it = multimap.mapIterator(); it.hasNext();) {
            MultiKey key = (MultiKey) it.next();
            assertEquals(false, I1.equals(key.getKey(0)) && I1.equals(key.getKey(1)) && I2.equals(key.getKey(2)) && key.size() >= 4 && I3.equals(key.getKey(3)));
        }
    }
    
    //-----------------------------------------------------------------------
    public void testClone() {
        MultiKeyMap map = new MultiKeyMap();
        map.put(new MultiKey(I1, I2), "1-2");
        Map cloned = (Map) map.clone();
        assertEquals(map.size(), cloned.size());
        assertSame(map.get(new MultiKey(I1, I2)), cloned.get(new MultiKey(I1, I2)));
    }

    //-----------------------------------------------------------------------
    public void testLRUMultiKeyMap() {
        MultiKeyMap map = MultiKeyMap.decorate(new LRUMap(2));
        map.put(I1, I2, "1-2");
        map.put(I1, I3, "1-3");
        assertEquals(2, map.size());
        map.put(I1, I4, "1-4");
        assertEquals(2, map.size());
        assertEquals(true, map.containsKey(I1, I3));
        assertEquals(true, map.containsKey(I1, I4));
        assertEquals(false, map.containsKey(I1, I2));
        
        MultiKeyMap cloned = (MultiKeyMap) map.clone();
        assertEquals(2, map.size());
        assertEquals(true, cloned.containsKey(I1, I3));
        assertEquals(true, cloned.containsKey(I1, I4));
        assertEquals(false, cloned.containsKey(I1, I2));
        cloned.put(I1, I5, "1-5");
        assertEquals(2, cloned.size());
        assertEquals(true, cloned.containsKey(I1, I4));
        assertEquals(true, cloned.containsKey(I1, I5));
    }

    //-----------------------------------------------------------------------
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/MultiKeyMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/MultiKeyMap.fullCollection.version3.1.obj");
//    }
}
