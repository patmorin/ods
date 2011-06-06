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
package org.apache.commons.collections.comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for {@link BooleanComparator}.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Rodney Waldhoff
 */
public class TestBooleanComparator extends AbstractTestComparator {

    // conventional
    // ------------------------------------------------------------------------

    public TestBooleanComparator(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestBooleanComparator.class);
    }

    // collections testing framework
    // ------------------------------------------------------------------------

    public Comparator makeComparator() {
        return new BooleanComparator();
    }

    public List getComparableObjectsOrdered() {
        List list = new ArrayList();
        list.add(new Boolean(false));
        list.add(Boolean.FALSE);
        list.add(new Boolean(false));
        list.add(Boolean.TRUE);
        list.add(new Boolean(true));
        list.add(Boolean.TRUE);
        return list;
    }
    
    public String getCompatibilityVersion() {
        return "3";
    }

    // tests
    // ------------------------------------------------------------------------

    public void testConstructors() {
        allTests(false,new BooleanComparator());
        allTests(false,new BooleanComparator(false));
        allTests(true,new BooleanComparator(true));        
    }
    
    public void testStaticFactoryMethods() {
        allTests(false,BooleanComparator.getFalseFirstComparator());
        allTests(false,BooleanComparator.getBooleanComparator(false));
        allTests(true,BooleanComparator.getTrueFirstComparator());
        allTests(true,BooleanComparator.getBooleanComparator(true));
    }
    
    public void testEqualsCompatibleInstance() {
        assertEquals(new BooleanComparator(),new BooleanComparator(false));
        assertEquals(new BooleanComparator(false),new BooleanComparator(false));
        assertEquals(new BooleanComparator(false),BooleanComparator.getFalseFirstComparator());
        assertSame(BooleanComparator.getFalseFirstComparator(),BooleanComparator.getBooleanComparator(false));

        assertEquals(new BooleanComparator(true),new BooleanComparator(true));
        assertEquals(new BooleanComparator(true),BooleanComparator.getTrueFirstComparator());
        assertSame(BooleanComparator.getTrueFirstComparator(),BooleanComparator.getBooleanComparator(true));

        assertTrue(!(new BooleanComparator().equals(new BooleanComparator(true))));
        assertTrue(!(new BooleanComparator(true).equals(new BooleanComparator(false))));
    }
    
    // utilities
    // ------------------------------------------------------------------------

    protected void allTests(boolean trueFirst, BooleanComparator comp) {
        orderIndependentTests(comp);
        if(trueFirst) {
            trueFirstTests(comp);
        } else {
            falseFirstTests(comp);
        }
    }

    protected void trueFirstTests(BooleanComparator comp) {
        assertNotNull(comp);
        assertEquals(0,comp.compare(Boolean.TRUE,Boolean.TRUE));
        assertEquals(0,comp.compare(Boolean.FALSE,Boolean.FALSE));
        assertTrue(comp.compare(Boolean.FALSE,Boolean.TRUE) > 0);
        assertTrue(comp.compare(Boolean.TRUE,Boolean.FALSE) < 0);

        assertEquals(0,comp.compare((Object)(Boolean.TRUE),(Object)(Boolean.TRUE)));
        assertEquals(0,comp.compare((Object)(Boolean.FALSE),(Object)(Boolean.FALSE)));
        assertTrue(comp.compare((Object)(Boolean.FALSE),(Object)(Boolean.TRUE)) > 0);
        assertTrue(comp.compare((Object)(Boolean.TRUE),(Object)(Boolean.FALSE)) < 0);
    }

    protected void falseFirstTests(BooleanComparator comp) {
        assertNotNull(comp);
        assertEquals(0,comp.compare(Boolean.TRUE,Boolean.TRUE));
        assertEquals(0,comp.compare(Boolean.FALSE,Boolean.FALSE));
        assertTrue(comp.compare(Boolean.FALSE,Boolean.TRUE) < 0);
        assertTrue(comp.compare(Boolean.TRUE,Boolean.FALSE) > 0);

        assertEquals(0,comp.compare((Object)(Boolean.TRUE),(Object)(Boolean.TRUE)));
        assertEquals(0,comp.compare((Object)(Boolean.FALSE),(Object)(Boolean.FALSE)));
        assertTrue(comp.compare((Object)(Boolean.FALSE),(Object)(Boolean.TRUE)) < 0);
        assertTrue(comp.compare((Object)(Boolean.TRUE),(Object)(Boolean.FALSE)) > 0);
    }

    protected void orderIndependentTests(BooleanComparator comp) {
        nullArgumentTests(comp);
        nonBooleanArgumentTests(comp);
        nullAndNonBooleanArgumentsTests(comp);
    }
    
    protected void nullArgumentTests(BooleanComparator comp) {
        assertNotNull(comp);
        try {
            comp.compare(null,null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // expected
        }
        try {
            comp.compare(Boolean.TRUE,null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // expected
        }
        try {
            comp.compare(Boolean.FALSE,null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // expected
        }
        try {
            comp.compare(null,Boolean.TRUE);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // expected
        }
        try {
            comp.compare(null,Boolean.FALSE);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // expected
        }
    }
    
    protected void nonBooleanArgumentTests(BooleanComparator comp) {
        assertNotNull(comp);
        try {
            comp.compare("string","string");
            fail("Expected ClassCastException");
        } catch(ClassCastException e) {
            // expected
        }
        try {
            comp.compare(Boolean.TRUE,"string");
            fail("Expected ClassCastException");
        } catch(ClassCastException e) {
            // expected
        }
        try {
            comp.compare("string",Boolean.TRUE);
            fail("Expected ClassCastException");
        } catch(ClassCastException e) {
            // expected
        }
        try {
            comp.compare("string",new Integer(3));
            fail("Expected ClassCastException");
        } catch(ClassCastException e) {
            // expected
        }
        try {
            comp.compare(new Integer(3),"string");
            fail("Expected ClassCastException");
        } catch(ClassCastException e) {
            // expected
        }
    }
    
    protected void nullAndNonBooleanArgumentsTests(BooleanComparator comp) {
        assertNotNull(comp);
        try {
            comp.compare(null,"string");
            fail("Expected ClassCast or NullPointer Exception");
        } catch(ClassCastException e) {
            // expected
        } catch(NullPointerException e) {
            // expected
        }
        try {
            comp.compare("string",null);
            fail("Expected ClassCast or NullPointer Exception");
        } catch(ClassCastException e) {
            // expected
        } catch(NullPointerException e) {
            // expected
        }
    }

}
