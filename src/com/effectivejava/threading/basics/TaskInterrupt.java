package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Here demonstrate how to stop/interrupt a thread.
 * If a thread is running, i.e. not blocked, controller can call 
 * {@link Thread#interrupt()} to it, and inside the thread's run() can
 * check against the interrupted status via {@link Thread#interrupted()} to
 * break out the run() loop. If run() ignore interrupted status, no break out,
 * of course.
 * On the other hand, if a thread is blocking. Controller can also call
 * {@link Thread#interrupt()}, thread can exit run() through InterruptedException. 
 */
public class TaskInterrupt implements Runnable {
	private int duration;
	private volatile double d;
	
	public TaskInterrupt(final int d) {
		duration = d;
		this.d = 0.0f;
	}
	
	/*
	 * This run() will always exit from InterruptedException regardless of when
	 * {@link Thread#interrupt()} is called. Because even
	 * {@link Thread#interrupt()} is called during the calculation, the loop must
	 * be finished before checking interrupted status from which the start of the 
	 * run() loop.
	 * If comment out sleep(), run() exit on the start of second loop, because 
	 * interrupted status is set during the first loop.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println("Starting time-consuming calculation!");
				int i;
				for (i = 0; i < duration; i++) {
					d = d + (Math.PI + Math.E) / d;
				}
				System.out.println("Finish the calculation with [" + i + "] = " + d);
				
				System.out.println("Start sleeping");
				Thread.sleep(duration);
				System.out.println("finish sleeping");
			}
			System.out.println("Exiting via while loop");
		} catch (Exception e) {
			System.out.println("Exiting via InterruptedException: " + e.getMessage());
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Thread t = new Thread(new TaskInterrupt(10000000));
//		t.start();
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//		}
//		t.interrupt();
		// code invariant, use Executor
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new TaskInterrupt(10000000));
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		exec.shutdownNow();
	}
}