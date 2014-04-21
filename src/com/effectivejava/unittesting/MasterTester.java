/**
 * 
 */
package com.effectivejava.unittesting;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author Alex Hilton
 *
 */
public class MasterTester extends TestCase {
	public static Test getSuite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new SampleTargetTester("testAdd"));
		suite.addTest(new SampleTargetTester("testSub"));
		suite.addTest(new SampleTargetTester("testMul"));
		suite.addTest(new SampleTargetTester("testDiv"));
		return suite;
	}
	
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("a")) {
			System.out.println("Testing all tests");
			TestRunner.run(new TestSuite(SampleTargetTester.class));			
		} else {
			TestRunner.run(getSuite());
		}
	}
}
