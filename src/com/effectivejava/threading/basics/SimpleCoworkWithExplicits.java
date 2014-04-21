package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Coordinate tasks with explicit lock and condition rather than
 * built-in synchronized, wait() and notify().
 */
public class SimpleCoworkWithExplicits {
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
		new SimpleCoworkWithExplicits().test();
	}

	private class Task1 extends Task implements Runnable {
		public Lock lock;
		private Condition cond;
		
		public Task1() {
			lock = new ReentrantLock();
			cond = lock.newCondition();
		}
		
		@SuppressWarnings("unused")
		public Task1(final Runnable r) {
			super(r);
			lock = new ReentrantLock();
			cond = lock.newCondition();
		}
		
		public void run() {
			try {
				lock.lock();
				try {
					System.out.println("i don't have a plan, wait a second...");
					cond.await();
					System.out.println("Well, wait is over, i already have a plan");
				} finally {
					lock.unlock();
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
			((Task1) atask).lock.lock();
			try {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					
				}
				// single lock and single condition, notify() is well enough
				((Task1) atask).cond.signal();
			} finally {
				((Task1) atask).lock.unlock();
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
