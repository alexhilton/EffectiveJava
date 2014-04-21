package com.effectivejava.generics;

public class GenericsMethods {
	public <A, B, C> void f(A a, B b, C c) {
		System.out.println(a.getClass().getSimpleName());
		System.out.println(b.getClass().getSimpleName());
		System.out.println(c.getClass().getSimpleName());
	}
	
	public <A, B> int g(A a, B b, int in) {
		System.out.println(a.getClass().getSimpleName());
		System.out.println(b.getClass().getSimpleName());
		return in * 2;
	}
	public static void main(String[] args) {
		GenericsMethods g = new GenericsMethods();
		g.f(4, "me", 5f);
		
		System.out.println(g.g("hello", true, 10));
	}
}