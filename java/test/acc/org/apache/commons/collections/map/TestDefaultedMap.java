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

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantFactory;

/**
 * Extension of {@link TestMap} for exercising the 
 * {@link DefaultedMap} implementation.
 *
 * @since Commons Collections 3.2
 * @version $Revision: 155406 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestDefaultedMap extends AbstractTestMap {

    protected static final Factory nullFactory = FactoryUtils.nullFactory();

    public TestDefaultedMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestDefaultedMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestDefaultedMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    //-----------------------------------------------------------------------    
    public Map makeEmptyMap() {
        return DefaultedMap.decorate(new HashMap(), nullFactory);
    }

    //-----------------------------------------------------------------------    
    public void testMapGet() {
        Map map = new DefaultedMap("NULL");
        
        assertEquals(0, map.size());
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        
        map.put("Key", "Value");
        assertEquals(1, map.size());
        assertEquals(true, map.containsKey("Key"));
        assertEquals("Value", map.get("Key"));
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
    }

    public void testMapGet2() {
        HashMap base = new HashMap();
        Map map = DefaultedMap.decorate(base, "NULL");
        
        assertEquals(0, map.size());
        assertEquals(0, base.size());
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        
        map.put("Key", "Value");
        assertEquals(1, map.size());
        assertEquals(1, base.size());
        assertEquals(true, map.containsKey("Key"));
        assertEquals("Value", map.get("Key"));
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
    }

    public void testMapGet3() {
        HashMap base = new HashMap();
        Map map = DefaultedMap.decorate(base, ConstantFactory.getInstance("NULL"));
        
        assertEquals(0, map.size());
        assertEquals(0, base.size());
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        
        map.put("Key", "Value");
        assertEquals(1, map.size());
        assertEquals(1, base.size());
        assertEquals(true, map.containsKey("Key"));
        assertEquals("Value", map.get("Key"));
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
    }

    public void testMapGet4() {
        HashMap base = new HashMap();
        Map map = DefaultedMap.decorate(base, new Transformer() {
            public Object transform(Object input) {
                if (input instanceof String) {
                    return "NULL";
                }
                return "NULL_OBJECT";
            }
        });
        
        assertEquals(0, map.size());
        assertEquals(0, base.size());
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        assertEquals("NULL_OBJECT", map.get(new Integer(0)));
        
        map.put("Key", "Value");
        assertEquals(1, map.size());
        assertEquals(1, base.size());
        assertEquals(true, map.containsKey("Key"));
        assertEquals("Value", map.get("Key"));
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        assertEquals("NULL_OBJECT", map.get(new Integer(0)));
    }

    public String getCompatibilityVersion() {
        return "3.2";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "c:/commons/collections/data/test/DefaultedMap.emptyCollection.version3.2.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "c:/commons/collections/data/test/DefaultedMap.fullCollection.version3.2.obj");
//    }

}
