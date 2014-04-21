package com.effectivejava.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Provide generic utilities to manipulate text files.
 * Read the text file as a string by specifying file name.
 * Read the text file content and form them into an ArrayList with each
 * item is a line in the text file.
 * Write a string to a file.
 * Write ArrayList of strings into a file.
 * @author AM
 *
 */
public class TextFileUtils extends ArrayList<String> {
    public TextFileUtils(String filename) {
        this(filename, "\n");
    }
    
    public TextFileUtils(String filename, String spliter) {
        super(Arrays.asList(getFileContent(filename).split(spliter)));
        if (get(0).equals("")) {
            remove(0);
        }
    }
    
    public static void writeStringToFile(String fn, String buffer) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fn)));
        out.print(buffer);
        out.close();
    }
    
    /**
     * Return the file pointed by filename content as a string.
     * @param fn filename of the file
     * @return
     * @throws IOException
     */
    public static String getFileContent(String fn) {
        StringBuilder sb =  new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new FileReader(fn));
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return sb.toString();
    }
    
    public void writeToFile(String filename) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            for (String item : this) {
                out.print(item);
            }
            out.close();
        } catch (IOException e) {
            //
        }
    }
}