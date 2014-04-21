package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FibonacciTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		for (int i = 0; i < 10; i++) {
//			new Thread(new Fibonacci(i)).start();
//		}
		
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			exec.execute(new Fibonacci(i));
		}
		exec.shutdown();
	}
	
	private static class Fibonacci implements Runnable {
		private int limit;
		
		public Fibonacci(final int n) {
			limit = n;
		}
		
		public void run() {
			int n1 = 1;
			int n2 = 1;
			int n = 1;
			for (int i = 1; i < limit; i++) {
				n = n1 + n2;
				n1 = n2;
				n2 = n;
			}
			
			System.out.println("Fib(" + limit + ") = " + n);
		}
	}
}