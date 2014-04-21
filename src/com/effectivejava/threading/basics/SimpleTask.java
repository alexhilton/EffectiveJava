/**
 * 
 */
package com.effectivejava.threading.basics;

/**
 * @author AM
 *
 */
public class SimpleTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++ ) {
			new Thread(new MessageTask()).start();
		}
	}

	private static class MessageTask implements Runnable {
		public MessageTask() {
			System.out.println("This is where everything begins!");
		}
		
		public void run() {
			System.out.println("Freedom is nothing but a chance to be better");
			Thread.yield();
			
			System.out.println("Think different");
			Thread.yield();
			
			System.out.println("Thinking differently \255");
			Thread.yield();
			
			System.out.println("Dust to dust, all ends here");
		}
	}
}
