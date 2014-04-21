package com.effectivejava.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ImportContacts {
	private static final String inputFile = "/home/alex/contacts.txt";
    private static final String outputFile = "/home/alex/contacts_vcard.vcf";
	public static void main(String[] args) {
		try {
			BufferedReader bf = null;
			PrintWriter pw = null;
			try {
				bf = new BufferedReader(new FileReader(inputFile));
				pw = new PrintWriter(new FileWriter(outputFile));
				String s = null;
				while ((s = bf.readLine()) != null) {
//					System.out.println("got : " + s);
					String[] parts = s.split(",");
					System.out.println("name: " + parts[1] + ", number " + parts[2] + ", part: " + parts[3] + ", email " + parts[4]);
		            StringBuilder sb = generateEntry(parts[1], parts[2], parts[4]);
		            pw.write(sb.toString());
				}
			} finally {
				if (bf != null) {
					bf.close();
				}
				if (pw != null) {
					pw.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static StringBuilder generateEntry(final String name, final String number, final String email) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("BEGIN:VCARD\n");
		sb.append("VERSION:2.1\n");
		final String encoded = URLEncoder.encode(name, "utf-8");
		sb.append("N:" + name + ";;;\n");
		sb.append("FN:" + name + "\n");
		sb.append("TEL;CELL:" + number + "\n");
		sb.append("EMAIL;WORK:" + email + "\n");
		sb.append("TITLE:Software Developer\n");
		sb.append("ORG:Opera\n");
		sb.append("ADR;HOME:Nanjing\n");
		sb.append("END:VCARD\n");
		return sb;
	}
}