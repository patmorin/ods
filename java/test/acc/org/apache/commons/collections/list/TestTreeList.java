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

import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;

/**
 * JUnit tests
 * 
 * @since Commons Collections 3.1
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Joerg Schmuecker
 */
public class TestTreeList extends AbstractTestList {
    
	public TestTreeList(String name) {
		super(name);
	}

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
//        System.out.println("         add; toArray; iterator; insert; get; indexOf; remove");
//        System.out.print("   TreeList = ");
//        benchmark(new TreeList());
//        System.out.print("\n  ArrayList = ");
//        benchmark(new java.util.ArrayList());
//        System.out.print("\n LinkedList = ");
//        benchmark(new java.util.LinkedList());
//        benchmark(new NodeCachingLinkedList());
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestTreeList.class);
    }

    public static void benchmark(List l) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            l.add(new Integer(i));
        }
        System.out.print(System.currentTimeMillis() - start + ";");
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            l.toArray();
        }
        System.out.print(System.currentTimeMillis() - start + ";");
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            java.util.Iterator it = l.iterator();
            while (it.hasNext()) {
                it.next();
            }
        }
        System.out.print(System.currentTimeMillis() - start + ";");
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int j = (int) (Math.random() * 100000);
            l.add(j, new Integer(-j));
        }
        System.out.print(System.currentTimeMillis() - start + ";");
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 50000; i++) {
            int j = (int) (Math.random() * 110000);
            l.get(j);
        }
        System.out.print(System.currentTimeMillis() - start + ";");
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            int j = (int) (Math.random() * 100000);
            l.indexOf(new Integer(j));
        }
        System.out.print(System.currentTimeMillis() - start + ";");
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int j = (int) (Math.random() * 100000);
            l.remove(j);
        }
        System.out.print(System.currentTimeMillis() - start + ";");
    }

    //-----------------------------------------------------------------------
	public List makeEmptyList() {
		return new TreeList();
	}

    //-----------------------------------------------------------------------
	public void testAddMultiple() {
		List l = makeEmptyList();
		l.add("hugo");
		l.add("erna");
		l.add("daniel");
		l.add("andres");
		l.add("harald");
		l.add(0, null);
		assertEquals(null, l.get(0));
		assertEquals("hugo", l.get(1));
		assertEquals("erna", l.get(2));
		assertEquals("daniel", l.get(3));
		assertEquals("andres", l.get(4));
		assertEquals("harald", l.get(5));
	}

	public void testRemove() {
		List l = makeEmptyList();
		l.add("hugo");
		l.add("erna");
		l.add("daniel");
		l.add("andres");
		l.add("harald");
		l.add(0, null);
		int i = 0;
		assertEquals(null, l.get(i++));
		assertEquals("hugo", l.get(i++));
		assertEquals("erna", l.get(i++));
		assertEquals("daniel", l.get(i++));
		assertEquals("andres", l.get(i++));
		assertEquals("harald", l.get(i++));

		l.remove(0);
		i = 0;
		assertEquals("hugo", l.get(i++));
		assertEquals("erna", l.get(i++));
		assertEquals("daniel", l.get(i++));
		assertEquals("andres", l.get(i++));
		assertEquals("harald", l.get(i++));

		i = 0;
		l.remove(1);
		assertEquals("hugo", l.get(i++));
		assertEquals("daniel", l.get(i++));
		assertEquals("andres", l.get(i++));
		assertEquals("harald", l.get(i++));

		i = 0;
		l.remove(2);
		assertEquals("hugo", l.get(i++));
		assertEquals("daniel", l.get(i++));
		assertEquals("harald", l.get(i++));
	}

	public void testInsertBefore() {
		List l = makeEmptyList();
		l.add("erna");
		l.add(0, "hugo");
		assertEquals("hugo", l.get(0));
		assertEquals("erna", l.get(1));
	}

    public void testIndexOf() {
        List l = makeEmptyList();
        l.add("0");
        l.add("1");
        l.add("2");
        l.add("3");
        l.add("4");
        l.add("5");
        l.add("6");
        assertEquals(0, l.indexOf("0"));
        assertEquals(1, l.indexOf("1"));
        assertEquals(2, l.indexOf("2"));
        assertEquals(3, l.indexOf("3"));
        assertEquals(4, l.indexOf("4"));
        assertEquals(5, l.indexOf("5"));
        assertEquals(6, l.indexOf("6"));
        
        l.set(1, "0");
        assertEquals(0, l.indexOf("0"));
        
        l.set(3, "3");
        assertEquals(3, l.indexOf("3"));
        l.set(2, "3");
        assertEquals(2, l.indexOf("3"));
        l.set(1, "3");
        assertEquals(1, l.indexOf("3"));
        l.set(0, "3");
        assertEquals(0, l.indexOf("3"));
    }

//    public void testCheck() {
//        List l = makeEmptyList();
//        l.add("A1");
//        l.add("A2");
//        l.add("A3");
//        l.add("A4");
//        l.add("A5");
//        l.add("A6");
//    }

    public void testBug35258() {
        Object objectToRemove = new Integer(3);
        
        List treelist = new TreeList();
        treelist.add(new Integer(0));
        treelist.add(new Integer(1));
        treelist.add(new Integer(2));
        treelist.add(new Integer(3));
        treelist.add(new Integer(4));
        
        // this cause inconsistence of ListIterator()
        treelist.remove(objectToRemove);
        
        ListIterator li = treelist.listIterator();
        assertEquals(new Integer(0), li.next());
        assertEquals(new Integer(0), li.previous());
        assertEquals(new Integer(0), li.next());
        assertEquals(new Integer(1), li.next());
        // this caused error in bug 35258
        assertEquals(new Integer(1), li.previous());
        assertEquals(new Integer(1), li.next());
        assertEquals(new Integer(2), li.next());
        assertEquals(new Integer(2), li.previous());
        assertEquals(new Integer(2), li.next());
        assertEquals(new Integer(4), li.next());
        assertEquals(new Integer(4), li.previous());
        assertEquals(new Integer(4), li.next());
        assertEquals(false, li.hasNext());
    }

}
