/**
 * 
 */
package com.effectivejava.unittesting;

/**
 * @author AM
 *
 */
public class Target {
	private int oprandA;
	private int oprandB;
	
	public Target() {
		oprandA = 0;
		oprandB = 0;
	}
	
	public Target(int a, int b) {
		oprandA = a;
		oprandB = b;
	}
	
	public void set(int a, int b) {
	    oprandA = a;
	    oprandB = b;
	}
	
	public int add() {
		return oprandA + oprandB;
	}
	
	public int sub() {
		return oprandA - oprandB;
	}
}