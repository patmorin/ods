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

import junit.framework.TestCase;

/**
 * Abstract tests that can be extended to test any Map.Entry implementation.
 * Subclasses must implement {@link #makeMapEntry(Object, Object)} to return
 * a new Map.Entry of the type being tested. Subclasses must also implement
 * {@link #testConstructors()} to test the constructors of the Map.Entry
 * type being tested.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Neil O'Toole
 */
public abstract class AbstractTestMapEntry extends TestCase {
    
    protected final String key = "name";
    protected final String value = "duke";

    /**
     * JUnit constructor.
     * 
     * @param testName  the test name
     */
    public AbstractTestMapEntry(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------
    /**
     * Make an instance of Map.Entry with the default (null) key and value.
     * This implementation simply calls {@link #makeMapEntry(Object, Object)}
     * with null for key and value. Subclasses can override this method if desired.
     */
    public Map.Entry makeMapEntry() {
        return makeMapEntry(null, null);
    }

    /**
     * Make an instance of Map.Entry with the specified key and value.
     * Subclasses should override this method to return a Map.Entry
     * of the type being tested.
     */
    public abstract Map.Entry makeMapEntry(Object key, Object value);

    /**
     * Makes a Map.Entry of a type that's known to work correctly.
     */
    public Map.Entry makeKnownMapEntry() {
        return makeKnownMapEntry(null, null);
    }

    /**
     * Makes a Map.Entry of a type that's known to work correctly.
     */
    public Map.Entry makeKnownMapEntry(Object key, Object value) {
        Map map = new HashMap(1);
        map.put(key, value);
        Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
        return entry;
    }

    //-----------------------------------------------------------------------
    public void testAccessorsAndMutators() {
        Map.Entry entry = makeMapEntry(key, value);

        assertTrue(entry.getKey() == key);

        entry.setValue(value);
        assertTrue(entry.getValue() == value);

        // check that null doesn't do anything funny
        entry = makeMapEntry(null, null);
        assertTrue(entry.getKey() == null);

        entry.setValue(null);
        assertTrue(entry.getValue() == null);
    }

    /**
     * Subclasses should override this method to test the
     * desired behaviour of the class with respect to
     * handling of self-references.
     *
     */

    public void testSelfReferenceHandling() {
        // test that #setValue does not permit
        //  the MapEntry to contain itself (and thus cause infinite recursion
        //  in #hashCode and #toString)

        Map.Entry entry = makeMapEntry();

        try {
            entry.setValue(entry);
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // expected to happen...

            // check that the KVP's state has not changed
            assertTrue(entry.getKey() == null && entry.getValue() == null);
        }
    }

    /**
     * Subclasses should provide tests for their constructors.
     *
     */
    public abstract void testConstructors();

    public void testEqualsAndHashCode() {
        // 1. test with object data
        Map.Entry e1 = makeMapEntry(key, value);
        Map.Entry e2 = makeKnownMapEntry(key, value);

        assertTrue(e1.equals(e1));
        assertTrue(e2.equals(e1));
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());

        // 2. test with nulls
        e1 = makeMapEntry();
        e2 = makeKnownMapEntry();

        assertTrue(e1.equals(e1));
        assertTrue(e2.equals(e1));
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    public void testToString() {
        Map.Entry entry = makeMapEntry(key, value);
        assertTrue(entry.toString().equals(entry.getKey() + "=" + entry.getValue()));

        // test with nulls
        entry = makeMapEntry();
        assertTrue(entry.toString().equals(entry.getKey() + "=" + entry.getValue()));
    }

}
