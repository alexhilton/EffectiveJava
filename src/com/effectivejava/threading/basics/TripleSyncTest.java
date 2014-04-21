/**
 * 
 */
package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author AM
 *
 */
public class TripleSyncTest implements Runnable {
	private TripleSync sync;
	
	public TripleSyncTest() {
		
	}
	
	public TripleSyncTest(TripleSync sync) {
		this.sync = sync;
	}
	
	public void run() {
		final int a = (int) (Math.random() * 1000.0f);
		sync.setValue(a);
		sync.alpha();
		sync.beta();
		sync.delta();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final TripleSync sync = new TripleSync();
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			exec.execute(new TripleSyncTest(sync));	
		}
		exec.shutdown();
	}
	
	/*
	 * There are three methods defined in this class which each synchronized
	 * the same object to void race condition.
	 * If synchronized the same object, there is no race condition.
	 * Instead, if synchronized on different objects, collision occurred.
	 */
	@SuppressWarnings("unused")
	private static class TripleSync {
		// Four objects to synchronize methods
		private Object obj = new Object();
		private Object another = new Object();
		private Object other = new Object();
		
		private Lock lock;
		
		private int value;
		
		public TripleSync() {
			value = 0;
			lock = new ReentrantLock();
		}
		
		public TripleSync(final int v) {
			if ((v & 0x01) != 0) {
				value = v + 1;
			} else {
				value = v;
			}
		}
		
		public void setValue(final int v) {
//			synchronized (obj) {
//			lock.lock();
			try {
				if ((v & 0x01) != 0) {
					value = v + 1;
				} else {
					value = v;
				}
			} finally {
//				lock.unlock();
			}
//			}
		}
		
		private void alpha() {
			// TODO: to fix collision, synchronized the same obj
//			System.out.println("TripleSync.alpha()");
//			synchronized (obj) {
			lock.lock();
			try {
				value += 1;
				Thread.yield();
				value += 1;
				System.out.println("value[" + value + "] = " + 
						((value & 0x01) == 0));
			} finally {
				lock.unlock();
			}
//			}
		}
		
		private void beta() {
			// TODO: to fix collision, synchronized the same obj
//			System.out.println("TripleSync.beta()");
//			synchronized (obj) {
			lock.lock();
			try {
				value += 1;
				Thread.yield();
				value += 1;
				System.out.println("value[" + value + "] = " + 
						((value & 0x01) == 0));
			} finally {
				lock.unlock();
			}
//			}
		}
		
		private void delta() {
			// TODO: to fix collision, synchronized the same obj
//			System.out.print("TripleSync.delta()\t");
//			synchronized (this) {
			lock.lock();
			try {
				value += 1;
				Thread.yield();
				value += 1;
				System.out.println("value[" + value + "] = " + 
						((value & 0x01) == 0));
			} finally {
				lock.unlock();
			}
		}
	}
}