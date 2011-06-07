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
package org.apache.commons.collections.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.BulkTest;

/**
 * Abstract test class for {@link SortedSet} methods and contracts.
 * <p>
 * To use, subclass and override the {@link #makeEmptySet()}
 * method.  You may have to override other protected methods if your
 * set is not modifiable, or if your set restricts what kinds of
 * elements may be added; see {@link AbstractTestCollection} for more details.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 * @author Dieter Wimberger
 */
public abstract class AbstractTestSortedSet extends AbstractTestSet {

    /**
     * JUnit constructor.
     *
     * @param name  name for test
     */
    public AbstractTestSortedSet(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    /**
     * Verification extension, will check the order of elements,
     * the sets should already be verified equal.
     */
    public void verify() {
        super.verify();
        
        // Check that iterator returns elements in order and first() and last()
        // are consistent
        Iterator colliter = collection.iterator();
        Iterator confiter = confirmed.iterator();
        Object first = null;
        Object last = null;
        while (colliter.hasNext()) {
            if (first == null) {
                first = colliter.next();
                last = first;
            } else {
              last = colliter.next();
            }  
            assertEquals("Element appears to be out of order.", last, confiter.next());
        }
        if (collection.size() > 0) {
            assertEquals("Incorrect element returned by first().", first,
                ((SortedSet) collection).first());
            assertEquals("Incorrect element returned by last().", last,
                ((SortedSet) collection).last());
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Overridden because SortedSets don't allow null elements (normally).
     * @return false
     */
    public boolean isNullSupported() {
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns an empty {@link TreeSet} for use in modification testing.
     *
     * @return a confirmed empty collection
     */
    public Collection makeConfirmedCollection() {
        return new TreeSet();
    }

    //-----------------------------------------------------------------------
    /**
     * Return the {@link AbstractTestCollection#confirmed} fixture, but cast as a
     * SortedSet.
     */
    public SortedSet getConfirmedSortedSet() {
        return (SortedSet) confirmed;
    }

    //-----------------------------------------------------------------------
    /**
     * Override to return comparable objects.
     */
    public Object[] getFullNonNullElements() {
        Object[] elements = new Object[30];

        for (int i = 0; i < 30; i++) {
            elements[i] = new Integer(i + i + 1);
        }
        return elements;
    }

    /**
     * Override to return comparable objects.
     */
    public Object[] getOtherNonNullElements() {
        Object[] elements = new Object[30];
        for (int i = 0; i < 30; i++) {
            elements[i] = new Integer(i + i + 2);
        }
        return elements;
    }

    //-----------------------------------------------------------------------
    /**
     * Bulk test {@link SortedSet#subSet(Object, Object)}.  This method runs through all of
     * the tests in {@link AbstractTestSortedSet}.
     * After modification operations, {@link #verify()} is invoked to ensure
     * that the set and the other collection views are still valid.
     *
     * @return a {@link AbstractTestSet} instance for testing a subset.
     */
    public BulkTest bulkTestSortedSetSubSet() {
        int length = getFullElements().length;

        int lobound = length / 3;
        int hibound = lobound * 2;
        return new TestSortedSetSubSet(lobound, hibound);

    }

    /**
     * Bulk test {@link SortedSet#headSet(Object)}.  This method runs through all of
     * the tests in {@link AbstractTestSortedSet}.
     * After modification operations, {@link #verify()} is invoked to ensure
     * that the set and the other collection views are still valid.
     *
     * @return a {@link AbstractTestSet} instance for testing a headset.
     */
    public BulkTest bulkTestSortedSetHeadSet() {
        int length = getFullElements().length;

        int lobound = length / 3;
        int hibound = lobound * 2;
        return new TestSortedSetSubSet(hibound, true);

    }

    /**
     * Bulk test {@link SortedSet#tailSet(Object)}.  This method runs through all of
     * the tests in {@link AbstractTestSortedSet}.
     * After modification operations, {@link #verify()} is invoked to ensure
     * that the set and the other collection views are still valid.
     *
     * @return a {@link AbstractTestSet} instance for testing a tailset.
     */
    public BulkTest bulkTestSortedSetTailSet() {
        int length = getFullElements().length;
        int lobound = length / 3;
        return new TestSortedSetSubSet(lobound, false);
    }

    public class TestSortedSetSubSet extends AbstractTestSortedSet {

        private int m_Type;
        private int m_LowBound;
        private int m_HighBound;
        private Object[] m_FullElements;
        private Object[] m_OtherElements;

        public TestSortedSetSubSet(int bound, boolean head) {
            super("TestSortedSetSubSet");
            if (head) {
                //System.out.println("HEADSET");
                m_Type = TYPE_HEADSET;
                m_HighBound = bound;
                m_FullElements = new Object[bound];
                System.arraycopy(AbstractTestSortedSet.this.getFullElements(), 0, m_FullElements, 0, bound);
                m_OtherElements = new Object[bound - 1];
                System.arraycopy(//src src_pos dst dst_pos length
                AbstractTestSortedSet.this.getOtherElements(), 0, m_OtherElements, 0, bound - 1);
                //System.out.println(new TreeSet(Arrays.asList(m_FullElements)));
                //System.out.println(new TreeSet(Arrays.asList(m_OtherElements)));
            } else {
                //System.out.println("TAILSET");
                m_Type = TYPE_TAILSET;
                m_LowBound = bound;
                Object[] allelements = AbstractTestSortedSet.this.getFullElements();
                //System.out.println("bound = "+bound +"::length="+allelements.length);
                m_FullElements = new Object[allelements.length - bound];
                System.arraycopy(allelements, bound, m_FullElements, 0, allelements.length - bound);
                m_OtherElements = new Object[allelements.length - bound - 1];
                System.arraycopy(//src src_pos dst dst_pos length
                AbstractTestSortedSet.this.getOtherElements(), bound, m_OtherElements, 0, allelements.length - bound - 1);
                //System.out.println(new TreeSet(Arrays.asList(m_FullElements)));
                //System.out.println(new TreeSet(Arrays.asList(m_OtherElements)));
                //resetFull();
                //System.out.println(collection);
                //System.out.println(confirmed);

            }

        } //type

        public TestSortedSetSubSet(int lobound, int hibound) {
            super("TestSortedSetSubSet");
            //System.out.println("SUBSET");
            m_Type = TYPE_SUBSET;
            m_LowBound = lobound;
            m_HighBound = hibound;
            int length = hibound - lobound;
            //System.out.println("Low=" + lobound + "::High=" + hibound + "::Length=" + length);
            m_FullElements = new Object[length];
            System.arraycopy(AbstractTestSortedSet.this.getFullElements(), lobound, m_FullElements, 0, length);
            m_OtherElements = new Object[length - 1];
            System.arraycopy(//src src_pos dst dst_pos length
            AbstractTestSortedSet.this.getOtherElements(), lobound, m_OtherElements, 0, length - 1);

            //System.out.println(new TreeSet(Arrays.asList(m_FullElements)));
            //System.out.println(new TreeSet(Arrays.asList(m_OtherElements)));

        }

        public boolean isNullSupported() {
            return AbstractTestSortedSet.this.isNullSupported();
        }
        public boolean isAddSupported() {
            return AbstractTestSortedSet.this.isAddSupported();
        }
        public boolean isRemoveSupported() {
            return AbstractTestSortedSet.this.isRemoveSupported();
        }
        public boolean isFailFastSupported() {
            return AbstractTestSortedSet.this.isFailFastSupported();
        }

        public Object[] getFullElements() {
            return m_FullElements;
        }
        public Object[] getOtherElements() {
            return m_OtherElements;
        }

        private SortedSet getSubSet(SortedSet set) {
            Object[] elements = AbstractTestSortedSet.this.getFullElements();
            switch (m_Type) {
                case TYPE_SUBSET :
                    return set.subSet(elements[m_LowBound], elements[m_HighBound]);
                case TYPE_HEADSET :
                    return set.headSet(elements[m_HighBound]);
                case TYPE_TAILSET :
                    return set.tailSet(elements[m_LowBound]);
                default :
                    return null;
            }
        }

        public Set makeEmptySet() {
            SortedSet s = (SortedSet) AbstractTestSortedSet.this.makeEmptySet();
            return getSubSet(s);
        }

        public Set makeFullSet() {
            SortedSet s = (SortedSet) AbstractTestSortedSet.this.makeFullCollection();
            return getSubSet(s);
        }
        
        public boolean isTestSerialization() {
            return false;
        }
        
        public BulkTest bulkTestSortedSetSubSet() {
            return null;  // prevent infinite recursion
        }
        public BulkTest bulkTestSortedSetHeadSet() {
            return null;  // prevent infinite recursion
        }
        public BulkTest bulkTestSortedSetTailSet() {
            return null;  // prevent infinite recursion
        }

        static final int TYPE_SUBSET = 0;
        static final int TYPE_TAILSET = 1;
        static final int TYPE_HEADSET = 2;

    }

}
