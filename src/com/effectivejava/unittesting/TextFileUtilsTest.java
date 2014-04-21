package com.effectivejava.unittesting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.effectivejava.io.TextFileUtils;

import junit.framework.TestCase;

public class TextFileUtilsTest extends TestCase {
    private String filename;
    private String buffer;
    
    @Override
    public void setUp() {
        filename = "TextFileUtilsTesting.txt";
        buffer = "I pledge alligiance to the flag of the United States of America, and to the republic for which it stands" +
            " one nation, under God, indivisible, with liberty and justice for all!";
    }
    
    @Override
    public void tearDown() {
        
    }
    
    public void test01WriteStringToFile() {
        try {
            TextFileUtils.writeStringToFile(filename, buffer);
            File tmp = new File(filename);
            assertTrue("file exists", tmp.exists());
            assertEquals("File size", buffer.length(), tmp.length());
        } catch (IOException e) {
            assertTrue("Exception caught: ", e instanceof IOException);
        }
    }
    
    public void test02GetFileContent() {
        try {
            TextFileUtils.writeStringToFile(filename, buffer);
            
            assertEquals("read content", buffer, TextFileUtils.getFileContent(filename));
        } catch (Exception e) {
            assertTrue("Exception caught: ", e instanceof IOException);            
        }
    }
    
    public void test03Initialize() {
        TextFileUtils one = new TextFileUtils(filename);
        assertTrue("it subclasses ArrayList", one instanceof ArrayList);
        assertFalse("with file should not be empty any more", one.isEmpty());
    }
    
    public void test04Initialize() {
        final String spliter = ",";
        TextFileUtils one;
        one = new TextFileUtils(filename, spliter);
        assertTrue("splitted", one.size() > 1);
        assertEquals("array size", 5, one.size());
    }
    
    public void test05WriteToFile() {
        TextFileUtils one = new TextFileUtils(filename);
        one.writeToFile(filename);
        File f = new File(filename);
        assertTrue("file exists", f.exists());
        assertEquals("size is right", buffer.length(), f.length());
        assertEquals("content should not change", buffer, TextFileUtils.getFileContent(filename));
    }
}