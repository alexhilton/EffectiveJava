package com.effectivejava.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ReverseTextFile {
    private List<String> fileContent;
    
    public ReverseTextFile() {
        fileContent = new LinkedList<String>();
    }
    public void readin(String fn) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fn));
        String s;
        while ((s = reader.readLine()) != null) {
            fileContent.add(s);
        }
    }
    
    public void reverse() {
        if (fileContent != null && fileContent.size() > 0) {
            int size = fileContent.size();
            while (--size >= 0) {
                System.out.println(fileContent.remove(size));
            }
        }
    }
    
    public void grep(String pattern) {
        if (fileContent != null && fileContent.size() > 0) {
            int size = fileContent.size();
            for (int i = 0; i < size; i++) {
                if (fileContent.get(i).contains(pattern)) {
                    System.out.println((i+1) + ":" + fileContent.get(i));
                }
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String fn = null;
        String pattern = null;
        if (args == null || args.length < 1) {
            System.err.println("You must specify pattern");
            return;
        } else if (args.length < 2) {
            fn = "src/com/javatour/io/ReverseTextFile.java";
            pattern = args[0];
        } else {
            fn = args[0];
            pattern = args[1];
        }
        ReverseTextFile r = new ReverseTextFile();
        try {
            r.readin(fn);
            r.grep(pattern);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}