package com.effectivejava.networks;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 8199);
			Scanner in = new Scanner(System.in);
			OutputStream os = socket.getOutputStream();
			PrintWriter out = new PrintWriter(os, true);
			
			boolean done = false;
			while (!done) {
				final String line = in.nextLine();
				out.println(line);
				if (line.trim().equals("quit")) {
					done = true;
				}
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}