package com.effectivejava.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FillStrings {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(args[0]));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String filename = parts[0].trim();
                String str = parts[1].trim();
                PrintWriter writer = new PrintWriter(
                        new BufferedWriter(new FileWriter(filename)));
                writer.append(str);
                writer.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
