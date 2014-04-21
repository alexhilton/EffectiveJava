package com.effectivejava.threading.basics;

public class InterruptedTask implements Runnable {
	
	public void run() {
		try {
			new Task(2000).block();
		} catch (InterruptedException e) {
			System.out.println("Interrupted with " + e.getMessage());
		}
		System.out.println("Task completed");
	}
	
	/**
	 * Interrupt a blocked task.
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t = new Thread(new InterruptedTask());
		t.start();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			System.err.println("InterruptedException thrown in main thread");
		}
		t.interrupt();
	}
}

class Task {
	private final int duration;
	
	public Task(final int duration) {
		this.duration = duration;
	}
	
	public void block() throws InterruptedException {
		Thread.sleep(duration);
	}
}