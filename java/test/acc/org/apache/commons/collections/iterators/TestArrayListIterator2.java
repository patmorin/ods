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
package org.apache.commons.collections.iterators;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test the ArrayListIterator class with primitives.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * @author Neil O'Toole
 */
public class TestArrayListIterator2 extends TestArrayIterator2 {

    public TestArrayListIterator2(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestArrayListIterator2.class);
    }

    public Iterator makeEmptyIterator() {
        return new ArrayListIterator(new int[0]);
    }

    public Iterator makeFullIterator() {
        return new ArrayListIterator(testArray);
    }

    public ArrayIterator makeArrayIterator() {
        return (ArrayIterator) makeEmptyIterator();
    }

    public ArrayIterator makeArrayIterator(Object array) {
        return new ArrayListIterator(array);
    }

    public ArrayIterator makeArrayIterator(Object array, int index) {
        return new ArrayListIterator(array, index);
    }

    public ArrayIterator makeArrayIterator(Object array, int start, int end) {
        return new ArrayListIterator(array, start, end);
    }

}
