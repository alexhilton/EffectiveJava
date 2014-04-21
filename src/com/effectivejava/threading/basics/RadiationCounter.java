package com.effectivejava.threading.basics;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RadiationCounter {
	private final Counter counter;
	private boolean cancelled;
	private final int size;
	private final ArrayList<RemoteSensor> sensors;
	
	/**
	 * @hide
	 */
	public RadiationCounter() {
		size = 0;
		counter = null;
		sensors = null;
	}
	
	public RadiationCounter(final int size) {
		counter = new Counter();
		cancelled = false;
		this.size = size;
		sensors = new ArrayList<RemoteSensor>(size);
		for (int i = 0; i < size; i++) {
			sensors.add(new RemoteSensor(i+1));
		}
	}
	
	/*
	 * It is safe to remove 'synchronized'
	 * form the following two methods. Neither need to add 'volatile' to canceled.
	 * Boolean operation is atomic, perhaps. 
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public int getTotal() {
		return counter.getCount();
	}
	
	public int summarize() {
		int t = 0;
		for (RemoteSensor s : sensors) {
			t += s.getCount();
		}
		return t;
	}
	
	public void test() {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < size; i++) {
			exec.execute(sensors.get(i));
		}
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
//		cancel();
		// Using interrupt() to stop 'em
		exec.shutdownNow();
		exec.shutdown();
		try {
			if (!exec.awaitTermination(300, TimeUnit.MILLISECONDS)) {
				System.out.println("Some tasks were not terminated");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Total Radiation: " + getTotal());
		System.out.println("Summary of Radiation Sensors: " + summarize());
	}
	
	/**
	 * Main program for RadiationCounter
	 * @param args
	 */
	public static void main(String[] args) {
		final int size = 5;
		RadiationCounter tester = new RadiationCounter(size);
		tester.test();
	}
	
	/*
	 * Count class to encapsulate the counting process.
	 * TODO: remove 'synchronized' to see counting failure.
	 */
	private class Counter {
		private int count;
		
		// TODO: remove 'synchronized' to see counting failure
		public synchronized int increment() {
			int tmp = count;
			Thread.yield();
			tmp++;
			count = tmp;
			return tmp;
		}
		
		public synchronized int getCount() {
			return count;
		}
	}
	
	/*
	 * RemoteSensor class which can detect radiations. Once it 
	 * detects radiation, it increase its own counter and then 
	 * increase the total count of RadiationCounter.
	 */
	private class RemoteSensor implements Runnable {
		private int count;
		private int id;
		
		public RemoteSensor(final int id) {
			count = 0;
			this.id = id;
		}
		
		public void run() {
			while (!isCancelled()) {
				synchronized (this) {
					count++;
				}
				
				System.out.println(this);
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					/*
					 * Caution: System.err and System.out might have different channels,
					 * which means the order of output you see in console might not be
					 * the same to the order of your codes, even if there is no threads.
					 */
					System.out.println(e.getMessage());
					break;
				}
			}
			System.out.println("Stopping: Sensor[" + id + "] with count: " + getCount());
		}
		
		/*
		 * use getCount() instead of reference member directly, because getCount() is 
		 * protected by 'synchronized'.
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "Sensor[" + id + "]: with count: " + getCount() + 
					", Total: " + counter.increment(); 
		}
		
		// it is safe to remove 'synchronized'.
		public int getCount() {
			return count;
		}
	}
}