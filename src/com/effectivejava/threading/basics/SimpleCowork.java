package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleCowork {
	public void test() {
		ExecutorService exec = Executors.newCachedThreadPool();
		Runnable r = new Task1();
		exec.execute(r);
		exec.execute(new Task2(r));
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			
		}
		exec.shutdown();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SimpleCowork().test();
	}

	private class Task1 extends Task implements Runnable {
		public Task1() {}
		
		@SuppressWarnings("unused")
		public Task1(final Runnable r) {
			super(r);
		}
		
		public void run() {
			try {
				synchronized (this) {
					System.out.println("i don't have a plan, wait a second...");
					this.wait();
					System.out.println("Well, wait is over, i already have a plan");
				}
			} catch (InterruptedException e) {
				System.out.println("Exit via InterruptedException: " + e.getMessage());
			}
		}
	}

	private class Task2 extends Task implements Runnable {
		@SuppressWarnings("unused")
		public Task2() {}
		
		public Task2(final Runnable r) {
			super(r);
		}
		
		public void run() {
			synchronized (atask) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					
				}
				// single lock and single condition, notify() is well enough
				atask.notify();
			}
		}
	}
	
	private class Task {
		protected Runnable atask;
		
		public Task() {}
		
		public Task(final Runnable r) {
			atask = r;
		}
		
		@SuppressWarnings("unused")		
		public void setTask(final Runnable r) {
			atask = r;
		}
		
		@SuppressWarnings("unused")
		public Runnable getTask() {
			return atask;
		}
	}
}