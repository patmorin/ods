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
package org.apache.commons.collections.bag;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.collection.TestTransformedCollection;

/**
 * Extension of {@link TestBag} for exercising the {@link TransformedBag}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestTransformedBag extends AbstractTestBag {
    
    public TestTransformedBag(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestTransformedBag.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestTransformedBag.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public Bag makeBag() {
        return TransformedBag.decorate(new HashBag(), TestTransformedCollection.NOOP_TRANSFORMER);
    }

    public void testTransformedBag() {
        Bag bag = TransformedBag.decorate(new HashBag(), TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, bag.size());
        Object[] els = new Object[] {"1", "3", "5", "7", "2", "4", "6"};
        for (int i = 0; i < els.length; i++) {
            bag.add(els[i]);
            assertEquals(i + 1, bag.size());
            assertEquals(true, bag.contains(new Integer((String) els[i])));
            assertEquals(false, bag.contains(els[i]));
        }
        
        assertEquals(false, bag.remove(els[0]));
        assertEquals(true, bag.remove(new Integer((String) els[0])));
        
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        Bag bag = makeBag();
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/TransformedBag.emptyCollection.version3.1.obj");
//        bag = makeBag();
//        bag.add("A");
//        bag.add("A");
//        bag.add("B");
//        bag.add("B");
//        bag.add("C");
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/TransformedBag.fullCollection.version3.1.obj");
//    }

}
