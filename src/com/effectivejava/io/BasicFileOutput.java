package com.effectivejava.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

public class BasicFileOutput {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(
                new StringReader(
                        TextFileUtils.getFileContent(
                            "src/com/javatour/io/BasicFileOutput.java")));
        PrintWriter writer = new PrintWriter(
                new BufferedWriter(new FileWriter("BasicFileOutput.out")));
        int lineCount = 1;
        String s;
        while ((s = reader.readLine()) != null) {
            writer.println(lineCount++ + ": " + s);
        }
        reader.close();
        writer.close();
    }
}
