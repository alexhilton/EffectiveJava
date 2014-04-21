/**
 * 
 */
package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author AM
 *
 */
public class SynchronizedTask {
	private TaskWithExplicitLock task;
	private int testCount;
	
	public SynchronizedTask(final int count) {
		testCount = count;
		task = new TaskWithExplicitLock();
	}
	
	public void test() {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < testCount; i++) {
			exec.execute(new SyncTask(task));
		}
		exec.shutdown();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SynchronizedTask(10).test();
	}
	
	/*
	 * Be careful, in order to create race condition, all threads must share
	 * the same Task object. If each thread has its own Task object, there is
	 * no content between these threads.
	 */
	private class SyncTask implements Runnable {
		private TaskWithExplicitLock task;

//		public SyncTask(final Task task) {
//			this.task = task;
//		}

		public SyncTask(final TaskWithExplicitLock task) {
			this.task = task;
		}
		
		public void run() {
			int x = (int) (Math.random() * 1000.0f);
			int y = (int) (Math.random() * 1000.0f);
			task.setX(x);
			task.setY(y);
			task.genRound();
		}
	}
	
	/*
	 * The task demonstrate the race condition.
	 * The purpose of this task is increment the two members evenly, and then
	 * check whether the two members are even. There are more-than-normal steps to 
	 * increment the variables and calling {@link Thread.yield()} just to increase 
	 * the chances of thread collisions.
	 * The three public methods which use member variables must be synchronized,
	 * otherwise the result is not correct.
	 */
	@SuppressWarnings("unused")
	private class Task {
		private int x;
		private int y;
		private boolean round;
		
		public Task() {
			x = 0;
			y = 0;
			round = true;
		}
		
		public Task(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
		
		public synchronized void setX(final int x) {
			if ((x & 0x01) != 0) {
				this.x = x + 1;
			} else {
				this.x = x;
			}
			Thread.yield();
		}
		
		public synchronized void setY(final int y) {
			if ((y & 0x01) != 0) {
				this.y = y + 1;
			} else {
				this.y = y;
			}
			Thread.yield();
		}
		
		public synchronized void genRound() {
			x += 1;
			Thread.yield();
			x += 1;
			Thread.yield();
			
			y += 1;
			Thread.yield();
			y += 1;
			Thread.yield();
			
			round = (((x & 0x01) == 0) && ((y & 0x01) == 0));
			print();
		}
		
		private void print() {
			System.out.println("(" + x + ", " + y + ") = " + round);
		}
	}
	
	/*
	 * This version uses explicit lock instead of built-in keyword 'synchronized'.
	 * It is pretty curious that tryLock cannot work well.
	 */
	@SuppressWarnings("unused")
	private class TaskWithExplicitLock {
		private int x;
		private int y;
		private boolean round;
		private ReentrantLock lock;
		
		public TaskWithExplicitLock() {
			x = 0;
			y = 0;
			round = true;
			lock = new ReentrantLock();
		}
		
		public TaskWithExplicitLock(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
		
		public void setX(final int x) {
//			final boolean locked = lock.tryLock();
			lock.lock();
			try {
				if ((x & 0x01) != 0) {
					this.x = x + 1;
				} else {
					this.x = x;
				}
				Thread.yield();
			} finally {
//				if (locked) {
					lock.unlock();
//				}
			}
		}
		
		public void setY(final int y) {
//			final boolean locked = lock.tryLock();
			lock.lock();
			try {
				if ((y & 0x01) != 0) {
					this.y = y + 1;
				} else {
					this.y = y;
				}
				Thread.yield();
			} finally {
//				if (locked) {
					lock.unlock();
//				}
			}
		}
		
		public void genRound() {
//			final boolean locked = lock.tryLock();
			lock.lock();
			try {
				x += 1;
				Thread.yield();
				x += 1;
				Thread.yield();

				y += 1;
				Thread.yield();
				y += 1;
				Thread.yield();

				round = (((x & 0x01) == 0) && ((y & 0x01) == 0));
				print();
			} finally {
//				if (locked) {
					lock.unlock();
//				}
			}
		}
		
		private void print() {
			System.out.println("(" + x + ", " + y + ") = " + round);
		}
	}
}
