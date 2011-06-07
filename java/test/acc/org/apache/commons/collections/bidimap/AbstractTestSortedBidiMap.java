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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.SortedBidiMap;
import org.apache.commons.collections.map.AbstractTestSortedMap;

/**
 * Abstract test class for {@link SortedBidiMap} methods and contracts.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Matthew Hawthorne
 * @author Stephen Colebourne
 */
public abstract class AbstractTestSortedBidiMap extends AbstractTestOrderedBidiMap {

    protected List sortedKeys = new ArrayList();
    protected List sortedValues = new ArrayList();
    protected SortedSet sortedNewValues = new TreeSet();

    public AbstractTestSortedBidiMap(String testName) {
        super(testName);
        sortedKeys.addAll(Arrays.asList(getSampleKeys()));
        Collections.sort(sortedKeys);
        sortedKeys = Collections.unmodifiableList(sortedKeys);
        
        Map map = new TreeMap();
        for (int i = 0; i < getSampleKeys().length; i++) {
            map.put(getSampleKeys()[i], getSampleValues()[i]);
        }
        sortedValues.addAll(map.values());
        sortedValues = Collections.unmodifiableList(sortedValues);
        
        sortedNewValues.addAll(Arrays.asList(getNewSampleValues()));
    }

    public AbstractTestSortedBidiMap() {
        super();
        sortedKeys.addAll(Arrays.asList(getSampleValues()));
        Collections.sort(sortedKeys);
        sortedKeys = Collections.unmodifiableList(sortedKeys);
        
        Map map = new TreeMap();
        for (int i = 0; i < getSampleKeys().length; i++) {
            map.put(getSampleValues()[i], getSampleKeys()[i]);
        }
        sortedValues.addAll(map.values());
        sortedValues = Collections.unmodifiableList(sortedValues);
        
        sortedNewValues.addAll(Arrays.asList(getNewSampleValues()));
    }

    //-----------------------------------------------------------------------
    public boolean isAllowNullKey() {
        return false;
    }
    public boolean isAllowNullValue() {
        return false;
    }
    public Map makeConfirmedMap() {
        return new TreeMap();
    }

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    public void testBidiHeadMapContains() {
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        Object first = it.next();
        Object toKey = it.next();
        Object second = it.next();
        Object firstValue = sm.get(first);
        Object secondValue = sm.get(second);
        
        SortedMap head = sm.headMap(toKey);
        assertEquals(1, head.size());
        assertEquals(true, sm.containsKey(first));
        assertEquals(true, head.containsKey(first));
        assertEquals(true, sm.containsValue(firstValue));
        assertEquals(true, head.containsValue(firstValue));
        assertEquals(true, sm.containsKey(second));
        assertEquals(false, head.containsKey(second));
        assertEquals(true, sm.containsValue(secondValue));
        assertEquals(false, head.containsValue(secondValue));
    }
                
