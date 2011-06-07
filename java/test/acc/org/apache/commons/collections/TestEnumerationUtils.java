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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import junit.framework.Assert;
import junit.framework.Test;

/**
 * Tests EnumerationUtils.
 * 
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @version $Id: TestEnumerationUtils.java 646780 2008-04-10 12:48:07Z niallp $
 */
public class TestEnumerationUtils extends BulkTest {

    public TestEnumerationUtils(String name) {
        super(name);
    }

    public static final String TO_LIST_FIXTURE = "this is a test";
    
    public void testToListWithStringTokenizer() {
        List expectedList1 = new ArrayList();
        StringTokenizer st = new StringTokenizer(TO_LIST_FIXTURE);
             while (st.hasMoreTokens()) {
                 expectedList1.add(st.nextToken());
             }        
        List expectedList2 = new ArrayList();
        expectedList2.add("this");
        expectedList2.add("is");
        expectedList2.add("a");
        expectedList2.add("test");
        List actualList = EnumerationUtils.toList(new StringTokenizer(TO_LIST_FIXTURE));
        Assert.assertEquals(expectedList1, expectedList2);
        Assert.assertEquals(expectedList1, actualList);
        Assert.assertEquals(expectedList2, actualList);
    }

    public void testToListWithHashtable() {
        Hashtable expected = new Hashtable();
        expected.put("one", new Integer(1));
        expected.put("two", new Integer(2));
        expected.put("three", new Integer(3));
        // validate elements.
        List actualEltList = EnumerationUtils.toList(expected.elements());
        Assert.assertEquals(expected.size(), actualEltList.size());
        Assert.assertTrue(actualEltList.contains(new Integer(1)));
        Assert.assertTrue(actualEltList.contains(new Integer(2)));
        Assert.assertTrue(actualEltList.contains(new Integer(3)));
        List expectedEltList = new ArrayList();
        expectedEltList.add(new Integer(1));
        expectedEltList.add(new Integer(2));
        expectedEltList.add(new Integer(3));
        Assert.assertTrue(actualEltList.containsAll(expectedEltList));

        // validate keys.
        List actualKeyList = EnumerationUtils.toList(expected.keys());
        Assert.assertEquals(expected.size(), actualEltList.size());
        Assert.assertTrue(actualKeyList.contains("one"));
        Assert.assertTrue(actualKeyList.contains("two"));
        Assert.assertTrue(actualKeyList.contains("three"));
        List expectedKeyList = new ArrayList();
        expectedKeyList.add("one");
        expectedKeyList.add("two");
        expectedKeyList.add("three");
        Assert.assertTrue(actualKeyList.containsAll(expectedKeyList));
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestEnumerationUtils.class);
    }

}
