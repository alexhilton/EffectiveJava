package com.effectivejava.generics;

public class ErasureTest<T extends TwoMethod> {
	public void testInterface(T t) {
		t.one();
		t.two();
	}
	
	public static void main(String[] args) {
		new ErasureTest<ThreeMethods>().testInterface(new ThreeMethods());
	}
}

interface TwoMethod {
	public void one();
	public void two();
}

class ThreeMethods implements TwoMethod {
	@Override
	public void one() {
		System.out.println("Method 1");
	}

	@Override
	public void two() {
		System.out.println("Method 2");
	}
	
	public void three() {
		System.out.println("method three");
	}
}