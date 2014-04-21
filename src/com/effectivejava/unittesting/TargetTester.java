/**
 * 
 */
package com.effectivejava.unittesting;

import junit.framework.TestCase;

public class TargetTester extends TestCase {
    private Target target;
    
	public TargetTester(String testName) {
		super(testName);
	}
	
    @Override
	public void setUp() {
	    target = new Target();
	}
	
    @Override
	public void tearDown() {
	    target = null;
	}
	
	public void testAdd() {
		assertEquals("Test Target().add", 0, target.add());
	}
	
	public void testAdd2() { 
		target.set(1, 2);
		assertEquals("Test Target(1,2).add", 3, target.add());
	}
	
	public void testSub() {
		assertEquals("Test Target().sub", 0, target.sub());
	}
	
	public void testSub2() {
		target.set(1, 2);
		assertEquals("Test Target(1,2).sub", -1, target.sub());
	}
	
	public void additionTest() {
	    target.set(4, 5);
	    assertEquals("Tet addition(4, 5) ", 9, target.add());
	}
}