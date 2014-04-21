package com.effectivejava.networks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SingleEchoServer {
	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(8199);
			Socket incoming = s.accept();
			try {
				InputStream is = incoming.getInputStream();
				OutputStream os = incoming.getOutputStream();

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}