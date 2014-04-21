/**
 * 
 */
package com.effectivejava.threading;

/**
 * @author AM
 *
 */
public class FirstSight {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
				}
				System.out.println("Freedom is nothing but a chance to be better");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Debugging involves backwards reasoning, like " +
						"solving murder mysteries. Something impossible occurred," +
						" and the only solid information is that it really did occur");
			}
		}).start();
		System.out.println("The tree of liberty must be refreshed from time to time" +
				" with bloods of patrouts and tyrants");
	}

}
