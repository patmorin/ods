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
package org.apache.commons.collections.set;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.collection.TestTransformedCollection;

/**
 * Extension of {@link TestSet} for exercising the {@link TransformedSet}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestTransformedSet extends AbstractTestSet {
    
    public TestTransformedSet(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestTransformedSet.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestTransformedSet.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public Collection makeConfirmedCollection() {
        return new HashSet();
    }

    public Collection makeConfirmedFullCollection() {
        Set set = new HashSet();
        set.addAll(Arrays.asList(getFullElements()));
        return set;
    }
    
    public Set makeEmptySet() {
        return TransformedSet.decorate(new HashSet(), TestTransformedCollection.NOOP_TRANSFORMER);
    }

    public Set makeFullSet() {
        Set list = new HashSet();
        list.addAll(Arrays.asList(getFullElements()));
        return TransformedSet.decorate(list, TestTransformedCollection.NOOP_TRANSFORMER);
    }
    
    public void testTransformedSet() {
        Set set = TransformedSet.decorate(new HashSet(), TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, set.size());
        Object[] els = new Object[] {"1", "3", "5", "7", "2", "4", "6"};
        for (int i = 0; i < els.length; i++) {
            set.add(els[i]);
            assertEquals(i + 1, set.size());
            assertEquals(true, set.contains(new Integer((String) els[i])));
            assertEquals(false, set.contains(els[i]));
        }
        
        assertEquals(false, set.remove(els[0]));
        assertEquals(true, set.remove(new Integer((String) els[0])));
        
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/TransformedSet.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/TransformedSet.fullCollection.version3.1.obj");
//    }

}