    //-----------------------------------------------------------------------
    public void testBidiClearByHeadMap() {
        if (isRemoveSupported() == false) return;
        
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        Object first = it.next();
        Object second = it.next();
        Object toKey = it.next();
        
        Object firstValue = sm.get(first);
        Object secondValue = sm.get(second);
        Object toKeyValue = sm.get(toKey);
        
        SortedMap sub = sm.headMap(toKey);
        int size = sm.size();
        assertEquals(2, sub.size());
        sub.clear();
        assertEquals(0, sub.size());
        assertEquals(size - 2, sm.size());
        assertEquals(size - 2, sm.inverseBidiMap().size());
        
        assertEquals(false, sm.containsKey(first));
        assertEquals(false, sm.containsValue(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(first));
        assertEquals(false, sub.containsKey(first));
        assertEquals(false, sub.containsValue(firstValue));
        
        assertEquals(false, sm.containsKey(second));
        assertEquals(false, sm.containsValue(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(second));
        assertEquals(false, sub.containsKey(second));
        assertEquals(false, sub.containsValue(secondValue));
        
        assertEquals(true, sm.containsKey(toKey));
        assertEquals(true, sm.containsValue(toKeyValue));
        assertEquals(true, sm.inverseBidiMap().containsKey(toKeyValue));
        assertEquals(true, sm.inverseBidiMap().containsValue(toKey));
        assertEquals(false, sub.containsKey(toKey));
        assertEquals(false, sub.containsValue(toKeyValue));
    }

    //-----------------------------------------------------------------------
    public void testBidiRemoveByHeadMap() {
        if (isRemoveSupported() == false) return;
        
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        Object first = it.next();
        Object second = it.next();
        Object toKey = it.next();
        
        int size = sm.size();
        SortedMap sub = sm.headMap(toKey);
        assertEquals(2, sub.size());
        assertEquals(true, sm.containsKey(first));
        assertEquals(true, sub.containsKey(first));
        assertEquals(true, sm.containsKey(second));
        assertEquals(true, sub.containsKey(second));
        
        Object firstValue = sub.remove(first);
        assertEquals(1, sub.size());
        assertEquals(size - 1, sm.size());
        assertEquals(size - 1, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(first));
        assertEquals(false, sm.containsValue(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(first));
        assertEquals(false, sub.containsKey(first));
        assertEquals(false, sub.containsValue(firstValue));
        
        Object secondValue = sub.remove(second);
        assertEquals(0, sub.size());
        assertEquals(size - 2, sm.size());
        assertEquals(size - 2, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(second));
        assertEquals(false, sm.containsValue(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(second));
        assertEquals(false, sub.containsKey(second));
        assertEquals(false, sub.containsValue(secondValue));
    }

    //-----------------------------------------------------------------------
    public void testBidiRemoveByHeadMapEntrySet() {
        if (isRemoveSupported() == false) return;
        
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        Object first = it.next();
        Object second = it.next();
        Object toKey = it.next();
        
        int size = sm.size();
        SortedMap sub = sm.headMap(toKey);
        Set set = sub.entrySet();
        assertEquals(2, sub.size());
        assertEquals(2, set.size());
        
        Iterator it2 = set.iterator();
        Map.Entry firstEntry = cloneMapEntry((Map.Entry) it2.next());
        Map.Entry secondEntry = cloneMapEntry((Map.Entry) it2.next());
        assertEquals(true, sm.containsKey(first));
        assertEquals(true, sub.containsKey(first));
        assertEquals(true, set.contains(firstEntry));
        assertEquals(true, sm.containsKey(second));
        assertEquals(true, sub.containsKey(second));
        assertEquals(true, set.contains(secondEntry));
        
        set.remove(firstEntry);
        assertEquals(1, sub.size());
        assertEquals(size - 1, sm.size());
        assertEquals(size - 1, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(firstEntry.getKey()));
        assertEquals(false, sm.containsValue(firstEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsKey(firstEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsValue(firstEntry.getKey()));
        assertEquals(false, sub.containsKey(firstEntry.getKey()));
        assertEquals(false, sub.containsValue(firstEntry.getValue()));
        assertEquals(false, set.contains(firstEntry));
        
        set.remove(secondEntry);
        assertEquals(0, sub.size());
        assertEquals(size - 2, sm.size());
        assertEquals(size - 2, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(secondEntry.getKey()));
        assertEquals(false, sm.containsValue(secondEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsKey(secondEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsValue(secondEntry.getKey()));
        assertEquals(false, sub.containsKey(secondEntry.getKey()));
        assertEquals(false, sub.containsValue(secondEntry.getValue()));
        assertEquals(false, set.contains(secondEntry));
    }

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    public void testBidiTailMapContains() {
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        Object first = it.next();
        Object fromKey = it.next();
        Object second = it.next();
        Object firstValue = sm.get(first);
        Object fromKeyValue = sm.get(fromKey);
        Object secondValue = sm.get(second);
        
        SortedMap sub = sm.tailMap(fromKey);
        assertEquals(sm.size() - 1, sub.size());
        assertEquals(true, sm.containsKey(first));
        assertEquals(false, sub.containsKey(first));
        assertEquals(true, sm.containsValue(firstValue));
        assertEquals(false, sub.containsValue(firstValue));
        assertEquals(true, sm.containsKey(fromKey));
        assertEquals(true, sub.containsKey(fromKey));
        assertEquals(true, sm.containsValue(fromKeyValue));
        assertEquals(true, sub.containsValue(fromKeyValue));
        assertEquals(true, sm.containsKey(second));
        assertEquals(true, sub.containsKey(second));
        assertEquals(true, sm.containsValue(secondValue));
        assertEquals(true, sub.containsValue(secondValue));
    }

    //-----------------------------------------------------------------------
    public void testBidiClearByTailMap() {
        if (isRemoveSupported() == false) return;
        
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        it.next();
        it.next();
        Object first = it.next();
        Object fromKey = it.next();
        Object second = it.next();
        
        Object firstValue = sm.get(first);
        Object fromKeyValue = sm.get(fromKey);
        Object secondValue = sm.get(second);
        
        SortedMap sub = sm.tailMap(fromKey);
        int size = sm.size();
        assertEquals(size - 3, sub.size());
        sub.clear();
        assertEquals(0, sub.size());
        assertEquals(3, sm.size());
        assertEquals(3, sm.inverseBidiMap().size());
        
        assertEquals(true, sm.containsKey(first));
        assertEquals(true, sm.containsValue(firstValue));
        assertEquals(true, sm.inverseBidiMap().containsKey(firstValue));
        assertEquals(true, sm.inverseBidiMap().containsValue(first));
        assertEquals(false, sub.containsKey(first));
        assertEquals(false, sub.containsValue(firstValue));
        
        assertEquals(false, sm.containsKey(fromKey));
        assertEquals(false, sm.containsValue(fromKeyValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(fromKeyValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(fromKey));
        assertEquals(false, sub.containsKey(fromKey));
        assertEquals(false, sub.containsValue(fromKeyValue));
        
        assertEquals(false, sm.containsKey(second));
        assertEquals(false, sm.containsValue(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(second));
        assertEquals(false, sub.containsKey(second));
        assertEquals(false, sub.containsValue(secondValue));
    }

    //-----------------------------------------------------------------------                
    public void testBidiRemoveByTailMap() {
        if (isRemoveSupported() == false) return;
        
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        it.next();
        it.next();
        Object fromKey = it.next();
        Object first = it.next();
        Object second = it.next();
        
        int size = sm.size();
        SortedMap sub = sm.tailMap(fromKey);
        assertEquals(true, sm.containsKey(first));
        assertEquals(true, sub.containsKey(first));
        assertEquals(true, sm.containsKey(second));
        assertEquals(true, sub.containsKey(second));
        
        Object firstValue = sub.remove(first);
        assertEquals(size - 3, sub.size());
        assertEquals(size - 1, sm.size());
        assertEquals(size - 1, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(first));
        assertEquals(false, sm.containsValue(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(first));
        assertEquals(false, sub.containsKey(first));
        assertEquals(false, sub.containsValue(firstValue));
        
        Object secondValue = sub.remove(second);
        assertEquals(size - 4, sub.size());
        assertEquals(size - 2, sm.size());
        assertEquals(size - 2, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(second));
        assertEquals(false, sm.containsValue(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(second));
        assertEquals(false, sub.containsKey(second));
        assertEquals(false, sub.containsValue(secondValue));
    }

    //-----------------------------------------------------------------------
    public void testBidiRemoveByTailMapEntrySet() {
        if (isRemoveSupported() == false) return;
        
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        it.next();
        it.next();
        Object fromKey = it.next();
        Object first = it.next();
        Object second = it.next();
        
        int size = sm.size();
        SortedMap sub = sm.tailMap(fromKey);
        Set set = sub.entrySet();
        Iterator it2 = set.iterator();
        Object fromEntry = it2.next();
        Map.Entry firstEntry = cloneMapEntry((Map.Entry) it2.next());
        Map.Entry secondEntry = cloneMapEntry((Map.Entry) it2.next());
        assertEquals(true, sm.containsKey(first));
        assertEquals(true, sub.containsKey(first));
        assertEquals(true, set.contains(firstEntry));
        assertEquals(true, sm.containsKey(second));
        assertEquals(true, sub.containsKey(second));
        assertEquals(true, set.contains(secondEntry));
        
        set.remove(firstEntry);
        assertEquals(size - 3, sub.size());
        assertEquals(size - 1, sm.size());
        assertEquals(size - 1, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(firstEntry.getKey()));
        assertEquals(false, sm.containsValue(firstEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsKey(firstEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsValue(firstEntry.getKey()));
        assertEquals(false, sub.containsKey(firstEntry.getKey()));
        assertEquals(false, sub.containsValue(firstEntry.getValue()));
        assertEquals(false, set.contains(firstEntry));
        
        set.remove(secondEntry);
        assertEquals(size - 4, sub.size());
        assertEquals(size - 2, sm.size());
        assertEquals(size - 2, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(secondEntry.getKey()));
        assertEquals(false, sm.containsValue(secondEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsKey(secondEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsValue(secondEntry.getKey()));
        assertEquals(false, sub.containsKey(secondEntry.getKey()));
        assertEquals(false, sub.containsValue(secondEntry.getValue()));
        assertEquals(false, set.contains(secondEntry));
    }

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    public void testBidiSubMapContains() {
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        Object first = it.next();
        Object fromKey = it.next();
        Object second = it.next();
        Object toKey = it.next();
        Object third = it.next();
        Object firstValue = sm.get(first);
        Object fromKeyValue = sm.get(fromKey);
        Object secondValue = sm.get(second);
        Object thirdValue = sm.get(third);
        
        SortedMap sub = sm.subMap(fromKey, toKey);
        assertEquals(2, sub.size());
        assertEquals(true, sm.containsKey(first));
        assertEquals(false, sub.containsKey(first));
        assertEquals(true, sm.containsValue(firstValue));
        assertEquals(false, sub.containsValue(firstValue));
        assertEquals(true, sm.containsKey(fromKey));
        assertEquals(true, sub.containsKey(fromKey));
        assertEquals(true, sm.containsValue(fromKeyValue));
        assertEquals(true, sub.containsValue(fromKeyValue));
        assertEquals(true, sm.containsKey(second));
        assertEquals(true, sub.containsKey(second));
        assertEquals(true, sm.containsValue(secondValue));
        assertEquals(true, sub.containsValue(secondValue));
        assertEquals(true, sm.containsKey(third));
        assertEquals(false, sub.containsKey(third));
        assertEquals(true, sm.containsValue(thirdValue));
        assertEquals(false, sub.containsValue(thirdValue));
    }

    //-----------------------------------------------------------------------
    public void testBidiClearBySubMap() {
        if (isRemoveSupported() == false) return;
        
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        it.next();
        Object fromKey = it.next();
        Object first = it.next();
        Object second = it.next();
        Object toKey = it.next();
        
        Object fromKeyValue = sm.get(fromKey);
        Object firstValue = sm.get(first);
        Object secondValue = sm.get(second);
        Object toKeyValue = sm.get(toKey);
        
        SortedMap sub = sm.subMap(fromKey, toKey);
        int size = sm.size();
        assertEquals(3, sub.size());
        sub.clear();
        assertEquals(0, sub.size());
        assertEquals(size - 3, sm.size());
        assertEquals(size - 3, sm.inverseBidiMap().size());
        
        assertEquals(false, sm.containsKey(fromKey));
        assertEquals(false, sm.containsValue(fromKeyValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(fromKeyValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(fromKey));
        assertEquals(false, sub.containsKey(fromKey));
        assertEquals(false, sub.containsValue(fromKeyValue));
        
        assertEquals(false, sm.containsKey(first));
        assertEquals(false, sm.containsValue(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(first));
        assertEquals(false, sub.containsKey(first));
        assertEquals(false, sub.containsValue(firstValue));
        
        assertEquals(false, sm.containsKey(second));
        assertEquals(false, sm.containsValue(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(second));
        assertEquals(false, sub.containsKey(second));
        assertEquals(false, sub.containsValue(secondValue));
        
        assertEquals(true, sm.containsKey(toKey));
        assertEquals(true, sm.containsValue(toKeyValue));
        assertEquals(true, sm.inverseBidiMap().containsKey(toKeyValue));
        assertEquals(true, sm.inverseBidiMap().containsValue(toKey));
        assertEquals(false, sub.containsKey(toKey));
        assertEquals(false, sub.containsValue(toKeyValue));
    }

    //-----------------------------------------------------------------------
    public void testBidiRemoveBySubMap() {
        if (isRemoveSupported() == false) return;
        
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        it.next();
        it.next();
        Object fromKey = it.next();
        Object first = it.next();
        Object second = it.next();
        Object toKey = it.next();
        
        int size = sm.size();
        SortedMap sub = sm.subMap(fromKey, toKey);
        assertEquals(true, sm.containsKey(first));
        assertEquals(true, sub.containsKey(first));
        assertEquals(true, sm.containsKey(second));
        assertEquals(true, sub.containsKey(second));
        
        Object firstValue = sub.remove(first);
        assertEquals(2, sub.size());
        assertEquals(size - 1, sm.size());
        assertEquals(size - 1, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(first));
        assertEquals(false, sm.containsValue(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(firstValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(first));
        assertEquals(false, sub.containsKey(first));
        assertEquals(false, sub.containsValue(firstValue));
        
        Object secondValue = sub.remove(second);
        assertEquals(1, sub.size());
        assertEquals(size - 2, sm.size());
        assertEquals(size - 2, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(second));
        assertEquals(false, sm.containsValue(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsKey(secondValue));
        assertEquals(false, sm.inverseBidiMap().containsValue(second));
        assertEquals(false, sub.containsKey(second));
        assertEquals(false, sub.containsValue(secondValue));
    }

    //-----------------------------------------------------------------------
    public void testBidiRemoveBySubMapEntrySet() {
        if (isRemoveSupported() == false) return;
        
        // extra test as other tests get complex
        SortedBidiMap sm = (SortedBidiMap) makeFullMap();
        Iterator it = sm.keySet().iterator();
        it.next();
        it.next();
        Object fromKey = it.next();
        Object first = it.next();
        Object second = it.next();
        Object toKey = it.next();
        
        int size = sm.size();
        SortedMap sub = sm.subMap(fromKey, toKey);
        Set set = sub.entrySet();
        assertEquals(3, set.size());
        Iterator it2 = set.iterator();
        Object fromEntry = it2.next();
        Map.Entry firstEntry = cloneMapEntry((Map.Entry) it2.next());
        Map.Entry secondEntry = cloneMapEntry((Map.Entry) it2.next());
        assertEquals(true, sm.containsKey(first));
        assertEquals(true, sub.containsKey(first));
        assertEquals(true, set.contains(firstEntry));
        assertEquals(true, sm.containsKey(second));
        assertEquals(true, sub.containsKey(second));
        assertEquals(true, set.contains(secondEntry));
        
        set.remove(firstEntry);
        assertEquals(2, sub.size());
        assertEquals(size - 1, sm.size());
        assertEquals(size - 1, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(firstEntry.getKey()));
        assertEquals(false, sm.containsValue(firstEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsKey(firstEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsValue(firstEntry.getKey()));
        assertEquals(false, sub.containsKey(firstEntry.getKey()));
        assertEquals(false, sub.containsValue(firstEntry.getValue()));
        assertEquals(false, set.contains(firstEntry));
        
        set.remove(secondEntry);
        assertEquals(1, sub.size());
        assertEquals(size - 2, sm.size());
        assertEquals(size - 2, sm.inverseBidiMap().size());
        assertEquals(false, sm.containsKey(secondEntry.getKey()));
        assertEquals(false, sm.containsValue(secondEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsKey(secondEntry.getValue()));
        assertEquals(false, sm.inverseBidiMap().containsValue(secondEntry.getKey()));
        assertEquals(false, sub.containsKey(secondEntry.getKey()));
        assertEquals(false, sub.containsValue(secondEntry.getValue()));
        assertEquals(false, set.contains(secondEntry));
    }

    //-----------------------------------------------------------------------    
    public BulkTest bulkTestHeadMap() {
        return new AbstractTestSortedMap.TestHeadMap(this);
    }

    public BulkTest bulkTestTailMap() {
        return new AbstractTestSortedMap.TestTailMap(this);
    }

    public BulkTest bulkTestSubMap() {
        return new AbstractTestSortedMap.TestSubMap(this);
    }

}
