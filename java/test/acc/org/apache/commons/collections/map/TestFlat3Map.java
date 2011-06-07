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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.iterators.AbstractTestMapIterator;

/**
 * JUnit tests.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestFlat3Map extends AbstractTestIterableMap {

    private static final Integer ONE = new Integer(1);
    private static final Integer TWO = new Integer(2);
    private static final String TEN = "10";
    private static final String TWENTY = "20";
        
    public TestFlat3Map(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        return BulkTest.makeSuite(TestFlat3Map.class);
    }

    public Map makeEmptyMap() {
        return new Flat3Map();
    }

    //-----------------------------------------------------------------------
    public void testEquals1() {
        Flat3Map map1 = new Flat3Map();
        map1.put("a", "testA");
        map1.put("b", "testB");
        Flat3Map map2 = new Flat3Map();
        map2.put("a", "testB");
        map2.put("b", "testA");
        assertEquals(false, map1.equals(map2));
    }

    public void testEquals2() {
        Flat3Map map1 = new Flat3Map();
        map1.put("a", "testA");
        map1.put("b", "testB");
        Flat3Map map2 = new Flat3Map();
        map2.put("a", "testB");
        map2.put("c", "testA");
        assertEquals(false, map1.equals(map2));
    }

    public void testClone2() {
        Flat3Map map = new Flat3Map();
        assertEquals(0, map.size());
        map.put(ONE, TEN);
        map.put(TWO, TWENTY);
        assertEquals(2, map.size());
        assertEquals(true, map.containsKey(ONE));
        assertEquals(true, map.containsKey(TWO));
        assertSame(TEN, map.get(ONE));
        assertSame(TWENTY, map.get(TWO));

        // clone works (size = 2)        
        Flat3Map cloned = (Flat3Map) map.clone();
        assertEquals(2, cloned.size());
        assertEquals(true, cloned.containsKey(ONE));
        assertEquals(true, cloned.containsKey(TWO));
        assertSame(TEN, cloned.get(ONE));
        assertSame(TWENTY, cloned.get(TWO));
        
        // change original doesn't change clone
        map.put(TEN, ONE);
        map.put(TWENTY, TWO);
        assertEquals(4, map.size());
        assertEquals(2, cloned.size());
        assertEquals(true, cloned.containsKey(ONE));
        assertEquals(true, cloned.containsKey(TWO));
        assertSame(TEN, cloned.get(ONE));
        assertSame(TWENTY, cloned.get(TWO));
    }        
    public void testClone4() {
        Flat3Map map = new Flat3Map();
        assertEquals(0, map.size());
        map.put(ONE, TEN);
        map.put(TWO, TWENTY);
        map.put(TEN, ONE);
        map.put(TWENTY, TWO);
        
        // clone works (size = 4)
        Flat3Map cloned = (Flat3Map) map.clone();
        assertEquals(4, map.size());
        assertEquals(4, cloned.size());
        assertEquals(true, cloned.containsKey(ONE));
        assertEquals(true, cloned.containsKey(TWO));
        assertEquals(true, cloned.containsKey(TEN));
        assertEquals(true, cloned.containsKey(TWENTY));
        assertSame(TEN, cloned.get(ONE));
        assertSame(TWENTY, cloned.get(TWO));
        assertSame(ONE, cloned.get(TEN));
        assertSame(TWO, cloned.get(TWENTY));
        
        // change original doesn't change clone
        map.clear();
        assertEquals(0, map.size());
        assertEquals(4, cloned.size());
        assertEquals(true, cloned.containsKey(ONE));
        assertEquals(true, cloned.containsKey(TWO));
        assertEquals(true, cloned.containsKey(TEN));
        assertEquals(true, cloned.containsKey(TWENTY));
        assertSame(TEN, cloned.get(ONE));
        assertSame(TWENTY, cloned.get(TWO));
        assertSame(ONE, cloned.get(TEN));
        assertSame(TWO, cloned.get(TWENTY));
    }
    
    public void testSerialisation0() throws Exception {
        Flat3Map map = new Flat3Map();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(map);
        byte[] bytes = bout.toByteArray();
        out.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bin);
        Flat3Map ser = (Flat3Map) in.readObject();
        in.close();
        assertEquals(0, map.size());
        assertEquals(0, ser.size());
    }
    
    public void testSerialisation2() throws Exception {
        Flat3Map map = new Flat3Map();
        map.put(ONE, TEN);
        map.put(TWO, TWENTY);
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(map);
        byte[] bytes = bout.toByteArray();
        out.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bin);
        Flat3Map ser = (Flat3Map) in.readObject();
        in.close();
        assertEquals(2, map.size());
        assertEquals(2, ser.size());
        assertEquals(true, ser.containsKey(ONE));
        assertEquals(true, ser.containsKey(TWO));
        assertEquals(TEN, ser.get(ONE));
        assertEquals(TWENTY, ser.get(TWO));
    }
    
    public void testSerialisation4() throws Exception {
        Flat3Map map = new Flat3Map();
        map.put(ONE, TEN);
        map.put(TWO, TWENTY);
        map.put(TEN, ONE);
        map.put(TWENTY, TWO);
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(map);
        byte[] bytes = bout.toByteArray();
        out.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bin);
        Flat3Map ser = (Flat3Map) in.readObject();
        in.close();
        assertEquals(4, map.size());
        assertEquals(4, ser.size());
        assertEquals(true, ser.containsKey(ONE));
        assertEquals(true, ser.containsKey(TWO));
        assertEquals(true, ser.containsKey(TEN));
        assertEquals(true, ser.containsKey(TWENTY));
        assertEquals(TEN, ser.get(ONE));
        assertEquals(TWENTY, ser.get(TWO));
        assertEquals(ONE, ser.get(TEN));
        assertEquals(TWO, ser.get(TWENTY));
    }
    
    //-----------------------------------------------------------------------
    public BulkTest bulkTestMapIterator() {
        return new TestFlatMapIterator();
    }
    
    public class TestFlatMapIterator extends AbstractTestMapIterator {
        public TestFlatMapIterator() {
            super("TestFlatMapIterator");
        }
        
        public Object[] addSetValues() {
            return TestFlat3Map.this.getNewSampleValues();
        }
        
        public boolean supportsRemove() {
            return TestFlat3Map.this.isRemoveSupported();
        }

        public boolean supportsSetValue() {
            return TestFlat3Map.this.isSetValueSupported();
        }

        public MapIterator makeEmptyMapIterator() {
            resetEmpty();
            return ((Flat3Map) TestFlat3Map.this.map).mapIterator();
        }

        public MapIterator makeFullMapIterator() {
            resetFull();
            return ((Flat3Map) TestFlat3Map.this.map).mapIterator();
        }
        
        public Map getMap() {
            // assumes makeFullMapIterator() called first
            return TestFlat3Map.this.map;
        }
        
        public Map getConfirmedMap() {
            // assumes makeFullMapIterator() called first
            return TestFlat3Map.this.confirmed;
        }
        
        public void verify() {
            super.verify();
            TestFlat3Map.this.verify();
        }
    }
    
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/Flat3Map.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/Flat3Map.fullCollection.version3.1.obj");
//    }
}
