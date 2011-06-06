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
package org.apache.commons.collections.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.TransformerUtils;

/**
 * Extension of {@link TestCollection} for exercising the {@link TransformedCollection}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestTransformedCollection extends AbstractTestCollection {
    
    private static class StringToInteger implements Transformer {
        public Object transform(Object input) {
            return new Integer((String) input);
        }
    }
    
    public static final Transformer NOOP_TRANSFORMER = TransformerUtils.nopTransformer();
    public static final Transformer STRING_TO_INTEGER_TRANSFORMER = new StringToInteger();

    public TestTransformedCollection(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestTransformedCollection.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestTransformedCollection.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    //-----------------------------------------------------------------------
    public Collection makeConfirmedCollection() {
        return new ArrayList();
    }

    public Collection makeConfirmedFullCollection() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }
    
    public Collection makeCollection() {
        return TransformedCollection.decorate(new ArrayList(), NOOP_TRANSFORMER);
    }

    public Collection makeFullCollection() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return TransformedCollection.decorate(list, NOOP_TRANSFORMER);
    }
    
    //-----------------------------------------------------------------------
    public Object[] getFullElements() {
        return new Object[] {"1", "3", "5", "7", "2", "4", "6"};
    }

    public Object[] getOtherElements() {
        return new Object[] {"9", "88", "678", "87", "98", "78", "99"};
    }

    //-----------------------------------------------------------------------
    public void testTransformedCollection() {
        Collection coll = TransformedCollection.decorate(new ArrayList(), STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, coll.size());
        Object[] els = getFullElements();
        for (int i = 0; i < els.length; i++) {
            coll.add(els[i]);
            assertEquals(i + 1, coll.size());
            assertEquals(true, coll.contains(new Integer((String) els[i])));
            assertEquals(false, coll.contains(els[i]));
        }
        
        assertEquals(true, coll.remove(new Integer((String) els[0])));
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/TransformedCollection.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/TransformedCollection.fullCollection.version3.1.obj");
//    }

}
