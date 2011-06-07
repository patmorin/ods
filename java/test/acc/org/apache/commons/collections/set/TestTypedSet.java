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

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;

/**
 * Extension of {@link AbstractTestSet} for exercising the 
 * {@link TypedSet} implementation.
 *
 * @since Commons Collections 3.1
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Phil Steitz
 */
public class TestTypedSet extends AbstractTestSet{
    
    public TestTypedSet(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        return BulkTest.makeSuite(TestTypedSet.class);
    }
    
    public static void main(String args[]) {
        String[] testCaseName = { TestTypedSet.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
   //-------------------------------------------------------------------      
    public Set makeEmptySet() {
        return TypedSet.decorate(new HashSet(), Object.class);
    }

    public boolean isNullSupported() {
        return false;
    }

    public boolean skipSerializedCanonicalTests() {
        return true;  // Typed and Predicated get confused
    }

}
