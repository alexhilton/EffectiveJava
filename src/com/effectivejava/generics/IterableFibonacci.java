package com.effectivejava.generics;

import java.util.Iterator;

public class IterableFibonacci implements Iterable<Integer> {
	public static void main(String[] args) {
		for (int i : new IterableFibonacci(10)) {
			System.out.println(i);
		}
	}
	private int count;
	private Fibonacci fib;
	
	public IterableFibonacci(int count) {
		this.count = count;
		fib = new Fibonacci();
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			@Override
			public boolean hasNext() {
				return count > 0;
			}

			@Override
			public Integer next() {
				count--;
				return fib.next();
			}

			@Override
			public void remove() {
			}
		};
	}
}