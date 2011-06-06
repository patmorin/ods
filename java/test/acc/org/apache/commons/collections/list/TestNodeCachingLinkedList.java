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
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;

/**
 * Test class for NodeCachingLinkedList, a performance optimised LinkedList.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Jeff Varszegi
 * @author Phil Steitz
 */
public class TestNodeCachingLinkedList extends TestAbstractLinkedList {

    public TestNodeCachingLinkedList(String testName) {
        super(testName);
    }

    public static void main(String args[]) {
        compareSpeed();
        String[] testCaseName = { TestNodeCachingLinkedList.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestNodeCachingLinkedList.class);
    }

    //-----------------------------------------------------------------------    
    public List makeEmptyList() {
        return new NodeCachingLinkedList();
    }

    public String getCompatibilityVersion() {
        return "3";
    }
    
    //-----------------------------------------------------------------------
    public void testShrinkCache() {
        if (isRemoveSupported() == false || isAddSupported() == false) return;
        resetEmpty();
        NodeCachingLinkedList list = (NodeCachingLinkedList) collection;
        
        list.addAll( Arrays.asList( new String[]{"1", "2", "3", "4"}));
        list.removeAllNodes();        // Will dump all 4 elements into cache
        ((NodeCachingLinkedList) list).setMaximumCacheSize(2); // shrink cache
        list.addAll( Arrays.asList( new String[]{"1", "2", "3", "4"}));
        checkNodes();
        list.removeNode(list.getNode(0, false)); // no room in cache
        list.removeNode(list.getNode(0, false)); 
        list.removeNode(list.getNode(0, false)); 
        checkNodes();    
        list.addAll( Arrays.asList( new String[]{"1", "2", "3", "4"}));
        checkNodes();     
    }       
    
    //-----------------------------------------------------------------------
    public static void compareSpeed() {
        NodeCachingLinkedList ncll = new NodeCachingLinkedList();
        LinkedList ll = new LinkedList();
        
        Object o1 = new Object();
        Object o2 = new Object();
        
        int loopCount = 4000000;
        
        long startTime, endTime;
        
        System.out.println("Testing relative execution time of commonly-used methods...");
        
        startTime = System.currentTimeMillis();   
        for(int x = loopCount; x > 0; x--) {
            // unrolled a few times to minimize effect of loop
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
            //
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
            //
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
        }
        endTime = System.currentTimeMillis();   
        System.out.println("Time with LinkedList: " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();   
        for(int x = loopCount; x > 0; x--) {
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
            //
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
            //
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
        }
        endTime = System.currentTimeMillis();   
        System.out.println("Time with NodeCachingLinkedList: " + (endTime - startTime) + " ms");

    }
    
//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection,
//           "D:/dev/collections/data/test/NodeCachingLinkedList.emptyCollection.version3.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection,
//            "D:/dev/collections/data/test/NodeCachingLinkedList.fullCollection.version3.obj");
//    }
}
