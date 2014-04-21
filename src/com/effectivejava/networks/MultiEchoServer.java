package com.effectivejava.networks;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
public class MultiEchoServer {
	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(8199);
			while (true) {
				final Socket incoming = s.accept();
				new Thread(new Runnable() {
					public void run() {
						try {
							handleRequest(incoming);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handleRequest(Socket incoming) throws IOException {
		try {
			final InputStream is = incoming.getInputStream();
			final OutputStream os = incoming.getOutputStream();

			Scanner in = new Scanner(is);
			PrintWriter out = new PrintWriter(os, true);

			boolean done = false;
			while (!done) {
				final String line = in.nextLine();
				out.println("Echo: " + line);
				if (line.trim().equals("BYE")) {
					done = true;
				}
			}
		} finally {
			incoming.close();
		}
	}
}