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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.SortedBidiMap;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * JUnit tests.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Matthew Hawthorne
 * @author Stephen Colebourne
 * @author Jonas Van Poucke
 */
public class TestDualTreeBidiMap2 extends AbstractTestSortedBidiMap {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        return BulkTest.makeSuite(TestDualTreeBidiMap2.class);
    }

    public TestDualTreeBidiMap2(String testName) {
        super(testName);
    }

    public BidiMap makeEmptyBidiMap() {
        return new DualTreeBidiMap(new ReverseComparator(ComparableComparator.getInstance()));
    }

    public Map makeConfirmedMap() {
        return new TreeMap(new ReverseComparator(ComparableComparator.getInstance()));
    }

    public void testComparator() {
        resetEmpty();
        SortedBidiMap bidi = (SortedBidiMap) map;
        assertNotNull(bidi.comparator());
        assertTrue(bidi.comparator() instanceof ReverseComparator);
    }

    public void testSerializeDeserializeCheckComparator() throws Exception {
        SortedBidiMap obj = (SortedBidiMap) makeObject();
        if (obj instanceof Serializable && isTestSerialization()) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            out.writeObject(obj);
            out.close();

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            Object dest = in.readObject();
            in.close();
            
            SortedBidiMap bidi = (SortedBidiMap) dest;
            assertNotNull(obj.comparator());
            assertNotNull(bidi.comparator());
            assertTrue(bidi.comparator() instanceof ReverseComparator);
        }
    }

    public void testSortOrder() throws Exception {
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();

        // Sort by the comparator used in the makeEmptyBidiMap() method
        List newSortedKeys = Arrays.asList(getSampleKeys());
        Collections.sort(newSortedKeys, new ReverseComparator(ComparableComparator.getInstance()));
        newSortedKeys = Collections.unmodifiableList(newSortedKeys);

        Iterator mapIter = sm.keySet().iterator();
        Iterator expectedIter = newSortedKeys.iterator();
        while (expectedIter.hasNext()) {
            Object expectedKey = expectedIter.next();
            Object mapKey = mapIter.next();
            assertNotNull("key in sorted list may not be null", expectedKey);
            assertNotNull("key in map may not be null", mapKey);
            assertEquals("key from sorted list and map must be equal", expectedKey, mapKey);
        }
    }

    public String getCompatibilityVersion() {
        return "3.Test2";
    }

    /**
     * Override to prevent infinite recursion of tests.
     */
    public String[] ignoredTests() {
        return new String[] {"TestDualTreeBidiMap2.bulkTestInverseMap.bulkTestInverseMap"};
    }
    
//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) map, "D:/dev/collections/data/test/DualTreeBidiMap.emptyCollection.version3.Test2.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) map, "D:/dev/collections/data/test/DualTreeBidiMap.fullCollection.version3.Test2.obj");
//    }
}
