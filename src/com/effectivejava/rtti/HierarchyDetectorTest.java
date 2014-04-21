package com.effectivejava.rtti;

import junit.framework.TestCase;

public class HierarchyDetectorTest extends TestCase {
    public HierarchyDetectorTest() {
        super("Test HierarchyDetector");
    }
    
    public void testNoParent() {
        assertEquals("Object has no parent", "", HierarchyDetector.analyze(Object.class));
    }
    
    public void testSingleParent() {
        assertEquals("Object is default parent", "->Object", HierarchyDetector.analyze(HierarchyDetector.class));
    }
    
    public void testThreeParents() {
        assertEquals("test->testcase->object", "->TestCase->Assert->Object", 
                HierarchyDetector.analyze(HierarchyDetectorTest.class));
    }
}
