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
package org.apache.commons.collections.keyvalue;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test the TiedMapEntry class.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestTiedMapEntry extends AbstractTestMapEntry {

    public TestTiedMapEntry(String testName) {
        super(testName);

    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestTiedMapEntry.class);
    }

    public static Test suite() {
        return new TestSuite(TestTiedMapEntry.class);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the instance to test
     */
    public Map.Entry makeMapEntry(Object key, Object value) {
        Map map = new HashMap();
        map.put(key, value);
        return new TiedMapEntry(map, key);
    }

    //-----------------------------------------------------------------------
    /**
     * Tests the constructors.
     */
    public void testConstructors() {
        // ignore
    }

    /**
     * Tests the constructors.
     */
    public void testSetValue() {
        Map map = new HashMap();
        map.put("A", "a");
        map.put("B", "b");
        map.put("C", "c");
        Map.Entry entry = new TiedMapEntry(map, "A");
        assertSame("A", entry.getKey());
        assertSame("a", entry.getValue());
        assertSame("a", entry.setValue("x"));
        assertSame("A", entry.getKey());
        assertSame("x", entry.getValue());
        
        entry = new TiedMapEntry(map, "B");
        assertSame("B", entry.getKey());
        assertSame("b", entry.getValue());
        assertSame("b", entry.setValue("y"));
        assertSame("B", entry.getKey());
        assertSame("y", entry.getValue());
        
        entry = new TiedMapEntry(map, "C");
        assertSame("C", entry.getKey());
        assertSame("c", entry.getValue());
        assertSame("c", entry.setValue("z"));
        assertSame("C", entry.getKey());
        assertSame("z", entry.getValue());
    }

}
