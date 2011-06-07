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

import java.util.Arrays;

/**
 * Test case for {@link AbstractLinkedList}.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Rich Dougherty
 * @author David Hay
 * @author Phil Steitz
 */
public abstract class TestAbstractLinkedList extends AbstractTestList {
    
    public TestAbstractLinkedList(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------    
    public void testRemoveFirst() {
        resetEmpty();
        AbstractLinkedList list = (AbstractLinkedList) collection;
        if (isRemoveSupported() == false) {
            try {
                list.removeFirst();
            } catch (UnsupportedOperationException ex) {}
        } 
        
        list.addAll( Arrays.asList( new String[]{"value1", "value2"}));
        assertEquals( "value1", list.removeFirst() );
        checkNodes();
        list.addLast( "value3");
        checkNodes();
        assertEquals( "value2", list.removeFirst() );
        assertEquals( "value3", list.removeFirst() );
        checkNodes();
        list.addLast( "value4" );
        checkNodes();
        assertEquals( "value4", list.removeFirst() );
        checkNodes();
    }
    
    public void testRemoveLast() {
        resetEmpty();
        AbstractLinkedList list = (AbstractLinkedList) collection;
        if (isRemoveSupported() == false) {
            try {
                list.removeLast();
            } catch (UnsupportedOperationException ex) {}
        } 
        
        list.addAll( Arrays.asList( new String[]{"value1", "value2"}));
        assertEquals( "value2", list.removeLast() );
        list.addFirst( "value3");
        checkNodes();
        assertEquals( "value1", list.removeLast() );
        assertEquals( "value3", list.removeLast() );
        list.addFirst( "value4" );
        checkNodes();
        assertEquals( "value4", list.removeFirst() );
    }
    
    public void testAddNodeAfter() {
        resetEmpty();
        AbstractLinkedList list = (AbstractLinkedList) collection;
        if (isAddSupported() == false) {
            try {
                list.addFirst(null);
            } catch (UnsupportedOperationException ex) {}
        } 
        
        list.addFirst("value1");
        list.addNodeAfter(list.getNode(0,false),"value2");
        assertEquals("value1", list.getFirst());
        assertEquals("value2", list.getLast());
        list.removeFirst();
        checkNodes();
        list.addNodeAfter(list.getNode(0,false),"value3");
        checkNodes();
        assertEquals("value2", list.getFirst());
        assertEquals("value3", list.getLast());
        list.addNodeAfter(list.getNode(0, false),"value4");
        checkNodes();
        assertEquals("value2", list.getFirst());
        assertEquals("value3", list.getLast());
        assertEquals("value4", list.get(1));
        list.addNodeAfter(list.getNode(2, false), "value5");
        checkNodes();
        assertEquals("value2", list.getFirst());
        assertEquals("value4", list.get(1));
        assertEquals("value3", list.get(2));
        assertEquals("value5", list.getLast());
    }
    
    public void testRemoveNode() {
        resetEmpty();
        if (isAddSupported() == false || isRemoveSupported() == false) return;
        AbstractLinkedList list = (AbstractLinkedList) collection;
        
        list.addAll( Arrays.asList( new String[]{"value1", "value2"}));
        list.removeNode(list.getNode(0, false));
        checkNodes();
        assertEquals("value2", list.getFirst());
        assertEquals("value2", list.getLast());
        list.addFirst("value1");
        list.addFirst("value0");
        checkNodes();
        list.removeNode(list.getNode(1, false));
        assertEquals("value0", list.getFirst());
        assertEquals("value2", list.getLast());
        checkNodes();
        list.removeNode(list.getNode(1, false));
        assertEquals("value0", list.getFirst());
        assertEquals("value0", list.getLast());
        checkNodes();
    }
    
    public void testGetNode() {
        resetEmpty();
        AbstractLinkedList list = (AbstractLinkedList) collection;
        // get marker
        assertEquals(list.getNode(0, true).previous, list.getNode(0, true).next);
        try {
            Object obj = list.getNode(0, false);
            fail("Expecting IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        list.addAll( Arrays.asList( new String[]{"value1", "value2"}));
        checkNodes();
        list.addFirst("value0");
        checkNodes();
        list.removeNode(list.getNode(1, false));
        checkNodes();
        try {
            Object obj = list.getNode(2, false);
            fail("Expecting IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
        try {
            Object obj = list.getNode(-1, false);
            fail("Expecting IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
         try {
            Object obj = list.getNode(3, true);
            fail("Expecting IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }       
    }
    
    protected void checkNodes() {
        AbstractLinkedList list = (AbstractLinkedList) collection;
        for (int i = 0; i < list.size; i++) {
            assertEquals(list.getNode(i, false).next, list.getNode(i + 1, true));
            if (i < list.size - 1) {
                assertEquals(list.getNode(i + 1, false).previous, 
                    list.getNode(i, false));  
            }
        }
    }
        
}
