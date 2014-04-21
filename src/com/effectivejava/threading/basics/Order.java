package com.effectivejava.threading.basics;

public class Order {
    private boolean ready;
    private String name;
    
    public Order(String n) {
        name = n;
        ready = false;
    }
    public boolean isReady() {
        return ready;
    }
    
    public void ready() {
        ready = true;
    }
    
    @Override
    public String toString() {
        return name;
    }
}