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

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.KeyValue;

/**
 * Test the DefaultMapEntry class.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Neil O'Toole
 */
public class TestDefaultMapEntry extends AbstractTestMapEntry {

    public TestDefaultMapEntry(String testName) {
        super(testName);

    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestDefaultMapEntry.class);
    }

    public static Test suite() {
        return new TestSuite(TestDefaultMapEntry.class);
    }

    //-----------------------------------------------------------------------
    /**
     * Make an instance of Map.Entry with the default (null) key and value.
     * Subclasses should override this method to return a Map.Entry
     * of the type being tested.
     */
    public Map.Entry makeMapEntry() {
        return new DefaultMapEntry(null, null);
    }

    /**
     * Make an instance of Map.Entry with the specified key and value.
     * Subclasses should override this method to return a Map.Entry
     * of the type being tested.
     */
    public Map.Entry makeMapEntry(Object key, Object value) {
        return new DefaultMapEntry(key, value);
    }

    //-----------------------------------------------------------------------
    /**
     * Subclasses should override this method.
     *
     */
    public void testConstructors() {
        // 1. test key-value constructor
        Map.Entry entry = new DefaultMapEntry(key, value);
        assertSame(key, entry.getKey());
        assertSame(value, entry.getValue());

        // 2. test pair constructor
        KeyValue pair = new DefaultKeyValue(key, value);
        assertSame(key, pair.getKey());
        assertSame(value, pair.getValue());

        // 3. test copy constructor
        Map.Entry entry2 = new DefaultMapEntry(entry);
        assertSame(key, entry2.getKey());
        assertSame(value, entry2.getValue());

        // test that the objects are independent
        entry.setValue(null);
        assertSame(value, entry2.getValue());
    }

    public void testSelfReferenceHandling() {
        Map.Entry entry = makeMapEntry();

        try {
            entry.setValue(entry);
            assertSame(entry, entry.getValue());

        } catch (Exception e) {
            fail("This Map.Entry implementation supports value self-reference.");
        }
    }

}
