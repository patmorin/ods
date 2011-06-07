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

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for ComparableComparator.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Unknown
 */
public class TestComparableComparator extends AbstractTestComparator {

    public TestComparableComparator(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestComparableComparator.class);
    }

    public Comparator makeComparator() {
        return new ComparableComparator();
    }

    public List getComparableObjectsOrdered() {
        List list = new LinkedList();
        list.add(new Integer(1));
        list.add(new Integer(2));
        list.add(new Integer(3));
        list.add(new Integer(4));
        list.add(new Integer(5));
        return list;
    }

}
