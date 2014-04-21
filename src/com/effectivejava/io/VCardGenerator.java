/**
 * 
 */
package com.effectivejava.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class VCardGenerator {
    public static void main(String[] args) throws IOException {
        int count = 1510;
        String filename = "vcard" + String.valueOf(count) + ".vcf";
        String prefix = "song";
        PrintWriter writer = new PrintWriter(
                new BufferedWriter(new FileWriter(filename)));
        long number = 13013490000L; // phone number
        long number2 = 5273489L;
        while (count-- > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("BEGIN:VCARD\n");
            sb.append("VERSION:2.1\n");
            sb.append("N:;" + prefix + String.valueOf(count) + ";;;\n");
            sb.append("FN:" + prefix + String.valueOf(count) + "\n");
            sb.append("TEL;CELL:" + String.valueOf(number) + "\n");
            sb.append("TEL;HOME:" + String.valueOf(number2) + "\n");
            sb.append("EMAIL;HOME:asdefsf@gmail.com\n");
            sb.append("ADR;WORK:Aqqw;Yuhuatai;Software Road;Nanjing;Jiangsu;210012;\n");
            sb.append("END:VCARD\n");
            writer.write(sb.toString());
            number++;
            number2++;
        }
        writer.close();
    }
}