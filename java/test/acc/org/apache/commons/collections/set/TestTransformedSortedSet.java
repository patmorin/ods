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
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.collection.TestTransformedCollection;

/**
 * Extension of {@link TestSortedSet} for exercising the {@link TransformedSortedSet}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestTransformedSortedSet extends AbstractTestSortedSet {
    
    public TestTransformedSortedSet(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestTransformedSortedSet.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestTransformedSortedSet.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    //-----------------------------------------------------------------------
    public Set makeEmptySet() {
        return TransformedSortedSet.decorate(new TreeSet(), TestTransformedCollection.NOOP_TRANSFORMER);
    }

    public Set makeFullSet() {
        SortedSet set = new TreeSet();
        set.addAll(Arrays.asList(getFullElements()));
        return TransformedSortedSet.decorate(set, TestTransformedCollection.NOOP_TRANSFORMER);
    }
    
    //-----------------------------------------------------------------------   
    public void testTransformedSet() {
        Set set = TransformedSortedSet.decorate(new HashSet(), TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
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
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/TransformedSortedSet.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/TransformedSortedSet.fullCollection.version3.1.obj");
//    }

}
