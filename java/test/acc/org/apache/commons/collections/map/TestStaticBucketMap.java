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

import org.apache.commons.collections.BulkTest;

/**
 * Unit tests.
 * {@link StaticBucketMap}.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Michael A. Smith
 */
public class TestStaticBucketMap extends AbstractTestMap {

    public TestStaticBucketMap(String name) {
        super(name);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestStaticBucketMap.class);
    }

    public static void main(String[] args) {
        String[] testCaseName = { TestStaticBucketMap.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        return new StaticBucketMap(30);
    }

    public String[] ignoredTests() {
        String pre = "TestStaticBucketMap.bulkTestMap";
        String post = ".testCollectionIteratorFailFast";
        return new String[] {
            pre + "EntrySet" + post,
            pre + "KeySet" + post,
            pre + "Values" + post
        };
    }

    // Bugzilla 37567
    public void test_get_nullMatchesIncorrectly() {
        StaticBucketMap map = new StaticBucketMap(17);
        map.put(null, "A");
        assertEquals("A", map.get(null));
        // loop so we find a string that is in the same bucket as the null
        for (int i = 'A'; i <= 'Z'; i++) {
            String str = String.valueOf((char) i);
            assertEquals("String: " + str, null, map.get(str));
        }
    }

    public void test_containsKey_nullMatchesIncorrectly() {
        StaticBucketMap map = new StaticBucketMap(17);
        map.put(null, "A");
        assertEquals(true, map.containsKey(null));
        // loop so we find a string that is in the same bucket as the null
        for (int i = 'A'; i <= 'Z'; i++) {
            String str = String.valueOf((char) i);
            assertEquals("String: " + str, false, map.containsKey(str));
        }
    }

    public void test_containsValue_nullMatchesIncorrectly() {
        StaticBucketMap map = new StaticBucketMap(17);
        map.put("A", null);
        assertEquals(true, map.containsValue(null));
        // loop so we find a string that is in the same bucket as the null
        for (int i = 'A'; i <= 'Z'; i++) {
            String str = String.valueOf((char) i);
            assertEquals("String: " + str, false, map.containsValue(str));
        }
    }

}
