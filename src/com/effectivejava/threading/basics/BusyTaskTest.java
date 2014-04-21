package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BusyTaskTest {
	public void test() {
		final Task task = new Task();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new BusyWait(task));
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			
		}
		task.setFlag();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			
		}
		exec.shutdownNow();
		exec.shutdown();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BusyTaskTest().test();
	}
	
	private class BusyWait implements Runnable {
		private Task task;
		
		@SuppressWarnings("unused")
		public BusyWait() {}
		
		public BusyWait(final Task t) {
			task = t;
		}
		
		public void run() {
			while (true && !Thread.interrupted()) {
				if (task.isFinished()) {
					System.out.println("Task is finished. Start a new one");
					task.unsetFlag();
				} else {
					System.out.println("task is ongoing, waiting 200ms and check again");
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						break; // break out the while loop
					}
				}
			}
			System.out.println("Exiting while loop");
		}
	}
	
	private class Task {
		private boolean finished;
		
		public Task() {
			finished = false;
		}
		
		public synchronized void setFlag() {
			finished = true;
		}
		
		public synchronized void unsetFlag() {
			finished = false;
		}
		
		public synchronized boolean isFinished() {
			return finished;
		}
	}
}