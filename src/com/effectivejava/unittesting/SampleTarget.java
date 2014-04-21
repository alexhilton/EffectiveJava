package com.effectivejava.unittesting;

public class SampleTarget {
	private int oprandA;
	private int oprandB;
	
	public SampleTarget() {
		oprandA = 0;
		oprandB = 0;
	}
	
	public SampleTarget(final int a, final int b) {
		oprandA = a;
		oprandB = b;
	}

	public int add() {
		return oprandB + oprandA;
	}
	
	public int sub() {
		return oprandA - oprandB;
	}
	
	public int mul() {
		return oprandA * oprandB;
	}
	
	public int div() {// throws IllegalArgumentException {
		if (oprandB == 0) {
//			throw new IllegalArgumentException("Divide by 0");
			return 0;
		}
		return oprandA / oprandB;
	}
}