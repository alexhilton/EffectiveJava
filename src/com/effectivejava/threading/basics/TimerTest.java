/**
 * 
 */
package com.effectivejava.threading.basics;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author AM
 *
 */
public class TimerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Timer().schedule(new TimerTask() {
				public void run() {
					for (int i = 0; i < 10; i++) {
						final double x = Math.random() * 1000;
						final double y = Math.random() * 1000;
						System.out.println("(" + x + ", " + y + ") = " + 
								Math.sqrt(x*x + y*y));
					}
				}
			}, 1000);
		}
	}

}
