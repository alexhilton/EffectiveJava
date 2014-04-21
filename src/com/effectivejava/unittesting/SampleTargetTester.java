/**
 * 
 */
package com.effectivejava.unittesting;

import junit.framework.TestCase;

/**
 * @author Alex Hilton
 *
 */
public class SampleTargetTester extends TestCase {
	// wondering why add a name to constructor
	public SampleTargetTester(String name) {
		super(name);
	}
	
	protected void setUp() {
		
	}
	
	protected void tearDown() {
		
	}
	
	public void testAdd() {
		SampleTarget target;
		target = new SampleTarget();
		assertEquals(target.add(), 0);
		
		target = new SampleTarget(2, 4);
		assertEquals(target.add(), 6);
		
		target = new SampleTarget(1, -1);
		assertEquals(target.add(), 0);
	}
	
	public void testSub() {
		SampleTarget target;
		target = new SampleTarget();
		assertEquals(target.sub(), 0);
		
		target = new SampleTarget(2, 4);
		assertEquals(target.sub(),-2);
		
		target = new SampleTarget(1, -1);
		assertEquals(target.sub(), 2);
	}
	
	public void testMul() {
		SampleTarget target;
		target = new SampleTarget();
		assertEquals(target.mul(), 0);
		
		target = new SampleTarget(2, 4);
		assertEquals(target.mul(), 8);
		
		target = new SampleTarget(1, -1);
		assertEquals(target.mul(), -1);
	}
	
	public void testDiv() {
		SampleTarget target;
		
		try {
			target = new SampleTarget();
			assertEquals(target.div(), 0);
			fail("Exception 'IllegalArgumentException' must be thrown when dividing 0");
		} catch (IllegalArgumentException e) {
		}
		
		target = new SampleTarget(2, 4);
		assertEquals(target.div(), 0);
		
		target = new SampleTarget(1, -1);
		assertEquals(target.div(), -1);
	}
}
