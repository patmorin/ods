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
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Extension of {@link TestList} for exercising the {@link FixedSizeList}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestFixedSizeList extends AbstractTestList {

    public TestFixedSizeList(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestFixedSizeList.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestFixedSizeList.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public List makeEmptyList() {
        return FixedSizeList.decorate(new ArrayList());
    }

    public List makeFullList() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return FixedSizeList.decorate(list);
    }
    
    public boolean isAddSupported() {
        return false;
    }

    public boolean isRemoveSupported() {
        return false;
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/FixedSizeList.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/FixedSizeList.fullCollection.version3.1.obj");
//    }

}
