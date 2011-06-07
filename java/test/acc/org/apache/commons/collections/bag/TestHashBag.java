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

/**
 * Extension of {@link TestBag} for exercising the {@link HashBag}
 * implementation.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Chuck Burdick
 */
public class TestHashBag extends AbstractTestBag {
    
    public TestHashBag(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestHashBag.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestHashBag.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public Bag makeBag() {
        return new HashBag();
    }
    
    public String getCompatibilityVersion() {
        return "3";
    }
    
//    public void testCreate() throws Exception {
//        Bag bag = makeBag();
//        writeExternalFormToDisk((Serializable) bag, "D:/dev/collections/data/test/HashBag.emptyCollection.version3.obj");
//        bag = makeBag();
//        bag.add("A");
//        bag.add("A");
//        bag.add("B");
//        bag.add("B");
//        bag.add("C");
//        writeExternalFormToDisk((Serializable) bag, "D:/dev/collections/data/test/HashBag.fullCollection.version3.obj");
//    }
}
