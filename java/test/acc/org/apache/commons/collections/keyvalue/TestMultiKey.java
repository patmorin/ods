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

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for {@link org.apache.commons.collections.MultiKey}.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestMultiKey extends TestCase {

    Integer ONE = new Integer(1);
    Integer TWO = new Integer(2);
    Integer THREE = new Integer(3);
    Integer FOUR = new Integer(4);
    Integer FIVE = new Integer(5);
    
    public TestMultiKey(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestMultiKey.class);
    }

    public static void main(String[] args) {
        String[] testCaseName = { TestMultiKey.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    //-----------------------------------------------------------------------
    public void testConstructors() throws Exception {
        MultiKey mk = null;
        mk = new MultiKey(ONE, TWO);
        Assert.assertTrue(Arrays.equals(new Object[] {ONE, TWO}, mk.getKeys()));

        mk = new MultiKey(ONE, TWO, THREE);
        Assert.assertTrue(Arrays.equals(new Object[] {ONE, TWO, THREE}, mk.getKeys()));

        mk = new MultiKey(ONE, TWO, THREE, FOUR);
        Assert.assertTrue(Arrays.equals(new Object[] {ONE, TWO, THREE, FOUR}, mk.getKeys()));

        mk = new MultiKey(ONE, TWO, THREE, FOUR, FIVE);
        Assert.assertTrue(Arrays.equals(new Object[] {ONE, TWO, THREE, FOUR, FIVE}, mk.getKeys()));

        mk = new MultiKey(new Object[] {THREE, FOUR, ONE, TWO}, false);
        Assert.assertTrue(Arrays.equals(new Object[] {THREE, FOUR, ONE, TWO}, mk.getKeys()));
    }
    
    public void testConstructorsByArray() throws Exception {
        MultiKey mk = null;
        Object[] keys = new Object[] {THREE, FOUR, ONE, TWO};
        mk = new MultiKey(keys);
        Assert.assertTrue(Arrays.equals(new Object[] {THREE, FOUR, ONE, TWO}, mk.getKeys()));
        keys[3] = FIVE;  // no effect
        Assert.assertTrue(Arrays.equals(new Object[] {THREE, FOUR, ONE, TWO}, mk.getKeys()));

        keys = new Object[] {};
        mk = new MultiKey(keys);
        Assert.assertTrue(Arrays.equals(new Object[] {}, mk.getKeys()));

        keys = new Object[] {THREE, FOUR, ONE, TWO};
        mk = new MultiKey(keys, true);
        Assert.assertTrue(Arrays.equals(new Object[] {THREE, FOUR, ONE, TWO}, mk.getKeys()));
        keys[3] = FIVE;  // no effect
        Assert.assertTrue(Arrays.equals(new Object[] {THREE, FOUR, ONE, TWO}, mk.getKeys()));

        keys = new Object[] {THREE, FOUR, ONE, TWO};
        mk = new MultiKey(keys, false);
        Assert.assertTrue(Arrays.equals(new Object[] {THREE, FOUR, ONE, TWO}, mk.getKeys()));
        // change key - don't do this!
        // the hashcode of the MultiKey is now broken
        keys[3] = FIVE;
        Assert.assertTrue(Arrays.equals(new Object[] {THREE, FOUR, ONE, FIVE}, mk.getKeys()));
    }        
    
    public void testConstructorsByArrayNull() throws Exception {
        Object[] keys = null;
        try {
            new MultiKey(keys);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new MultiKey(keys, true);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new MultiKey(keys, false);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    public void testSize() {
        Assert.assertEquals(2, new MultiKey(ONE, TWO).size());
        Assert.assertEquals(2, new MultiKey(null, null).size());
        Assert.assertEquals(3, new MultiKey(ONE, TWO, THREE).size());
        Assert.assertEquals(3, new MultiKey(null, null, null).size());
        Assert.assertEquals(4, new MultiKey(ONE, TWO, THREE, FOUR).size());
        Assert.assertEquals(4, new MultiKey(null, null, null, null).size());
        Assert.assertEquals(5, new MultiKey(ONE, TWO, THREE, FOUR, FIVE).size());
        Assert.assertEquals(5, new MultiKey(null, null, null, null, null).size());
        
        Assert.assertEquals(0, new MultiKey(new Object[] {}).size());
        Assert.assertEquals(1, new MultiKey(new Object[] {ONE}).size());
        Assert.assertEquals(2, new MultiKey(new Object[] {ONE, TWO}).size());
        Assert.assertEquals(7, new MultiKey(new Object[] {ONE, TWO, ONE, TWO, ONE, TWO, ONE}).size());
    }
    
    public void testGetIndexed() {
        MultiKey mk = new MultiKey(ONE, TWO);
        Assert.assertSame(ONE, mk.getKey(0));
        Assert.assertSame(TWO, mk.getKey(1));
        try {
            mk.getKey(-1);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
        try {
            mk.getKey(2);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
    }
    
    public void testGetKeysSimpleConstructor() {
        MultiKey mk = new MultiKey(ONE, TWO);
        Object[] array = mk.getKeys();
        Assert.assertSame(ONE, array[0]);
        Assert.assertSame(TWO, array[1]);
        Assert.assertEquals(2, array.length);
    }
    
    public void testGetKeysArrayConstructorCloned() {
        Object[] keys = new Object[] {ONE, TWO};
        MultiKey mk = new MultiKey(keys, true);
        Object[] array = mk.getKeys();
        Assert.assertTrue(array != keys);
        Assert.assertTrue(Arrays.equals(array, keys));
        Assert.assertSame(ONE, array[0]);
        Assert.assertSame(TWO, array[1]);
        Assert.assertEquals(2, array.length);
    }
    
    public void testGetKeysArrayConstructorNonCloned() {
        Object[] keys = new Object[] {ONE, TWO};
        MultiKey mk = new MultiKey(keys, false);
        Object[] array = mk.getKeys();
        Assert.assertTrue(array != keys);  // still not equal
        Assert.assertTrue(Arrays.equals(array, keys));
        Assert.assertSame(ONE, array[0]);
        Assert.assertSame(TWO, array[1]);
        Assert.assertEquals(2, array.length);
    }
    
    public void testHashCode() {
        MultiKey mk1 = new MultiKey(ONE, TWO);
        MultiKey mk2 = new MultiKey(ONE, TWO);
        MultiKey mk3 = new MultiKey(ONE, "TWO");
        
        Assert.assertTrue(mk1.hashCode() == mk1.hashCode());
        Assert.assertTrue(mk1.hashCode() == mk2.hashCode());
        Assert.assertTrue(mk1.hashCode() != mk3.hashCode());
        
        int total = (0 ^ ONE.hashCode()) ^ TWO.hashCode();
        Assert.assertEquals(total, mk1.hashCode());
    }
    
    public void testEquals() {
        MultiKey mk1 = new MultiKey(ONE, TWO);
        MultiKey mk2 = new MultiKey(ONE, TWO);
        MultiKey mk3 = new MultiKey(ONE, "TWO");
        
        Assert.assertEquals(mk1, mk1);
        Assert.assertEquals(mk1, mk2);
        Assert.assertTrue(mk1.equals(mk3) == false);
        Assert.assertTrue(mk1.equals("") == false);
        Assert.assertTrue(mk1.equals(null) == false);
    }
    
}
