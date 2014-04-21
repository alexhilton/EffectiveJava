package com.effectivejava.io;

import java.io.IOException;
import java.io.StringReader;

public class MemoryInput {
    public static void main(String[] args) throws IOException {
        String fn = null;
        if (args.length < 1) {
            fn = TextFileUtils.getFileContent("src/com/javatour/io/MemoryInput.java");
        } else {
            fn = args[0];
        }
        StringReader reader = new StringReader(fn);
        int c;
        while ((c = reader.read()) != -1) {
            System.out.print((char)c);
        }
    }
}