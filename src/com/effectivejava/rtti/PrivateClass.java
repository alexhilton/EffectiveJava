package com.effectivejava.rtti;

public class PrivateClass {
    private final String name = "PrivateClass";
    private static final String DEFAULT_NAME = "NULL";
    
    private String myName;
    
    public PrivateClass(String name) {
        myName = name;
    }
    
    private void setName(String name) {
        myName = name;
        System.out.println(name + " setting name with " + name);
    }
    
    private String getName() {
        System.out.println(name + " getting name returns " + myName);
        return myName;
    }
}