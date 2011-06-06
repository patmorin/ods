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
package org.apache.commons.collections.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.collection.TestTransformedCollection;

/**
 * Extension of {@link TestList} for exercising the {@link TransformedList}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestTransformedList extends AbstractTestList {
    
    public TestTransformedList(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestTransformedList.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestTransformedList.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public Collection makeConfirmedCollection() {
        return new ArrayList();
    }

    public Collection makeConfirmedFullCollection() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }
    
    public List makeEmptyList() {
        return TransformedList.decorate(new ArrayList(), TestTransformedCollection.NOOP_TRANSFORMER);
    }

    public List makeFullList() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return TransformedList.decorate(list, TestTransformedCollection.NOOP_TRANSFORMER);
    }
    
    public void testTransformedList() {
        List list = TransformedList.decorate(new ArrayList(), TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, list.size());
        Object[] els = new Object[] {"1", "3", "5", "7", "2", "4", "6"};
        for (int i = 0; i < els.length; i++) {
            list.add(els[i]);
            assertEquals(i + 1, list.size());
            assertEquals(true, list.contains(new Integer((String) els[i])));
            assertEquals(false, list.contains(els[i]));
        }
        
        assertEquals(false, list.remove(els[0]));
        assertEquals(true, list.remove(new Integer((String) els[0])));
        
        list.clear();
        for (int i = 0; i < els.length; i++) {
            list.add(0, els[i]);
            assertEquals(i + 1, list.size());
            assertEquals(new Integer((String) els[i]), list.get(0));
        }
        
        list.set(0, "22");
        assertEquals(new Integer(22), list.get(0));
        
        ListIterator it = list.listIterator();
        it.next();
        it.set("33");
        assertEquals(new Integer(33), list.get(0));
        it.add("44");
        assertEquals(new Integer(44), list.get(1));
        
        List adds = new ArrayList();
        adds.add("1");
        adds.add("2");
        list.clear();
        list.addAll(adds);
        assertEquals(new Integer(1), list.get(0));
        assertEquals(new Integer(2), list.get(1));
        
        adds.clear();
        adds.add("3");
        list.addAll(1, adds);
        assertEquals(new Integer(1), list.get(0));
        assertEquals(new Integer(3), list.get(1));
        assertEquals(new Integer(2), list.get(2));
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/TransformedList.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/TransformedList.fullCollection.version3.1.obj");
//    }

}
