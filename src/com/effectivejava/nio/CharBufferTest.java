package com.effectivejava.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class CharBufferTest {
    private static final int BUFFER_SIZE = 1024;
    private static final String FILENAME = "charbuf.txt";
    private static final String ENCODING = "UTF-16BE";
    
    public static void main(String[] args) throws Exception {
        FileChannel foc = new FileOutputStream(FILENAME).getChannel();
        foc.write(ByteBuffer.wrap("The fox jumps quickly over the lazy dog".getBytes(ENCODING)));
        foc.close();
        ByteBuffer buff = ByteBuffer.allocate(BUFFER_SIZE);
        FileChannel fic = new FileInputStream(FILENAME).getChannel();
        fic.read(buff);
        fic.close();
        buff.flip();
        System.out.print("'" + buff.asCharBuffer() + "'");
        System.out.println();
        
        foc = new FileOutputStream(FILENAME).getChannel();
        buff = ByteBuffer.allocate(BUFFER_SIZE);
        buff.asCharBuffer().put("Goodbye");
        foc.write(buff);
        foc.close();
        fic = new FileInputStream(FILENAME).getChannel();
        fic.read(buff);
        fic.close();
        buff.flip();
        CharBuffer cbuff = buff.asCharBuffer();
        System.out.print("'");
        for (int i = 0; i < cbuff.length(); i++) {
            char ch = cbuff.get(i);
            if (Character.isISOControl(ch)) {
                break;
            }
            System.out.print(ch);
        }
        System.out.print("'");
    }
}