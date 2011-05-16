/*
 * Copyright (c) 2003, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug     4822887
 * @summary Basic test for Collections.addAll
 * @author  Josh Bloch
 *
 * @compile -source 1.5 AddAll.java
 * @run main AddAll
 */

import java.util.*;
import ods.*;

public class AddAll {
    final static int N = 100;
    public static void main(String args[]) {
        test(new ArrayList<Integer>());
        test(new LinkedList<Integer>());
        test(new HashSet<Integer>());
        test(new LinkedHashSet<Integer>());
	test(new HashTable<Integer>());
	test(new ods.ArrayDeque<Integer>(Integer.class));
	test(new ArrayStack<Integer>(Integer.class));
	test(new SkiplistList<Integer>());
	test(new FastArrayStack<Integer>(Integer.class));
	test(new DLList<Integer>());
	test(new SEList<Integer>(5, Integer.class));
	test(new SEListRaw<Integer>(5, Integer.class));
	test(new DualArrayDeque<Integer>(Integer.class));
	test(new RootishArrayStack<Integer>(Integer.class));
	test(new DualRootishArrayDeque<Integer>(Integer.class));
	test(new ArrayQueue<Integer>(Integer.class));
	test(new SLList<Integer>());
	test(new BinaryHeap<Integer>(Integer.class));
	test(new SortedSSet<Integer>(new SkiplistSet<Integer>()));
	test(new SortedSSet<Integer>(new Treap<Integer>()));
	test(new SortedSSet<Integer>(new BinarySearchTree<SimpleBSTNode<Integer>,Integer>(new SimpleBSTNode<Integer>())));
	test(new SortedSSet<Integer>(new ScapegoatTree<Integer>(new ScapegoatNode<Integer>())));
    }

    private static Random rnd = new Random();

    static void test(Collection<Integer> c) {
        int x = 0;
        for (int i = 0; i < N; i++) {
            int rangeLen = rnd.nextInt(10);
            if (Collections.addAll(c, range(x, x + rangeLen)) !=
                (rangeLen != 0))
                throw new RuntimeException("" + rangeLen);
            x += rangeLen;
        }
        if (c instanceof List) {
            if (!c.equals(Arrays.asList(range(0, x))))
                throw new RuntimeException(x +": "+c);
        } else {
            if (!c.equals(new HashSet<Integer>(Arrays.asList(range(0, x)))))
                throw new RuntimeException(x +": "+c);
        }
    }

    private static Integer[] range(int from, int to) {
        Integer[] result = new Integer[to - from];
        for (int i = from, j=0; i < to; i++, j++)
            result[j] = new Integer(i);
        return result;
    }
}
