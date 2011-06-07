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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.map.Flat3Map;

/** 
 * <code>TestMapPerformance</code> is designed to perform basic Map performance tests.
 *
 * @author Stephen Colebourne
 */
public class MapPerformance {

    /** The total number of runs for each test */    
    private static final int RUNS = 20000000;
    
    /**
     * Main method
     */
    public static void main(String[] args) {
        testAll();
    }
    
    private static void testAll() {
        Map dummyMap = new DummyMap();
        Map hashMap = new HashMap();
//        hashMap.put("Alpha", "A");
//        hashMap.put("Beta", "B");
//        hashMap.put("Gamma", "C");
//        hashMap.put("Delta", "D");
        Map flatMap = new Flat3Map(hashMap);
        System.out.println(flatMap);
        Map unmodHashMap = Collections.unmodifiableMap(new HashMap(hashMap));
        Map fastHashMap = new FastHashMap(hashMap);
        Map treeMap = new TreeMap(hashMap);
        Map seqMap = new SequencedHashMap(hashMap);
//        Map linkedMap = new LinkedHashMap(hashMap);
//        Map syncMap = Collections.unmodifiableMap(new HashMap(hashMap));
//        Map bucketMap = new StaticBucketMap();
//        bucketMap.putAll(hashMap);
//        Map doubleMap = new DoubleOrderedMap(hashMap);
        
        // dummy is required as the VM seems to hotspot the first call to the
        // test method with the given type
        test(dummyMap,      "         Dummy ");
        test(dummyMap,      "         Dummy ");
        test(dummyMap,      "         Dummy ");
        test(flatMap,       "         Flat3 ");
        test(hashMap,       "       HashMap ");
        
        test(flatMap,       "         Flat3 ");
        test(flatMap,       "         Flat3 ");
        test(flatMap,       "         Flat3 ");
        
        test(hashMap,       "       HashMap ");
        test(hashMap,       "       HashMap ");
        test(hashMap,       "       HashMap ");
        
//        test(treeMap,       "       TreeMap ");
//        test(treeMap,       "       TreeMap ");
//        test(treeMap,       "       TreeMap ");
        
//        test(unmodHashMap,  "Unmod(HashMap) ");
//        test(unmodHashMap,  "Unmod(HashMap) ");
//        test(unmodHashMap,  "Unmod(HashMap) ");
//        
//        test(syncMap,       " Sync(HashMap) ");
//        test(syncMap,       " Sync(HashMap) ");
//        test(syncMap,       " Sync(HashMap) ");
//        
//        test(fastHashMap,   "   FastHashMap ");
//        test(fastHashMap,   "   FastHashMap ");
//        test(fastHashMap,   "   FastHashMap ");
//        
//        test(seqMap,        "    SeqHashMap ");
//        test(seqMap,        "    SeqHashMap ");
//        test(seqMap,        "    SeqHashMap ");
//        
//        test(linkedMap,     " LinkedHashMap ");
//        test(linkedMap,     " LinkedHashMap ");
//        test(linkedMap,     " LinkedHashMap ");
//        
//        test(bucketMap,     "     BucketMap ");
//        test(bucketMap,     "     BucketMap ");
//        test(bucketMap,     "     BucketMap ");
//        
//        test(doubleMap,     "     DoubleMap ");
//        test(doubleMap,     "     DoubleMap ");
//        test(doubleMap,     "     DoubleMap ");
    }

    private static void test(Map map, String name) {
        long start = 0, end = 0;
        int total = 0;
        start = System.currentTimeMillis();
        for (int i = RUNS; i > 0; i--) {
//            if (map.get("Alpha") != null) total++;
//            if (map.get("Beta") != null) total++;
//            if (map.get("Gamma") != null) total++;
            map.put("Alpha", "A");
            map.put("Beta", "B");
            map.put("Beta", "C");
            map.put("Gamma", "D");
//            map.remove("Gamma");
//            map.remove("Beta");
//            map.remove("Alpha");
            map.put("Delta", "E");
            map.clear();
        }
        end = System.currentTimeMillis();
        System.out.println(name + (end - start));
    }

    // ----------------------------------------------------------------------

    private static class DummyMap implements Map {
        public void clear() {
        }
        public boolean containsKey(Object key) {
            return false;
        }
        public boolean containsValue(Object value) {
            return false;
        }
        public Set entrySet() {
            return null;
        }
        public Object get(Object key) {
            return null;
        }
        public boolean isEmpty() {
            return false;
        }
        public Set keySet() {
            return null;
        }
        public Object put(Object key, Object value) {
            return null;
        }
        public void putAll(Map t) {
        }
        public Object remove(Object key) {
            return null;
        }
        public int size() {
            return 0;
        }
        public Collection values() {
            return null;
        }
    }
    
}

