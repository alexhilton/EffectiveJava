package com.effectivejava.rtti;

import java.io.Serializable;

public class ClassExtractorTest extends ClassExtractor implements Runnable,
        Serializable, Cloneable {
    /**
     * 
     */
    private static final long serialVersionUID = -5054007892592227440L;

    private String name;
    private static long uid;
    
    public ClassExtractorTest() {
        // TODO Auto-generated constructor stub
    }

    public void run() {
        // TODO Auto-generated method stub

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    
    public void goToSomewhere() {
        
    }
    
    public static void die() {
        
    }
    
    private void cannotSeeThis() {
        
    }
    enum Direction {
        North,
        West,
    };
    private class InnerClass {
        
    }
    
    private static class StaticInnerClass {
        
    }
}