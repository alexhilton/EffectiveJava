/**
 * 
 */
package com.effectivejava.networks;

import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Alex Hilton
 *
 */
public class SocketBasic {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("time-A.timefreq.bldrdoc.gov", 13);
			InputStream is = socket.getInputStream();
			Scanner scanner = new Scanner(is);
			
			while (scanner.hasNext()) {
				final String line = scanner.nextLine();
				System.out.println(line);
			}
		} catch (Exception ex) {
			//
		}
	}
}