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
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.collections.collection.TestTransformedCollection;

/**
 * Extension of {@link AbstractTestSortedMap} for exercising the {@link TransformedSortedMap}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestTransformedSortedMap extends AbstractTestSortedMap {
    
    public TestTransformedSortedMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestTransformedSortedMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestTransformedSortedMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    //-----------------------------------------------------------------------
    public Map makeEmptyMap() {
        return TransformedSortedMap.decorate(new TreeMap(), TransformerUtils.nopTransformer(), TransformerUtils.nopTransformer());
    }

    public boolean isSubMapViewsSerializable() {
        // TreeMap sub map views have a bug in deserialization.
        return false;
    }

    //-----------------------------------------------------------------------    
    public void testTransformedMap() {
        Object[] els = new Object[] {"1", "3", "5", "7", "2", "4", "6"};

        Map map = TransformedSortedMap.decorate(new TreeMap(), TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER, null);
        assertEquals(0, map.size());
        for (int i = 0; i < els.length; i++) {
            map.put(els[i], els[i]);
            assertEquals(i + 1, map.size());
            assertEquals(true, map.containsKey(new Integer((String) els[i])));
            try {
                map.containsKey(els[i]);
                fail();
            } catch (ClassCastException ex) {}
            assertEquals(true, map.containsValue(els[i]));
            assertEquals(els[i], map.get(new Integer((String) els[i])));
        }
        
        try {
            map.remove(els[0]);
            fail();
        } catch (ClassCastException ex) {}
        assertEquals(els[0], map.remove(new Integer((String) els[0])));
        
        map = TransformedSortedMap.decorate(new TreeMap(), null, TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, map.size());
        for (int i = 0; i < els.length; i++) {
            map.put(els[i], els[i]);
            assertEquals(i + 1, map.size());
            assertEquals(true, map.containsValue(new Integer((String) els[i])));
            assertEquals(false, map.containsValue(els[i]));
            assertEquals(true, map.containsKey(els[i]));
            assertEquals(new Integer((String) els[i]), map.get(els[i]));
        }

        assertEquals(new Integer((String) els[0]), map.remove(els[0]));
        
        Set entrySet = map.entrySet();
        Map.Entry[] array = (Map.Entry[]) entrySet.toArray(new Map.Entry[0]);
        array[0].setValue("66");
        assertEquals(new Integer(66), array[0].getValue());
        assertEquals(new Integer(66), map.get(array[0].getKey()));
        
        Map.Entry entry = (Map.Entry) entrySet.iterator().next();
        entry.setValue("88");
        assertEquals(new Integer(88), entry.getValue());
        assertEquals(new Integer(88), map.get(entry.getKey()));
    }

    //-----------------------------------------------------------------------
    public void testFactory_Decorate() {
        SortedMap base = new TreeMap();
        base.put("A", "1");
        base.put("B", "2");
        base.put("C", "3");
        
        SortedMap trans = TransformedSortedMap.decorate(base, null, TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(3, trans.size());
        assertEquals("1", trans.get("A"));
        assertEquals("2", trans.get("B"));
        assertEquals("3", trans.get("C"));
        trans.put("D", "4");
        assertEquals(new Integer(4), trans.get("D"));
    }

    public void testFactory_decorateTransform() {
        SortedMap base = new TreeMap();
        base.put("A", "1");
        base.put("B", "2");
        base.put("C", "3");
        
        SortedMap trans = TransformedSortedMap.decorateTransform(base, null, TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(3, trans.size());
        assertEquals(new Integer(1), trans.get("A"));
        assertEquals(new Integer(2), trans.get("B"));
        assertEquals(new Integer(3), trans.get("C"));
        trans.put("D", "4");
        assertEquals(new Integer(4), trans.get("D"));
    }

    //-----------------------------------------------------------------------
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/TransformedSortedMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/TransformedSortedMap.fullCollection.version3.1.obj");
//    }
}
