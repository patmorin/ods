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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Unmodifiable;

/**
 * Tests the UnmodifiableListIterator.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestUnmodifiableListIterator extends AbstractTestListIterator {

    protected String[] testArray = { "One", "Two", "Three" };
    protected List testList = new ArrayList(Arrays.asList(testArray));

    public static Test suite() {
        return new TestSuite(TestUnmodifiableListIterator.class);
    }

    public TestUnmodifiableListIterator(String testName) {
        super(testName);
    }

    public ListIterator makeEmptyListIterator() {
        return UnmodifiableListIterator.decorate(Collections.EMPTY_LIST.listIterator());
    }

    public ListIterator makeFullListIterator() {
        return UnmodifiableListIterator.decorate(testList.listIterator());
    }

    public boolean supportsRemove() {
        return false;
    }

    public boolean supportsAdd() {
        return false;
    }

    public boolean supportsSet() {
        return false;
    }

    //-----------------------------------------------------------------------
    public void testListIterator() {
        assertTrue(makeEmptyListIterator() instanceof Unmodifiable);
    }
    
    public void testDecorateFactory() {
        ListIterator it = makeFullListIterator();
        assertSame(it, UnmodifiableListIterator.decorate(it));
        
        it = testList.listIterator();
        assertTrue(it != UnmodifiableListIterator.decorate(it));
        
        try {
            UnmodifiableListIterator.decorate(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

}
