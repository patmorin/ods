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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.collections.functors.ConstantFactory;

/**
 * Tests the org.apache.commons.collections.FactoryUtils class.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Stephen Colebourne
 */
public class TestFactoryUtils extends junit.framework.TestCase {

    /**
     * Construct
     */
    public TestFactoryUtils(String name) {
        super(name);
    }

    /**
     * Main.
     * @param args
     */    
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Return class as a test suite.
     */
    public static Test suite() {
        return new TestSuite(TestFactoryUtils.class);
    }

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
    }

    // exceptionFactory
    //------------------------------------------------------------------

    public void testExceptionFactory() {
        assertNotNull(FactoryUtils.exceptionFactory());
        assertSame(FactoryUtils.exceptionFactory(), FactoryUtils.exceptionFactory());
        try {
            FactoryUtils.exceptionFactory().create();
        } catch (FunctorException ex) {
            try {
                FactoryUtils.exceptionFactory().create();
            } catch (FunctorException ex2) {
                return;
            }
        }
        fail();
    }
    
    // nullFactory
    //------------------------------------------------------------------
    
    public void testNullFactory() {
        Factory factory = FactoryUtils.nullFactory();
        assertNotNull(factory);
        Object created = factory.create();
        assertNull(created);
    }

    // constantFactory
    //------------------------------------------------------------------
    
    public void testConstantFactoryNull() {
        Factory factory = FactoryUtils.constantFactory(null);
        assertNotNull(factory);
        Object created = factory.create();
        assertNull(created);
    }

    public void testConstantFactoryConstant() {
        Integer constant = new Integer(9);
        Factory factory = FactoryUtils.constantFactory(constant);
        assertNotNull(factory);
        Object created = factory.create();
        assertSame(constant, created);
    }

    // prototypeFactory
    //------------------------------------------------------------------
    
    public void testPrototypeFactoryNull() {
        assertSame(ConstantFactory.NULL_INSTANCE, FactoryUtils.prototypeFactory(null));
    }

    public void testPrototypeFactoryPublicCloneMethod() throws Exception {
        Date proto = new Date();
        Factory factory = FactoryUtils.prototypeFactory(proto);
        assertNotNull(factory);
        Object created = factory.create();
        assertTrue(proto != created);
        assertEquals(proto, created);
        
        // check serialisation works
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        out.writeObject(factory);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        Object dest = in.readObject();
        in.close();
    }

    public void testPrototypeFactoryPublicCopyConstructor() throws Exception {
        Mock1 proto = new Mock1(6);
        Factory factory = FactoryUtils.prototypeFactory(proto);
        assertNotNull(factory);
        Object created = factory.create();
        assertTrue(proto != created);
        assertEquals(proto, created);
        
        // check serialisation works
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        try {
            out.writeObject(factory);
        } catch (NotSerializableException ex) {
            out.close();
        }
        factory = FactoryUtils.prototypeFactory(new Mock2("S"));
        buffer = new ByteArrayOutputStream();
        out = new ObjectOutputStream(buffer);
        out.writeObject(factory);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        Object dest = in.readObject();
        in.close();
    }

    public void testPrototypeFactoryPublicSerialization() throws Exception {
        Integer proto = new Integer(9);
        Factory factory = FactoryUtils.prototypeFactory(proto);
        assertNotNull(factory);
        Object created = factory.create();
        assertTrue(proto != created);
        assertEquals(proto, created);
        
        // check serialisation works
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        out.writeObject(factory);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        Object dest = in.readObject();
        in.close();
    }

    public void testPrototypeFactoryPublicSerializationError() {
        Mock2 proto = new Mock2(new Object());
        Factory factory = FactoryUtils.prototypeFactory(proto);
        assertNotNull(factory);
        try {
            Object created = factory.create();
            
        } catch (FunctorException ex) {
            assertTrue(ex.getCause() instanceof IOException);
            return;
        }
        fail();
    }

    public void testPrototypeFactoryPublicBad() {
        Object proto = new Object();
        try {
            Factory factory = FactoryUtils.prototypeFactory(proto);
            
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    public static class Mock1 {
        private final int iVal;
        public Mock1(int val) {
            iVal = val;
        }
        public Mock1(Mock1 mock) {
            iVal = mock.iVal;
        }
        public boolean equals(Object obj) {
            if (obj instanceof Mock1) {
                if (iVal == ((Mock1) obj).iVal) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public static class Mock2 implements Serializable {
        private final Object iVal;
        public Mock2(Object val) {
            iVal = val;
        }
        public boolean equals(Object obj) {
            if (obj instanceof Mock2) {
                if (iVal == ((Mock2) obj).iVal) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public static class Mock3 {
        private static int cCounter = 0;
        private final int iVal;
        public Mock3() {
            iVal = cCounter++;
        }
        public int getValue() {
            return iVal;
        }
    }
    
    // instantiateFactory
    //------------------------------------------------------------------
    
    public void testInstantiateFactoryNull() {
        try {
            Factory factory = FactoryUtils.instantiateFactory(null);
            
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    public void testInstantiateFactorySimple() {
        Factory factory = FactoryUtils.instantiateFactory(Mock3.class);
        assertNotNull(factory);
        Object created = factory.create();
        assertEquals(0, ((Mock3) created).getValue());
        created = factory.create();
        assertEquals(1, ((Mock3) created).getValue());
    }

    public void testInstantiateFactoryMismatch() {
        try {
            Factory factory = FactoryUtils.instantiateFactory(Date.class, null, new Object[] {null});
            
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    public void testInstantiateFactoryNoConstructor() {
        try {
            Factory factory = FactoryUtils.instantiateFactory(Date.class, new Class[] {Long.class}, new Object[] {null});
            
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    public void testInstantiateFactoryComplex() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        // 2nd Jan 1970
        Factory factory = FactoryUtils.instantiateFactory(Date.class,
            new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE},
            new Object[] {new Integer(70), new Integer(0), new Integer(2)});
        assertNotNull(factory);
        Object created = factory.create();
        assertTrue(created instanceof Date);
        // long time of 1 day (== 2nd Jan 1970)
        assertEquals(new Date(1000 * 60 * 60 * 24), created);
    }

}
