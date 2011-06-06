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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Transformer;

/**
 * Testcase.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestObjectGraphIterator extends AbstractTestIterator {

    protected String[] testArray = { "One", "Two", "Three", "Four", "Five", "Six" };

    protected List list1 = null;
    protected List list2 = null;
    protected List list3 = null;
    protected List iteratorList = null;

    public TestObjectGraphIterator(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestObjectGraphIterator.class);
    }

    public void setUp() {
        list1 = new ArrayList();
        list1.add("One");
        list1.add("Two");
        list1.add("Three");
        list2 = new ArrayList();
        list2.add("Four");
        list3 = new ArrayList();
        list3.add("Five");
        list3.add("Six");
        iteratorList = new ArrayList();
        iteratorList.add(list1.iterator());
        iteratorList.add(list2.iterator());
        iteratorList.add(list3.iterator());
    }

    //-----------------------------------------------------------------------
    public Iterator makeEmptyIterator() {
        ArrayList list = new ArrayList();
        return new ObjectGraphIterator(list.iterator(), null);
    }

    public Iterator makeFullIterator() {
        return new ObjectGraphIterator(iteratorList.iterator(), null);
    }

    //-----------------------------------------------------------------------
    public void testIteratorConstructor_null1() {
        Iterator it = new ObjectGraphIterator(null);

        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
        try {
            it.remove();
            fail();
        } catch (IllegalStateException ex) {
        }
    }

    public void testIteratorConstructor_null_next() {
        Iterator it = new ObjectGraphIterator(null);
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    public void testIteratorConstructor_null_remove() {
        Iterator it = new ObjectGraphIterator(null);
        try {
            it.remove();
            fail();
        } catch (IllegalStateException ex) {
        }
    }

    //-----------------------------------------------------------------------
    public void testIteratorConstructorIteration_Empty() {
        List iteratorList = new ArrayList();
        Iterator it = new ObjectGraphIterator(iteratorList.iterator());

        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
        try {
            it.remove();
            fail();
        } catch (IllegalStateException ex) {
        }
    }

    public void testIteratorConstructorIteration_Simple() {
        List iteratorList = new ArrayList();
        iteratorList.add(list1.iterator());
        iteratorList.add(list2.iterator());
        iteratorList.add(list3.iterator());
        Iterator it = new ObjectGraphIterator(iteratorList.iterator());

        for (int i = 0; i < 6; i++) {
            assertEquals(true, it.hasNext());
            assertEquals(testArray[i], it.next());
        }
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    public void testIteratorConstructorIteration_SimpleNoHasNext() {
        List iteratorList = new ArrayList();
        iteratorList.add(list1.iterator());
        iteratorList.add(list2.iterator());
        iteratorList.add(list3.iterator());
        Iterator it = new ObjectGraphIterator(iteratorList.iterator());

        for (int i = 0; i < 6; i++) {
            assertEquals(testArray[i], it.next());
        }
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    public void testIteratorConstructorIteration_WithEmptyIterators() {
        List iteratorList = new ArrayList();
        iteratorList.add(IteratorUtils.EMPTY_ITERATOR);
        iteratorList.add(list1.iterator());
        iteratorList.add(IteratorUtils.EMPTY_ITERATOR);
        iteratorList.add(list2.iterator());
        iteratorList.add(IteratorUtils.EMPTY_ITERATOR);
        iteratorList.add(list3.iterator());
        iteratorList.add(IteratorUtils.EMPTY_ITERATOR);
        Iterator it = new ObjectGraphIterator(iteratorList.iterator());

        for (int i = 0; i < 6; i++) {
            assertEquals(true, it.hasNext());
            assertEquals(testArray[i], it.next());
        }
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    public void testIteratorConstructorRemove() {
        List iteratorList = new ArrayList();
        iteratorList.add(list1.iterator());
        iteratorList.add(list2.iterator());
        iteratorList.add(list3.iterator());
        Iterator it = new ObjectGraphIterator(iteratorList.iterator());

        for (int i = 0; i < 6; i++) {
            assertEquals(testArray[i], it.next());
            it.remove();
        }
        assertEquals(false, it.hasNext());
        assertEquals(0, list1.size());
        assertEquals(0, list2.size());
        assertEquals(0, list3.size());
    }

    //-----------------------------------------------------------------------
    public void testIteration_IteratorOfIterators() {
        List iteratorList = new ArrayList();
        iteratorList.add(list1.iterator());
        iteratorList.add(list2.iterator());
        iteratorList.add(list3.iterator());
        Iterator it = new ObjectGraphIterator(iteratorList.iterator(), null);

        for (int i = 0; i < 6; i++) {
            assertEquals(true, it.hasNext());
            assertEquals(testArray[i], it.next());
        }
        assertEquals(false, it.hasNext());
    }

    public void testIteration_IteratorOfIteratorsWithEmptyIterators() {
        List iteratorList = new ArrayList();
        iteratorList.add(IteratorUtils.EMPTY_ITERATOR);
        iteratorList.add(list1.iterator());
        iteratorList.add(IteratorUtils.EMPTY_ITERATOR);
        iteratorList.add(list2.iterator());
        iteratorList.add(IteratorUtils.EMPTY_ITERATOR);
        iteratorList.add(list3.iterator());
        iteratorList.add(IteratorUtils.EMPTY_ITERATOR);
        Iterator it = new ObjectGraphIterator(iteratorList.iterator(), null);

        for (int i = 0; i < 6; i++) {
            assertEquals(true, it.hasNext());
            assertEquals(testArray[i], it.next());
        }
        assertEquals(false, it.hasNext());
    }

    //-----------------------------------------------------------------------
    public void testIteration_RootNull() {
        Iterator it = new ObjectGraphIterator(null, null);

        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
        try {
            it.remove();
            fail();
        } catch (IllegalStateException ex) {
        }
    }

    public void testIteration_RootNoTransformer() {
        Forest forest = new Forest();
        Iterator it = new ObjectGraphIterator(forest, null);

        assertEquals(true, it.hasNext());
        assertSame(forest, it.next());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    public void testIteration_Transformed1() {
        Forest forest = new Forest();
        Leaf l1 = forest.addTree().addBranch().addLeaf();
        Iterator it = new ObjectGraphIterator(forest, new LeafFinder());

        assertEquals(true, it.hasNext());
        assertSame(l1, it.next());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    public void testIteration_Transformed2() {
        Forest forest = new Forest();
        forest.addTree();
        forest.addTree();
        forest.addTree();
        Branch b1 = forest.getTree(0).addBranch();
        Branch b2 = forest.getTree(0).addBranch();
        Branch b3 = forest.getTree(2).addBranch();
        Branch b4 = forest.getTree(2).addBranch();
        Branch b5 = forest.getTree(2).addBranch();
        Leaf l1 = b1.addLeaf();
        Leaf l2 = b1.addLeaf();
        Leaf l3 = b2.addLeaf();
        Leaf l4 = b3.addLeaf();
        Leaf l5 = b5.addLeaf();

        Iterator it = new ObjectGraphIterator(forest, new LeafFinder());

        assertEquals(true, it.hasNext());
        assertSame(l1, it.next());
        assertEquals(true, it.hasNext());
        assertSame(l2, it.next());
        assertEquals(true, it.hasNext());
        assertSame(l3, it.next());
        assertEquals(true, it.hasNext());
        assertSame(l4, it.next());
        assertEquals(true, it.hasNext());
        assertSame(l5, it.next());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    public void testIteration_Transformed3() {
        Forest forest = new Forest();
        forest.addTree();
        forest.addTree();
        forest.addTree();
        Branch b1 = forest.getTree(1).addBranch();
        Branch b2 = forest.getTree(1).addBranch();
        Branch b3 = forest.getTree(2).addBranch();
        Branch b4 = forest.getTree(2).addBranch();
        Branch b5 = forest.getTree(2).addBranch();
        Leaf l1 = b1.addLeaf();
        Leaf l2 = b1.addLeaf();
        Leaf l3 = b2.addLeaf();
        Leaf l4 = b3.addLeaf();
        Leaf l5 = b4.addLeaf();

        Iterator it = new ObjectGraphIterator(forest, new LeafFinder());

        assertEquals(true, it.hasNext());
        assertSame(l1, it.next());
        assertEquals(true, it.hasNext());
        assertSame(l2, it.next());
        assertEquals(true, it.hasNext());
        assertSame(l3, it.next());
        assertEquals(true, it.hasNext());
        assertSame(l4, it.next());
        assertEquals(true, it.hasNext());
        assertSame(l5, it.next());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException ex) {
        }
    }

    //-----------------------------------------------------------------------
    static class LeafFinder implements Transformer {
        public Object transform(Object input) {
            if (input instanceof Forest) {
                return ((Forest) input).treeIterator();
            }
            if (input instanceof Tree) {
                return ((Tree) input).branchIterator();
            }
            if (input instanceof Branch) {
                return ((Branch) input).leafIterator();
            }
            if (input instanceof Leaf) {
                return input;
            }
            throw new ClassCastException();
        }
    }

    //-----------------------------------------------------------------------
    static class Forest {
        List trees = new ArrayList();

        Tree addTree() {
            trees.add(new Tree());
            return getTree(trees.size() - 1);
        }

        Tree getTree(int index) {
            return (Tree) trees.get(index);
        }

        Iterator treeIterator() {
            return trees.iterator();
        }
    }

    static class Tree {
        List branches = new ArrayList();

        Branch addBranch() {
            branches.add(new Branch());
            return getBranch(branches.size() - 1);
        }

        Branch getBranch(int index) {
            return (Branch) branches.get(index);
        }

        Iterator branchIterator() {
            return branches.iterator();
        }
    }

    static class Branch {
        List leaves = new ArrayList();

        Leaf addLeaf() {
            leaves.add(new Leaf());
            return getLeaf(leaves.size() - 1);
        }

        Leaf getLeaf(int index) {
            return (Leaf) leaves.get(index);
        }

        Iterator leafIterator() {
            return leaves.iterator();
        }
    }

    static class Leaf {
        String colour;

        String getColour() {
            return colour;
        }

        void setColour(String colour) {
            this.colour = colour;
        }
    }

}
