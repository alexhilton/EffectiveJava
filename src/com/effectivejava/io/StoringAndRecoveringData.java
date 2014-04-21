package com.effectivejava.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class StoringAndRecoveringData {
    public static void main(String[] args) throws IOException {
        DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream("StoringAndRecoveringData.out")));
        out.writeDouble(3.1415926);
        out.writeUTF("the quick fox jumps over the lazy dog");
        out.writeDouble(1.41413);
        out.writeUTF("the freedom is nothing but a chance to be better");
        out.close();
        
        DataInputStream in = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream("StoringAndRecoveringData.out")));
        System.out.println(in.readDouble());
        System.out.println(in.readUTF());
        System.out.println(in.readDouble());
        System.out.println(in.readUTF());
    }
}
