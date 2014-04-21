package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NotBusyTaskTest {
	public void test() {
		final Task task = new Task();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new EasyWait(task));
		
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
		new NotBusyTaskTest().test();
	}
	
	private class EasyWait implements Runnable {
		private Task task;
		
		@SuppressWarnings("unused")
		public EasyWait() {}
		
		public EasyWait(final Task t) {
			task = t;
		}
		
		public void run() {
			while (!Thread.interrupted()) {
				try {
					task.waitForFlag();
				} catch (InterruptedException e) {
					break;
				}
				System.out.println("Task is finished. Start a new one");
				task.unsetFlag();
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
			// Since single lock, single condition, notify() is well enough
			notify();
		}
		
		public synchronized void unsetFlag() {
			finished = false;
		}
		
		@SuppressWarnings("unused")
		public synchronized boolean isFinished() {
			return finished;
		}
		
		public synchronized void waitForFlag() throws InterruptedException {
			while (!finished) {
				System.out.println("task is ongoing, waiting and check again");
				wait();
			}
		}
	}
}