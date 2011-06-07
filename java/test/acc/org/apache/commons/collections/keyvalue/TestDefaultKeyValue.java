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
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test the DefaultKeyValue class.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Neil O'Toole
 */
public class TestDefaultKeyValue extends TestCase {
    
    private final String key = "name";
    private final String value = "duke";

    /**
     * JUnit constructor.
     * 
     * @param testName  the test name
     */
    public TestDefaultKeyValue(String testName) {
        super(testName);

    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestDefaultKeyValue.class);
    }

    public static Test suite() {
        return new TestSuite(TestDefaultKeyValue.class);
    }

    //-----------------------------------------------------------------------
    /**
     * Make an instance of DefaultKeyValue with the default (null) key and value.
     * Subclasses should override this method to return a DefaultKeyValue
     * of the type being tested.
     */
    protected DefaultKeyValue makeDefaultKeyValue() {
        return new DefaultKeyValue(null, null);
    }

    /**
     * Make an instance of DefaultKeyValue with the specified key and value.
     * Subclasses should override this method to return a DefaultKeyValue
     * of the type being tested.
     */
    protected DefaultKeyValue makeDefaultKeyValue(Object key, Object value) {
        return new DefaultKeyValue(key, value);
    }

    //-----------------------------------------------------------------------
    public void testAccessorsAndMutators() {
        DefaultKeyValue kv = makeDefaultKeyValue();

        kv.setKey(key);
        assertTrue(kv.getKey() == key);

        kv.setValue(value);
        assertTrue(kv.getValue() == value);

        // check that null doesn't do anything funny
        kv.setKey(null);
        assertTrue(kv.getKey() == null);

        kv.setValue(null);
        assertTrue(kv.getValue() == null);

    }

    public void testSelfReferenceHandling() {
        // test that #setKey and #setValue do not permit
        //  the KVP to contain itself (and thus cause infinite recursion
        //  in #hashCode and #toString)

        DefaultKeyValue kv = makeDefaultKeyValue();

        try {
            kv.setKey(kv);
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // expected to happen...

            // check that the KVP's state has not changed
            assertTrue(kv.getKey() == null && kv.getValue() == null);
        }

        try {
            kv.setValue(kv);
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // expected to happen...

            // check that the KVP's state has not changed
            assertTrue(kv.getKey() == null && kv.getValue() == null);
        }
    }

    /**
     * Subclasses should override this method to test their own constructors.
     */
    public void testConstructors() {
        // 1. test default constructor
        DefaultKeyValue kv = new DefaultKeyValue();
        assertTrue(kv.getKey() == null && kv.getValue() == null);

        // 2. test key-value constructor
        kv = new DefaultKeyValue(key, value);
        assertTrue(kv.getKey() == key && kv.getValue() == value);

        // 3. test copy constructor
        DefaultKeyValue kv2 = new DefaultKeyValue(kv);
        assertTrue(kv2.getKey() == key && kv2.getValue() == value);

        // test that the KVPs are independent
        kv.setKey(null);
        kv.setValue(null);

        assertTrue(kv2.getKey() == key && kv2.getValue() == value);

        // 4. test Map.Entry constructor
        Map map = new HashMap();
        map.put(key, value);
        Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();

        kv = new DefaultKeyValue(entry);
        assertTrue(kv.getKey() == key && kv.getValue() == value);

        // test that the KVP is independent of the Map.Entry
        entry.setValue(null);
        assertTrue(kv.getValue() == value);

    }

    public void testEqualsAndHashCode() {
        // 1. test with object data
        DefaultKeyValue kv = makeDefaultKeyValue(key, value);
        DefaultKeyValue kv2 = makeDefaultKeyValue(key, value);

        assertTrue(kv.equals(kv));
        assertTrue(kv.equals(kv2));
        assertTrue(kv.hashCode() == kv2.hashCode());

        // 2. test with nulls
        kv = makeDefaultKeyValue(null, null);
        kv2 = makeDefaultKeyValue(null, null);

        assertTrue(kv.equals(kv));
        assertTrue(kv.equals(kv2));
        assertTrue(kv.hashCode() == kv2.hashCode());
    }

    public void testToString() {
        DefaultKeyValue kv = makeDefaultKeyValue(key, value);
        assertTrue(kv.toString().equals(kv.getKey() + "=" + kv.getValue()));

        // test with nulls
        kv = makeDefaultKeyValue(null, null);
        assertTrue(kv.toString().equals(kv.getKey() + "=" + kv.getValue()));
    }

    public void testToMapEntry() {
        DefaultKeyValue kv = makeDefaultKeyValue(key, value);

        Map map = new HashMap();
        map.put(kv.getKey(), kv.getValue());
        Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();

        assertTrue(entry.equals(kv.toMapEntry()));
        assertTrue(entry.hashCode() == kv.hashCode());
    }

}
