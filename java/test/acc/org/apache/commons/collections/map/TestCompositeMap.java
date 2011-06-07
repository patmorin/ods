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

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

/**
 * Extension of {@link AbstractTestMap} for exercising the 
 * {@link CompositeMap} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Brian McCallister
 */
public class TestCompositeMap extends AbstractTestMap {
    /** used as a flag in MapMutator tests */
    private boolean pass = false;
    
    public TestCompositeMap(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return new TestSuite(TestCompositeMap.class);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        this.pass = false;
    }
    
    public static void main(String args[]) {
        String[] testCaseName = {TestCompositeMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
    public Map makeEmptyMap() {
        CompositeMap map = new CompositeMap();
        map.addComposited(new HashMap());
        map.setMutator(new CompositeMap.MapMutator() {
            public void resolveCollision(CompositeMap composite,
            Map existing,
            Map added,
            Collection intersect) {
                // Do nothing
            }
            
            public Object put(CompositeMap map, Map[] composited, Object key, Object value) {
                return composited[0].put(key, value);
            }
            
            public void putAll(CompositeMap map, Map[] composited, Map t) {
                composited[0].putAll(t);
            }
            
        });
        return map;
    }
    
    private Map buildOne() {
        HashMap map = new HashMap();
        map.put("1", "one");
        map.put("2", "two");
        return map;
    }
    
    public Map buildTwo() {
        HashMap map = new HashMap();
        map.put("3", "three");
        map.put("4", "four");
        return map;
    }
    
    public void testGet() {
        CompositeMap map = new CompositeMap(buildOne(), buildTwo());
        Assert.assertEquals("one", map.get("1"));
        Assert.assertEquals("four", map.get("4"));
    }
    
    public void testAddComposited() {
        CompositeMap map = new CompositeMap(buildOne(), buildTwo());
        HashMap three = new HashMap();
        three.put("5", "five");
        map.addComposited(three);
        assertTrue(map.containsKey("5"));
        try {
            map.addComposited(three);
            fail("Expecting IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    public void testRemoveComposited() {
        CompositeMap map = new CompositeMap(buildOne(), buildTwo());
        HashMap three = new HashMap();
        three.put("5", "five");
        map.addComposited(three);
        assertTrue(map.containsKey("5"));
        
        map.removeComposited(three);
        assertFalse(map.containsKey("5"));
        
        map.removeComposited(buildOne());
        assertFalse(map.containsKey("2"));
        
    }
    
    public void testRemoveFromUnderlying() {
        CompositeMap map = new CompositeMap(buildOne(), buildTwo());
        HashMap three = new HashMap();
        three.put("5", "five");
        map.addComposited(three);
        assertTrue(map.containsKey("5"));
        
        //Now remove "5"
        three.remove("5");
        assertFalse(map.containsKey("5"));
    }
    
    public void testRemoveFromComposited() {
        CompositeMap map = new CompositeMap(buildOne(), buildTwo());
        HashMap three = new HashMap();
        three.put("5", "five");
        map.addComposited(three);
        assertTrue(map.containsKey("5"));
        
        //Now remove "5"
        map.remove("5");
        assertFalse(three.containsKey("5"));
    }
    
    public void testResolveCollision() {
        CompositeMap map = new CompositeMap(buildOne(), buildTwo(), 
            new CompositeMap.MapMutator() {
            public void resolveCollision(CompositeMap composite,
            Map existing,
            Map added,
            Collection intersect) {
                pass = true;
            }
            
            public Object put(CompositeMap map, Map[] composited, Object key, 
                Object value) {
                throw new UnsupportedOperationException();
            }
            
            public void putAll(CompositeMap map, Map[] composited, Map t) {
                throw new UnsupportedOperationException();
            }
        });
        
        map.addComposited(buildOne());
        assertTrue(pass);
    }
    
    public void testPut() {
        CompositeMap map = new CompositeMap(buildOne(), buildTwo(), 
            new CompositeMap.MapMutator() {
            public void resolveCollision(CompositeMap composite,
            Map existing,
            Map added,
            Collection intersect) {
                throw new UnsupportedOperationException();
            }
            
            public Object put(CompositeMap map, Map[] composited, Object key, 
                Object value) {
                pass = true;
                return "foo";
            }
            
            public void putAll(CompositeMap map, Map[] composited, Map t) {
                throw new UnsupportedOperationException();
            }
        });
        
        map.put("willy", "wonka");
        assertTrue(pass);
    }
    
    public void testPutAll() {
        CompositeMap map = new CompositeMap(buildOne(), buildTwo(), 
            new CompositeMap.MapMutator() {
            public void resolveCollision(CompositeMap composite,
            Map existing,
            Map added,
            Collection intersect) {
                throw new UnsupportedOperationException();
            }
            
            public Object put(CompositeMap map, Map[] composited, Object key,
                Object value) {
                throw new UnsupportedOperationException();
            }
            
            public void putAll(CompositeMap map, Map[] composited, Map t) {
                pass = true;
            }
        });
        
        map.putAll(null);
        assertTrue(pass);
    }
}

